package fr.tse.fi2.hpp.labs.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.MergeSort;

public class MergeSortTest {

	
	private int[] tab1;
	private int[] tab2;
	private final static int SIZE = 1000;
	
	@Before
	  public void init() throws Exception {
	    tab1 = new int[SIZE];
	    tab2 = new int[SIZE];
	    Random generator = new Random();
	    for (int i = 0; i < tab1.length; i++) {
	      tab1[i] = generator.nextInt(10000);
	      tab2[i]=tab1[i];
	    }
	}	
	@Test
	public void test() {
		long startTime = System.currentTimeMillis();

	    MergeSort sorter = new MergeSort();
	    sorter.sort(tab1);

	    long stopTime = System.currentTimeMillis();
	    long exeTime = stopTime - startTime;
	    System.out.println("Mergesort " + exeTime);
	    System.out.println("tab1: ");
	    for(int i:tab1){
            System.out.print(i);
            System.out.print(" ");
        }
	    
	    System.out.println("\n");
	    Arrays.sort(tab2);
	    System.out.println("tab2: ");
	    for(int i:tab2){
            System.out.print(i);
            System.out.print(" ");
        }
	    
	    assertTrue(Arrays.equals(tab1,tab2) );
	}

	@Test
	public void multitest() throws Exception {
		System.out.println("\n");
		System.out.println("Mergesort multi : \n");
		long startTime = System.currentTimeMillis();
		int m=100;
		int[] tabtest;
		tabtest = new int[SIZE];
		for (int j=0;j<m;j++)
		{
			Random generator = new Random();
		    for (int i = 0; i < tabtest.length; i++) {
		      tabtest[i] = generator.nextInt(1000);
		      
		    }
		
	    MergeSort sorter = new MergeSort();
	    sorter.sort(tabtest);

	    Arrays.sort(tabtest);

	    assertTrue(Arrays.equals(tab1,tab2) );
	  
		}
		  
	    long stopTime = System.currentTimeMillis();
	    long exeTime = stopTime - startTime;
	    System.out.println("le temps d'execution du Multi_Mergesort est: " + exeTime);
	}
}
