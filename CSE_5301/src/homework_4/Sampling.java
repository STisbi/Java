package homework_4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * This class satisfies the requirements of homework 4.
 * It generates samples from a given set of distributions based on the user's description.
 * The formula's and methods used to get a sample from a particular distribution were taken from material
 * in lecture as well as lecture notes found online. It has been included in this package.
 * 
 * @author STisb
 *
 */
public class Sampling
{
	// The SEED value
	private final int SEED = 1;
	
	private int numberOfSamples = 0;
	
	private String[] userArgs = null;
	
	private List<Float> parameters = new ArrayList<Float>();
	private List<Float> samples    = new ArrayList<Float>();
	
	private HashMap<String, Distribution> map = new HashMap<String, Distribution>();
	
	private Random random = new Random();
	
	private Distribution distribution = Distribution.UNKNOWN;
	
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
	
	
	/**
	 * The construction site. It needs user input to continue execution.
	 * 
	 * @param args Given in the format (number of samples), (the desired distribution), (parameter_1), (parameter_2), ... , (parameter_i)
	 */
	public Sampling(String[] args)
	{
		if (args.length != 0)
		{
			this.userArgs = args;
			
			random.setSeed(SEED);
			
			populateMap();
			parseArguments();
			printUserArguments();
		}
		else
		{
			System.err.println("Command line arguments missing.");
		}
	}
	
	
	/**
	 * An instance of this class would call this method to get the desired samples. Delegates to the appropriate method.
	 */
	public void getSamples()
	{
		// Give the user some affirmation that their request is being handled
		System.out.println(String.format("Getting %d samples for the %s distribution. The seed value is %d.", this.numberOfSamples, this.getDisitrubtion().toString(), this.SEED));
		
		// Be nice and clear any previous runs
		this.samples.clear();
		
		switch (this.getDisitrubtion())
		{
			case BERNOULLI:
			{
				this.getBernoulliSamples(this.numberOfSamples, this.getParameter(0));
				break;
			}
			case BINOMIAL:
			{
				this.getBinomialSamples(this.numberOfSamples, Math.round(this.getParameter(0)), this.getParameter(1));
				break;
			}
			case GEOMETRIC:
			{
				this.getGeometricSamples(this.numberOfSamples, this.getParameter(0));
				break;
			}
			case NEGATIVE_BINOMIAL:
			{
				this.getNegativeBinomial(this.numberOfSamples, Math.round(this.getParameter(0)), this.getParameter(1));
				break;
			}
			case POISSON:
			{
				this.getPoisson(this.numberOfSamples, this.getParameter(0));
				break;
			}
			case ARB_DISCRETE:
			{
				
				break;
			}
			case UNIFORM:
			{
				this.getUniform(this.numberOfSamples, this.getParameter(0), this.getParameter(1));
				break;
			}
			case EXPONENTIAL:
			{
				this.getExponential(this.numberOfSamples, this.getParameter(0));
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
	
	
	/**
	 * Prints in a formatted way, the arguments the user entered; for their affirmation.
	 */
	public void printUserArguments()
	{
		String arguments = String.format("The user requested %d samples from a %s distribution with parameters ", this.numberOfSamples, this.distribution.toString());
		
		for (Float parameters : this.parameters)
		{
			arguments += String.format("%.2f ", parameters);
		}
		
		System.out.println(arguments);
	}
	
	
	/**
	 * Rather than print the samples from the method itself, it was separated into it's own method so that
	 * if a method for one sampling distribution called another getSample method, it did would not print
	 * that intermediate step.
	 * 
	 * @param distributionName The name of the distribution. It DOES NOT print that samples distribution, rather it is only
	 * for formatting and beautification purposes.
	 */
	public void printSamples(String distributionName)
	{
		System.out.print(distributionName + ": ");
		this.samples.forEach(sample -> System.out.print(String.format("%.0f ", sample)));
	}
	
	
	/**
	 * Gets the enum of distribution that was requested when the class was instanced.
	 * 
	 * @return An enumeration of the distribution requested when the class was instanced.
	 */
	public Distribution getDisitrubtion()
	{
		return this.distribution;
	}
	
	
	/**
	 * Wrapper function for the array list used. Created so that the array list variable would not be accessed directly.
	 * 
	 * @param index The position of the value desired.
	 * 
	 * @return The value at the given index.
	 */
	public float getParameter(int index)
	{
		return this.parameters.get(index);
	}
	
	
	/**
	 * Populates the HashMap with a mapping from a set of strings describing the distribution to their enum counterpart.
	 * The string key is identical to the spelling used in the homework description.
	 */
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
	
	
	/**
	 * Parses the user argument in the given method described in the constructor {@link homework_4.Sampling#Sampling(String[])}
	 */
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
		
		// Note the starting index
		for (int i = 2; i < this.userArgs.length; i ++)
		{
			parameters.add(Float.parseFloat(this.userArgs[i]));
		}
	}
	
	
	/**
	 * Gets the requested number of samples using the given probability from a Bernoulli distribution.
	 * If the random number is less than the given probability then the sample is 1, else 0.
	 * 
	 * @param sampleSize  The number of samples requested by the user from this distribution.
	 * @param probability A given probability specified by the user.
	 */
	private void getBernoulliSamples(int sampleSize, double probability)
	{	
		for (int i = 0; i < sampleSize; i++)
		{
			 this.samples.add((float) (this.random.nextFloat() <= probability ? 1 : 0));
		}
	}
	
	
	/**
	 * Gets the requested number of samples using the given probability and n value from a Binomial
	 * distribution. The algorithm proceeds by first getting n samples from a Bernoulli distribution
	 * using the given probability. The number of 1's in that sample are counted and that value is
	 * taken to be a sample for the Binomial distribution. This is repeated for however many samples
	 * are requested.
	 * 
	 * @param sampleSize  The number of samples requested by the user from this distribution.
	 * @param n           A parameter of the Binomial distribution.
	 * @param probability A given probability specified by the user.
	 */
	private void getBinomialSamples(int sampleSize, int n, float probability)
	{
		ArrayList<Float> localList = new ArrayList<Float>();
		
		// Runs for however many samples we need
		for (int i = 0; i < sampleSize; i++)
		{
			// Each binomial sample consists of a set of bernoulli samples
			this.getBernoulliSamples(n, probability);
			
			float sum = 0;
			for (Float sample : this.samples)
			{
				if (sample == 1)
				{
					sum++;
				}
			}
			
			// Imperative! Remove the last sample
			this.samples.clear();

			localList.add(sum);
		}
		
		this.samples = localList;
	}
	
	
	/**
	 * Gets the requested number of samples using the given probability from a Geometric distribution.
	 * Similar to the Binomial distribution, it first gets Bernoulli samples. Then the value for however
	 * many trials it took before the first success is obatined and this value is used as a sample. The
	 * process is repeated for however many samples are requested.
	 * 
	 * @param sampleSize  The number of samples requested by the user from this distribution.
	 * @param probability A given probability specified by the user.
	 */
	private void getGeometricSamples(int sampleSize, float probability)
	{
		float firstSuccess = 1;
		
		ArrayList<Float> localList = new ArrayList<Float>();
		
		for (int i = 0; i < sampleSize; i++)
		{
			this.getBernoulliSamples(sampleSize, probability);
			
			// Plus 1 since these lists are 0 based
			localList.add((float) (this.samples.indexOf(firstSuccess) + 1));
			
			// Imperative! Remove the last sample
			this.samples.clear();
		}
		
		this.samples = localList;
	}
	
	
	/**
	 * Gets the requested number of samples using the given probability and k value for a Geometric
	 * distribution. Similar to the Binomial and Geometric methods, it first gets a number of Bernoulli
	 * samples. The number of trials it took before k successes were obtained is used as a sample.
	 * The process is then repeated for however many samples have been requested.
	 * 
	 * @param sampleSize The number of samples requested by the user from this distribution.
	 * @param k A parameter that represents how many success are needed.
	 * @param probability A given probability specified by the user.
	 */
	private void getNegativeBinomial(int sampleSize, int k, float probability)
	{
		ArrayList<Float> localList = new ArrayList<Float>();
		
		for (int i = 0; i < sampleSize; i++)
		{
			float kthSuccess = 0;
			
			this.getBernoulliSamples(sampleSize, probability);
			
			int x = 0;
			while (kthSuccess < k)
			{
				if (x < this.samples.size() - 1)
				{
					if (this.samples.get(x++) == 1)
					{
						kthSuccess++;
					}
				}
				else
				{
					break;
				}
			}
			
			// Imperative! Remove the last sample
			this.samples.clear();
			
			// Minus one since the above post increments; after it found the kth success
			// it incremented once more.
			localList.add((float) x - 1);
		}
		
		this.samples = localList;
	}
	
	
	/**
	 * Gets the requested number of samples from a Poisson distribution. Generates a random
	 * number and compares it to a value formulated from lambda. The number of trials before
	 * the random number is less than the formulated value is taken as a sample. The process
	 * is repeated for however many samples have been requested. 
	 * 
	 * @param sampleSize The number of samples requested by the user from this distribution.
	 * @param lambda A parameter of the Poisson distribution.
	 */
	private void getPoisson(int sampleSize, float lambda)
	{
		// Computing e^(-lambda)
		double compareValue = Math.exp(lambda * (-1));
		
		// Remove any previous sample values
		this.samples.clear();
		
		for (int sampleX = 0; sampleX < sampleSize; sampleX++)
		{
			int i = 1;
			while (this.random.nextFloat() > compareValue)
			{
				i++;
			}
			
			this.samples.add((float) i);
		}
	}
	
	
	/**
	 * Gets the requested number of samples from a Uniform distribution. The individual steps
	 * in this process were separated out so that it is easier to read. The sample is simply 
	 * calculated using this formula, a + (b - a)U, where U is the random number generated.
	 * 
	 * @param sampleSize The number of samples requested by the user from this distribution.
	 * @param a A parameter of the uniform distribution.
	 * @param b A parameter of the uniform distribution.
	 */
	private void getUniform(int sampleSize, float a, float b)
	{
		for (int i = 0; i < sampleSize; i++)
		{
			float step_1 = (b - a);
			float step_2 = step_1 * this.random.nextFloat();
			float step_3 = step_2 + a;
			
			this.samples.add(step_3);
		}
	}
	
	
	/**
	 * Gets the requested number of samples from a Exponential distribution. Similar to the 
	 * Uniform distribution, the individual steps have been separated out so that it is easier
	 * to read. The sample is generated using the formula, -ln(U)/lambda, where U is the random 
	 * number generated.
	 * 
	 * @param sampleSize The number of samples requested by the user from this distribution.
	 * @param lambda A parameter of the Exponential distribution.
	 */
	private void getExponential(int sampleSize, float lambda)
	{
		for (int i = 0; i < sampleSize; i++)
		{
			float step_1 = (float) Math.log(this.random.nextFloat());
			float step_2 = step_1 * -1;
			float step_3 = step_2 / lambda;
			
			this.samples.add(step_3);
		}
	}
	

	/**
	 * Runs the class {@link Sampling}.
	 * 
	 * @param args User arguments following the pattern specified in the homework description, {@link homework_4.Sampling#Sampling(String[])} 
	 */
	public static void main(String[] args)
	{
		// In an instant
		Sampling sampling = new Sampling(args);
		
		// go get some samples
		sampling.getSamples();
		
		// and print them.
		sampling.printSamples(sampling.getDisitrubtion().toString());
	}

}
