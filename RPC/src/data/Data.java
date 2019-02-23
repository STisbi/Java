package data;

import java.io.Serializable;

// Serializable was needed so that this object could be sent over the socket
public class Data implements Serializable
{
	private int command = 0;
	private int addend1 = Integer.MAX_VALUE;
	private int addend2 = Integer.MAX_VALUE;
	private int sum     = Integer.MAX_VALUE;
	
	private double pi   = Double.MAX_VALUE;
	
	private int[] arrayA = null;
	private int[] matrixA = null;
	private int[] matrixB = null;
	private int[] matrixC = null;
	
	// This was required to get rid of a warning for using serializable
	private static final long serialVersionUID = 1L;

	
	public void setCommand(int command)
	{
		this.command = command;
	}
	
	public int getCommand()
	{
		return this.command;
	}
	
	public void setAddend1(int addend1)
	{
		this.addend1 = addend1;
	}
	
	public int getAddend1()
	{
		return this.addend1;
	}
	
	public void setAddend2(int addend2)
	{
		this.addend2 = addend2;
	}
	
	public int getAddend2()
	{
		return this.addend2;
	}
	
	public void setSum(int sum)
	{
		this.sum = sum;
	}
	
	public int getSum()
	{
		return this.sum;
	}

	public void setPi(Double pi)
	{
		this.pi = pi;
	}
	
	public double getPi()
	{
		return this.pi;
	}
	
	public void setArrayA(int[] arrayA)
	{
		this.arrayA = arrayA;
	}
	
	public int[] getArrayA()
	{
		return this.arrayA;
	}
	
	public void setMatrixA(int[] matrixA)
	{
		this.matrixA = matrixA;
	}
	
	public int[] getMatrixA()
	{
		return this.matrixA;
	}
	
	public void setMatrixB(int[] matrixB)
	{
		this.matrixB = matrixB;
	}
	
	public int[] getMatrixB()
	{
		return this.matrixB;
	}
	public void setMatrixC(int[] matrixC)
	{
		this.matrixC = matrixC;
	}
	
	public int[] getMatrixC()
	{
		return this.matrixC;
	}

}
