import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList; 
 
//http://stackoverflow.com/questions/302371/which-data-structure-would-you-use-treemap-or-hashmap-java
public class RandomWriter {

	static String source = "";
	static double[] probability = new double[224];

	public static void main(String[] args) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader("test.txt"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test_random.txt")));

//		System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddd".length());

		while(f.ready()){												// reads text file into string
			source += f.readLine();
		}
		
		System.out.println(source.length());

		int seedlength = 5;
		
		if(source.length() < seedlength){
			System.out.println("Source text too short!");
			return;
		}
		
		String newText = genSeed(seedlength);
//		System.out.println("seed: " + newText);									// seed

		int passagelength = 25;
		
		for(int i = seedlength; i < passagelength; i++){
			
			char c = getNextChar(newText, seedlength);
			newText += c;
//			System.out.println("next: " + c);
		}

		System.out.println(newText);
		System.out.println(newText.length());

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
//			System.out.println(findText);

			occIndex = source.indexOf(findText, occIndex);
	//		System.out.println(occIndex);
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
//					System.out.println("seedlength became 0! crap");
					nextChar = source.charAt((int) (Math.random() * source.length()));
//					System.out.println(source.length());
					break;
				}
			}
			else{
				nextChar = charOccurs.get((int) (Math.random() * charOccurs.size()));		// select random character from the ones found
	//			System.out.println(charOccurs);
				break;
			}
		}
		return nextChar;
	}
	
	/** 
	 * Generates a random seed consisting only letters whose first character is uppercase while the rest are lowercase
	 * @param seedLength 	the length of the seed to be generated
	 * @return 				the seed, a string of length seedLength
	 */
	private static String randomSeed(int seedLength) {
		String s = "";

		s += (char) (Math.random()*26 + 65);							// uppercase

		for(int i = 1; i < seedLength; i++){
			s += (char) (Math.random()*26 + 97);						// rest are lowercase
		}

		return s;
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

	
/*	old genSeed:
		int[] counts = new int[224];
		String seed = "";

		int length = source.length();
		char c;

		for(int i = 0; i < source.length(); i++){
			c = source.charAt(i);
			if(c > 31 && c < 256)
				counts[c - 32]++;
		}

		//		for(int i = 0; i < counts.length; i++){
		//			System.out.println((char) (i + 32) + " " + counts[i]);
		//		}

		probability[0] = (double) counts[0] / length;
		for(int i = 1; i < counts.length; i++){
			probability[i] = (double) counts[i] / length + probability[i-1];
		}

		//		for(int i = 0; i < probability.length; i++){
		//			System.out.println((char) (i + 32) + " " + probability[i]);
		//		}

		for(int i = 0; i < seedLength; i++){

			double r = Math.random();
			int j = 0; 
			while(r > probability[j]){
				j++;
			}

			seed += (char) (j + 32);
		}

		return seed;
 */
}
