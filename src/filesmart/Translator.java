package filesmart;

import java.io.File;
import java.util.StringTokenizer;
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
	static final String xml = "resource\\c2j.xml";
	static final String ALPHANUMNERIC_WORD = "\\s*([A-Za-z0-9-]+)\\s*";
	static final String ALPHABETIC_WORD = "\\s*([A-Za-z]+)\\s*";
	static final String ALPHANUMERIC_WORD_ENDING_IN_ANYTHING = "\\s*([a-zA-Z0-9-]*)\\s*.*";
	static final String SYMBOL = "[A-Za-z0-9-\",/()@#:*=.]+";
	static final String SPACE = "\\s*";
	static final String WORD = "\\s*" + SYMBOL + "\\s*";
	static final String SENTENCE = "[" + WORD + "]+";
	static final String ALNUM_STARTING_WITH_NEW_LINE = "\\n*"
			+ ALPHANUMERIC_WORD_ENDING_IN_ANYTHING;
	static final String NUMBER = "[0-9.]+";
	static final String OPERAND = "\\s*" + NUMBER + "\\s*";
	static final String OPERATOR = "[=]+";
	static final String CONDITION = WORD + OPERATOR + WORD;
	static final String COMMAND = "(MOVE)|(OPEN)|(ELSE)|(FD)";
	static final String COBOL_STATEMENT = COMMAND + SENTENCE;
	static final String PROCEDURE_COMMANDS = "(PERFORM)|(PROCEDURE)|(CALL)|(ELSE)|(IF)|(FD)|(OPEN)|(MOVE)|(COMPUTE)|(CLOSE)|(STOP)|(GO)|(READ)|(COPY)|(EXIT)|(DECLARATIVES)|(ADD)|(SELECT)|(MWOAUD)|(VUPORT)";
	static final String IDENTIFICATION_COMMANDS = "(IDENTIFICATION)|(PROGRAM-ID)|(ENVIRONMENT)|(CONFIGURATION)|(INPUT-OUTPUT)|(FILE-CONTROL)|(DATA)|(FILE)";
	static final String SET = "(\\S++\\s++)";
	static final String PAR1 = "([0-9]{1,}-[A-Z]{1,})";
	static final String PARAGRAPH_NAMES = "\\p{Digit}+[-]+([A-Z]+)";
	static final String PARAGRAPH2 = "\\p{Digit}+[-]+([A-Z]+[-][A-Z]+)";
	static final String PARAGRAPH = PAR1;
	static final String GARBAGE = "([A-Z]{3}[0-9]{5})|([A-Z]{2}[0-9]{5})|([A-Z]{1}[0-9]{6,7})|([A-Z]{3}[0-9]{4})";
	static final String LOGICAL_OPERATOR = "(AND)|(OR)";
	static final String BODY_COMMANDS = "IF";
	static final String VARNAME = "([A-Z0-9]+(-[A-Z0-9]{1,}){1,})";
	static final String DVARIABLE = "[0-9]{2,}\\s+(VARNAME|(FILLER))";
	static final String PICVARIABLE = "(\\s+PIC\\s+[X9()0-9]{1,})*";
	static final String OCVARIABLE = "(\\s+OCCURS\\s+[0-9]{1,})*";
	static final String BINVARIABLE = "(\\s+BINARY\\s*)*";
	static final String VALUEVARIABLE = "(\\s+VALUE\\s+[\"A-Z0-9]+)*";
	static final String REDVARIABLE = "(\\s+REDEFINES\\s+" + VARNAME + ")*";
	static final String TIMESVARIABLE = "(\\s+TIMES\\s*)*";
	static final String LEVELEX = "([0-9]{2,})\\s+(VARNAME|(FILLER))"
			+ PICVARIABLE + OCVARIABLE + BINVARIABLE + VALUEVARIABLE
			+ REDVARIABLE + TIMESVARIABLE;
	static final String VARIABLE = DVARIABLE + PICVARIABLE + OCVARIABLE
			+ BINVARIABLE + VALUEVARIABLE + REDVARIABLE + TIMESVARIABLE;

	static String level, levelNext, previousLevel;
	static {
		level = null;
		levelNext = null;
		previousLevel = "00";
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

//	public static String getDependentVariables(String line) {
//		String name = null;
//		if (match(LEVELEX, line) != null) {
//			if (level == null) {
//
//				if (name != null)
//					return name;
//				name = match(DVARIABLE, line);
//
//				if (name != null) {
//					level = match(LEVELEX, line);
//					if (Integer.parseInt(level) > Integer
//							.parseInt(previousLevel))
//						name = "\npublic static String " + name + "=";
//					else {
//						level = null;
//						name = null;
//					}
//				}
//			}
//			if (name != null)
//				return name;
//			if (level != null) {
//				if (levelNext == null) {
//
//					if (Integer.parseInt(level) < Integer.parseInt(match(
//							LEVELEX, line))) {
//						levelNext = match(LEVELEX, line);
//					}
//				}
//			}
//			if (levelNext != null) {
//
//				if (Integer.parseInt(levelNext) == Integer.parseInt(match(
//						LEVELEX, line))) {
//					name = match(VARIABLE, line);
//					if (name == "FILLER") {
//						// 03 FILLER PIC X(16)
//						name = "FILLER("
//								+ match(SET + "{4}", line.trim() + " ") + ")";
//					} else {
//						name = "+" + name;
//					}
//
//					if (name != null)
//						return name;
//					if (Integer.parseInt(levelNext) > Integer.parseInt(match(
//							LEVELEX, line))) {
//						level = null;
//						levelNext = null;
//						name = ";\n";
//					}
//				}
//			}
//			if (name != null)
//				return name;
//		} else {
//			if (level != null) {
//				level = null;
//				levelNext = null;
//				name = ";\n";
//				return name;
//			}
//		}
//		return null;
//	}

	public static String tight2looseMatching(String line) {
//	try {
			return "c2j";
//		String name = null;
//		/* Get All the memory variables */
//		if (name != null)
//			return name.trim();
//		name = match(".*(FILLER).*", line);
//		if (name == null) {
//			name = match("\\p{Space}*" + SET + "{3}.*", line);
//			if (name != null) {
//				if (name.trim().equals("PIC")) {
//					name = "MV";
//				} else
//					name = null;
//			}
//		}
//		/* checking for commands defined in xml */
//		if (name != null)
//			return name.trim();
//		name = match(SPACE + "(" + PROCEDURE_COMMANDS + "|"
//				+ IDENTIFICATION_COMMANDS + ")" + SENTENCE, line);
//		/* Paragraphs starting */
//		if (name != null)
//			return name.trim();
//		name = match(PARAGRAPH, line.trim());
//		if (name != null) {
//			String caseValue = match("(" + OPERAND + ")" + "-" + SYMBOL, line
//					.trim());
//			if (caseValue == null) {
//				int caseValue1 = (int) (Math.random() * 10000);
//				caseValue = String.valueOf(caseValue1);
//			}
//			name = "PARAGRAPH";
//		}
//		/* search for tag */
//		if (name != null)
//			return name.trim();
//		String[] line0 = line.split("\\p{Space}");
//		for (int i = 0; i < line0.length; i++) {
//			command = doc.getElementsByTagName(line0[0]).item(0);
//			if (command != null) {
//				return line0[i];
//			}
//		}
//	} catch (InvalidPathException e) {
//		// TODO: handle exception
//		System.out.println("Invalid Path Exception:"+e.pattern);
//	}
//		return null;
	}

	public static String match(String pattern, String line) throws InvalidPathException {
		String result=null;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		if (m.matches()) {
			try {
			result = m.group(1).trim();
			} catch(NullPointerException e) {
				throw new InvalidPathException(pattern,line);
			} 
		} else {
			result = null;
		}
		return result;
	}

	public static String slowWriteAsItIs(String text1) {
		CobolStatement ss8 = (CobolStatement) Thread.currentThread();
		ss8.text = ss8.text + text1;
		return text1;
	}

	public static String slowWrite(String text) {
		CobolStatement ss9 = (CobolStatement) Thread.currentThread();
		String line = ss9.fullLine;
		try {
			Thread.sleep(1);
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
		//ss9.text = ss9.text + text;
		return text;
	}
public static void  log(String name,String line, int handled) {
	line = line.replaceAll("'","''");
	String queryString = null;
		try {
			Class.forName( "net.sourceforge.jtds.jdbc.Driver" );
		
		java.sql.Connection con = java.sql.DriverManager.getConnection( "jdbc:jtds:sqlserver://10.1.1.203:1433/abhishek;instance=SQLEXPRESS", "sa", "outline@123" );
		java.sql.Statement stmt = con.createStatement();

		          queryString = "insert into cobol2java values ('"+name+"', '"+line+"', "+handled+") ";
		          stmt.executeUpdate(queryString);
		        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(queryString);
			e.printStackTrace();
		}   

}
public static void  logUpdate(String name,String line, int handled) {
	line = line.replaceAll("'","''");
	String queryString = null;
		try {
			Class.forName( "net.sourceforge.jtds.jdbc.Driver" );
		
		java.sql.Connection con = java.sql.DriverManager.getConnection( "jdbc:jtds:sqlserver://10.1.1.203:1433/abhishek;instance=SQLEXPRESS", "sa", "outline@123" );
		java.sql.Statement stmt = con.createStatement();

		          queryString = "update cobol2java set handled="+handled+" where name= '"+name+"' AND line= '"+line+"'";
		          stmt.executeUpdate(queryString);
		        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(queryString);
			e.printStackTrace();
		}   

}
	public static void main(String[] args) {
		String line = "AND NOT = \"R\" ";
		
		// String group = match("\\p{Space}*" + SET + "{1}.*", line + " ");
		// String group = match("\\p{Space}.*OR\\p{Space}("+SET+"{1}).*",line);
		//String group = match("([(AND)]+).*", line.trim());
		//System.out.println(group);

	}
}
