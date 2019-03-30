package client;

import java.io.File;

import data.Utilities;

public class Client
{
	static final long MY_PID     = ProcessHandle.current().pid();
	
	static final String JAVAHOME  = System.getProperty("java.home");
	static final String CLASSPATH = System.getProperty("java.class.path");
	static final String NEWLINE   = System.lineSeparator();
	static final String SEPARATOR = File.separator;
	
	static final String JAVABIN = JAVAHOME + SEPARATOR + "bin" + SEPARATOR + "java";
	
	ClientProcess[] processes = new ClientProcess[Utilities.PROCESSCOUNT];
	
	ProcessBuilder processBuilder = null;
	
	Process process = null;

	public void CreateSubprocess(String subProgram, int portNumber)
	{
		try
		{
			// https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
	        this.processBuilder = new ProcessBuilder(JAVABIN, "-cp", CLASSPATH, subProgram, Integer.toString(portNumber));
	        
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
		Client mainProc = new Client();
		
		for (int i = 0; i < Utilities.PROCESSCOUNT; i++)
		{
			// This class will be executed as a child process
			mainProc.processes[i] = new ClientProcess();
			
			// Create a child process
			mainProc.CreateSubprocess(mainProc.processes[i].getClassName(), Utilities.PORTS[i]);
		}
		
		try
		{
			// Wait for all child processes to die
			mainProc.process.waitFor();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.out.println(MY_PID + ": Exiting process.");
	}
}
