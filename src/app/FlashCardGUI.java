package app;
//import java.util.List;
//import java.util.LinkedList;
import java.util.Set;
//import java.util.Random;
//import java.io.*;
//import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import util.FlashCard;
import util.Constants;

/**
 * Simple GUI flash card program
 * @author Milan Justel (milanj91)
 * 
 * Features to do:
 * 	- support writing into the file to add new words
 * 	- feature to check if file is of a valid format
 *
 */

public class FlashCardGUI {
	private static final String ALL = "All";
	private static final String BLANK = "";
	
	
	private FlashCardModel fcm;
	
	//private static PrintStream output;
	
	private JFrame frame;
	private JLabel titleLabel;
	private JLabel categoryLabel;
	private JLabel vocabLabel;
	private JComboBox sectionBox;
	private JComboBox fileBox;
	private JButton next;
	private JButton swap;
	
	private boolean swapped;
	private FlashCard currentCard;
	private boolean showBackSide;
	private String currentSection;
	
	public static void main(String[] args) {
		try {
            // Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception ex) {}
		
		FlashCardModel fcm = new FlashCardModel();
		new FlashCardGUI(fcm);		
	}
	
	public FlashCardGUI(FlashCardModel model) {
		this.fcm = model;
		initializeVariables();
		
		frame = new JFrame("Flash Cards");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(300,400));
		frame.setLayout(new BorderLayout());	
		
		// Create and initialize Labels
		titleLabel = new JLabel(fcm.getTitle(), JLabel.CENTER);
		titleLabel.setFont(Constants.TITLE_FONT);
		categoryLabel = new JLabel(BLANK, JLabel.CENTER);
		vocabLabel = new JLabel(BLANK, JLabel.CENTER);
		vocabLabel.setFont(Constants.VOCAB_FONT);
		vocabLabel.setPreferredSize(new Dimension(1,1));
		
		// Initialize objects
		next = new JButton(Constants.NEXT_BUTTON_TEXT);
		swap = new JButton(Constants.SWAP_BUTTON_TEXT);
		sectionBox = new JComboBox(setToArray(fcm.getAllSections()));
		fileBox = new JComboBox(FlashCardModel.allDataFiles());
		
		// Add listeners
		next.addActionListener(new NextButtonListener());
		next.setPreferredSize(new Dimension(70, 25));
		swap.addActionListener(new SwapButtonListener());
		swap.setPreferredSize(new Dimension(70, 25)); 
		sectionBox.addActionListener(new SectionBoxListener());
		fileBox.addActionListener(new FileBoxListener());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(next);
		buttonPanel.add(swap);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(0, 1));
		
		JPanel sectionPanel = new JPanel();
		sectionPanel.setLayout(new FlowLayout());
		
		JLabel sectionTitle = new JLabel("Section:", JLabel.CENTER);
		sectionPanel.add(sectionTitle);
		sectionPanel.add(sectionBox);
		
		JPanel fileBoxPanel = new JPanel();
		fileBoxPanel.setLayout(new FlowLayout());
		
		JLabel fileBoxTitle = new JLabel("File:", JLabel.CENTER);
		fileBoxPanel.add(fileBoxTitle);
		fileBoxPanel.add(fileBox);
		
		topPanel.add(titleLabel);
		topPanel.add(fileBoxPanel);
		topPanel.add(sectionPanel);
		topPanel.add(categoryLabel);
		
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(vocabLabel);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		
		//frame.pack();
		frame.setVisible(true);
	}
	
	private String[] setToArray(Set<String> sections) {
		String[] sectionArray = new String[sections.size() + 1];
		
		int i = 1;
		sectionArray[0] = ALL;
		for (String section : sections) {
			sectionArray[i++] = section;
		}
		
		return sectionArray;
	}
	
	private void initializeVariables() {
		this.swapped = false;
		this.currentSection = ALL;
		this.currentCard = fcm.getCardFromSection(ALL);
		this.showBackSide = false;
	}
	
	public class NextButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String header;
			if (!showBackSide) {
				header = "Q: ";
				currentCard = fcm.getCardFromSection(currentSection);
				showBackSide = true;
				String startType = swapped ? fcm.getSecondType(currentCard.section) : fcm.getFirstType(currentCard.section);
				categoryLabel.setText(startType);
				next.setText(Constants.FLIP_BUTTON_TEXT);
			} else {
				header = "A: ";
				showBackSide = false;
				next.setText(Constants.NEXT_BUTTON_TEXT);
			}
			String result = (swapped != showBackSide) ? currentCard.front : currentCard.back;
			vocabLabel.setText("<html>" + header + result + "</html>");
		}
	}
	
	public class SwapButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			swapped = !swapped;
			showBackSide = false;
			String startType = BLANK;
			if (!currentSection.equalsIgnoreCase(ALL)) {
				startType = swapped ? fcm.getSecondType(currentSection) : fcm.getFirstType(currentSection);
			}
			categoryLabel.setText(startType);
			vocabLabel.setText(BLANK);
			next.setText(Constants.NEXT_BUTTON_TEXT);
		}
	}
	
	public class SectionBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			vocabLabel.setText(BLANK);
			currentSection = (String) sectionBox.getSelectedItem();
			currentCard = fcm.getCardFromSection(currentSection);
			showBackSide = false;
			next.setText(Constants.NEXT_BUTTON_TEXT);
			
			if (currentSection.equalsIgnoreCase(ALL)) {
				categoryLabel.setText(BLANK);
			} else {
				// Label should be known
				String startType = swapped ? fcm.getSecondType(currentSection) : fcm.getFirstType(currentSection);
				categoryLabel.setText(startType);
			}
		}
	}
	
	public class FileBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String newFileName = (String) fileBox.getSelectedItem();
			fcm.changeFile(newFileName);
			
			// Set labels and combo boxes
			titleLabel.setText(fcm.getTitle());
			categoryLabel.setText(BLANK);
			vocabLabel.setText(BLANK);
			
			//sectionBox = new JComboBox(setToArray(fcm.getAllSections()));
			sectionBox.setModel(new JComboBox(setToArray(fcm.getAllSections())).getModel());
		}
	}
}