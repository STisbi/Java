package threads;

public class RunnableThread
{
	static final int THREAD_COUNT = 10;
	
	Thread[] threads = new Thread[THREAD_COUNT];
	
	public static void main(String[] args) throws InterruptedException
	{
		RunnableThread runThreads = new RunnableThread();
		
		for (int i = 0; i < THREAD_COUNT; i++)
		{
			runThreads.threads[i] = new Thread(new RunThread(Integer.toString(i)));	
			runThreads.threads[i].start();
		}
		
		
		for (int i = 0; i < THREAD_COUNT; i++)
		{
			runThreads.threads[i].join();
		}
		
		
		System.out.println("Done.");
	}
}

class RunThread implements Runnable
{
	String myName = new String();
	
	public RunThread(String myName)
	{
		this.myName = myName;
	}

	@Override
	public void run()
	{
		System.out.println("My name is " + this.myName);
	}
}
