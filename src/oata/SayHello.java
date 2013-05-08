package oata;

import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import oata.HelloWorld.ABC;

public class SayHello {

	/**
	 * @param args
	 */
	public static void main(String[] 
	                               args) {
		char ab[];
		int a[];
		char a[]={'1','2','5'} , b[]={'A','V','f'};
		ab = a;
		System.out.println(ab);
		String ab1 = "Abhishek"; 
		CharArrayReader car;
		try {
			FileReader fr = new FileReader("");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		HelloWorld obj = new HelloWorld();
		ABC nn = new ABC();
		nn.sayHello(ab1);
System.out.println();
	}

}
