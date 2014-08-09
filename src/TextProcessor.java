import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class TextProcessor {

	public static void main(String[] args){
//		BufferedReader f = new BufferedReader(new FileReader("sonnets.txt"));
//		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("newsonnets.txt")));
//				
//		String line;
//		while(f.ready()){
//			line = f.readLine();
//			if(line.length() > 20){
//				line = line.trim();
//				out.println(line);
//			}
//		}
//		out.close();
//		System.exit(0);
		
		StringSelection selection = new StringSelection("hello");
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
		
		String data = "";
		try {
			data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println(data);
		
	}
}
