package filesmart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Translator {

	/**
	 * @param args
	 */

	private String javaeq;
	CobolStatement cobol;
	int index = 0;
	Document doc;

	public Translator() {
		// TODO Auto-generated constructor stub

	}

	public Translator(String xmlfile) {
		cobol = new CobolStatement();
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

	String translate(String line) {

		String name = match("\\s*([a-zA-Z0-9-]*)\\s*", line);
		String text = line;
		CobolTag tag = new CobolTag(name, text);
		cobol.addTag(tag);
		Node command = doc.getElementsByTagName(tag.name).item(0);
		System.out.println(command);
		NodeList syntax = command.getChildNodes();
		for (int i = 0; i < syntax.getLength(); i++) {
			Element type = (Element) syntax.item(i);
			String value = type.getNodeValue();
			if (type.getNodeName() == "path") {
				if (value == "") {
					return null;
				} else {
					this.translate(match(value, line));
				}
			} else if (type.getNodeName() == "text") {
				tag.text = tag.text + value;
			}
			cobol.removeTag(tag);
		}
		return null;
	}

	private String match(String pattern, String line) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(line);
		System.out.println(m.matches());
		return m.group(1);
	}

}
