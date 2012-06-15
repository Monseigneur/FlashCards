package app;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import util.FlashCard;

/**
 * Simple GUI flash card program
 * @author Milan Justel (milanj91)
 * 
 * Features to do:
 * 	- support writing into the file to add new words
 * 	- choosing different files to open with
 *
 */

public class FlashCardGUI {
	private static final String fileName = "data/test.txt";
	public static final Random rand = new Random();
	
	private FlashCardModel fcm;
	
	//private static PrintStream output;
	
	private JFrame frame;
	private JLabel titleLabel;
	private JLabel categoryLabel;
	private JLabel vocabLabel;
	//private JComboBox sectionBox;
	private JButton next;
	private JButton swap;
	
	private boolean swapped;
	private FlashCard currentCard;
	private List<FlashCard> allPairs;
	private boolean showAnswer;
	//private String currentSection;
	
	private List<String> sections;
	
	public static void main(String[] args) {
		FlashCardModel fcm = new FlashCardModel(fileName);
		new FlashCardGUI(fcm);		
	}
	
	public FlashCardGUI(FlashCardModel model) {
		this.fcm = model;
		this.swapped = false;
		currentCard = null;
		allPairs = new LinkedList<FlashCard>();
		this.showAnswer = false;
		sections = fcm.getAllSections();
		//currentSection = "all";
		
		frame = new JFrame("Flash Cards");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(300,300));
		frame.setLayout(new BorderLayout());	
		
		titleLabel = new JLabel(fcm.getFirstType() + " - " + fcm.getSecondType(), JLabel.CENTER);
		Font font = titleLabel.getFont();
		titleLabel.setFont(new Font(font.getFontName(), font.getStyle(), 16));
		
		
		String startType = swapped ? fcm.getSecondType() : fcm.getFirstType();
		categoryLabel = new JLabel(startType, JLabel.CENTER);
		vocabLabel = new JLabel(" ", JLabel.CENTER);
		vocabLabel.setFont(new Font(font.getFontName(), font.getStyle(), 18));
		vocabLabel.setPreferredSize(new Dimension(1,1));
		
		next = new JButton("Next");
		swap = new JButton("Swap");
		
		/*sectionBox = new JComboBox(convertList(sections));
		sectionBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentSection = sections.get(sectionBox.getSelectedIndex());
				
			}
		});*/
		
		allPairs = fcm.getPairsForSections(sections);
		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String header;
				if (!showAnswer) {
					header = "Q: ";
					int index = rand.nextInt(allPairs.size());
					currentCard = allPairs.get(index);
					showAnswer = true;
				} else {
					header = "A: ";
					showAnswer = false;
				}
				// S show	T	F
				// T		F	T
				// F		T	F
				// xor
				String result = (swapped != showAnswer) ? currentCard.front : currentCard.back;
				vocabLabel.setText("<html>" + header + result + "</html>");
			}
		});
		
		swap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapped = !swapped;
				showAnswer = false;
				String startType = swapped ? fcm.getSecondType() : fcm.getFirstType();

				categoryLabel.setText(startType);
				vocabLabel.setText(" ");
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(next);
		buttonPanel.add(swap);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BorderLayout());
		
		labelPanel.add(categoryLabel, BorderLayout.NORTH);
		//labelPanel.add(sectionBox);
		labelPanel.add(vocabLabel);
		
		frame.add(titleLabel, BorderLayout.NORTH);
		frame.add(labelPanel);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		//frame.pack();
		frame.setVisible(true);
	}
	/*
	private String[] convertList(List<String> sections) {
		String[] sectionArray = new String[sections.size()];
		
		int i = 0;
		for (String section : sections) {
			sectionArray[i++] = section;
		}
		
		return sectionArray;
	}*/
}


/*
 * file choosing stuff, this finds and makes a list of the files in the directory
 * how do we have a combo box to choose a file before starting up the main program??
 * 
try {
File vocabDirectory = new File("vocab");
if (vocabDirectory.isDirectory()) {
	String[] names = vocabDirectory.list();
	for (String s : names) {
		System.out.println(s);	
		File text = new File(s);
		System.out.println(text.getCanonicalPath());
	}
	//File text = new File(names[0]);
	
}
} catch (Exception e) {}
*/
