package util;

public class FlashCard {
	public String front;
	public String back;
	
	public FlashCard(String firstWord, String secondWord) {
		this.front = firstWord;
		this.back = secondWord;	
	}	
	
	public String toString() {
		return "{}"; //"{" + front + " == " + back + "}";	
	}
}