package filesmart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl;

public class FileSmartMavenMain {

	/**
	 * @param args
	 */
	Scanner s;

	public FileSmartMavenMain(String filePath, String delimiter) {

		s = null;

		try {
			s = new Scanner(new BufferedReader(new FileReader(filePath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.useLocale(Locale.US);
		s.useDelimiter(delimiter);

	}

	public String readLine() {
		String line = null;
		if (s.hasNext()) {
			line = s.next();

		} else {
			line = null;
		}

		return line;
	}

	public static void main(String[] files) throws FileNotFoundException {

		// TODO Auto-generated method stub
	//	FileSmartMavenMain smartFile = new FileSmartMavenMain(
				//"C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE",
				//".");
		File file = new File("C:\\Users\\abhishekba\\COBOL\\IWIMS_Code\\EPSSUBS\\EMATCRE");
		Scanner scan = new Scanner(file);
		scan.useDelimiter("\\Z");
		
		int i=0;
		while(i<2000) {
		System.out.println( scan.findWithinHorizon("\\w\\w",0));
	i++;
		}
		}
}
