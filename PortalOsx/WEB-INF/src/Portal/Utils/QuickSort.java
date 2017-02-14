package Portal.Utils;

public class QuickSort
{

	public static void metodoQuickSort(String[] array)
	{
		quicksort(0, array.length - 1, array);
		//imprimeArray(array);
	}
	
//	private void imprimeArray(String[] array)
//	{
//		for (int i = 0; i < array.length; i++)
//		{
//			System.out.println(array[i]);
//		}
//	}
	private static void quicksort(int p, int q, String array[])
	{
		if (p < q)
		{
			int x = particao(p, q, array);
			quicksort(p, x - 1, array);
			quicksort(x + 1, q, array);
		}		
	}
		
	private static int particao(int p, int q, String array[]){
		 int j = p - 1;
		 String aux = array[q];
		 for (int i = p; i <= q; i++)
		 {
			 if (array[i].compareTo(aux)<=0) 
				 troca(array, i, ++j);
		 }
		 return j;		
	}
	
	 private static void troca(String[] a, int i, int j) {
	        String swap = a[i];
	        a[i] = a[j];
	        a[j] = swap;
	 }
	 
//	public static void main(String[] args)
//	{
//		//int[] array = {1000,125,125478,235,1,2,3,1,4,5,77,5,1,4,7,1,2,5,6,8,7,99,5,8,85};
//		String[] array = {"a","z","","9","0","2","x","t","5","K","g","p","b","o","i","c","n","m","c"};
//		new QuickSort(array);
//	}
}
