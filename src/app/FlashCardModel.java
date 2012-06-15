package app;
import java.util.*;
import java.io.*;

import util.WordPair;

public class FlashCardModel {
	public static String COMMENT_MARK = "#";
	public static String DELIMITER = ":";
	public static String FILE_ENCODING = "UTF-8";
	
	private String fileName;
	
	private Map<String, List<WordPair>> vocabMap;
	
	private String firstType;
	private String secondType;
	
	public FlashCardModel(String fileName) {
		this.fileName = fileName;
		
		vocabMap = new TreeMap<String, List<WordPair>>();
		getData();
	}
	
	public String getFirstType() {
		return firstType;
	}
	
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
	
	public List<WordPair> getPairsForSections(List<String> sections) {
		List<WordPair> wordPairs = new LinkedList<WordPair>();
		
		for (String section : sections) {
			List<WordPair> sectionPairs = vocabMap.get(section);
			
			if (sectionPairs != null) {
				for (WordPair wp : sectionPairs) {
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
			
			String currentSection = "none";
			while (fileScanner.hasNextLine()) {
				String wholeLine = fileScanner.nextLine().trim();

				if (!wholeLine.startsWith(COMMENT_MARK)) {	// not a "comment"
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
								vocabMap.put(lineParts[0], new LinkedList<WordPair>());
							}
						} else if (lineParts.length == 2) {
							// 	word pairs
							WordPair wp = new WordPair(lineParts[0], lineParts[1]);
							vocabMap.get(currentSection).add(wp);
						}
					}
				}
			}				
		} catch (Exception ex) {} 
	}
}
