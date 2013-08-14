public class BuildPacket {
	private static int DATA_SIZE = 20;

	
	public String name;
	public Integer number;
	public byte[] data;
	public int checksum;
	
	public BuildPacket() {
		this.name = "";
		this.number  = null;
		this.data = new byte[DATA_SIZE];
		//this.checksum = null;			
	}
	
	public int getDataLen() {
		return DATA_SIZE;
	}
	
	@Override
	public String toString() {
		String temp = "";
		for(byte b : data)
			temp+= (char)b + ":"+ b+ " ";
		return "name:"+name+" num:"+ number+" data:"+temp+" checksum:"+checksum;
	}
}
