package filesmart;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Translator {

	/**
	 * @param args
	 */
	
	private String javaeq;
	static HashSet hierarchy;
	int index = 0;
	Document doc;

	public Translator(String xmlfile) {
		InputStream xml = null;
		try {
			xml = new FileInputStream(xmlfile);
		} catch (FileNotFoundException e1) {
				e1.printStackTrace();
		}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xml);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private String translate() {
		String addCode = null;
		String commandName;
		/*Node command = this.doc.getElementsByTagName(cobolStatementArray[0]).item(0);
		NodeList syntax = command.getChildNodes();
		
		for (int i = 0; i < syntax.getLength(); i++) {
			Element type = (Element) syntax.item(i);
			if (type.getNodeName() == "path") {
			} else if (type.getNodeName() == "text") {
			}
		}*/
		return addCode;
	}

	public static void main(String[] args) {
		
		FileSmartMavenMain smartFile = new FileSmartMavenMain("C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE","\\.");
		String line = smartFile.readLine();
		System.out.println(line);
		
		Translator tobj = new Translator("C:\\Users\\abhishekba\\Workspaces\\MyEclipse 8.6\\FileSmart\\resource\\c2j.xml");
		tobj.translate();
		
	}

	private void match() {
		Pattern p = Pattern.compile(".*(PIC).*");
		Matcher m = p.matcher("03  FILLER                  PIC X(03)");
		
		System.out.println(m.matches());
		System.out.println(m.group(1));
	}

}
