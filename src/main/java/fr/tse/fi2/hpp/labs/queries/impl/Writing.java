package fr.tse.fi2.hpp.labs.queries.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public  class Writing implements Runnable {

	/**
	 * Writer to write the output of the queries
	 */
	private BufferedWriter outputWriter;
	final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);
	private int id;
	public  BlockingQueue<String> writequeue;
	public Writing (int id, BlockingQueue<String> writequeue)
	{
		super();
		this.id=id;
		this.writequeue = writequeue;
		try {
			outputWriter = new BufferedWriter(new FileWriter(new File(
					"result/query" + id + ".txt")));
		} catch (IOException e) {
			logger.error("Cannot open output file for " + id, e);
			System.exit(-1);
		}
	}
	


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				String line= writequeue.take();
				if(line.equals("DIE!!!")){ 
					break;
				}
				writeLine(line);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		finish();
	}
	/**
	 * Poison pill has been received, close output
	 */
	protected void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}
	}



	/**
	 * 
	 * @param line
	 *            the line to write as an answer
	 */
	protected void writeLine(String line) {
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}

	}
}
