package filesmart;

import static filesmart.Translator.ALPHANUMERIC_WORD_ENDING_IN_ANYTHING;
import static filesmart.Translator.OPERAND;
import static filesmart.Translator.SENTENCE;
import static filesmart.Translator.SYMBOL;
import static filesmart.Translator.WORD;
import static filesmart.Translator.command;
import static filesmart.Translator.doc;
import static filesmart.Translator.match;

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
	String value = "";
	public static FileSmartMavenMain smartFile;

	static {
		smartFile = new FileSmartMavenMain(
				"C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE_copy.cob",
				"\\.");
	}

	public CobolTag() {
		for (line = smartFile.readLine(); line != null; line = smartFile
				.readLine()) {
			newThread(line);
		}

	}

	public void tight2looseMatching(String currLine) {
		// First I check if it is a case
		name = match(OPERAND + "-" + "(" + SYMBOL + ")", currLine);
		if (name != null) {
			name = "case " + name + ":";
			return;
		}
		// Record Format Description
		if (name != null)
			return;
		name = match("(" + OPERAND + ")" + SENTENCE, currLine);

		if (name != null) {
			name = "C" + name.trim();
			if (name != null)
				return;
		}
		// checking for single word command like "Program-Id"
		if (name == null) {
			name = match("(" + WORD + ")", currLine);

		}
		if (name != null)
			return;
		

		name = null;
	}

	// Shared Code
	@Override
	public void run() {

		if (name != null) {
			command = doc.getElementsByTagName(name.trim()).item(0);
			Element comm = (Element) command;

			if (command != null) {
				syntax = comm.getChildNodes();

				for (int i = 0; i < syntax.getLength(); i++) {

					Node type = syntax.item(i);

					value = type.getTextContent();
					if (value == "") {
						line = smartFile.readLine();
						command = null;
						newThread(line);

					}
					if (type.getNodeName() == "path") {

						if (value != "") {
							name = match(value, line);
							if (name != null)
								command = doc.getElementsByTagName(name.trim())
										.item(0);
							if (command != null) {
								newThread("");
							} else {
								slowWriteAsItIs(name);
								text = text + name;
							}
						}

					}

					if (type.getNodeName() == "text") {
						slowWrite(value);
						text = text + value;
					}
				}
			} else {
				slowWriteAsItIs(name);
				text = text + name;
			}
		}

	}

	private void slowWriteAsItIs(String text1) {
		// TODO Auto-generated method stub
		text = text + text1;
		System.out.print(text1);
	}

	public void newThread(String whichLine) {
		tight2looseMatching(whichLine);
		CobolStatement cs1 = new CobolStatement(this, name, whichLine);

		cs1.start();
		try {
			cs1.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		name = cs1.getName();
		line = cs1.getLine();
	}

	private void slowWrite(String text) {

		try {
			Thread.sleep(10);
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
			text = text.replaceAll(Integer.toString(j), token);
		}
		System.out.print(text);
	}

	public static void main(String[] args) {
		CobolTag obj = new CobolTag();
	}
}
