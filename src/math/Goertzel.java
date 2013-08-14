package math;


/**
 * Contains an implementation of the Goertzel algorithm. It can be used to
 * detect if one or more predefined frequencies are present in a signal. E.g. to
 * do DTMF decoding.
 * 
 * @author Joren Six
 */
public class Goertzel {

	/**
	 * If the power in dB is higher than this threshold, the frequency is
	 * present in the signal.
	 */
	private static final double POWER_THRESHOLD = 35;// in dB

	/**
	 * A list of frequencies to detect.
	 */
	private static double[] frequenciesToDetect;
	/**
	 * Cached cosine calculations for each frequency to detect.
	 */
	private static  double[] precalculatedCosines;
	/**
	 * Cached wnk calculations for each frequency to detect.
	 */
	private static  double[] precalculatedWnk;
	/**
	 * A calculated power for each frequency to detect. This array is reused for
	 * performance reasons.
	 */
	private static  double[] calculatedPowers;

	private final FrequenciesDetectedHandler handler;

	public Goertzel(final float audioSampleRate, final int bufferSize,
			double[] frequencies, FrequenciesDetectedHandler handler) {

		frequenciesToDetect = frequencies;
		precalculatedCosines = new double[frequencies.length];
		precalculatedWnk = new double[frequencies.length];
		this.handler = handler;

		calculatedPowers = new double[frequencies.length];

		for (int i = 0; i < frequenciesToDetect.length; i++) {
			precalculatedCosines[i] = 2 * Math.cos(2 * Math.PI
					* frequenciesToDetect[i] / audioSampleRate);
			precalculatedWnk[i] = Math.exp(-2 * Math.PI
					* frequenciesToDetect[i] / audioSampleRate);
		}
	}

	/**
	 * An interface used to react on detected frequencies.
	 * 
	 * @author Joren Six
	 */
	public static interface FrequenciesDetectedHandler {
		/**
		 * React on detected frequencies.
		 * 
		 * @param frequencies
		 *            A list of detected frequencies.
		 * @param powers
		 *            A list of powers of the detected frequencies.
		 * @param allFrequencies
		 *            A list of all frequencies that were checked.
		 * @param allPowers
		 *            A list of powers of all frequencies that were checked.
		 */
		void handleDetectedFrequencies(final double[] frequencies,
				final double[] powers, final double[] allFrequencies,
				final double allPowers[]);
	}

	public static double[] process(double[] audioFloatBuffer) {
		/////////////init////////////////
		int len = 1024;
		int audioSampleRate = 16000;
		frequenciesToDetect = new double[len];
		for(int i=1; i < len; i++)
			frequenciesToDetect[i] = 7.125*i;
		precalculatedCosines = new double[len];
		precalculatedWnk = new double[len];
	

		calculatedPowers = new double[len];

		for (int i = 0; i < frequenciesToDetect.length; i++) {
			precalculatedCosines[i] = 2 * Math.cos(2 * Math.PI
					* frequenciesToDetect[i] / audioSampleRate);
			precalculatedWnk[i] = Math.exp(-2 * Math.PI
					* frequenciesToDetect[i] / audioSampleRate);
		}
		
		
		//////////////////////////////////
//		float[] audioFloatBuffer = audioEvent.getFloatBuffer();
		double skn0, skn1, skn2;
		int numberOfDetectedFrequencies = 0;
		for (int j = 0; j < frequenciesToDetect.length; j++) {
			skn0 = skn1 = skn2 = 0;
			for (int i = 0; i < audioFloatBuffer.length; i++) {
				skn2 = skn1;
				skn1 = skn0;
				skn0 = precalculatedCosines[j] * skn1 - skn2
						+ audioFloatBuffer[i];
			}
			double wnk = precalculatedWnk[j];
			calculatedPowers[j] = 20 * Math.log10(Math.abs(skn0 - wnk * skn1));
			if (calculatedPowers[j] > POWER_THRESHOLD) {
				numberOfDetectedFrequencies++;
			}
		}
		double[] frequencies = null;
		double[] powers = null;
		if (numberOfDetectedFrequencies > 0) {
			frequencies = new double[numberOfDetectedFrequencies];
			powers = new double[numberOfDetectedFrequencies];
			int index = 0;
			for (int j = 0; j < frequenciesToDetect.length; j++) {
				if (calculatedPowers[j] > POWER_THRESHOLD) {
					frequencies[index] = frequenciesToDetect[j];
					powers[index] = calculatedPowers[j] - POWER_THRESHOLD;
					index++;
				}
			}
//			handler.handleDetectedFrequencies(frequencies, powers,
//					DTMF.DTMF_FREQUENCIES, calculatedPowers.clone());
		}

		return powers;
	}


}