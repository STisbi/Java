package threads;

import java.util.concurrent.CountDownLatch;

/**
 * This class demonstrates multi-threading while using the count down latch (CDL).
 * The main method creates two threads that add to different elements
 * of the array contained in the Data object. The threads then decrement from
 * the CDL. The main method in the main thread calls await, which waits for 
 * the CDL to reach 0, which when it does it resumes execution
 * 
 * @author STisb
 *
 */
class UserThread extends Thread
{
	public int[] array = new int[2];
	
	private int threadInt;
	
	private Thread thread;
	private String threadName;
	private Data data;
	private CountDownLatch latch;

	/**
	 * Constructor for this class, assigns variables.
	 * 
	 * @param name The name of this thread.
	 * @param threadInt The element number of the array to which to add to.
	 * @param data The {@link Data} object which holds the array to be added to.
	 * @param latch The Count Down Latch.
	 */
	UserThread(String name, int threadInt, Data data, CountDownLatch latch)
	{
		this.threadName = name;
		this.threadInt = threadInt;
		this.data = data;
		this.latch = latch;
		
		System.out.println("Creating " + this.threadName + " with threadInt " + this.data.array[threadInt]);
	}

	
	/**
	 * Called by JVM (I think?) when the thread is started. Adds to the element in the array
	 * contained within {@link Data} then decrements the CDL.
	 */
	public void run()
	{
		System.out.println("Running " + this.threadName);

		System.out.println("Thread: " + this.threadName + " adding 5 to threadInt ");
		
		this.data.array[threadInt] += 5;
		
		latch.countDown();
		System.out.println("Thread " + this.threadName + " exiting.");
	}

	
	/**
	 * Starts the thread.
	 */
	public void start()
	{
		System.out.println("Starting " + this.threadName);
		if (thread == null)
		{
			thread = new Thread(this, this.threadName);
			thread.start();
		}
	}
}


/**
 * Holds the main method from which to run.
 * 
 * @author STisb
 *
 */
public class CountDownLatchDemo
{
	final static int THREADCOUNT = 2;
	
	/**
	 * Creats and runs the specified number of threads.
	 * 
	 * @param args Command line arguments that are not used.
	 * 
	 * @throws InterruptedException Exception for if the thread is interrupted.
	 */
	public static void main(String args[]) throws InterruptedException
	{
		CountDownLatch latch = new CountDownLatch(THREADCOUNT);
		
		Data data = new Data(THREADCOUNT);
		
		UserThread[] threadArray = new UserThread[THREADCOUNT];
		
		// Before
		System.out.println("Before");
		for (int i = 0; i < THREADCOUNT; i++)
		{
			System.out.println(String.format("data.array[%d] = %d", i, data.array[i]));
		}
		System.out.println("\n___________________________________\n\n");
		
		// Create the threads
		for(int i = 0; i < THREADCOUNT; i++)
		{
			String threadName = "Thread_" + Integer.toString(data.array[i]);
			
			// Create a new thread, notice it needs the CDL
			UserThread newThread = new UserThread(threadName, data.array[i], data, latch);
			
			// Assign it to array and start it
			threadArray[i] = newThread;
			threadArray[i].start();
		}
		
		// The main thread calls this and waits for the CDL to reach 0. The child threads
		// all decrement the latch when the exit.
		latch.await();
		
		System.out.println("\n___________________________________\n\n");
		// After
		System.out.println("After");
		for (int i = 0; i < THREADCOUNT; i++)
		{
			System.out.println(String.format("data.array[%d] = %d", i, data.array[i]));
		}
	}
}