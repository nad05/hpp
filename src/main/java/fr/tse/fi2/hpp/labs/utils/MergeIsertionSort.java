package fr.tse.fi2.hpp.labs.utils;

public class MergeIsertionSort {

	
	public static int[] doMergeInsertionSort(int[] tab, final int CUTOFF)
	{
		int len = tab.length;
		
		if (len <= CUTOFF)
		{
			return InsertionSort.tri_insertion(tab);
		}
		
		else
		{
			int[] lefttab = new int[len/2];
			int[] righttab = new int[len - len/2];
			
			for(int k = 0; k < len/2; k++)
			{
				lefttab[k] = tab[k];
			}
			
			for(int k = 0; k < len - len/2; k++)
			{
				righttab[k] = tab[len/2 + k];
			}
			
			return mergetabs(doMergeInsertionSort(lefttab, CUTOFF), doMergeInsertionSort(righttab, CUTOFF));
			
		}
	}
	
	public static int[] mergetabs(int [] tab1, int[] tab2)
	{
		int taille = tab1.length + tab2.length;
		int[] result = new int[taille];
		
		int i = 0;
		int j = 0;
		
		while( i < tab1.length && j < tab2.length)
		{
			if (tab1[i] <= tab2[j])
			{
				result[i+j] = tab1[i];
				i++;
			}
			else
			{
				result[i+j] = tab2[j];
				j++;
			}
		}
		
		while( i < tab1.length)
		{
			result[i+j] = tab1[i];
			i++;
		}
		
		while( j < tab2.length)
		{
			result[i+j] = tab2[j];
			j++;
		}
		
		return result;
	}
	
	
	public static void main(String[] args) 
	{
		int[] tab1 = {9, 1, 31, 5, 4, 9, 6, 3, 7, 2, 10, 5, 5, 4, 9, 4, 3, 8, 2,9, 1, 31, 5, 4, 9, 4, 3, 8, 2, 10, 5, 5, 4, 9, 4,11, 3, 2,};

		InsertionSort.affichage_tab(tab1);
		InsertionSort.affichage_tab(doMergeInsertionSort(tab1, 19));

	}
}
