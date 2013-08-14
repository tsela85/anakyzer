import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;



public class createAll {


	//boolean firstTime;
	Vector<filePackets> files;

	public createAll() {
		files = new Vector<>();
	}
	//???
	public void addPacket(String filename,int packetNum,byte[] data,int checkSum)
	{
		File dir = new File(filename);
		//File file = new File(filename + File.separator +  packetNum);

		dir.mkdirs();
		//file.createNewFile();

		filePackets f = getOrCreateFile(filename);


		f.addPacket(packetNum, data, checkSum);


	}

	public boolean createFile(String filename)
	{

		filePackets f = getFile(filename);
		if (f == null)
			return false;

		return f.writeToFiles();


	}


	public void createAllFiles()
	{
		for (filePackets fp : files)
			createFile(fp.filename);
	}


	public boolean createFileStats(String filename)
	{
		//File dir = new File(filename);
		//File file = new File(filename + File.separator +  packetNum);

		//dir.mkdirs();
		//file.createNewFile();

		filePackets f = getFile(filename);
		if (f == null)
			return false;

		return f.writeStats();


	}
	
	
	
	public void createAllFilesStats()
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!found " + files.size() + " files!!!!!!!!!!!!!!!!!!!!!");
		for (filePackets fp : files)
		{
			System.out.println("HERE ARE THE STATS FOR " + fp.filename + "\n");
			createFileStats(fp.filename);
		}
		

	}


	private filePackets getFile(String filename) {
		for (filePackets fp : files)
			if (fp.filename.equals(filename))
				return fp;
		return null;
	}


	private filePackets getOrCreateFile(String filename) {
		for (filePackets fp : files)
			if (fp.filename.equals(filename))
				return fp;
		filePackets newFile = new filePackets(filename);
		files.add(newFile);
		return newFile;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		createAll all = new createAll();

		String a0 = "hello World";
		byte[] b0 = new byte[12];
		b0 = a0.getBytes();

		String a1 = "FUCK";
		byte[] b1 = new byte[5];
		b1 = a1.getBytes();


		String a2 = "YOU";
		byte[] b2 = new byte[4];
		b2 = a2.getBytes();


		all.addPacket("a.txt",2,b2,177);

		all.addPacket("a.txt",1,b1,177);

		all.addPacket("a.txt",0,b0,177);
		all.addPacket("a.txt",2,b2,177);

		all.createFile("a.txt");
		all.createAllFilesStats();
	}




	private class Packet {
		int packetNum;
		byte[] data;
		int checkSum;

		public Packet(int packetNum,byte[] data,int checkSum)
		{
			this.packetNum = packetNum;
			this.data = new byte[data.length];

			for(int i=0;i<data.length;i++)
				this.data[i] = data[i];

			this.checkSum = checkSum;
		}



		public void writeToFile(File file) {
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(data);
				bos.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



		public boolean checkSumVaildation()
		{
			int cs = 0;
			for (int i=0;i<data.length;i++)
				cs = ((37 * cs) + data[i]) & 0xff;
			//			System.out.println(cs);
			return (cs == this.checkSum);
			//return true;
		}



		public int CcheckSum() {
			int cs = 0;
			for (int i=0;i<data.length;i++)
				cs = ((37 * cs) + data[i]) & 0xff;
			return cs;
		}


	}


	private class filePackets {
		String filename;
		Vector<Packet> packet;

		public filePackets(String fn) {
			packet = new Vector<>();
			this.filename = fn;
		}

		public boolean writeStats() {
			//File file = new File(filename + "Stats");
			//file.createNewFile();


			Vector<Packet> validP = new Vector<>();
			Vector<Packet> INvalidP = new Vector<>();

			for (Packet p : packet)
			{
				if(p.checkSumVaildation())
					validP.add(p);
				else
					INvalidP.add(p);
			}

			System.out.println("Total received " + (validP.size()+INvalidP.size()) + " Packets" );
			System.out.println(validP.size() + " Valid packets..." );
			System.out.println(INvalidP.size() + " INValid packets...:(" );
			System.out.println("AMAZING percentage of "  +  (double)validP.size()/(validP.size()+INvalidP.size())*100 + "%\n"   );


			int TotalFilePackets = 6;

			for(int i=0;i<TotalFilePackets;i++)
			{
				int countValid = 0;
				int countINValid = 0;
				System.out.println("###########################################");

				for (Packet p : validP)
				{


					if (p.packetNum == i ) //valid i packet !
					{
						countValid++;

						System.out.print("Valid packet number " + i + " checkSum: " + p.checkSum + " DataLength : " + p.data.length + " Data: " );
						for(int j=0;j<p.data.length;j++)
							System.out.print((char)p.data[j]);
						System.out.println();
					}
				}


				for (Packet p : INvalidP)
				{
					if (p.packetNum == i ) //in valid i packet :(
					{
						countINValid++;
						System.out.print("INValid packet number " + i + " checkSum: " + p.checkSum + " CorrectCS: " + p.CcheckSum() + " DataLength : " + p.data.length + " Data: " );
						for(int j=0;j<p.data.length;j++)
							System.out.print((char)p.data[j]);
						System.out.println();
					}
				}

				System.out.println("found " + countValid + " valid packets of " + i);


				System.out.println("found " + countINValid + " INvalid packets of " + i );


			}



			return true;
		}





		public boolean writeToFiles() {
			Vector<Integer> writedPacketNums = new Vector<>();

			Vector<Packet> invalidPackets = new Vector<>();
			//	Integer inte = new Integer(0);
			//inte.

			for (Packet p : packet)
			{
				if (writedPacketNums.contains(new Integer(p.packetNum)))
					continue;

				if (p.checkSumVaildation() == false) //invalid checksum of p!!!
				{
					invalidPackets.add(p);
				}

				else //valid check sum of p 
				{
					File file = new File(filename + File.separator +  p.packetNum);


					p.writeToFile(file);


					//remove   Invalid  Packets///
					for(int i=0;i<invalidPackets.size();i++)
						if (invalidPackets.elementAt(i).packetNum == p.packetNum)
						{ 
							invalidPackets.remove(i); 
							i--; 
						}
					//////////////////////////////

					writedPacketNums.add(new Integer(p.packetNum));
				}

			}



			//here in invalidPackets we have all the invalid packets.

			return false;
		}

		public void addPacket(int packetNum,byte[] data,int checkSum) {
			packet.add(new Packet(packetNum, data, checkSum));
		}



	}



}
