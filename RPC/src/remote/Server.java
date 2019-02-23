package remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import data.Data;

public class Server
{
	private int command = 0;
	
	private final int portNumber = 8080;
	
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
		System.out.print("\nServer Info: Server started.");
	}
	
	public void run() throws IOException
	{
		connectionSetup();
		
		// Server info
		System.out.println("Server Info: Waiting for client command.");
		
		// READ - Get the client command
		command = input.read();
		
		// Set the command received from the client as an acknowledgement within Data
		serverData.setCommand(command);
		
		// WRITE - Send the acknowledgement contained within the data object
		objOut.writeObject(serverData);
		objOut.flush();
		
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
		
		switch (command)
		{
			// Get Pi
			case 1:
			{
				System.out.println("Server Info: Sending the value of Pi");
				
				calculate_pi();
				
				break;
			}
			// DOWNLOAD to client from server
			case 2:
			{
				System.out.println("Server Info: Calculating the sum of: " + clientData.getAddend1() + " and " + clientData.getAddend2());
				
				add(clientData.getAddend1(), clientData.getAddend2());
				
				break;
			}
			// DELETE a file on the server
			case 3:
			{
				System.out.println("Server Info: Sorting the given array.");
				
				sort(clientData.getArrayA());
				
				break;
			}
			// RENAME a file on the server
			case 4:
			{
				System.out.println("Server Info: Multiplying the given matrices.");
				
				
				break;
			}
			// An unknown command
			default:
			{
				System.err.println("Server Error: Recieved the unsupported command: " + Integer.toString(command));
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
	
	private void connectionSetup()
	{
		// Accept connection to the client
		try
		{
			System.out.println("\nServer Info: Waiting for client on port " + Integer.toString(portNumber));
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
	
	private void calculate_pi() throws IOException
	{
		Data piData = new Data();
		
		piData.setPi(Math.PI);
		
		// WRITE - Send the value of Pi back to the client
		objOut.writeObject(piData);
	}
	
	private void add(int i, int j) throws IOException
	{
		Data sumData = new Data();
		
		sumData.setSum(i + j);
		
		// WRITE - Send the sum back
		objOut.writeObject(sumData);
	}
	
	private void sort(int[] arrayA) throws IOException
	{
		Data sortedData = new Data();
		
		Arrays.sort(arrayA);
		
		sortedData.setArrayA(arrayA);
		
		// WRITE - Send the sorted array back
		objOut.writeObject(sortedData);
	}
	
	
	public static void main(String [] args) throws IOException
	{
		Server server = new Server();
		
		while (true)
		{
			server.run();
		}
		
		// server.close();

	}
}
