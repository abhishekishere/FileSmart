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
	static String operand_bkup;
	String operand_new;
	String line; // full cobol statement
	String group; // matching group
	NodeList syntax;
	String value = "";
	int i;
	public static FileSmartMavenMain smartFile;

	static {
		smartFile = new FileSmartMavenMain(
				"C:\\Users\\abhishekba\\MWOAUD.dat",
				"\\.");
	}

	// C:\\Users\\abhishekba\\git\\FileSmart\\resource\\MWOA.xml

	public CobolTag() {

		for (line = smartFile.readLine(); line != null; line = smartFile
				.readLine()) {
			line = line + " ";
			tight2looseMatching(line);
			newThread(line);
		}

	}

	/**
	 * @param currLine
	 */
	public void tight2looseMatching(String currLine) {

		/* Paragraphs starting */
		// name = match(OPERAND + "-" + "(" + SYMBOL + ")", currLine);
		// if (name != null) {
		// String caseValue = match("(" + OPERAND + ")" + "-" + SYMBOL,
		// currLine);
		// name = "\n\tcase " + name + ":" + "\npublic static final int "
		// + name + " = " + caseValue.trim() + ";\n";
		// // return;
		// slowWriteAsItIs(name);
		// text = text + name;
		// line = smartFile.readLine();
		// tight2looseMatching(line);
		// newThread(line);
		// }
		// if (name != null)
		// return;
		/* Get All the memory variables */

		// name = match("\\p{Space}*"+SET+"{3}.*" , currLine);
		// if (name != null){
		// if (name.equals("PIC")) {
		//		
		// name = "MV";
		// if (name != null)
		// return;
		//		
		// }}
		/* Get the hierarchy */
		// if(operand_bkup == null) operand_bkup = "03";
		// name = match("\\p{Space}*" + SET + "{1}.*", currLine);
		//		
		// if (name != null) {
		// if (match("(\\p{Digit}{2})", name) != null) {
		// if(Integer.parseInt(name)>) {
		// if (name != null)
		// name = "REC";
		// return;
		//
		// }else {
		// //operand_bkup = name;
		// name = "BACK";
		//				
		// return;
		// }
		// } }
		/*
		 * Run to get 01 command tags properly
		 */
		// operand_bkup = "01";
		// name = match("(" + OPERAND + ")" + SENTENCE, currLine);
		// operand_new = name;
		// if (name != null) {
		// if ((Integer.parseInt(operand_bkup.trim()) == Integer
		// .parseInt(operand_new.trim()) - 2)
		// || Integer.parseInt(name.trim()) == 01) {
		//
		// name = "C" + name.trim();
		// if (name != null)
		// return;
		// }
		// }
		/*
		 * Run to get 03 command tags properly
		 */
		// operand_bkup = "03";
		// name = match("(" + OPERAND + ")" + SENTENCE, currLine);
		// operand_new = name;
		// if (name != null) {
		// if ((Integer.parseInt(operand_bkup.trim()) == Integer
		// .parseInt(operand_new.trim()) - 2)
		// || Integer.parseInt(name.trim()) == 03) {
		//		
		// name = "CC" + name.trim();
		// if (name != null)
		// return;
		// }
		// }

		/*
		 * Run to get 05 command tags properly
		 */
		// operand_bkup = "05";
		// name = match("(" + OPERAND + ")" + SENTENCE, currLine);
		// operand_new = name;
		//		
		// if (name != null) {
		// if ((Integer.parseInt(operand_bkup.trim()) == Integer
		// .parseInt(operand_new.trim()) - 2)
		// || Integer.parseInt(name.trim()) == 05) {
		//
		// name = "CCC" + name.trim();
		// if (name != null)
		// return;
		// }
		// }

		/* checking for single word command like "Program-Id" */
		 if (name == null) {
		 name = match(SPACE + "(" + PROCEDURE_COMMANDS + ")" + SENTENCE,
		 currLine);
		
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
				NodeList syntax_bkup = syntax;
				int i_bkup = i;
				syntax = comm.getChildNodes();

				for (i = 0; i < syntax.getLength(); i++) {

					Node type = syntax.item(i);

					value = type.getTextContent();
					if (value == "") {
						line = smartFile.readLine();
						line = line + " ";
						command = null;

						tight2looseMatching(line);
						newThread(line);

					}
					if (type.getNodeName() == "path") {
						name = match(value, line);
						if (name != null)
							newThread(line);
						else
							continue;
					}
					if (type.getNodeName() == "text") {
						slowWrite(value);
						text = text + value;
					}
				}
				syntax = syntax_bkup;
				i = i_bkup;
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

		CobolStatement cs1 = new CobolStatement(this, name, whichLine, Thread
				.currentThread());

		cs1.start();
		try {
			cs1.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		name = cs1.getName1();
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
