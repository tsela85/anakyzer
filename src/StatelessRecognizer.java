

import java.util.ArrayList;
import java.util.Collection;

public class StatelessRecognizer {

	private Spectrum spectrum;
	private Collection<Tone> tones;
	private int maxL = 0;
	private int maxH = 0;
	private int max = 0;
	private double maxVal = 0;
	private double maxLVal = 0;
	private double maxHVal = 0;
	
	int counter;
	
	public StatelessRecognizer(Spectrum spectrum) 
	{
		this.spectrum = spectrum;
		
		tones = new ArrayList<Tone>();
		
		fillTones();
	}
	
	private void fillTones() {

		int R[] = new int[]{14,19,26,32,40,45,51,58,64,71,76,93,98,104,110,115};
		int L[] = new int[]{137,144,150,154,161,170,176,183,190,196,204,209,215,219,225,232};
		

		for(int i=0; i < R.length; i++)
			for(int j=0; j < L.length; j++)
				tones.add(new Tone(R[i], L[j], i*R.length + j));
		
		tones.add(new Tone(232,232,-2));
		tones.add(new Tone(237,237,-3));

		
		
	}

	public int getRecognizedKey()
	{

		// Low
		SpectrumFragment lowFragment= new SpectrumFragment(13, 118, spectrum);
		maxL = lowFragment.getMax();
		maxLVal = lowFragment.getValueAt(maxL);
		int lowdist = lowFragment.getDistinctsNumber();
		//High
		SpectrumFragment highFragment= new SpectrumFragment(135, 235, spectrum);
		maxH = highFragment.getMax();
		maxHVal = highFragment.getValueAt(maxH);
		int highdist = highFragment.getDistinctsNumber();
		//All
		SpectrumFragment allSpectrum = new SpectrumFragment(13, spectrum.length() /2, spectrum);
		max = allSpectrum.getMax();
		maxVal = allSpectrum.getValueAt(max);
		int alldist = allSpectrum.getDistinctsNumber();
				
		if (Main.DEBUG > 3) System.out.println("MaxL:" + maxL+ " MaxH:" + maxH +" Max:" + max + "\t\t\tMaxL:" + (int)maxLVal + " MaxH:" + (int)maxHVal +" Max:" + (int)maxVal + " avg: " + (int)allSpectrum.getAverageOrig());
		if (Main.DEBUG > 4) System.out.println("Dist -- MaxL:" + lowdist+ " MaxH:" + highdist +" Max:" + alldist);


		for (Tone t : tones) {
			if((max == maxL || max == maxH) && t.match(maxL, maxH))
				//if ((maxLVal > 1000000) && (maxHVal > 100000))
				return t.getKey();
			//else if((maxVal > (3/2)*maxLVal && maxVal > (3/2)*maxHVal) && t.matchOneFreq(max))
				//return t.getKey();		
		}
		
		return -1;
	}
}
