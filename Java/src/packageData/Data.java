package packageData;

import java.io.File;
import java.io.Serializable;

public class Data implements Serializable
{
	private String fileName = "";
	
	private static final long serialVersionUID = 1L;

	File file = null;
	

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
