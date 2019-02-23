package data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

// Serializable was needed so that this object could be sent over the socket
public class Data implements Serializable
{
	private int command = 0;
	
	private String fileName    = "";
	private String newFileName = ""; 
	
	// This was required to get rid of a warning for using serializable
	private static final long serialVersionUID = 1L;

	private byte[] fileData = null;
	
	private ArrayList<File> serverFiles = new ArrayList<File>();
	
	public void setCommand(int command)
	{
		this.command = command;
	}
	
	public int getCommand()
	{
		return this.command;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setNewFileName(String newFileName)
	{
		this.newFileName = newFileName;
	}
	
	public String getNewFileName()
	{
		return this.newFileName;
	}

	public void setFileData(Path path)
	{
		try
		{
			this.fileData = Files.readAllBytes(path);
		}
		catch (IOException e)
		{
			System.err.println("Unable to read file data to byte array.");
			e.printStackTrace();
		}
	}

	public byte[] getFileData()
	{
		return this.fileData;
	}
	
	public void setServerFiles(ArrayList<File> serverFiles)
	{
		this.serverFiles = serverFiles;
	}
	
	public ArrayList<File> getServerFiles()
	{
		return this.serverFiles;
	}

}
