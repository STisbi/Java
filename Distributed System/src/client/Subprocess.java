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

public class Subprocess
{
	static final int PORT_NUMBER = 8080;
	
	static final long PARENT_PID = ProcessHandle.current().parent().get().pid();
	static final long MY_PID     = ProcessHandle.current().pid();

	static final String PORT_NAME = "localhost";
	static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\packageClient\\Files\\";
	
	int command = 0;
	
	String className = "";
	
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
	
	public Subprocess()
	{
		data.setClientPID(MY_PID);
		
		this.className = this.getClass().getName();	
	}
	
	public void run() throws IOException
	{
		System.out.println("Client Info: Running.");
		
		// WRITE - OBJECT - Give the server this process's PID
		objOut.writeObject(data);
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
			client = new Socket(PORT_NAME, PORT_NUMBER);
		} 
		catch (IOException e)
		{
			System.err.println("Client Error: Failed to connect to the server " + PORT_NAME + " on port " + Integer.toString(PORT_NUMBER));
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
		
		System.out.println("Client Info: Finished setup.");
	}
	
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Subprocess PID " + MY_PID + " created by " + PARENT_PID);
		
		Subprocess subProc = new Subprocess();
		
		// This is a separate function to allow the Client to call this constructor without setting up a connection
		// Otherwise a bug is introduced whereby the client hangs on setting up a connection
		subProc.setupConnection();
		
		subProc.run();
		
		subProc.close();
		
		System.out.println(MY_PID + ": Exiting process.");
	}
}

