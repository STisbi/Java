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

public class Server
{
	static final int PORT_NUMBER = 8080;
	
	static final String NEWLINE   = System.lineSeparator();
	static final String FILE_PATH = System.getProperty("user.dir") + "\\src\\packageServer\\Files\\";
	
	int command = 0;
	
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
	
	public Server()
	{
		// Setup a server on the port
		try
		{
			socket = new ServerSocket(PORT_NUMBER);
		}
		catch (IOException e)
		{
			System.err.println("Server Info: Could not setup server on port:" + Integer.toString(PORT_NUMBER));
			e.printStackTrace();
		}
		
		// Server Info
		System.out.print(NEWLINE + "Server Info: Server started.");
	}
	
	public void run() throws IOException
	{
		// Server info
		System.out.println("Server Info: Waiting for client command.");
		
		long clientPID = 0;
		
		// READ - OBJECT - Get the client's PID
		try
		{
			clientPID = ((Data) objIn.readObject()).getClientPID();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("READ: " + "Client's PID is " + clientPID);
	}
	
	public void close() throws IOException
	{
		// Client info
		System.out.println("Server Info: Closing streams, sockets, and the server connection.");
		
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
		// Accept connection to the client
		try
		{
			System.out.println("\nServer Info: Waiting for client on port " + Integer.toString(PORT_NUMBER));
			server = socket.accept();
		}
		catch (IOException e)
		{
			System.err.println("Server Info: Could not accept connection to the client.");
			e.printStackTrace();
		}
		
		// Server info
		System.out.println("Server Info: Accepted connection to client on port " + server.getRemoteSocketAddress().toString());
		
		// Setup the output streams
		try
		{
			output  = server.getOutputStream();
			dataOut = new DataOutputStream(server.getOutputStream());
			objOut  = new ObjectOutputStream(server.getOutputStream());
		}
		catch (IOException e)
		{
			System.err.println("Server Error: Error occurred in setting up output streams.");
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
			System.err.println("Server Error: Error occurred in setting up input streams.");
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		server.setupConnection();
		
		while (true)
		{
			server.run();
		}
		
		// server.close();

	}
}

