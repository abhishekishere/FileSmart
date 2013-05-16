package filesmart;

import java.util.HashSet;

public class CobolStatement extends Thread {

	String name1;
	

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	String line;
	
	HashSet<CobolTag> hierarchy;

	public CobolStatement() {
		// TODO Auto-generated constructor stub
		hierarchy = new HashSet<CobolTag>();
	}

	public CobolStatement(CobolTag cobolTag, String name, String line) {
		super(cobolTag);
		this.name1 = name;
		this.line = line;
		

	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public void addTag(CobolTag obj) {
		hierarchy.add(obj);
	}

	public void removeTag(CobolTag obj) {
		System.out.println(obj.text);
		hierarchy.remove(obj);
	}

	public static void main(String[] args) {

	}
}
