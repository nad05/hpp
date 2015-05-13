package fr.tse.fi2.hpp.labs.queries.impl.lab5;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.utils.MergeSort;



@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public  class BenchmarkMergeSort {

	
	public void dobench() throws Exception {

		final int[] tabtest;
		tabtest = new int[100000];
		Random generator = new Random();
		for (int i = 0; i < tabtest.length; i++) {
			tabtest[i] = generator.nextInt(1000);
		}
	    MergeSort sorter = new MergeSort();
	    sorter.sort(tabtest);

	  
		}
	
}
