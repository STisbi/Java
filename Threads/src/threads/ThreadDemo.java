package threads;

class UserThread implements Runnable
{
	private Thread t;
	private String threadName;

	UserThread(String name)
	{
		threadName = name;
		System.out.println("Creating " + threadName);
	}

	public void run()
	{
		System.out.println("Running " + threadName);
		try
		{
			for (int i = 4; i > 0; i--)
			{
				System.out.println("Thread: " + threadName + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(50);
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Thread " + threadName + " interrupted.");
			e.printStackTrace();
		}
		
		System.out.println("Thread " + threadName + " exiting.");
	}

	public void start()
	{
		System.out.println("Starting " + threadName);
		if (t == null)
		{
			t = new Thread(this, threadName);
			t.start();
		}
	}
}

public class ThreadDemo
{

	public static void main(String args[])
	{
		UserThread R1 = new UserThread("Thread-1");
		R1.start();

		UserThread R2 = new UserThread("Thread-2");
		R2.start();
	}
}