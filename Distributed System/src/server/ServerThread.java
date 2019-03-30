package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import data.Data;

public class ServerThread extends Thread
{	
	static final String NEWLINE = System.lineSeparator();
	
	int portNumber = 0;
	
	String threadName = "";
	String serverInfo = "";
	String serverError = "";
	
	ServerSocket socket = null;
	
	Socket server = null;
	
	InputStream  input  = null;
	OutputStream output = null;
	
	FileOutputStream fileOut = null;
	
	DataInputStream  dataIn  = null;
	DataOutputStream dataOut = null;
	
	ObjectInputStream  objIn  = null;
	ObjectOutputStream objOut = null;
	
	Data data = new Data();
	
	
	public ServerThread(String threadName)
	{
		this.threadName  = threadName;
		this.portNumber  = Integer.parseInt(threadName);
		this.serverInfo  = "Thread " + threadName + ": ";
		this.serverError = "Thread " + threadName + ": ";
	}
	
	
	public void run()
	{
		setupConnection();
		
		// Server info
		System.out.println(serverInfo + "Waiting for client command.");
		
		long clientPID = 0;
		
		// READ - OBJECT - Get the client's PID
		try
		{
			clientPID = ((Data) objIn.readObject()).getClientPID();
		}
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("READ: " + "Client's PID is " + clientPID);
		
		try
		{
			closeConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void closeConnection() throws IOException
	{
		// Client info
		System.out.println(serverInfo + "Closing streams, sockets, and the server connection.");
		
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
		server.close();
	}
	
	
	private void setupConnection()
	{
		// Setup a server on the port
		try
		{
			socket = new ServerSocket(portNumber);
		}
		catch (IOException e)
		{
			System.err.println(serverError + "Could not setup server on port:" + Integer.toString(portNumber));
			e.printStackTrace();
		}
		
		// Server Info
		System.out.println(serverInfo + "Server started on port: " + portNumber);
		
		// Accept connection to the client
		try
		{
			System.out.println(NEWLINE + serverInfo + "Waiting for client on port " + Integer.toString(portNumber));
			server = socket.accept();
		}
		catch (IOException e)
		{
			System.err.println(serverError + "Could not accept connection to the client.");
			e.printStackTrace();
		}
		
		// Server info
		System.out.println(serverInfo + "Accepted connection to client on port " + server.getRemoteSocketAddress().toString());
		
		// Setup the output streams
		try
		{
			output  = server.getOutputStream();
			dataOut = new DataOutputStream(server.getOutputStream());
			objOut  = new ObjectOutputStream(server.getOutputStream());
		}
		catch (IOException e)
		{
			System.err.println(serverError + "Error occurred in setting up output streams.");
			e.printStackTrace();
		}
		
		// Setup the input streams
		try
		{
			input  = server.getInputStream();
			dataIn = new DataInputStream(server.getInputStream());
			objIn  = new ObjectInputStream(server.getInputStream()); 
		}
		catch (IOException e)
		{
			System.err.println(serverError + "Error occurred in setting up input streams.");
			e.printStackTrace();
		}
	}
}
