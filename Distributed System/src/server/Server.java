package server;

import java.io.IOException;

public class Server
{
	static final int THREADCOUNT = 3;
	
	static final int[] PORTS = {8080, 8081, 8082};
	
	ServerThread[] threads = new ServerThread[THREADCOUNT];
	
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		for (int i = 0; i < THREADCOUNT; i++)
		{
			server.threads[i] = new ServerThread(Integer.toString(PORTS[i]));
			
			server.threads[i].start();
		}
		
		try
		{
			for (int i = 0; i < THREADCOUNT; i++)
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

