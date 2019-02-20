package packageClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{
	private int portNumber = 8080;
	
	private String portName = "localhost";
	
	private Socket client = null;
	
	private InputStream  input  = null;
	private OutputStream output = null;
	
	private DataInputStream  dataIn  = null;
	private DataOutputStream dataOut = null;
	
	public Client()
	{
		// Try to connect to the server
		try
		{
			client = new Socket(portName, portNumber);
		}
		catch (IOException e)
		{
			System.err.println("Client Error: Failed to connect to the server " + portName + " on port " + Integer.toString(portNumber));
		}
		
		// Server Info
		System.out.println("Client Info: Connected to server on port " + client.getRemoteSocketAddress().toString());
		
		// Setup the output streams
		try
		{
			output = client.getOutputStream();
			dataOut = new DataOutputStream(client.getOutputStream());
		}
		catch (IOException e)
		{
			System.err.println("Server Error: Error occurred in setting up output streams.");
			e.printStackTrace();
		}
		
		// Setup the input streams
		try
		{
			input = client.getInputStream();
			dataIn = new DataInputStream(client.getInputStream());
		}
		catch (IOException e)
		{
			System.err.println("Server Error: Error occurred in setting up input streams.");
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		System.out.println("Client Info: Waiting for user to send a command to the server.");
	}
	
	public static void main(String[] args)
	{
		Client client = new Client();
		
		client.run();
	}

}
