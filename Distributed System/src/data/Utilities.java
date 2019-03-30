package data;

public class Utilities
{
	// These two numbers should always match
	public static final int THREADCOUNT  = 3;
	public static final int PROCESSCOUNT = 3;
	
	// The number of threads that are permitted to access the shared resource at any given time
	public static final int PERMITS = 1;
	
	// Platform (UNIX or Windows) independent values
	public static final String NEWLINE        = System.lineSeparator();
	public static final String PATH_SEPARATOR = System.getProperty("file.separator");
	public static final String USERDIR        = System.getProperty("user.dir");
	
	// The ports the client and server will use
	public static final int[] PORTS = {8080, 8081, 8082};
	
}
