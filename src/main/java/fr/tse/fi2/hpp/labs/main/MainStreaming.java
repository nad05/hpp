package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.SimpleQuerySumEvent;
import fr.tse.fi2.hpp.labs.queries.impl.lab1.IncrementalAverage;
import fr.tse.fi2.hpp.labs.queries.impl.lab1.NaiveAverage;
import fr.tse.fi2.hpp.labs.queries.impl.lab4.RouteMembershipProcessor;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit
 * {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
public class MainStreaming {

	final static Logger logger = LoggerFactory.getLogger(MainStreaming.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher
		StreamingDispatcher dispatch = new StreamingDispatcher(
				//"src/main/resources/data/1000Records.csv");
				"src/main/resources/data/sorted_data.csv");

		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		RouteMembershipProcessor RMSP =new RouteMembershipProcessor(measure);
		// Add you query processor here
		//processors.add(new SimpleQuerySumEvent(measure));
		//processors.add(new NaiveAverage(measure));
		//processors.add(new IncrementalAverage(measure));
		processors.add(RMSP);
		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		// Start everything
		for (AbstractQueryProcessor queryProcessor : processors) {
			// queryProcessor.run();
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		Thread t1 = new Thread(dispatch);
		t1.setName("Dispatcher");
		t1.start();

		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();
		float plong = (float) -73.98353;
		float plat = (float)  40.749985;
		float dlong = (float) -73.99183;
		float dlat = (float)   40.74913;
		String lic= "441D5B00E6EC31C7951D9E5E81CA6A57";
		
		DebsRecord recordTestFaux = new DebsRecord("", "", 4, 4, 4, 4, 4, 4, 4, 4, "", 4, 4, 4, 4, 4, 4, false);
		DebsRecord recordTestVrai = new DebsRecord("", lic, 4, 4, 4, 4, plong, plat, dlong, dlat, "", 4, 4, 4, 4, 4, 4, false);
		
		System.out.println("Route find : " + RouteMembershipProcessor.CheckRoute(recordTestVrai));
		System.out.println("Route find : " + RouteMembershipProcessor.CheckRoute(recordTestFaux));
		boolean ligne = RMSP.CheckRoute(recordTestFaux);
		int nb = 0;
		if (ligne!=false)
		{
			nb+=1;
			System.out.println("Found it at :"+ ligne);
		}
		else
			System.out.println("Try again ...");

	}

}
