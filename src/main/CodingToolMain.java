package main;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.KeyMonitor;
import gui.View;

public class CodingToolMain {

	public static void main(String[] args) {
		Globals.initialize();
		Globals.fileIO.loadData();
		final JFrame frame = new JFrame();
		JPanel mainPanel = new View();
		KeyMonitor keyMonitor = new KeyMonitor();

		frame.setTitle("Batch " + Globals.BATCH_NUMBER);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				if (Globals.REVIEWER_ID != 0) {
					while (Globals.fileIO.saveData() == false) {
						int result = JOptionPane.showConfirmDialog(frame, "Error saving data. Try again?");
						// JOptionPane.showInternalConfirmDialog(desktop, "Continue printing?");
						if (result == JOptionPane.CANCEL_OPTION) {
							System.out.println("Canceling.");
							return;
						} else if (result == JOptionPane.YES_OPTION) {
							System.out.println("Trying to save again.");
							continue;
						} else if (result == JOptionPane.NO_OPTION) {
							System.out.println("Not saving.");
							//frame.dispose();
							break;
						}
					}
				}
				System.out.println("Disposing frame.");
				frame.dispose();
				System.exit(0);
			}
		});		

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyMonitor);

		//dataManager.getAllVisits();
		frame.pack();
		frame.setSize(880,680);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
