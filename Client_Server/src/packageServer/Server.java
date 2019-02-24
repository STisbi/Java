package packageServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;

import packageData.Data;

public class Server
{
	private int command = 0;
	
	private final int portNumber = 8080;
	
	private final String filePath = "C:\\Users\\STisb\\git\\Java\\Client_Server\\src\\packageServer\\Files\\";
	
	private ArrayList<File> fileList = new ArrayList<File>();
	
	private ServerSocket socket = null;
	
	private Socket server = null;
	
	private InputStream  input  = null;
	private OutputStream output = null;
	
	private FileOutputStream fileOut = null;
	
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
		
		updateFileList();
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
			// UPLOAD from client to server
			case 1:
			{
				System.out.println("Server Info: Receiving " + clientData.getFileName());
				
				// Create a file stream, write the file from the client data to the server, then close the stream
				fileOut = new FileOutputStream(this.filePath + clientData.getFileName());
				fileOut.write(clientData.getFileData());
				fileOut.close();
				
				break;
			}
			// DOWNLOAD to client from server
			case 2:
			{
				System.out.println("Server Info: Sending " + clientData.getFileName());
				
				// A new Data object has to be sent, otherwise the client receives null
				// I'm not sure why this is
				Data fileData = new Data();
				
				Path path = null;
				for (File file : fileList)
				{
					if (file.getName().equalsIgnoreCase(clientData.getFileName()))
					{
						path = file.toPath();
					}
				}
				
				fileData.setFileName(clientData.getFileName());
				fileData.setFileData(path);
				
				// WRITE - Test 3
				objOut.writeObject(fileData);
				objOut.flush();
				
				break;
			}
			// DELETE a file on the server
			case 3:
			{
				System.out.println("Server Info: Deleting " + clientData.getFileName());
				
				for (File file : fileList)
				{
					if (file.getName().equalsIgnoreCase(clientData.getFileName()))
					{
						file.delete();
					}
				}
				
				break;
			}
			// RENAME a file on the server
			case 4:
			{
				System.out.println("Server Info: Renaming " + clientData.getFileName() + " to " + clientData.getNewFileName());
				
				for (File file : fileList)
				{
					if (file.getName().equalsIgnoreCase(clientData.getFileName()))
					{
						File newFile = new File(filePath + clientData.getNewFileName());
						
						file.renameTo(newFile);
					}
				}
				
				break;
			}
			// An unknown command
			default:
			{
				System.err.println("Server Error: Recieved the unsupported command: " + Integer.toString(command));
			}
		}
		
		updateFileList();
		printFilesOnServer();
		
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
	
	private void updateFileList()
	{
		File folder = new File(filePath);
		ArrayList<File> fileList = new ArrayList<File>();
		
		// Add files from the file folder 
		for (File file : folder.listFiles())
		{
			String ext = file.getName().substring(file.getName().indexOf("."));
			
			// Don't add Java or Class files
			if (!ext.equalsIgnoreCase("java") || !ext.equalsIgnoreCase("class"))
			{
				fileList.add(file);
			}
		}
		
		// Update the local copy of all the files on the server
		this.fileList = fileList;
		
		// Update the Data object that the client gets with all the files on the server
		serverData.setServerFiles(fileList);
	}
	
	private void printFilesOnServer()
	{
		System.out.println("\nFiles currently on server:");
		
		int i = 0;
		
		for (File file : fileList)
		{
			System.out.println(Integer.toString(++i) + ". " + file.getName());
		}
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
