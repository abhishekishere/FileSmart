package filesmart;

import java.util.HashSet;

public class CobolStatement  {
	
	HashSet<CobolTag> hierarchy;
public CobolStatement() {
	// TODO Auto-generated constructor stub
	hierarchy = new HashSet<CobolTag>();
}
	public void addTag(CobolTag obj) {
		hierarchy.add(obj);
	}
	
	public void removeTag(CobolTag obj) {
		System.out.println(obj.text);
		hierarchy.remove(obj);
	}
	
public static void main(String[] args) {
		
		FileSmartMavenMain smartFile = new FileSmartMavenMain("C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE","\\.");
		Translator tobj = new Translator("C:\\Users\\abhishekba\\Workspaces\\MyEclipse 8.6\\FileSmart\\resource\\c2j.xml");
		String line = "";
		while(line!=null) {
		line = smartFile.readLine();
		System.out.println(line);
		tobj.translate(line);
		}
	}
}
