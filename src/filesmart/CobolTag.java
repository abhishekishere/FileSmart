package filesmart;

import static filesmart.Translator.*;


import java.util.StringTokenizer;

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
	NodeList syntax;
	public static FileSmartMavenMain smartFile;

	static {
		smartFile = new FileSmartMavenMain(
				"C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE",
				"\\.");
	}

	public CobolTag() {
		line = "";
		while (line != null) {
			line = smartFile.readLine();
			name = match(OPERAND,line);
			name = match(ALPHANUMERIC_WORD_ENDING_IN_ANYTHING, line);
			if (name == null) {
				name = match("(" + WORD + ")" + SENTENCE, line);
			}
			if (name == null) {
				name = "CMEDIT";
			}
			command = doc.getElementsByTagName(name.trim()).item(0);
			if (command != null) {
				Element comm = (Element) command;
				syntax = comm.getChildNodes();
				CobolStatement cs1 = new CobolStatement(this, name, line);
				cs1.start();
				try {
					cs1.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// Shared Code
	@Override
	public void run() {
		int freewill = 1;
		for (int i = 0; i < syntax.getLength(); i++) {

			Node type = syntax.item(i);

			String value = type.getTextContent();
			if (value == "") {
				line = smartFile.readLine();
				name = match(ALPHANUMNERIC_WORD, line);
				if (name == null) {
					name = match("(" + SENTENCE + ")", line);
				}
				slowWrite(name);
				text = text + name;
			}
			if (type.getNodeName() == "path" && freewill == 1) {
				freewill = 0;
				command = null;
				name = match(value, line);

				command = doc.getElementsByTagName(name).item(0);
				if (command != null) {
					Element comm = (Element) command;
					syntax = comm.getChildNodes();
					CobolStatement cs1 = new CobolStatement(this, name, line);
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

			if (type.getNodeName() == "text") {
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
		StringTokenizer lineArray = new StringTokenizer(line);
		for (int j = 0; j < lineArray.countTokens(); j++) {
			StringTokenizer lineArray1 = new StringTokenizer(line);
			String token = "";
			for (int i = 0; i <= j; i++) {
				token = lineArray1.nextToken();
			}
			text=text.replaceAll(Integer.toString(j), token);
		}
		System.out.print(text);
	}

	public static void main(String[] args) {
		CobolTag obj = new CobolTag();
	}
}
