import java.applet.Applet;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


public class WriterApplet extends Applet{


	// UI Stuff
//	Label status;
	JTextArea input = new JTextArea();
	JTextArea output = new JTextArea();
	JSpinner seedSpinner;
	JSpinner outputSpinner;
	
	int seedlength;
	int outputLength;

	static String source = "";
	String genText;

	public WriterApplet() {

	}

	public void init(){
		setSize(642, 1100);		//<-----

//		status = new Label("Please enter input text, set seed length, set output length, and click Generate", Label.CENTER);
//		add(status);
		
		add(new JLabel("Paste original text here:"));
		
		// Input Panel: textbox, copy, paste
		JPanel inputPanel = new JPanel();
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		JScrollPane inputScrollPane = new JScrollPane(input);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPane.setPreferredSize(new Dimension(500, 500));
		inputPanel.add(inputScrollPane);
		
		CopyButton inputCopy = new CopyButton(input);
		PasteButton inputPaste = new PasteButton(input);
		inputPanel.add(inputCopy);
		inputPanel.add(inputPaste);
		
		add(inputPanel);
		

		//*** Panel
		JPanel panel = new JPanel();

		panel.add(new Label("Set seed length (characters):"));
		SpinnerModel seedModel = new SpinnerNumberModel(5, 1, 10, 1);
		seedSpinner = new JSpinner(seedModel);
		panel.add(seedSpinner);

		panel.add(new Label("Set output length (characters):"));
		SpinnerModel outputModel = new SpinnerNumberModel(300, 20, 10000, 1);
		outputSpinner = new JSpinner(outputModel);
		panel.add(outputSpinner);
		
		GenButton gen = new GenButton();
		panel.add(gen);
		
		add(panel);
		//***// Panel

		add(new JLabel("Generated text will appear here:"));
		
		// Output Panel: TextBox, copy, paste
		JPanel outputPanel = new JPanel();
		output.setLineWrap(true);
		output.setWrapStyleWord(true);
		JScrollPane outputScrollPane = new JScrollPane(output);
		outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		outputScrollPane.setPreferredSize(new Dimension(500, 500));
		outputPanel.add(outputScrollPane);
		
		CopyButton outputCopy = new CopyButton(output);
		PasteButton outputPaste = new PasteButton(output);
		outputPanel.add(outputCopy);
		outputPanel.add(outputPaste);
		
		add(outputPanel);
	}

	//	public void start(){
	//	//	setVisible(true);
	//	}

	private void processInput() {

		source = input.getText();
		//		System.out.println(source);
		seedlength = (int) seedSpinner.getValue();
		outputLength = (int) outputSpinner.getValue();
		//		System.out.println(seedlength);
		//		System.out.println(outputLength);

		//		if(source.length() < 11){
		//			System.out.println("Source text too short!");
		//			return;
		//		}

		String genText = genSeed(seedlength);

		for(int i = seedlength; i < outputLength; i++){
			char c = getNextChar(genText, seedlength);
			genText += c;
		}

		output.setText(genText);
		//		System.out.println("output: " + genText);
		//		System.out.println(genText.length());
	}

	/**
	 * Find the next character to be appended to the already generated text currentText, by searching the source for matches of
	 * the substring of the last seedlength characters of currentText.
	 * If no match is found, the seedlength is decremented so that a shorter string is searched for.
	 * 
	 * @param currentText	the existing generated text to which the return character will be appended
	 * @param seedlength	the initial number of characters taken from the end of currentText,
	 * 							which will be used to find matches in the source
	 * @return				the character to be appended to currentText
	 */
	private static char getNextChar(String currentText, int seedlength) {
		char nextChar = 0;		//null character

		while(nextChar == 0){	// while no character has been found
			ArrayList<Character> charOccurs = new ArrayList<Character>();// characters that occur after each segment of text found in source
			int occIndex = 0;

			String findText = currentText.substring(currentText.length() - seedlength, currentText.length());	// String we're looking for

			occIndex = source.indexOf(findText, occIndex);
			while(occIndex != -1 && occIndex + findText.length() < source.length()){	// while a match for findText was found and  
				// there exists a character after the match
				charOccurs.add(source.charAt(occIndex + findText.length()));			// add the postceding character to the bank
				occIndex = source.indexOf(findText, occIndex + 1);						// find next occurrence of findText
			}		// fills charOccurs with all occurrences for matches of length seedlength

			if(charOccurs.isEmpty()){
				seedlength--;					// decrease length of String to be matched to increase chance of match, and continue searching
				if(seedlength == 0){
					// only occurs of the last character of currentText is not found anywhere else in the string
					// a random character must be generated, and we're done
					//				System.out.println("seedlength became 0! crap");
					nextChar = source.charAt((int) (Math.random() * source.length()));
					//				System.out.println(source.length());
					break;
				}
			}
			else{
				nextChar = charOccurs.get((int) (Math.random() * charOccurs.size()));		// select random character from the ones found
				break;
			}
		}
		return nextChar;
	}

	/**
	 * Generates a seed of consisting of random letters or numbers from the source
	 * @param seedLength 	the length of the seed to be generated
	 * @return 				the seed, a string of length seedLength, and all characters can be found in the String source
	 */
	private static String genSeed(int seedLength) {
		String seed = "";
		for(int i = 0; i < seedLength; i++){
			char c = source.charAt((int) (Math.random() * source.length()));				// finds a random character in the source text
			while(!Character.isLetterOrDigit(c)){											// if the character is not a letter or digit,
				c = source.charAt((int) (Math.random() * source.length()));					// then get a new random character
			}
			seed += c;
		}

		return seed;
	}


	private class GenButton extends Button implements ActionListener {
		GenButton() {
			super("Generate");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			processInput();
			repaint();
		}
	}
	
	private class PasteButton extends Button implements ActionListener {
		
		JTextArea area;

		PasteButton(JTextArea area) {
			super("Paste");
			this.area = area;
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0){
			String data = "";
			try {
				data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			area.setText(data);
		}
	}
	
	private class CopyButton extends Button implements ActionListener {
		
		JTextArea area;
		
		CopyButton(JTextArea area) {
			super("Copy");
			this.area = area;
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			StringSelection selection = new StringSelection(area.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	}
}
