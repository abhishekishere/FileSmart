package filesmart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Scanner;

import java.util.TimerTask;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FileSmartMavenMain extends Translator {

	/**
	 * @param args
	 */
	static Scanner s;
	Translator tt;
	String line; // full cobol statement

	// C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE_copy.cob

	public FileSmartMavenMain(String filePath, String delimiter) {
		tt = new Translator();
		s = null;
		try {

			s = new Scanner(new BufferedReader(new FileReader(filePath)));
			s.useLocale(Locale.US);
			s.useDelimiter(delimiter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static String readLine() {
		String line = null;
		if (s.hasNext()) {
			line = s.next();
		} else {
			line = null;
		}
		return line;
	}

	public String processFile(Boolean includeComments, String method1) {

		Method method;
		CobolTag shared = new CobolTag();
		try {

			String line;
			int i = 0;
			method = tt.getClass().getMethod(method1, new String().getClass());

			for (line = this.readLine(); line != null; line = this.readLine()) {
				i = i + 1;
				line = line + " ";
				line = line.replaceAll(GARBAGE, "");
				// if (tight2looseMatching(line) != null) {
				Object name = method.invoke(tt, line.trim());
				if (name != null) {
					// New Thread Created

					CobolStatement cs1 = new CobolStatement(shared,
							(String) name);
					cs1.fullLine = line;
					// cs1.dent = match("\\n*([\\p{Space}]*)" + SENTENCE,
					// line).length();
					// New Thread Started
					cs1.start();
					try {
						// Current Thread Waited ; New Thread Running
						cs1.join();
						// New Thread Completed ; Current Thread Resumed
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					if (includeComments == true) {
						shared.tagText = shared.tagText + "\n/* " + line + "*/";
					}
				}
			}
			shared.tagText = shared.tagText.replaceAll("-", "_");
			shared.tagText = shared.tagText.replaceAll("=\\+", "=");
			shared.tagText = shared.tagText.replaceAll("\\([0]+([1-9]?[0-9]*)",
					"(\\1");
			shared.tagText = shared.tagText.replaceAll("(public\\sstatic\\sString ){2}","public static String ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (shared.tagText);
	}

	public static void main(String[] files) {

		String dependence = "";
		String fileName = "MWOAUD";
//		for (int i = 1; i < 20; i = i+2) {
//			Translator.previousLevel = String.valueOf(i);
//			FileSmartMavenMain smartFile = new FileSmartMavenMain(
//			 "C:\\Users\\abhishekba\\" + fileName + ".dat", "\\.");
//				//	files[0], "\\.");
//			dependence = smartFile.processFile(false, "getDependentVariables")
//					+ dependence.trim();
//		}
		// dependence = "ppppp657767";

		// Node cobol = doc.getElementsByTagName("cobol").item(0);
		// Element elem2 = doc.createElement("dependence");
		// cobol.appendChild(elem2);
		// elem2.setTextContent(dependence);
		// Element elem = doc.createElement("text");
		// Node dep = doc.getElementsByTagName("dependence").item(0);
		// Element elem3 = doc.createElement("text");
		// elem3.setTextContent(dependence);
		// dep.appendChild(elem3);
		// Element elem4 = doc.createElement("path");
		//		
		// Node c2j = doc.getElementsByTagName("c2j").item(0);
		// c2j.appendChild(elem2);
		// elem.setTextContent("");
		//	
		FileSmartMavenMain smartFile = new FileSmartMavenMain(
		 "C:\\Users\\abhishekba\\" + fileName + ".dat", "\\.");
			//	files[0], "\\.");
		String fullProg = smartFile.processFile(true, "tight2looseMatching");
//		fullProg = fullProg.replace("public static void main", dependence
//				+ "\npublic static void main");
		
		try {
			PrintWriter writer = new PrintWriter(new File(
			 "C:\\Users\\abhishekba\\git\\FileSmart\\src\\oata\\" + fileName + ".java"));
			//		files[0] + ".java"));
			writer.print(fullProg + "\n\t\t\t}"
					+ "\n\t\t} while (block != FINISH);" + "\n\t\t}\n}");
			writer.flush();
			writer.close();
			System.out.print(fullProg + "\n\t\t\t}"
					+ "\n\t\t} while (block != FINISH);" + "\n\t\t}\n}");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
