package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlsPanel extends JPanel {
	private JSlider penSizeSlider, areaSizeSlider;
	private JButton saveButton, clearButton;
	private DrawingPanel parent;
	
	private final int MAX_AREA_SIZE = 100;
	private final int MIN_AREA_SIZE = 10;
	
	private final int MAX_PEN_WIDTH = 9;
	private final int MIN_PEN_WIDTH = 1;
	
	public ControlsPanel(DrawingPanel parent){
		this.parent = parent;
		
		setupPanel();
	}

	private void setupPanel() {
		JLabel penLabel = new JLabel("Pen Width");
		JLabel areaLabel = new JLabel("Image Size");
		
		setupPenSlider();
		setupAreaSlider();
		
		saveButton = new JButton("Save Image");
		clearButton = new JButton("Clear");
		
		saveButton.addActionListener(new ButtonListener());
		clearButton.addActionListener(new ButtonListener());
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(penLabel);
		this.add(penSizeSlider);
		this.add(areaLabel);
		this.add(areaSizeSlider);
		this.add(saveButton);
		this.add(clearButton);
	}

	private void setupAreaSlider() {
		areaSizeSlider = new JSlider(JSlider.HORIZONTAL, MAX_AREA_SIZE, MIN_AREA_SIZE);
		areaSizeSlider.setMajorTickSpacing(10);
		areaSizeSlider.setMinorTickSpacing(1);
		areaSizeSlider.setPaintTicks(true);
		areaSizeSlider.setPaintLabels(true);
		areaSizeSlider.setSnapToTicks(true);
		
		areaSizeSlider.addChangeListener(new SliderListener());
	}

	private void setupPenSlider() {
		penSizeSlider = new JSlider(JSlider.HORIZONTAL, MAX_PEN_WIDTH, MIN_PEN_WIDTH);
		penSizeSlider.setMinorTickSpacing(2);
		penSizeSlider.setPaintTicks(true);
		penSizeSlider.setPaintLabels(true);
		penSizeSlider.setSnapToTicks(true);
		
		Hashtable<Integer, JLabel> penLabels = new Hashtable<Integer, JLabel>();
		penLabels.put(MIN_PEN_WIDTH, new JLabel(new Integer(MIN_PEN_WIDTH).toString()));
		penLabels.put(MAX_PEN_WIDTH, new JLabel(new Integer(MAX_PEN_WIDTH).toString()));
		penSizeSlider.setLabelTable(penLabels);
		penSizeSlider.setPaintLabels(true);
		
		penSizeSlider.addChangeListener(new SliderListener());
	}
	
	private class SliderListener implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent evt) {
			
			if (evt.getSource().getClass() == JSlider.class && !((JSlider)evt.getSource()).getValueIsAdjusting()){
				JSlider source = (JSlider)evt.getSource();
				int value = source.getValue();
				
				if (source == penSizeSlider){
					parent.penSizeChanged(value);
				}
				else if (source == areaSizeSlider){
					parent.areaSizeChanged(value);
				}
			}
		}
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveButton){
				parent.saveClicked();
			}
			else if (evt.getSource() == clearButton){
				parent.clearClicked();
			}
		}
	}
}