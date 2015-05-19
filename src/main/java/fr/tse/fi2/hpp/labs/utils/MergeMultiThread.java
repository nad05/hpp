package fr.tse.fi2.hpp.labs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeMultiThread extends RecursiveTask<int []>{

	
	private static final int CUTOFF = 20;
	int[] tab;
	
	public  MergeMultiThread(int[] tab) {
		super();
		this.tab = new int[tab.length];
		System.arraycopy(tab, 0, this.tab, 0, tab.length);
	}
	
	@Override
	public int[] compute() {
		
		int len = this.tab.length;
		
		if (len <= CUTOFF)
		{
			return InsertionSort.tri_insertion(this.tab);
		}
		else
		{
			int[] lefttab = new int[len/2];
			int[] righttab = new int[len - len/2];
			
			System.arraycopy(this.tab, 0, lefttab, 0, len/2);
			System.arraycopy(this.tab, len/2, righttab, 0, len - len/2);
			List<MergeMultiThread> subtasks =
			        new ArrayList<MergeMultiThread>();
			
			MergeMultiThread subtask1 = new MergeMultiThread(lefttab);
			MergeMultiThread subtask2 = new MergeMultiThread(righttab);
			subtasks.add(subtask1);
	        subtasks.add(subtask2);
			
	        for(MergeMultiThread subtask : subtasks){
                subtask.fork();
            }
			
	        return MergeIsertionSort.mergetabs(subtask1.join(), subtask2.join());

		}
		
	}
	public static void main(String[] args) 
	{
		int[] tab1 = {9, 1, 31, 5, 4, 9, 6, 3, 7, 2, 10, 5, 5, 4, 9, 4, 3, 8, 2, 9, 1, 31, 5, 4, 9, 4, 3, 8, 2, 10, 5, 5, 4, 9, 4, 11, 3, 2};

		MergeMultiThread m1 = new MergeMultiThread(tab1);
		
		InsertionSort.affichage_tab(m1.compute());

	}
}
