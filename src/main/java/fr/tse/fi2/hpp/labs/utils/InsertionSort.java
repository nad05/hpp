package fr.tse.fi2.hpp.labs.utils;

public class InsertionSort {

	static int[] tri_insertion(int[] tab)
	{
	   int i, j;
	   for (i = 1; i < tab.length; ++i) {
	       int elem = tab[i];
	       for (j = i; j > 0 && tab[j-1] > elem; j--)
	           tab[j] = tab[j-1];
	       tab[j] = elem;
	   }
	   return tab;
	}
	
	public static void affichage_tab(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
		{
			System.out.print(tab[i] + " ");
		}
		
		System.out.println();
	}
	
	public static void main(String[] args) 
	{
		int[] tab1 = {9, 1, 31, 5, 4, 9, 4, 3, 3, 2, 10};

		affichage_tab(tab1);
		affichage_tab(tri_insertion(tab1));

	}
}
