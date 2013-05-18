package filesmart;

import java.util.HashSet;

public class CobolStatement extends Thread {

	String name1;
	Thread parent;

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

	public CobolStatement(CobolTag cobolTag, String name, String line, Thread parent ) {
		super(cobolTag);
		this.name1 = name;
		this.line = line;
		this.parent = parent;

	}

	public Thread getParent() {
		return parent;
	}

	public void setParent(Thread parent) {
		this.parent = parent;
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
