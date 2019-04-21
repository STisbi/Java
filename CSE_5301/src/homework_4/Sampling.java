package homework_4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Sampling
{
	// The SEED value
	private final int SEED = 1;
	
	private int numberOfSamples = 0;
	
	private String[] userArgs = null;
	
	private List<Float> parameters = new ArrayList<Float>();
	private List<Float> samples    = new ArrayList<Float>();
	
	private HashMap<String, Distribution> map = new HashMap<String, Distribution>();
	
	Random random = new Random();
	
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
	
	public void getSamples()
	{
		System.out.println(String.format("Getting %d samples for the %s distribution. The seed value is %d.", this.numberOfSamples, this.getDisitrubtion().toString(), this.SEED));
		
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
	
	public void printUserArguments()
	{
		String arguments = new String();
		
		arguments = String.format("The user requested %d samples from a %s distribution with parameters ", this.numberOfSamples, this.distribution.toString());
		
		for (Float parameters : this.parameters)
		{
			arguments += String.format("%.2f ", parameters);
		}
		
		System.out.println(arguments);
	}
	
	public void printSamples(String distributionName)
	{
		System.out.print(distributionName + ": ");
		this.samples.forEach(sample -> System.out.print(String.format("%.0f ", sample)));
	}
	
	public Distribution getDisitrubtion()
	{
		return this.distribution;
	}
	
	public float getParameter(int index)
	{
		return this.parameters.get(index);
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
		
		// Note the starting index
		for (int i = 2; i < this.userArgs.length; i ++)
		{
			parameters.add(Float.parseFloat(this.userArgs[i]));
		}
	}
	
	private void getBernoulliSamples(int sampleSize, double probability)
	{	
		for (int i = 0; i < sampleSize; i++)
		{
			 this.samples.add((float) (this.random.nextFloat() <= probability ? 1 : 0));
		}
	}
	
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
	

	public static void main(String[] args)
	{
		Sampling sampling = new Sampling(args);
		
		sampling.getSamples();
		sampling.printSamples(sampling.getDisitrubtion().toString());
	}

}
