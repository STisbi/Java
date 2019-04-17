package homework_4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Sampling
{
	// The SEED value
	final int SEED = 1;
	
	int numberOfSamples = 0;
	
	String[] userArgs = null;
	
	List<Double> parameters = new ArrayList<Double>();
	
	HashMap<String, Distribution> map = new HashMap<String, Distribution>();
	
	Distribution distribution = Distribution.UNKNOWN;
	
	enum Distribution
	{
		BERNOULLI,
		BINOMIAL,
		GEOMETRIC,
		NEGATIVE_BINOMIAL,
		POISSON,
		ARB_DISCRETE,
		UNIFORM,
		EXPONENTIAL,
		GAMMA,
		NORMAL,
		UNKNOWN
	}
	
	public Sampling(String[] args)
	{
		if (args.length != 0)
		{
			this.userArgs = args;
			
			populateMap();
			parseArguments();
			printUserArguments();
		}
		else
		{
			System.err.println("Command line arguments missing.");
		}
	}
	
	public void getSamples()
	{
		System.out.println(String.format("Getting %d samples for the %s distribution. The seed value is %d.", this.numberOfSamples, this.distribution.toString(), this.SEED));
		
		switch (this.distribution)
		{
			case BERNOULLI:
			{
				getBernoulliSamples();
				break;
			}
			case GEOMETRIC:
			{
				
				break;
			}
			case NEGATIVE_BINOMIAL:
			{
				
				
				break;
			}
			case POISSON:
			{
				
				
				break;
			}
			case ARB_DISCRETE:
			{
				
				
				break;
			}
			case UNIFORM:
			{
				
				
				break;
			}
			case EXPONENTIAL:
			{
				
				
				break;
			}
			case GAMMA:
			{
				
				
				break;
			}
			case NORMAL:
			{
				
				
				break;
			}
			default:
			{
				System.err.println(String.format("Unable to get samples from %s distribution", this.distribution.toString()));
			}
		}
	}
	
	public void printUserArguments()
	{
		String arguments = new String();
		
		arguments = String.format("The user requested %d samples from a %s distribution with parameters ", this.numberOfSamples, this.distribution.toString());
		
		for (Double parameters : this.parameters)
		{
			arguments += String.format("%.2f ", parameters);
		}
		
		System.out.println(arguments);
	}
	
	private void populateMap()
	{
		map.put("bernoulli", 	Distribution.BERNOULLI);
		map.put("binomial", 	Distribution.BINOMIAL);
		map.put("geometric", 	Distribution.GEOMETRIC);
		map.put("neg-binomial", Distribution.NEGATIVE_BINOMIAL);
		map.put("poisson", 		Distribution.POISSON);
		map.put("arb-discrete", Distribution.ARB_DISCRETE);
		map.put("uniform", 		Distribution.UNIFORM);
		map.put("exponential", 	Distribution.EXPONENTIAL);
		map.put("gamma", 		Distribution.GAMMA);
		map.put("normal", 		Distribution.NORMAL);
	}
	
	private void parseArguments()
	{
		// The number of samples
		this.numberOfSamples = Integer.parseInt(this.userArgs[0]);
		
		// The distribution type
		for (String distribution : map.keySet())
		{
			if (this.userArgs[1].equals(distribution))
			{
				this.distribution = map.get(distribution);
			}
		}
		
		// The parameters
		for (int i = 2; i < this.userArgs.length; i ++)
		{
			parameters.add(Double.parseDouble(this.userArgs[i]));
		}
	}
	
	private void getBernoulliSamples()
	{
		Random random = new Random();
		random.setSeed(this.SEED);
		
		double probability = this.parameters.get(0);
		
		for (int i = 0; i < this.numberOfSamples; i++)
		{
			System.out.print(String.format("%s ", random.nextDouble() <= probability ? "1" : "0"));
		}
	}

	public static void main(String[] args)
	{
		Sampling sampling = new Sampling(args);
		
		sampling.getSamples();
	}

}
