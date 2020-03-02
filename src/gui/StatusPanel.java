package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import data.Record;
import data.Tuple;
import main.Globals;

public class StatusPanel extends JPanel {
	
	JLabel participantIdLabel;
	JLabel trialIdLabel;
	
	private HashMap<Integer, JTabbedPane> databasePanels = null;
	private JFrame databaseFrame = null;
	private double scale = 0.5;
	private int currentDatabaseId = -1;
	private String currentCustomerLocation;
	
	
	public StatusPanel() {
		super();
		this.add(new JLabel("Reviewer ID: " + Globals.REVIEWER_ID));
		
		trialIdLabel = new JLabel("Trial ID: " + Integer.toString(Globals.dataManager.getCurrentRecord().trialId));
		
		trialIdLabel.setFont(new Font(trialIdLabel.getName(), Font.BOLD, 24));
		
		this.add(trialIdLabel);
		
		loadDatabaseImages();
	}
	
	
	public void setCurrentDatabase() {
		
		if (databaseFrame == null) {
			databaseFrame = new JFrame();
		}
		
		int newDatabaseId = Globals.dataManager.getCurrentRecord().dbId;
		String newCustomerLocation = Globals.dataManager.getCurrentRecord().customerLocation;
		
		if (currentDatabaseId != newDatabaseId) {
			databaseFrame.setContentPane(databasePanels.get(newDatabaseId));
			databaseFrame.setVisible(true);
			databaseFrame.pack();
			//databaseFrame.setMinimumSize(databaseFrame.getPreferredSize());
			databaseFrame.setMinimumSize(new Dimension(100, 100));
			
			currentDatabaseId = newDatabaseId;
			currentCustomerLocation = "";
		}
		
		if (newCustomerLocation.startsWith("CAMERA") && !newCustomerLocation.contentEquals(currentCustomerLocation)) {
			
			if (newCustomerLocation.contentEquals("CAMERA_1")) {
				databasePanels.get(currentDatabaseId).setSelectedIndex(0);
			}
			else if (newCustomerLocation.contentEquals("CAMERA_2")) {
				databasePanels.get(currentDatabaseId).setSelectedIndex(1);
			}
			else if (newCustomerLocation.contentEquals("CAMERA_3")) {
				databasePanels.get(currentDatabaseId).setSelectedIndex(2);
			}
			
			currentCustomerLocation = newCustomerLocation;
		}
	}
	
	
	public void refresh() {
		trialIdLabel.setText("Trial ID: " + Integer.toString(Globals.dataManager.getCurrentRecord().trialId));
		
		setCurrentDatabase();
		
		if (Globals.trialsToEvaluate.contains(Globals.dataManager.getCurrentRecord().trialId)) {
			trialIdLabel.setForeground(Color.red);
		}
		else {
			trialIdLabel.setForeground(Color.black);
		}
	}
	
	private void loadDatabaseImages() {
		String dbImageDir = "data/databaseimages/";
		File f = new File(dbImageDir);
        String[] dbImagePathnames = f.list();
        
        HashMap<String, HashMap<String, BufferedImage>> dbImages = new HashMap<String, HashMap<String, BufferedImage>>();
        
        // load the images
        for (String dbImgPn: dbImagePathnames) {
        	
        	// parse the pathname
        	// DATABASE_00-CAMERA_1.png
        	String dbId = dbImgPn.substring(9, 11);
        	String camId = dbImgPn.substring(19, 20);
        	
        	BufferedImage bImg = null;
			try {
				bImg = ImageIO.read(new File(dbImageDir+dbImgPn));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// scale
			int scaledWidth = (int) (bImg.getWidth() * scale);
	        int scaledHeight = (int) (bImg.getHeight() * scale);
			
	        BufferedImage bImgScaled = new BufferedImage(scaledWidth, scaledHeight, bImg.getType());
	        Graphics2D g2d = bImgScaled.createGraphics();
	        g2d.drawImage(bImg, 0, 0, scaledWidth, scaledHeight, null);
	        g2d.dispose();
			
			if (!dbImages.containsKey(dbId)) {
				dbImages.put(dbId, new HashMap<String, BufferedImage>());
			}
			
			dbImages.get(dbId).put(camId, bImgScaled);
        }
        
        // create the panels for display
        databasePanels = new HashMap<Integer, JTabbedPane>();
		
        for (String dbId: dbImages.keySet()) {
        	JTabbedPane tabbedPane = new JTabbedPane();
        	
        	JPanel panel1 = new JPanel();
        	JLabel label1 = new JLabel();
        	panel1.add(label1);
			label1.setIcon(new ImageIcon(dbImages.get(dbId).get("1")));
			
			JPanel panel2 = new JPanel();
			JLabel label2 = new JLabel();
			panel2.add(label2);
			label2.setIcon(new ImageIcon(dbImages.get(dbId).get("2")));
			
			JPanel panel3 = new JPanel();
			JLabel label3 = new JLabel();
			panel3.add(label3);
			label3.setIcon(new ImageIcon(dbImages.get(dbId).get("3")));
			
			tabbedPane.addTab("CAMERA_1", panel1);
			tabbedPane.addTab("CAMERA_2", panel2);
			tabbedPane.addTab("CAMERA_3", panel3);
			
			databasePanels.put(Integer.parseInt(dbId), tabbedPane);
			
        }
        
	}
			
}
