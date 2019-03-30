package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import data.Data;

public class ClientProcess
{
	static final long PARENT_PID = ProcessHandle.current().parent().get().pid();
	static final long MY_PID     = ProcessHandle.current().pid();

	static final String PORT_NAME = "localhost";

	int portNumber = 8080;
	
	int command = 0;
	
	String className = "";
	String clientInfo  = "Process " + MY_PID + ": ";
	String clientError = "Process " + MY_PID + ": ";
	
	ArrayList<File> fileList = new ArrayList<File>();

	Scanner reader = new Scanner(System.in);

	Socket client = null;

	InputStream  input  = null;
	OutputStream output = null;
	
	FileOutputStream fileOut = null;

	DataInputStream  dataIn  = null;
	DataOutputStream dataOut = null;

	ObjectInputStream  objIn  = null;
	ObjectOutputStream objOut = null;
	
	Data data = new Data();
	
	public ClientProcess()
	{
		data.setClientPID(MY_PID);
		
		this.className = this.getClass().getName();	
	}
	
	public int getPortNumber()
	{
		return this.portNumber;
	}
	
	
	public void setPortNumber(int portNumber)
	{
		this.portNumber = portNumber;
	}
	
	
	public void run() throws IOException, ClassNotFoundException
	{			
		// WRITE - OBJECT - Give the server this process's PID
		objOut.writeObject(data);
		System.out.println(clientInfo + "WRITE");
		
		// READ - OBJECT - The server acknowledgement
		data = (Data) objIn.readObject();
		System.out.println(clientInfo + "READ: " + data.getMessage());
	}

	
	public void close() throws IOException
	{
		// Client info
		System.out.println(clientInfo + "Closing streams, sockets, and the client connection.");

		// Close system input
		reader.close();

		// Close data streams
		dataIn.close();
		dataOut.close();

		// Close object streams
		objIn.close();
		objOut.close();

		// Close general streams
		input.close();
		output.close();

		// Close the client
		client.close();
	}
	
	
	public String getClassName()
	{
		return this.className;
	}
	
	
	public void setupConnection()
	{
		// Try to connect to the server
		try
		{
			client = new Socket(PORT_NAME, portNumber);
		} 
		catch (IOException e)
		{
			System.err.println(clientError + "Failed to connect to the server " + PORT_NAME + " on port " + Integer.toString(portNumber));
		}

		// Server Info
		System.out.println(clientInfo + "Connected to server on port " + client.getRemoteSocketAddress().toString());

		// Setup the output streams
		try
		{
			output = client.getOutputStream();
			dataOut = new DataOutputStream(client.getOutputStream());
			objOut = new ObjectOutputStream(client.getOutputStream());
		} 
		catch (IOException e)
		{
			System.err.println(clientError + "Error occurred in setting up output streams.");
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
			System.err.println(clientError + "Error occurred in setting up input streams.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * The actual client which communicates with the server
	 * 
	 * @param args The port through which to communicate with the server.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		System.out.println("Subprocess PID " + MY_PID + " created by " + PARENT_PID);
		
		ClientProcess clientProc = new ClientProcess();
		
		if (args != null && args.length > 0)
		{
			clientProc.setPortNumber(Integer.parseInt(args[0]));
		}
		
		// This is a separate function to allow the Client to call this constructor without setting up a connection
		// Otherwise a bug is introduced whereby the subprocess hangs on setting up a connection because the Client already set it up
		clientProc.setupConnection();
		
		clientProc.run();
		
		clientProc.close();
		
		System.out.println(MY_PID + ": Exiting process.");
	}
}

