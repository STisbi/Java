package processes;

import java.io.File;

public class ProcessDemo
{
	private static final long MY_PID     = ProcessHandle.current().pid();
	
	private static final String JAVAHOME  = System.getProperty("java.home");
	private static final String CLASSPATH = System.getProperty("java.class.path");
	private static final String NEWLINE   = System.lineSeparator();
	private static final String SEPARATOR = File.separator;
	
	private static final String JAVABIN = JAVAHOME + SEPARATOR + "bin" + SEPARATOR + "java";
	
	public ProcessBuilder processBuilder = null;
	
	public Process process = null;

	public void CreateSubprocess(String subProgram)
	{
		try
		{
			// https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
	        this.processBuilder = new ProcessBuilder(JAVABIN, "-cp", CLASSPATH, subProgram);
	        
	        this.process = this.processBuilder.inheritIO().start();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Main Process PID: " + MY_PID);
		
		// The current process that will spawn a child process
		ProcessDemo mainProc = new ProcessDemo();
		
		// This class will be executed as a child process
		SubProcess  subProc  = new SubProcess();
		
		// Create a child process
		mainProc.CreateSubprocess(subProc.getClassName());
		
		try
		{
			// Wait for the child process to die
			mainProc.process.waitFor();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println(MY_PID + ": Exiting process.");
	}
}
