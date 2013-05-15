package filesmart;

import static filesmart.Translator.*;

import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.soap.Text;

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

			tight2looseMatching();

			if (name != null)
				command = doc.getElementsByTagName(name.trim()).item(0);
			if (command != null) {
				newThread();
			}
		}
	}

	public void tight2looseMatching() {
		// First I check if it is a case
		name = match(OPERAND+"-"+"("+SYMBOL+")" ,line);
		if(name != null) {
			name = "case "+name+":" ;
			return;
		}
		
		if(name != null) return;
		name = match("(" + OPERAND + ")" + SENTENCE, line);
		
		if (name != null) {
			name = "C" + name.trim();
			if(name != null) return;
		}
		// checking for single word command like "Program-Id"
		if(name == null) {
			name = match("("+WORD+")",line);
			
		}
		if(name != null) return;
		// Checking for multiple word command
		if(name== null) {
			name = match("("+WORD+")"+SENTENCE,line); return;
		}
		if (name == null) {
			name = match(ALPHANUMERIC_WORD_ENDING_IN_ANYTHING, line);
			name = match("(" + WORD + ")" + WORD, line);
			if(name != null) return;
		}
		if (name == null) {
			name = match("(" + WORD + ")" + SENTENCE, line);
			if(name != null) return;
		}
		name =null;
	}

	// Shared Code
	@Override
	public void run() {

		for (int i = 0; i < syntax.getLength(); i++) {

			Node type = syntax.item(i);

			value = type.getTextContent();
			if (value == "") {
				line = smartFile.readLine();
				
				/*
				 * name = match(ALPHANUMNERIC_WORD, line); if (name == null) {
				 * name = match("(" + SENTENCE + ")", line); } slowWrite(name);
				 * text = text + name;
				 */
			}
			if (type.getNodeName() == "path") {
				
				command = null;
				if(value != null) {
					name = match(value,line);
				}
				
				if (name != null) {
					command = doc.getElementsByTagName(name.trim()).item(0);
					if (command != null) {
						NodeList syntax_bkup = syntax;
						int i_bkup = i;
						newThread();
						syntax = syntax_bkup;
						i = i_bkup;
					} else {

						slowWriteAsItIs(name);
						text = text + name;
					}
				}

			}

			if (type.getNodeName() == "text") {
				if(value=="") {
					slowWrite(line);
					text = text + line;
				}
				slowWrite(value);
				text = text + value;
			}
		}
	}

	private void slowWriteAsItIs(String text1) {
		// TODO Auto-generated method stub
		text = text + text1;
		System.out.print(text1);
	}

	public void newThread() {
		Element comm = (Element) command;

		syntax = comm.getChildNodes();
		CobolStatement cs1 = new CobolStatement(this, name, line);
		cs1.start();
		try {
			cs1.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
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
			text = text.replaceAll(Integer.toString(j), token);
		}
		System.out.print(text);
	}

	public static void main(String[] args) {
		CobolTag obj = new CobolTag();
	}
}
