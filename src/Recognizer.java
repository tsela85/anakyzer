

import java.util.ArrayList;

public class Recognizer 
{
	ArrayList<Integer> history;
	int acctualVaue;
	
	public Recognizer() 
	{
		clear();
	}

	private void clear() 
	{
		history = new ArrayList<Integer>();
		acctualVaue = -1;
	}

	public int getRecognizedKey(int recognizedKey) 
	{	
		
		history.add(recognizedKey);
		
		if(history.size() > 3) //TOM CH 4
			history.remove(0);
		
	
		int count = 0;
		
		for (int c: history) 
		{
			if(c != -1 && c == recognizedKey)
				count++;
		}
		if(count >= 2) {//TOM CH 2
			
			if (Main.DEBUG > 2)System.out.println(history.toString());
		//	history.clear();
			return recognizedKey;
		}
		
		return -1;
	}
}
