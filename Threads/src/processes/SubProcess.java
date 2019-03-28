package processes;

import java.util.concurrent.TimeUnit;

public class SubProcess
{
	private static final long PARENT_PID = ProcessHandle.current().parent().get().pid();
	private static final long MY_PID     = ProcessHandle.current().pid();
	
	private String className = "";
	
	
	public SubProcess()
	{
		this.className = this.getClass().getName();
	}
	
	
	public String getClassName()
	{
		return this.className;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println("Subprocess PID " + MY_PID + " created by " + PARENT_PID);
		
		int sleepTime = 10;
		try
		{
			for (int i = sleepTime; i > -1; i--)
			{
				System.out.println(String.format(MY_PID + ": waking up to die in %d", i));
				TimeUnit.SECONDS.sleep(1);
			}
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(MY_PID + ": Exiting process.");
	}
}
