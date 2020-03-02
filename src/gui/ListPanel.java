package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Record;
import data.Record.Rating;
import main.Globals;

public class ListPanel extends JPanel {

	JScrollPane idListPane;
	JList<Integer> idList;

	public ListPanel() {
		super();
		setLayout(new GridLayout(1,1));
		this.add(getIDListPane());
		this.setPreferredSize(new Dimension(50,100));
		if (Globals.REVIEWER_ID == 0) {
			setDifferenceCellRenderer();
		} else {
			setUncodedCellRenderer();
		}
		refresh();
	}

	private JScrollPane getIDListPane() {
		if (idListPane == null) {
			idListPane = new JScrollPane(getIdList());
			idListPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return idListPane;
	}
	private JList<Integer> getIdList() {
		if (idList == null) {
			idList = new JList<Integer>(); //data has type Object[]
			idList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			idList.setMinimumSize(new Dimension(50,100));
			idList.setFocusable(false);
			idList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting()) {
						return;
					}
					Globals.dataManager.setSelectedRecord(idList.getSelectedValue());
					//idList.ensureIndexIsVisible(idList.getSelectedIndex());
				}
			});
		}
		return idList;
	}

	private void setDifferenceCellRenderer() {
		idList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				Record record = Globals.dataManager.getRecord((Integer)value);
				boolean consistent = true;
				for (int i = 0; i < 5; ++i) {
					Rating rating1 = record.rater1[i];
					Rating rating2 = record.rater2[i];
					if (rating1 != rating2)
						consistent = false;
				}

				if (consistent == false) {
					label.setBackground(Color.YELLOW);
				} else {
					label.setBackground(null);
				}
//				if (record.isProactive == true) {
//					label.setForeground(Color.GRAY);
//					label.setHorizontalAlignment(SwingConstants.CENTER);
//				} else {
//					label.setForeground(Color.BLACK);
//					label.setHorizontalAlignment(SwingConstants.LEFT);
//				}

				if (isSelected) {
					label.setBorder(BorderFactory.createLineBorder(Color.black));
				}
				return label;
			}
		});
	}

	private void setUncodedCellRenderer() {
		idList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				Record record = Globals.dataManager.getRecord((Integer)value);
				boolean coded = true;
				for (int i = 0; i < 2; ++i) {
					if (Globals.REVIEWER_ID == 1) {
						if (record.rater1[i] == Rating.INCOMPLETE) {
							coded = false;
							break;
						}
					} else if (Globals.REVIEWER_ID == 2) {
						if (record.rater2[i] == Rating.INCOMPLETE) {
							coded = false;
							break;
						}
					}
				}

				if (coded == false) {
					label.setForeground(Color.BLACK);
				} else {
					label.setForeground(Color.gray);
				}

				if (isSelected) {
					label.setBorder(BorderFactory.createLineBorder(Color.black));
				}
				return label;
			}
		});
	}

	public void moveAhead() {
		int idx = idList.getSelectedIndex();
		idList.setSelectedIndex(idx+1);
	}

	public void moveBack() {
		int idx = idList.getSelectedIndex();
		if (idx > 0)
			idList.setSelectedIndex(idx-1);
	}

	private void refresh() {
		getIdList().setListData(Globals.dataManager.getRecordIDs());
		getIdList().setSelectedIndex(0);
		//getIdList().setSelectedValue(Globals.dataManager.getCurrentRecordID(), true);
	}

}
