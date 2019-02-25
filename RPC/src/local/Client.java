package local;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import data.Data;

public class Client
{
	private int command = 0;
	
	private final int portNumber = 8080;

	private final String portName = "localhost";
	private final String filePath = System.getProperty("user.dir") + "\\src\\local\\Files\\";
	
	private ArrayList<File> fileList = new ArrayList<File>();
	
	private final HashMap<Integer, String> cmdList = new HashMap<Integer, String>();

	private Scanner reader = new Scanner(System.in);

	private Socket client = null;

	private InputStream  input  = null;
	private OutputStream output = null;

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
		
		updateFileList();
	}

	public void run() throws IOException
	{
		// Client info
		System.out.println("Client Info: Waiting for user to send a command to the server.");
		
		// Ask the user for input
		System.out.println("\nPlease select an option by entering the corresponding number:\n" + 
		                   "1. Calculate Pi\n"   +
						   "2. Add 2 Integers\n" +
		                   "3. Sort An Integer Array\n"   + 
						   "4. Multiply 3 Matrices");
		
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
				// CALCUATE PI
				case 1:
				{	
					System.out.println("Client Info: Calculating the value of Pi.");

					double pi = calculate_pi();
					
					System.out.println("The value of Pi is " + pi);
					
					break;
				}
				// ADD
				case 2:
				{
					System.out.print("Enter the first addend: ");
					int i = reader.nextInt();
					
					System.out.print("Enter the second added: ");
					int j = reader.nextInt();
					
					int sum = add(i, j);
					
					System.out.println("The sum of " + i + " and " + j + " is " + sum);
					
					break;
				}
				// SORT
				case 3:
				{
					System.out.print("Enter the lenth of the array: ");
					int length = reader.nextInt();
					
					int[] arrayA = new int[length];
					
					System.out.println("Enter " + length + " integers, pressing enter after each one.");
					
					for (int i  = 0; i < length; i++)
					{
						System.out.print("Value " + (i + 1) + ": ");
						arrayA[i] = reader.nextInt();
					}
					
					int[] sortedArray = sort(arrayA);
					
					System.out.println("The unsorted array " + Arrays.toString(arrayA) + " was sorted to " + Arrays.toString(sortedArray));
					
					break;
				}
				// RENAME
				case 4:
				{
					System.out.println("Select the input matrix file");
					
					printLocalFiles();
					int choice = reader.nextInt() - 1;
					
					parseMatrixFile(fileList.get(choice));
					
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
	
	private double calculate_pi() throws IOException
	{
		Data piData = new Data();
		
		// WRITE - Send for the value of pi from the server
		objOut.writeObject(piData);
		
		try
		{
			// READ - Get the value of pi from the server
			piData = (Data) objIn.readObject();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Client Error: Failed to cast server object to type Data.");
			e.printStackTrace();
		}
		
		return piData.getPi();
	}
	
	private int add(int i, int j) throws IOException
	{
		Data addendData = new Data();
		
		addendData.setAddend1(i);
		addendData.setAddend2(j);
		
		// WRITE - Send the addend's to the server
		objOut.writeObject(addendData);
		
		try
		{
			// READ - Get the sum of i and j from the server
			serverData = (Data) objIn.readObject();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Client Error: Failed to cast server object to type Data.");
			e.printStackTrace();
		}
		
		return serverData.getSum();
	}
	
	private int[] sort(int[] arrayA) throws IOException
	{
		Data sortedData = new Data();
		
		sortedData.setArrayA(arrayA);
		
		// WRITE - Send the unsorted array to the server
		objOut.writeObject(sortedData);
		
		try
		{
			// READ - Get the sum of i and j from the server
			serverData = (Data) objIn.readObject();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Client Error: Failed to cast server object to type Data.");
			e.printStackTrace();
		}
		
		return serverData.getArrayA();
	}
	
	private void parseMatrixFile(File file)
	{
		System.out.println("Multiplying matrices in file " + file.getName());
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
	}
	
	private void printLocalFiles()
	{
		System.out.println("\nFiles available locally:");
		
		int i = 0;
		
		for (File file : fileList)
		{
			System.out.println(Integer.toString(++i) + ". " + file.getName());
		}
	}

	public static void main(String[] args) throws IOException
	{
		Client client = new Client();

		client.run();

		client.close();
	}

}
