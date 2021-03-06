package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import data.Data;
import data.Utilities;

public class ServerThread extends Thread
{	
	static final String NEWLINE = System.lineSeparator();
	
	int portNumber = 0;
	
	String threadName = "";
	String serverInfo = "";
	String serverError = "";
	
	Semaphore semaphore = null;
	
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
	
	File file = null;
	
	FileWriter fileWriter = null;
	
	public ServerThread(String threadName, Semaphore semaphore, File file)
	{
		this.file        = file;
		this.semaphore   = semaphore;
		this.threadName  = threadName;
		this.portNumber  = Integer.parseInt(threadName);
		this.serverInfo  = "Thread " + threadName + ": ";
		this.serverError = "Thread " + threadName + ": ";
	}
	
	
	public void run()
	{
		setupConnection();
		
		for (int x = 0; x < Utilities.LOOP_COUNT; x++)
		{
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
			
			System.out.println(serverInfo + "READ: " + "Client's PID is " + clientPID);
			
			////////////////////////////
			//                        //
			// CRITICAL SECTION START //
			//                        //
			////////////////////////////
			
			try
			{
				// Blocks if no permits are available
				this.semaphore.acquire();
			}
			catch (InterruptedException e1)
			{
				System.err.println(serverError + "Interrupted while waiting to acquire semaphore.");
				e1.printStackTrace();
			}
			
			try
			{
				// true allows appending to the file instead of overwriting
				fileWriter = new FileWriter(file, true);
				
				// Writes to the file this thread's ID, the client's PID, and the current count (post-increment)
				fileWriter.append(serverInfo + "Process: " + Long.toString(clientPID) + " Counter: " + Server.counter++ + Utilities.NEWLINE);
				
				fileWriter.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			// Release the semaphore
			this.semaphore.release();
			
			//////////////////////////
			//                      //
			// CRITICAL SECTION END //
			//                      //
			//////////////////////////
			
			Data outData = new Data();
			
			// WRITE - OBJECT - Send an acknowledgement message back to the client
			outData.setMessage("File accessed.");
			try
			{
				objOut.writeObject(outData);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			System.out.println(serverInfo + "WRITE");
		}
		
		try
		{
			closeConnection();
		}
		catch (IOException e)
		{
			System.err.println(serverError + "Failed to close connection on port:" + Integer.toString(portNumber));
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
