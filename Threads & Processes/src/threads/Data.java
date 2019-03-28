package threads;

/**
 * Holds data used by the threads.
 * 
 * @author STisb
 *
 */
public class Data
{
	public int[] array;
	
	
	/**
	 * Assigns variables.
	 * 
	 * @param threadCount The total number elements the array needs to hold.
	 */
	public Data(int threadCount)
	{
		array = new int[threadCount];
		
		init();
	}
	
	
	/**
	 * Assigns each element in the array to its position value.
	 */
	private void init()
	{
		for (int i = 0; i < array.length; i++)
		{
			array[i] = i;
		}
	}
}
