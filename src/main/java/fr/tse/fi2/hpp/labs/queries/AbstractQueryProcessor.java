package fr.tse.fi2.hpp.labs.queries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.impl.Writing;


/**
 * Every query must extend this class that provides basic functionalities such
 * as :
 * <ul>
 * <li>Receives event from {@link StreamingDispatcher}</li>
 * <li>Notify start/end time</li>
 * <li>Manages thread synchronization</li>
 * <li>Grid mapping: maps lat/long to x,y in a discrete grid of given size</li>
 * </ul>
 * 
 * @author Julien
 * 
 */
public abstract class AbstractQueryProcessor implements Runnable {

	private final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);

	/**
	 * Counter to uniquely identify the query processors
	 */
	private final static AtomicInteger COUNTER = new AtomicInteger();
	/**
	 * Unique ID of the query processor
	 */
	protected final int id = COUNTER.incrementAndGet();
	/**
	 * Writer to write the output of the queries
	 */
	
	/**
	 * Internal queue of events
	 */
	public final BlockingQueue<DebsRecord> eventqueue;
	public final BlockingQueue<String> writequeue;
	public final Thread thread;
	/**
	 * Global measurement
	 */
	private final QueryProcessorMeasure measure;
	/**
	 * For synchronisation purpose
	 */
	private CountDownLatch latch;

	/**
	 * Default constructor. Initialize event queue and writer
	 */
	public AbstractQueryProcessor(QueryProcessorMeasure measure) {
		// Set the global measurement instance
		this.measure = measure;
		// Initialize queue
		this.eventqueue = new LinkedBlockingQueue<>();
		this.writequeue = new LinkedBlockingQueue<>();
		 Writing write= new  Writing(id, writequeue);
		 thread = new Thread(write);
		 thread.start();

		// Initialize writer
		
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		logger.info("Starting query processor " + id);
		// Notify beginning of processing
		measure.notifyStart(this.id);
		while (true) {
			try {
				DebsRecord record = eventqueue.take();
				if (record.isPoisonPill()) {
					
					break;
				} else {
					process(record);
				}
			} catch (InterruptedException e) {
				logger.error(
						"Error taking element from internal queue, processor "
								+ id, e);
				break;
			}
		}
		// Finish, close the writer and notify the measurement
		finish();
		logger.info("Closing query processor " + id);
	}

	/**
	 * 
	 * @param record
	 *            record to be processed
	 */
	protected abstract void process(DebsRecord record);

	/**
	 * 
	 * @param record
	 *            the record to process
	 * @return the route in a 600*600 grid
	 */
	protected Route convertRecordToRoute(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert(lat1, long1);
		// Convert pickup coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert(lat2, long2);
		return new Route(pickup, dropoff);
	}

	/**
	 * 
	 * @param lat1
	 * @param long1
	 * @return The lat/long converted into grid coordinates
	 */
	private GridPoint convert(float lat1, float long1) {
		return new GridPoint(cellX(lat1), cellY(long1));
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param x
	 * @return
	 */
	private int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986 / 2;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param y
	 * @return
	 */
	private int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556 / 2;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}

	/**
	 * @return the id of the query processor
	 */
	public final int getId() {
		return id;
	}

	/**
	 * 
	 * @param line
	 *            the line to write as an answer
	 */
	protected void writeLine(String line) {
		/*try {
			getOutputWriter().write(line);
			getOutputWriter().newLine();
		} catch (IOException e) {
			getLogger().error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}*/
		writequeue.add(line);
		

	} 

	/**
	 * Poison pill has been received, close output
	 * @throws InterruptedException 
	 */
	protected void finish()  {
		// Close writer
		writeLine("DIE!!!");
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Notify finish time
		measure.notifyFinish(this.id);
		// Decrease latch count
		latch.countDown();
	}

	

}

