package server;

import java.io.IOException;

import data.Utilities;

public class Server
{
	ServerThread[] threads = new ServerThread[Utilities.THREADCOUNT];
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		for (int i = 0; i < Utilities.THREADCOUNT; i++)
		{
			server.threads[i] = new ServerThread(Integer.toString(Utilities.PORTS[i]));
			
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

