package app;
import java.util.*;
import java.io.*;

import util.FlashCard;

public class FlashCardModel {
	public static String COMMENT_MARK = "#";
	public static String DELIMITER = ":";
	public static String FILE_ENCODING = "UTF-8";
	
	private String fileName;
	
	private Map<String, List<FlashCard>> vocabMap;
	
	private String firstType;
	private String secondType;
	
	public FlashCardModel(String fileName) {
		this.fileName = fileName;
		
		vocabMap = new TreeMap<String, List<FlashCard>>();
		getData();
	}
	
	/**
	 * Return the first category of the FlashCard
	 * @return the first category
	 */
	public String getFirstType() {
		return firstType;
	}
	
	/**
	 * Return the second category of the FlashCard
	 * @return the second category
	 */
	public String getSecondType() {
		return secondType;	
	}
	
	public List<String> getAllSections() {
		List<String> list = new LinkedList<String>();
		
		for (String section : vocabMap.keySet()) {
			list.add(section);	
		}	
		
		return list;
	}
	
	public List<FlashCard> getPairsForSection(String sectionName) {
		List<FlashCard> wordPairs = new LinkedList<FlashCard>();
		
		if (vocabMap.containsKey(sectionName)) {
			// TODO fix to unmodifiable
			wordPairs = vocabMap.get(sectionName);
		}
		
		return wordPairs;
	}
	
	// TODO fix
	public List<FlashCard> getPairsForSection(List<String> sections) {
		List<FlashCard> wordPairs = new LinkedList<FlashCard>();
		
		for (String section : sections) {
			List<FlashCard> sectionPairs = vocabMap.get(section);
			
			if (sectionPairs != null) {
				for (FlashCard wp : sectionPairs) {
					wordPairs.add(wp);	
				}	
			}	
		}
		
		return wordPairs;
	}
	
	private void getData() {
		try {
			Scanner fileScanner = new Scanner(new File(fileName), FILE_ENCODING);
			
			boolean headerLineFound = false;
			
			String currentSection = "";
			while (fileScanner.hasNextLine()) {
				String wholeLine = fileScanner.nextLine().trim();

				if (!wholeLine.startsWith(COMMENT_MARK) && wholeLine.length() > 0) {	// not a "comment"
					if (!headerLineFound) {
						String[] headerParts = wholeLine.split(DELIMITER);
						firstType = headerParts[0].trim();
						secondType = headerParts[1].trim();
						headerLineFound = true;
					} else {
						String[] lineParts = wholeLine.split(DELIMITER);	
						if (lineParts.length == 1) {
							// new section header
							currentSection = lineParts[0];
							
							// If not already in the map, add a new section
							if (!vocabMap.containsKey(currentSection)) {
								vocabMap.put(lineParts[0], new LinkedList<FlashCard>());
							}
						} else if (lineParts.length == 2) {
							// 	word pairs
							FlashCard wp = new FlashCard(lineParts[0], lineParts[1]);
							vocabMap.get(currentSection).add(wp);
						}
					}
				}
			}				
		} catch (Exception ex) {} 
	}
}
