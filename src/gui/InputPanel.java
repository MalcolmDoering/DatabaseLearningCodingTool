package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import data.Record.Rating;
import main.Globals;

public class InputPanel extends JPanel {

	private ArrayList<ResponsePanel> panels = new ArrayList<ResponsePanel>();

	public InputPanel() {
		super();
		UIManager.put("ToggleButton.select", Color.YELLOW);
		this.setLayout(new GridLayout(0,1));
		
		for (int i=0; i < Globals.NUM_PREDICTION_CONDITIONS; ++i) {
			ResponsePanel panel = new ResponsePanel(i);
			panels.add(panel);
		}
		
	}
	
	
	public void randomizeLayout(int seed) {
		this.removeAll();
		Collections.sort(panels, 
				new Comparator<ResponsePanel>() {
					public int compare(ResponsePanel a, ResponsePanel b) {
						return Integer.compare(b.index, a.index);
					}
				}
		);
		
		// randomize
		Collections.shuffle(panels, new Random(seed));
		
		layoutPanels();
	}
	
	
	private void layoutPanels() {
		for (int i=0; i < Globals.NUM_PREDICTION_CONDITIONS; ++i) {
			this.add(panels.get(i));
			
			//System.out.println(panels.get(i).index);
		}
		
		//System.out.println("");
		
		this.revalidate();
		this.repaint();
	}
	
	
	public void refresh() {
		int seed = Globals.dataManager.getCurrentRecord().turnId;
		
		randomizeLayout(seed);
		
		for (ResponsePanel panel:panels) {
			panel.update();
		}
	}

	private class ResponsePanel extends JPanel {
		final int index;
		JToggleButton yesButton;
		JToggleButton noButton;
		JToggleButton unsureButton;
		JToggleButton incompleteButton;
		JTextArea contentPanel;

		ResponsePanel(final int index) {
			super();
			this.index = index;
			setLayout(new BorderLayout());
			JPanel ratingPanel = new JPanel();
			ratingPanel.setLayout(new GridLayout(1,Globals.NUM_PREDICTION_CONDITIONS));

			yesButton = new JToggleButton("GOOD");
			yesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Globals.REVIEWER_ID == 1) {
						Globals.dataManager.getCurrentRecord().rater1[index] = Rating.GOOD;
					} else if (Globals.REVIEWER_ID == 2) {
						Globals.dataManager.getCurrentRecord().rater2[index] = Rating.GOOD;
					}
				}
			});

			noButton = new JToggleButton("BAD");
			noButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Globals.REVIEWER_ID == 1) {
						Globals.dataManager.getCurrentRecord().rater1[index] = Rating.BAD;
					} else if (Globals.REVIEWER_ID == 2) {
						Globals.dataManager.getCurrentRecord().rater2[index] = Rating.BAD;
					}
				}
			});


			unsureButton = new JToggleButton("?");
			unsureButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Globals.REVIEWER_ID == 1) {
						Globals.dataManager.getCurrentRecord().rater1[index] = Rating.UNSURE;
					} else if (Globals.REVIEWER_ID == 2) {
						Globals.dataManager.getCurrentRecord().rater2[index] = Rating.UNSURE;
					}
				}
			});
			
			
			incompleteButton = new JToggleButton("Incomplete");
			incompleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (Globals.REVIEWER_ID == 1) {
						Globals.dataManager.getCurrentRecord().rater1[index] = Rating.INCOMPLETE;
					} else if (Globals.REVIEWER_ID == 2) {
						Globals.dataManager.getCurrentRecord().rater2[index] = Rating.INCOMPLETE;
					}
				}
			});

			SwingUtilities.updateComponentTreeUI(yesButton);
			SwingUtilities.updateComponentTreeUI(noButton);
			SwingUtilities.updateComponentTreeUI(unsureButton);
			
			ButtonGroup group = new ButtonGroup();
			group.add(yesButton);
			group.add(noButton);
			group.add(unsureButton);
			group.add(incompleteButton);
			
			ratingPanel.add(yesButton);
			ratingPanel.add(noButton);
			ratingPanel.add(unsureButton);

			this.add(ratingPanel, BorderLayout.EAST);

			contentPanel = new JTextArea();
			contentPanel.setLineWrap(true);
			contentPanel.setBorder(makeBorder());
			contentPanel.setFont(contentPanel.getFont().deriveFont(18f));
			this.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
			this.setPreferredSize(new Dimension(300,90));
		}

		void update() {
			Rating rating = Rating.INCOMPLETE;
			Rating rating1 = Rating.INCOMPLETE;
			Rating rating2 = Rating.INCOMPLETE;

			if (Globals.REVIEWER_ID == 0) {
				rating1 = Globals.dataManager.getCurrentRecord().rater1[index];
				rating2 = Globals.dataManager.getCurrentRecord().rater2[index];
				unsureButton.setBackground(null);
				noButton.setBackground(null);
				yesButton.setBackground(null);
				if (rating1 == rating2) {
					switch(rating1) {
					case INCOMPLETE:
						
						break;
					case UNSURE:
						unsureButton.setBackground(Color.GREEN);
						break;
					case BAD:
						noButton.setBackground(Color.GREEN);
						break;
					case GOOD:
						yesButton.setBackground(Color.GREEN);
						break;					
					}
				} else {
					switch(rating1) {
					case INCOMPLETE:
						break;
					case UNSURE:
						unsureButton.setBackground(Color.YELLOW);
						break;
					case BAD:
						noButton.setBackground(Color.YELLOW);
						break;
					case GOOD:
						yesButton.setBackground(Color.YELLOW);
						break;
					}
					switch(rating2) {
					case INCOMPLETE:
						break;
					case UNSURE:
						unsureButton.setBackground(Color.BLUE);
						break;
					case BAD:
						noButton.setBackground(Color.BLUE);
						break;
					case GOOD:
						yesButton.setBackground(Color.BLUE);
						break;
					}
				}

			} else {

				if (Globals.REVIEWER_ID == 1) {
					rating = Globals.dataManager.getCurrentRecord().rater1[index];
				} else if (Globals.REVIEWER_ID == 2) {
					rating = Globals.dataManager.getCurrentRecord().rater2[index];
				}
				switch(rating) {
				case INCOMPLETE:
					incompleteButton.setSelected(true);
					break;
				case BAD:
					noButton.setSelected(true);
					break;
				case GOOD:
					yesButton.setSelected(true);
					break;
				case UNSURE:
					unsureButton.setSelected(true);
					break;
				}
			}

			String text = Globals.dataManager.getCurrentRecord().response[index];
//			if (Globals.dataManager.getCurrentRecord().isRepetition(index)) {
//				contentPanel.setForeground(Color.gray);
//			} else {
//				contentPanel.setForeground(Color.black);
//			}
			contentPanel.setText(text);
			//			DebugTools.print(Globals.dataManager.getCurrentRecord().response[index]);
		}
	}


	private Border makeBorder() {
		return BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),
				BorderFactory.createEmptyBorder(4, 4, 4, 4));
	}
}
