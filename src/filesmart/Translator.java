package filesmart;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Translator {
	static Document doc;
	static Node command;
	static final String xml = "C:\\Users\\abhishekba\\Workspaces\\MyEclipse 8.6\\FileSmart\\resource\\c2j.xml";
	static final String ALPHANUMNERIC_WORD = "\\s*([A-Za-z0-9-]+)\\s*";
	
	static final String ALPHANUMERIC_WORD_ENDING_IN_ANYTHING = "\\s*([a-zA-Z0-9-]*)\\s*.*";
	static final String SYMBOL = "[A-Za-z0-9-\",/()]+";
	static final String WORD = "\\s*"+SYMBOL+"\\s*";
	static final String SENTENCE = "["+ WORD+"]+";
	static final String ALNUM_STARTING_WITH_NEW_LINE = "\\n*"+ALPHANUMERIC_WORD_ENDING_IN_ANYTHING;
	static final String NUMBER = "[0-9.]+";
	static final String OPERAND = "\\s*"+NUMBER+"\\s*";
	
	static {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			File fXmlFile = new File(xml);
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String match(String pattern, String line) {
		String result;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		if (m.matches()) {
			result = m.group(1);
		} else {
			result = null;
		}
		return result;
	}

	public static void main(String[] args) {
		String line = ""+
     "SELECT MTRL"+
         "ASSIGN TO \"MTRL\" \"DISK\" NODISPLAY"+
         "ORGANIZATION IS SEQUENTIAL"+
         "ACCESS MODE  IS DYNAMIC ";
		//command = doc.getElementsByTagName("IDENTIFICATION").item(0).getChildNodes().item(3);
		//System.out.println(command);
		String group = match(ALPHANUMERIC_WORD_ENDING_IN_ANYTHING, line);
		System.out.println(group);
		
	}
}
