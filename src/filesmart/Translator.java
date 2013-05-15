package filesmart;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Translator {
	static Document doc;
	static Node command;
	static final String xml = "C:\\Users\\abhishekba\\git\\FileSmart\\resource\\c2j.xml";
	static final String ALPHANUMNERIC_WORD = "\\s*([A-Za-z0-9-]+)\\s*";
	
	static final String ALPHANUMERIC_WORD_ENDING_IN_ANYTHING = "\\s*([a-zA-Z0-9-]*)\\s*.*";
	static final String SYMBOL = "[A-Za-z0-9-\",/()@#:*=]+";
	static final String WORD = "\\s*"+SYMBOL+"\\s*";
	static final String SENTENCE = "["+ WORD+"]+";
	static final String ALNUM_STARTING_WITH_NEW_LINE = "\\n*"+ALPHANUMERIC_WORD_ENDING_IN_ANYTHING;
	static final String NUMBER = "[0-9.]+";
	static final String OPERAND = "\\s*"+NUMBER+"\\s*";
	static final String OPERATOR = "[=]+";
	static final String CONDITION = WORD+OPERATOR+WORD;
	static final String COMMAND = "(MOVE)|(OPEN)|(ELSE)";
	static final String COBOL_STATEMENT = COMMAND+SENTENCE;
	
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
		String line = " FD  MTRL"+
     "LABEL RECORDS ARE STANDARD"+
         "VALUE OF FILENAME IS  \"MTRL\""+
                  "LIBRARY  IS  \"EPSDATA\""+
                  "VOLUME   IS   IVAR-WO-DATA-VOL"+
                  "SPACE    IS   OLD-FILE-SIZE";
		//command = doc.getElementsByTagName("IDENTIFICATION").item(0).getChildNodes().item(3);
		//System.out.println(command);
		String group = match(SENTENCE+"LIBRARY"+WORD+"\"("+SYMBOL+")\""+SENTENCE,line);
		System.out.println(group);
		
	}
}
