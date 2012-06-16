package util;

public class FlashCard {
	public String section;
	public String front;
	public String back;
	
	public FlashCard(String section, String firstWord, String secondWord) {
		this.section = section;
		this.front = firstWord;
		this.back = secondWord;	
	}	
	
	public FlashCard(String front, String back) {
		this.front = front;
		this.back = back;
	}
	
	public String toString() {
		return "{" + section + ":" + front + "=" + back + "}";	
	}
}