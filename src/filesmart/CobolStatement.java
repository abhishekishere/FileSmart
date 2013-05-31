package filesmart;

import java.util.HashSet;

public class CobolStatement extends Thread {

	Thread parent;
	String text;
	String fullLine;
	int dent;
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

	public CobolStatement(CobolTag cobolTag, String line) {
		super(cobolTag);

		this.line = line;
		this.parent = parent;

	}

	public Thread getParent() {
		return parent;
	}

	public void setParent(Thread parent) {
		this.parent = parent;
	}

	public void addTag(CobolTag obj) {
		hierarchy.add(obj);
	}

	

	public static void main(String[] args) {

	}
}
