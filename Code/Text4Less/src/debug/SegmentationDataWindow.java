package debug;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SegmentationDataWindow extends JFrame{
	
	public SegmentationDataWindow(List<SegmentationDatum> segData){
		JScrollPane scroller = new JScrollPane();
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		for (SegmentationDatum nextDatum : segData){
			JPanel nextPanel = new JPanel();
			nextPanel.setLayout(new BoxLayout(nextPanel, BoxLayout.X_AXIS));
			nextPanel.add(new JLabel(new ImageIcon(nextDatum.getImage())));
			nextPanel.add(new JLabel("std: " + nextDatum.getStdDev()));
			nextPanel.add(new JLabel("num standard devs: " + nextDatum.getNumStdDevs()));
			nextPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			main.add(nextPanel);
		}

		scroller = new JScrollPane(main);
		
		this.getContentPane().add(scroller);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		repaint();
	}
}
