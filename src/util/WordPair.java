package util;

public class WordPair {
	public String firstWord;
	public String secondWord;
	
	public WordPair(String firstWord, String secondWord) {
		this.firstWord = firstWord;
		this.secondWord = secondWord;	
	}	
	
	public String toString() {
		return "{" + firstWord + " == " + secondWord + "}";	
	}
}