package data;

import java.io.Serializable;

public class Data implements Serializable
{
	static final long serialVersionUID = 1L;
	
	long clientPID = 0;
	
	String message = "";
	
	
	public long getClientPID()
	{
		return this.clientPID;
	}
	
	
	public void setClientPID(long clientPID)
	{
		this.clientPID = clientPID;
	}
	
	
	public String getMessage()
	{
		return this.message;
	}
	
	
	public void setMessage(String message)
	{
		this.message = message;
	}
}
