package filesmart;

import static filesmart.Translator.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author abhishekba This Class acts as interface between Translator Class and
 *         FileSmartMavenMain Class . It receives a line for translation from
 *         FileSmartMavenClass and uses Translator to get the Tag in the Xml
 *         file, parses the tag(in "run" method) and returns the java equivalent
 */
public class CobolTag implements Runnable {

	String tagText;
	CobolStatement lastTag;

	public CobolTag() {
		tagText = "";
	}

	@Override
	public void run() {
		CobolStatement ss9 = (CobolStatement) Thread.currentThread();
		String name = ss9.getLine();
		String fullLine = ss9.fullLine;

		// String name = tight2looseMatching(currLine);
		ss9.text = "";
		command = doc.getElementsByTagName(name.trim()).item(0);
		Element comm = (Element) command;
		if (command != null) {
			NodeList syntax = comm.getChildNodes();
			for (int i = 0; i < syntax.getLength(); i++) {
				Node type = syntax.item(i);
				String value = type.getTextContent();
				if (value == "") {
					String line = FileSmartMavenMain.readLine();
					tagText = tagText + ss9.text;
					ss9.text = "";
					CobolStatement cs1 = new CobolStatement(this, line);
					cs1.fullLine = line;
					cs1.start();
					try {
						cs1.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(type.getNodeName() == "path") break;
					if(type.getNodeName() == "text")continue;
				}
				if (type.getNodeName() == "path") {
					String path = null;
					try {
						path = match(value, ss9.fullLine);
					} catch (InvalidPathException e1) {
						// TODO value match the line but doesn't return the group
						ss9.text = "\n/*"+ss9.fullLine+"*/";
						break;
					}
					if (path != null) {
						tagText = tagText + ss9.text;
						ss9.text = "";
						CobolStatement cs1 = new CobolStatement(this, path);
						cs1.fullLine = ss9.fullLine;
						cs1.start();
						try {
							cs1.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else
						continue;
				}
				if (type.getNodeName() == "text") {
					ss9.text = ss9.text + slowWrite(value);
//					tagText = tagText + ss9.text;
//					ss9.text = "";
					
				}
			} 
		} else {
			/*
			 * command == null
			 */
			ss9.text = ss9.text + slowWriteAsItIs(name);
			// CobolStatement ss8 = (CobolStatement) Thread.currentThread();
			//tagText = tagText + name;
			// ss8.text = "";
		}
		tagText = tagText + ss9.text;
	}
	// System.out.println(ss9.text+"\n<-------");
}
