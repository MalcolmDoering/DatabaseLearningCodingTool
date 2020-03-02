package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import data.Record;
import main.Globals;

public class ActionPanel extends JPanel{
	
	private JTextArea action1Label = getTextArea();
	//private JTextArea action2Label = getTextArea();
	//private JTextArea action3Label = getTextArea();
	
	private JScrollPane conversationScrollPane;
	private int previousTrialId = 0; 
	
	
	public ActionPanel() {
		
		super();
		this.setLayout(new GridLayout(1,1));
		action1Label.setLineWrap(true);
		//action2Label.setLineWrap(true);
		//action3Label.setLineWrap(true);
//		action1Label.setBorder(makeBorder());
//		action2Label.setBorder(makeBorder());
//		action3Label.setBorder(makeBorder());
		
		conversationScrollPane = new JScrollPane(action1Label);
		
		conversationScrollPane.setViewportView(action1Label);
		
		this.add(conversationScrollPane);
		
		//this.add(new JScrollPane(action2Label));
		//this.add(new JScrollPane(action3Label));
		this.setPreferredSize(new Dimension(300,300));
	}
	
	private static JTextArea getTextArea() {
		
		JTextArea textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(50,6000));
		textArea.setFont(textArea.getFont().deriveFont(18f));
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		return textArea;
	}

	private Border makeBorder() {
//		return BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED),BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}

	public void refresh() {
		Record currentRecord = Globals.dataManager.getCurrentRecord();
		
		action1Label.setText(currentRecord.conversationHistory);
		
		if (currentRecord.trialId != previousTrialId) {
			// reset the scroll bar to the top of the conversation
			conversationScrollPane.getVerticalScrollBar().setValue(0);
		}
		
		//conversationScrollPane.getVerticalScrollBar().setValue(action1Label.getCaretPosition());
		
		//action1Label.setText(" Customer:\t" + currentRecord.action1.replace(";", "\n "));
		//action2Label.setText(" Travel agent:\t" + currentRecord.action2.replace(";", "\n "));
	}
}
