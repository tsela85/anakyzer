
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecognizerTask {



	private Recognizer recognizer;
	String fileName;
	int blockSize = 1024;
	
	private Integer lastValue = null;
	private int keyCount=0;
	DataOutputStream out;
	BuildPacket packet;
	int packetPos;
	createAll create;

	public RecognizerTask(String fileName) 
	{
		this.fileName = fileName;
		recognizer = new Recognizer();
		packet = new BuildPacket();
		packetPos = 0;
		create = new createAll();
	}

	public void analyze(String outputFile)
	{

		DataInputStream dis;
		try {
			dis = new DataInputStream(
					new BufferedInputStream(new FileInputStream(
							fileName)));
			//out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));


		short buffer[] = new short[blockSize];
		byte b1,b2;

		while (dis.available() > 2) {
			{
				int i;
				for (i=0; i < blockSize; i++) {
					if (dis.available() == 0)
						break;
					b1 = dis.readByte();
					b2 = dis.readByte();
					buffer[i] = (short)((b2 << 8) + b1);
					//buffer[i] = dis.readShort();					
				}
				DataBlock dataBlock = new DataBlock(buffer,blockSize,i);

				Spectrum spectrum = dataBlock.FFT();

				spectrum.normalize();				

				StatelessRecognizer statelessRecognizer = new StatelessRecognizer(spectrum);

				Integer key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());

				keyReady(key);
			}
			
			//out.close();
			
		}
		//dis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
/*		try {
			create.createAllFiles();
			create.createAllFilesStats();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void keyReady(int key) 
	{				
		if(key != -1) {
			if(lastValue != key) {
				if (Main.DEBUG > 0) System.out.print("@"+(int)key);
//				fillPacket(key);
				Main.receved[key]++;
				keyCount = 0;
			} else if (keyCount  < 3) //TOM CH 5
				keyCount++;
			else {
				if (Main.DEBUG > 0) System.out.print("@"+(int)key);
				Main.receved[key]++;
//				fillPacket(key);
				keyCount = 0;

			}
		} //else 
			//keyCount = 0;
		lastValue = key;
	}
	
	public void fillPacket(int data){
		
		if (data == -2){ //packet start
			packetPos = 1;
			packet = new BuildPacket();
		} else if (packetPos == 1 && data != -3)
			packet.name += (char)data;
		else if ((packetPos == 1 || packetPos == 2) && data == -3)
			packetPos = 2;
		else if (packetPos == 2) {
				packet.number = data;
				packetPos = 3;
		} else if (packetPos > 2 && packetPos < packet.getDataLen()+3) {
			packet.data[packetPos-3] = (byte)data;
			packetPos++;
		} else if (packetPos == (packet.getDataLen()+3)) {
			packetPos = 0;
			packet.checksum = data;
			System.out.println("@@@" + packet.toString());
			try {
				create.addPacket(packet.name, packet.number, packet.data, packet.checksum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			packetPos = 0;
	}
}




