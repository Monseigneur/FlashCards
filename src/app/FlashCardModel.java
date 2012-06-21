package app;

import java.util.*;
import java.io.*;

import util.FlashCard;
import util.Constants;

/**
 * FlashCardModel is the model class for a flash card program. It can read data from
 * files and generate flash cards with that data, with features such as showing the
 * backs of the cards first, sorting cards by sections, and supporting multiple flash
 * card data files.
 * 
 * @author Milan Justel (milanj91)
 */

public class FlashCardModel {
	public String COMMENT_MARK;
	public String DELIMITER;

	private String currentDataFile;
	
	private String title;
	
	private Map<String, List<FlashCard>> vocabMap;
	private Map<String, FlashCard> sectionSides;
	
	/**
	 * Default Constructor
	 */
	public FlashCardModel() {
		vocabMap = new TreeMap<String, List<FlashCard>>();
		sectionSides = new TreeMap<String, FlashCard>();
		
		currentDataFile = Constants.DEFAULT_FILE;
		
		getData(currentDataFile);
	}
	
	/**
	 * Gets the title of the current FlashCard deck
	 * @return the title of the FlashCard deck
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Return the front side category name of the FlashCard
	 * @return the front side category name
	 */
	public String getFirstType(String section) {
		return sectionSides.get(section).front;
	}
	
	/**
	 * Return the back side category name of the FlashCard
	 * @return the back side category name
	 */
	public String getSecondType(String section) {
		return sectionSides.get(section).back;
	}
	
	/**
	 * Returns all of the section names for the current FlashCard data file
	 * @return A Set of all section names
	 */
	public Set<String> getAllSections() {
		return vocabMap.keySet();
	}
	
	/**
	 * Chooses a random FlashCard from the given section
	 * @param section the section to choose a FlashCard from
	 * @return the FlashCard from the section
	 */
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
	
	/**
	 * Changes the current data file
	 * @param newFileName the new data file to read from
	 */
	public void changeFile(String newFileName) {
		newFileName = Constants.DATA_FILE_DIRECTORY + File.separator + newFileName;
		if (!newFileName.equals(currentDataFile)) {
			currentDataFile = newFileName;
			
			vocabMap.clear();
			sectionSides.clear();
			
			getData(newFileName);
		}
	}
	
	/**
	 * Private method to read and parse the data file
	 * @param fileName the name of the data file to read from
	 */
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
						boolean commentMarkGood = constants[0].startsWith(Constants.COMMENT_STRING);
						boolean delimiterMarkGood = constants[1].startsWith(Constants.DELIMITER_STRING);
						if (!commentMarkGood || !delimiterMarkGood) {
							throw new IllegalStateException("File header is not formatted correctly: " + wholeLine);
						}
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
	
	/**
	 * Returns the names of all data files in the data directory
	 * @return a String array of all data files
	 */
	public static String[] allDataFiles() {
		try {
			File flashCardDataDirectory = new File(Constants.DATA_FILE_DIRECTORY);
			if (flashCardDataDirectory.isDirectory()) {
				String[] names = flashCardDataDirectory.list();
				List<String> dataFiles = new LinkedList<String>();
				
				for (int i = 0; i < names.length; i++) {
					File f = new File(names[i]);
					if (!f.isDirectory()) {
						String localName = flashCardDataDirectory.getName() + File.separator + f.getName();
						if (localName.equalsIgnoreCase(Constants.DEFAULT_FILE)) {
							dataFiles.add(0, names[i]);
						} else {
							dataFiles.add(names[i]);
						}
					}
				}
				
				String[] files = new String[dataFiles.size()];
				
				for (int i = 0; i < files.length; i++) {
					files[i] = dataFiles.get(i);
				}
				return files;
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		return null;
	}
}
