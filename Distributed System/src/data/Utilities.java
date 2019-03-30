package data;

public class Utilities
{
	// The number of times a client will write to the file
	public static final int LOOP_COUNT = 3;
	
	// These two numbers should always match
	public static final int THREADCOUNT  = 20;
	public static final int PROCESSCOUNT = 20;
	
	// The number of threads that are permitted to access the shared resource at any given time
	public static final int PERMITS = 1;
	
	// Platform (UNIX or Windows) independent values
	public static final String NEWLINE        = System.lineSeparator();
	public static final String PATH_SEPARATOR = System.getProperty("file.separator");
	public static final String USERDIR        = System.getProperty("user.dir");
	
	// The ports the client and server will use
	public static final int[] PORTS = {2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009,
			                           2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019};
	
}
