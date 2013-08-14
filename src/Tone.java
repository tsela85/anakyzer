

public class Tone {
	
	private int lowFrequency;
	private int highFrequency;
	

	private Integer  val;
	
	private static int FREQUENCY_DELTA = 2;

	public Tone(int lowFrequency, int highFrequency, int num) 
	{
		this.lowFrequency = lowFrequency;
		this.highFrequency = highFrequency;
		this.val = num;
	}

	public int getLowFrequency()
	{
		return lowFrequency;
	}

	public int getHighFrequency()
	{
		return highFrequency;
	}

	public int getKey()
	{
		return val;
	}

	public boolean isDistrinct(boolean[] distincts) 
	{
		if(match(lowFrequency, distincts) && match(highFrequency,distincts))
			return true;
		
		return false;
	}

	
	private boolean match(int frequency, boolean[] distincts) 
	{
		for(int i = frequency - FREQUENCY_DELTA; i <= frequency + FREQUENCY_DELTA; ++i)
			if(distincts[i])
				return true;
		
		return false;
	}
	
	public boolean matchOneFreq(int frequency) 
	{
		if (this.lowFrequency != this.highFrequency)
			return false;
		return this.matchFrequency(frequency, this.lowFrequency);
	}

	public boolean match(int lowFrequency, int highFrequency) 
	{
		if(matchFrequency(lowFrequency, this.lowFrequency) && matchFrequency(highFrequency, this.highFrequency))
			return true;
		
		return false;
	}
	
	private boolean matchFrequency(int frequency, int frequencyPattern)
	{
		if((frequency - frequencyPattern) * (frequency - frequencyPattern) < FREQUENCY_DELTA * FREQUENCY_DELTA)
			return true;
		
		return false;
	}

}
