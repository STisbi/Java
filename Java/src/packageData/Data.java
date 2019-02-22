package packageData;

import java.io.File;

public class Data
{
	private int command = 0;

	private String fileName = "";

	File file = null;

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

	public void setFile(File file)
	{
		this.file = file;
	}

	public File getFile()
	{
		return this.file;
	}

}
