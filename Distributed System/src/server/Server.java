package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import data.Utilities;

public class Server
{
	final String PATH_TO_FILES  = Utilities.USERDIR + Utilities.PATH_SEPARATOR + "src" + Utilities.PATH_SEPARATOR + "serverFiles";
	
	Semaphore semaphore = new Semaphore(Utilities.PERMITS, true);
	
	ArrayList<File> fileList = new ArrayList<File>();
	
	ServerThread[] threads = new ServerThread[Utilities.THREADCOUNT];
	
	
	private void updateFileList()
	{
		File folder = new File(PATH_TO_FILES);
		ArrayList<File> fileList = new ArrayList<File>();
		
		// Add files from the file folder 
		for (File file : folder.listFiles())
		{
			String ext = file.getName().substring(file.getName().indexOf("."));
			
			// Don't add Java or Class files
			if (!ext.equalsIgnoreCase("java") || !ext.equalsIgnoreCase("class"))
			{
				fileList.add(file);
			}
		}
		
		// Update the local copy of all the files on the server
		this.fileList = fileList;
	}
	
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		// Create a list of files available to the server
		server.updateFileList();
		
		for (int i = 0; i < Utilities.THREADCOUNT; i++)
		{
			server.threads[i] = new ServerThread(Integer.toString(Utilities.PORTS[i]), server.semaphore, server.fileList.get(0));
			
			server.threads[i].start();
		}
		
		try
		{
			for (int i = 0; i < Utilities.THREADCOUNT; i++)
			{
				if (server.threads[i] != null)
				{
					server.threads[i].join();	
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}

