
public class Main {
	
	public static int DEBUG = 4;
	public static int[] receved = new int[256];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String fileName = "/home/cyber/Downloads/close-range_slow-speed_good-cable_1.pcm";
		String fileName = "/home/cyber/Desktop/pcm/test (1).pcm";
		RecognizerTask recon = new RecognizerTask(fileName);
		recon.analyze("outflie");
		for(int i=0; i < receved.length;i++) {
			if (receved[i] != 1)
				System.out.println(i + " times " + receved[i]);
		}
	}

}
