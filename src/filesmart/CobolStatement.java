package filesmart;

import java.util.HashSet;

public class CobolStatement extends Thread {

	String name;
	String line;

	HashSet<CobolTag> hierarchy;

	public CobolStatement() {
		// TODO Auto-generated constructor stub
		hierarchy = new HashSet<CobolTag>();
	}

	public CobolStatement(CobolTag cobolTag, String name, String line) {
		super(cobolTag);
		this.name = name;
		this.line = line;

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
