package packageClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import packageData.Data;

public class Client
{
	private int portNumber = 8080;

	private String portName = "localhost";

	private Scanner reader = new Scanner(System.in);

	private Socket client = null;

	private InputStream input = null;
	private OutputStream output = null;

	private DataInputStream dataIn = null;
	private DataOutputStream dataOut = null;

	private ObjectInputStream objIn = null;
	private ObjectOutputStream objOut = null;
	
	Data clientData = new Data();
	Data serverData = new Data();

	public Client()
	{
		// Try to connect to the server
		try
		{
			client = new Socket(portName, portNumber);
		} 
		catch (IOException e)
		{
			System.err.println("Client Error: Failed to connect to the server " + portName + " on port "
			        + Integer.toString(portNumber));
		}

		// Server Info
		System.out.println("Client Info: Connected to server on port " + client.getRemoteSocketAddress().toString());

		// Setup the output streams
		try
		{
			output = client.getOutputStream();
			dataOut = new DataOutputStream(client.getOutputStream());
			objOut = new ObjectOutputStream(client.getOutputStream());
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
			objIn = new ObjectInputStream(client.getInputStream());
		} 
		catch (IOException e)
		{
			System.err.println("Server Error: Error occurred in setting up input streams.");
			e.printStackTrace();
		}
	}

	public void run() throws IOException
	{
		// Client info
		System.out.println("Client Info: Waiting for user to send a command to the server.");
		
		// Ask the user for input
		System.out.println("Please select an option by entering the corresponding number:\n" + 
		                   "UPLOAD\n"   +
						   "DOWNLOAD\n" +
		                   "DELETE\n"   + 
						   "RENAME");
		
		// Get user input
		clientData.setCommand(reader.nextInt());
		
		// WRITE - Send the data to the server
		objOut.writeObject(clientData);
	}

	public void close() throws IOException
	{
		// Client info
		System.out.println("Client Info: Closing streams, sockets, and the client connection.");

		// Close system input
		reader.close();

		// Close data streams
		dataIn.close();
		dataOut.close();

		// Close object streams
		objIn.close();
		objOut.close();

		// Close socket streams
		input.close();
		output.close();

		// Close the client
		client.close();
	}

	public static void main(String[] args) throws IOException
	{
		Client client = new Client();

		client.run();

		client.close();
	}

}
