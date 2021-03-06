package packageClient;

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
import java.util.HashMap;
import java.util.Scanner;
import packageData.Data;

public class Client
{
	private int command = 0;
	
	private final int portNumber = 8080;

	private final String portName = "localhost";
	private final String filePath = System.getProperty("user.dir") + "\\src\\packageClient\\Files\\";
	
	private ArrayList<File> fileList = new ArrayList<File>();
	
	private final HashMap<Integer, String> cmdList = new HashMap<Integer, String>();

	private Scanner reader = new Scanner(System.in);

	private Socket client = null;

	private InputStream  input  = null;
	private OutputStream output = null;
	
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
		                   "1. UPLOAD\n"   +
						   "2. DOWNLOAD\n" +
		                   "3. DELETE\n"   + 
						   "4. RENAME");
		
		// Get user input
		command = reader.nextInt();
		
		// WRITE - Send the command to the server
		output.write(command);
		output.flush();
		
		// Get the server acknowledgement
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
					
					// WRITE - Send the file to the server
					objOut.writeObject(clientData);
					objOut.flush();
					
					break;
				}
				// DOWNLOAD
				case 2:
				{
					System.out.println("Enter the number corresponding to the file to be downloaded.");
					
					for (int i = 0; i < serverData.getServerFiles().size(); i++)
					{
						System.out.println(Integer.toString(i + 1) + ". " + serverData.getServerFiles().get(i).getName());
					}
					
					// Get the user's file choice
					int choice = reader.nextInt() - 1;
					
					// Set the file name to be downloaded
					clientData.setFileName(serverData.getServerFiles().get(choice).getName());
					
					// WRITE - Send this out to the server
					objOut.writeObject(clientData);
					objOut.flush();
					
					System.out.println("Client Info: Downloading " + clientData.getFileName());
					
					try
					{
						// READ - Get the file from the server
						serverData = (Data) objIn.readObject();
					}
					catch (ClassNotFoundException e)
					{
						System.err.println("Client Error: Failed to cast server object to type Data.");
						e.printStackTrace();
					}
					
					fileOut = new FileOutputStream(this.filePath + serverData.getFileName());
					fileOut.write(serverData.getFileData());
					fileOut.close();
					
					break;
				}
				// DELETE
				case 3:
				{
					System.out.println("Enter the number corresponding to the file to be deleted.");
					
					for (int i = 0; i < serverData.getServerFiles().size(); i++)
					{
						System.out.println(Integer.toString(i + 1) + ". " + serverData.getServerFiles().get(i).getName());
					}
					
					// Get the user input
					int choice = reader.nextInt() - 1;
					
					// Set the file name to be deleted
					clientData.setFileName(serverData.getServerFiles().get(choice).getName());
					
					System.out.println("Client Info: " + serverData.getServerFiles().get(choice).getName() + " will be deleted.");
					
					// WRITE - Send this information to the server
					objOut.writeObject(clientData);
					objOut.flush();
					
					break;
				}
				// RENAME
				case 4:
				{
					System.out.println("Enter the number corresponding to the file to be renamed.");
					
					for (int i = 0; i < serverData.getServerFiles().size(); i++)
					{
						System.out.println(Integer.toString(i + 1) + ". " + serverData.getServerFiles().get(i).getName());
					}
					
					// Get the user input
					int choice = reader.nextInt() - 1;
					
					System.out.print("Enter the new name for the file " + serverData.getServerFiles().get(choice).getName() + ": ");
					
					// Get the new name
					String name = reader.next();
					
					// Set the name of the file to be changed
					clientData.setFileName(serverData.getServerFiles().get(choice).getName());
					
					// Set the new name
					clientData.setNewFileName(name);
					
					System.out.println("Client Info: " + serverData.getServerFiles().get(choice).getName() + " will be renamed to " + clientData.getNewFileName());
					
					// WRITE - Send out the new file name
					objOut.writeObject(clientData);
					objOut.flush();
					
					break;
				}
				// An unknown command
				default:
				{
					System.err.println("Client Error: Received the unsupported command: " + Integer.toString(command));
				}
			}
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

		// Close general streams
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
