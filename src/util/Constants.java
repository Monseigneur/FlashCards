package util;

import java.awt.Font;
import java.io.File;
import java.util.Random;

/**
 * Constants in a class that keeps track of useful constants for the
 * FlashCard model and gui classes.
 * 
 * @author Milan Justel (milanj91)
 *
 */
public class Constants {
	public static final String FILE_ENCODING = "UTF-8";
	
	public static final String DATA_FILE_DIRECTORY = "data";
	public static final String DEFAULT_FILE = DATA_FILE_DIRECTORY + File.separator + "default.txt";
	
	public static final String COMMENT_STRING = "comment";
	public static final String DELIMITER_STRING = "delimiter";
	
	public static final Random rand = new Random();
	
	public static final Font TITLE_FONT = new Font("Dialog", 1, 16);
	public static final Font VOCAB_FONT = new Font("Dialog", 1, 14);
	
	public static final String NEXT_BUTTON_TEXT = "Next";
	public static final String FLIP_BUTTON_TEXT = "Flip";
	public static final String SWAP_BUTTON_TEXT = "Swap";
}
