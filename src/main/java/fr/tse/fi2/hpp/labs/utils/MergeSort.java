package fr.tse.fi2.hpp.labs.utils;

public class MergeSort {

	
	private int[] tab;
    private int[] tmptab;
    private int length;
    
    public void sort(int inputArr[]) {
        this.tab = inputArr;
        this.length = inputArr.length;
        this.tmptab = new int[length];
        doMergeSort(0, length - 1);
    }

    private void doMergeSort(int lower, int higher) {
        
        if (lower < higher) {
            int middle = lower + (higher - lower) / 2;
            doMergeSort(lower, middle);
            doMergeSort(middle + 1, higher);
            merge(lower, middle, higher);
        }
    }
    
    private void merge(int lower, int middle, int higher) {
    	 
        for (int i = lower; i <= higher; i++) {
            tmptab[i] = tab[i];
        }
        int i = lower;
        int j = middle + 1;
        int k = lower;
        while (i <= middle && j <= higher) {
            if (tmptab[i] <= tmptab[j]) {
                tab[k] = tmptab[i];
                i++;
            } else {
                tab[k] = tmptab[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            tab[k] = tmptab[i];
            k++;
            i++;
        }
 
    }
    
    public static void main(String a[]){
        
        int[] inputArr = {45,23,11,89,77,98,4,28,65,43};
        MergeSort ms = new MergeSort();
        ms.sort(inputArr);
        for(int i:inputArr){
            System.out.print(i);
            System.out.print(" ");
        }
    }

}
