package filesmart;

import static filesmart.Translator.command;
import static filesmart.Translator.doc;
import static filesmart.Translator.match;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CobolTag implements Runnable {
	// immutable Shared data
	String text; // put the program in this string
	// mutable shared data
	String name; // name of new Cobol Statement thread to be made
	String line; // full cobol statement
	String group; // matching group

	public static FileSmartMavenMain smartFile;

	static {
		smartFile = new FileSmartMavenMain(
				"C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE",
				"\\.");
	}

	public CobolTag() {
		line = "cobol";
		while (line != null) {
			line = smartFile.readLine();
			name = match("\\s*([a-zA-Z0-9-]*)\\s*.*", line);
			CobolStatement cs1 = new CobolStatement(this, "cobol",
					"Just Started!");
			cs1.start();
			try {
				cs1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Shared Code
	@Override
	public void run() {

		// Match group with tag if exists, create the
		command = doc.getElementsByTagName(name).item(0);
		Element comm = (Element) command;
		NodeList syntax = comm.getChildNodes();
		for (int i = 0; i < syntax.getLength(); i++) {

			Node type = syntax.item(i);

			String value = type.getTextContent();
			if (type.getNodeName() == "path") {
				// When <path></path> is encountered, get new line in line
				if (value == "") {
					line = smartFile.readLine();
					name = match("\\s*([a-zA-Z0-9]*)\\s*", line);
					command = doc.getElementsByTagName(name).item(0);
					if (command != null) {
						CobolStatement cs1 = new CobolStatement(this, name,
								line);
						cs1.start();
						try {
							cs1.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						slowWrite(name);
						text = text + name;
					}

					// When <path>regexp</path> is encountered, get the matching
					// group
				} else {
					name = match(value, line);
					command = doc.getElementsByTagName(name).item(0);
					if (command != null) {
						CobolStatement cs1 = new CobolStatement(this, name,
								line);
						cs1.start();
						try {
							cs1.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						slowWrite(name);
						text = text + name;
					}
				}
			} else if (type.getNodeName() == "text") {
				slowWrite(value);
				text = text + value;
			}
		}

	}

	private void slowWrite(String text) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.print(text);
	}

	public static void main(String[] args) {
		CobolTag obj = new CobolTag();
	}
}
