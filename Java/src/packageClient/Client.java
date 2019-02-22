package packageClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import packageData.Data;

public class Client
{
	private int command = 0;
	
	private final int portNumber = 8080;

	private final String portName = "localhost";
	private final String filePath = "C:\\Users\\STisb\\git\\Java\\Java\\src\\packageClient\\Files\\";
	
	private ArrayList<File> fileList = new ArrayList<File>();
	
	private final HashMap<Integer, String> cmdList = new HashMap<Integer, String>();

	private Scanner reader = new Scanner(System.in);

	private Socket client = null;

	private InputStream  input  = null;
	private OutputStream output = null;
	
	private FileInputStream  fileIn  = null;
	private FileOutputStream fileOut = null;

	private DataInputStream  dataIn  = null;
	private DataOutputStream dataOut = null;

	private ObjectInputStream  objIn  = null;
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
		
		cmdList.put(1, "UPLOAD");
		cmdList.put(2, "DOWNLOAD");
		cmdList.put(3, "DELETE");
		cmdList.put(4, "RENAME");
		
		// Get the list of files currently in the file folder
		updateFileList();
	}

	public void run() throws IOException
	{
		// Client info
		System.out.println("Client Info: Waiting for user to send a command to the server.");
		
		// Ask the user for input
		System.out.println("\nPlease select an option by entering the corresponding number:\n" + 
		                   "UPLOAD\n"   +
						   "DOWNLOAD\n" +
		                   "DELETE\n"   + 
						   "RENAME");
		
		// Get user input
		command = reader.nextInt();
		
		// WRITE - Send the command to the server
		output.write(command);
		
		// READ - Get the server acknowledgement
		try
		{
			// READ - Get the server data object
			serverData = (Data) objIn.readObject();
		} 
		catch (ClassNotFoundException e)
		{
			System.out.println("Client Error: Failed to cast server object to type Data.");
			e.printStackTrace();
		}
		
		// If the server responds with the same command, it was acknowledged properly
		if (serverData.getCommand() == command)
		{
			switch (command)
			{
				// UPLOAD
				case 1:
				{	
					System.out.println("Enter the number corresponding to the file to be uploaded.");
					
					for (int i = 0; i < this.fileList.size(); i++)
					{
						System.out.println(Integer.toString(i + 1) + ". " + fileList.get(i));
					}
					
					// Get the user's file choice
					int choice = reader.nextInt() - 1;
					
					// Set the corresponding file and file name in the data
					clientData.setFileData(fileList.get(choice).toPath());
					clientData.setFileName(fileList.get(choice).getName());
					
					System.out.println("Client Info: Uploading " + clientData.getFileName());
					
					break;
				}
				// DOWNLOAD
				case 2:
				{
					break;
				}
				// DELETE
				case 3:
				{
					break;
				}
				// RENAME
				case 4:
				{
					break;
				}
				// An unknown command
				default:
				{
					System.err.println("Client Error: Recieved the unsupported command: " + Integer.toString(command));
				}
			}
			
			// WRITE - Send the data to the server
			objOut.writeObject(clientData);
		}
		else
		{
			// Client info
			System.err.println("Client Error: The client requested " + cmdList.get(command) + " but the server imporperly acknowledge a " + cmdList.get(serverData.getCommand()));
		}
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
		
		this.fileList = fileList;
	}

	public static void main(String[] args) throws IOException
	{
		Client client = new Client();

		client.run();

		client.close();
	}

}
