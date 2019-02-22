package packageServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import packageData.Data;

public class Server
{
	private int portNumber = 8080;
	
	private String portName = "localhost";
	
	private ServerSocket socket = null;
	
	private Socket server = null;
	
	private InputStream  input  = null;
	private OutputStream output = null;
	
	private DataInputStream  dataIn  = null;
	private DataOutputStream dataOut = null;
	
	private ObjectInputStream  objIn  = null;
	private ObjectOutputStream objOut = null;
	
	Data serverData = new Data();
	Data clientData = new Data();
	
	public Server()
	{
		// Setup a server on the port
		try
		{
			socket = new ServerSocket(portNumber);
		}
		catch (IOException e)
		{
			System.err.println("Server Info: Could not setup server on port:" + Integer.toString(portNumber));
			e.printStackTrace();
		}
		
		// Server Info
		System.out.println("Server Info: Server started, waiting for client on port " + Integer.toString(portNumber));
		
		// Accept your connection to the client
		try
		{
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
	
	public void run() throws IOException
	{
		// Server info
		System.out.println("Server Info: Waiting for client command.");
		
		try
		{
			// READ - Get the client data object
			clientData = (Data) objIn.readObject();
		} 
		catch (ClassNotFoundException e)
		{
			System.out.println("Server Error: Failed to cast client object to type Data.");
			e.printStackTrace();
		}
		
		switch (clientData.getCommand())
		{
			// UPLOAD from client to server
			case 1:
			{
				System.out.println("Server Info: Client requested file upload.");
			}
			// DOWNLOAD to client from server
			case 2:
			{
				
			}
			// DELETE a file on the server
			case 3:
			{
				
			}
			// RENAME a file on the server
			case 4:
			{
				
			}
			// An unknown command
			default:
			{
				System.err.println("System Error: Recieved the unsupported command: " + Integer.toString(clientData.getCommand()));
			}
		}
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
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		server.run();
	}
}
