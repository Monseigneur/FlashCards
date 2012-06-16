package app;

import java.util.*;
import java.io.*;

import util.FlashCard;
import util.Constants;

public class FlashCardModel {
	public String COMMENT_MARK;
	public String DELIMITER;

	private String title;
		
	// New fields
	private Map<String, List<FlashCard>> vocabMap;
	private Map<String, FlashCard> sectionSides;
	
	
	public FlashCardModel() {
		vocabMap = new TreeMap<String, List<FlashCard>>();
		sectionSides = new TreeMap<String, FlashCard>();
	}
	
	public FlashCardModel(String fileName) {
		//this.fileName = fileName;
		this();
		getData(fileName);
	}
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * Return the first category of the FlashCard
	 * @return the first category
	 */
	public String getFirstType(String section) {
		return sectionSides.get(section).front;
	}
	
	/**
	 * Return the second category of the FlashCard
	 * @return the second category
	 */
	public String getSecondType(String section) {
		return sectionSides.get(section).back;
	}
	
	public Set<String> getAllSections() {
		return vocabMap.keySet();
	}
	
	public FlashCard getCardFromSection(String section) {
		if (section.equalsIgnoreCase("ALL")) {
			int sectionIndex = Constants.rand.nextInt(vocabMap.keySet().size());
			
			for (String s : vocabMap.keySet()) {
				if (sectionIndex == 0) {
					section = s;
					break;
				} else {
					sectionIndex--;
				}
			}
		}
		List<FlashCard> sectionDeck = vocabMap.get(section);
		int cardIndex = Constants.rand.nextInt(sectionDeck.size());
		return sectionDeck.get(cardIndex);
	}
	
	public void changeFile(String fileName) {
		// TODO
	}
	
	private void getData(String fileName) {
		try {
			Scanner fileScanner = new Scanner(new File(fileName), Constants.FILE_ENCODING);
			
			boolean headerLineFound = false;
			
			String currentSection = "";
			while (fileScanner.hasNextLine()) {
				String wholeLine = fileScanner.nextLine().trim();

				if (wholeLine.length() > 0) {
					if (!headerLineFound) {
						// header line
						String[] constants = wholeLine.split(" ");
						COMMENT_MARK = constants[0].substring(7);
						DELIMITER = constants[1].substring(9);
						headerLineFound = true;
					} else if (!wholeLine.startsWith(COMMENT_MARK)) {
						String[] lineParts = wholeLine.split(DELIMITER);
						
						if (lineParts.length == 1) {
							// Title
							title = lineParts[0];
						} else if (lineParts.length == 2) {
							// FlashCard
							FlashCard fc = new FlashCard(currentSection, lineParts[0], lineParts[1]);
							vocabMap.get(currentSection).add(fc);
						} else if (lineParts.length == 3) {
							// Section heading
							currentSection = lineParts[0].trim();
							if (!vocabMap.containsKey(currentSection)) {
								vocabMap.put(currentSection, new LinkedList<FlashCard>());
								String f = lineParts[1].trim();
								String g = lineParts[2].trim();
								sectionSides.put(currentSection, new FlashCard(f, g));
							}
						}
					}
				}
			}				
		} catch (Exception ex) { ex.printStackTrace(); } 
	}
}
