package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlsPanel extends JPanel {
	private JSlider penSizeSlider, areaSizeSlider;
	private JButton saveButton, clearButton, eraseButton, drawButton;
	private DrawingPanel parent;
	
	private final int MAX_AREA_SIZE = 100;
	private final int MIN_AREA_SIZE = 10;
	private final int STARTING_AREA_VALUE = 30;
	
	private final int MAX_PEN_WIDTH = 9;
	private final int MIN_PEN_WIDTH = 1;
	private final int STARTING_PEN_VALUE = 3;
	
	private final Color optionSelectedColor = new Color(102, 0, 40);
	
	public ControlsPanel(DrawingPanel parent){
		this.parent = parent;
		
		setupPanel();
	}

	private void setupPanel() {
		JLabel penLabel = new JLabel("Pen Width");
		JLabel areaLabel = new JLabel("Image Size");
		
		penLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		areaLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		
		setupPenSlider();
		setupAreaSlider();
		parent.penSizeChanged(STARTING_PEN_VALUE);
		parent.areaSizeChanged(STARTING_AREA_VALUE);
		
		saveButton = new JButton("Save Image");
		clearButton = new JButton("Clear");
		eraseButton = new JButton("Erase");
		drawButton = new JButton("Draw");
		
		drawButton.setBorder(BorderFactory.createLineBorder(optionSelectedColor, 3));
		drawButton.setEnabled(false);
		
		saveButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		clearButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		eraseButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		drawButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		
		saveButton.addActionListener(new ButtonListener());
		clearButton.addActionListener(new ButtonListener());
		eraseButton.addActionListener(new ButtonListener());
		drawButton.addActionListener(new ButtonListener());
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(penLabel);
		this.add(penSizeSlider);
		this.add(areaLabel);
		this.add(areaSizeSlider);
		this.add(saveButton);
		
		JPanel drawingFunctionsPanel = new JPanel();
		drawingFunctionsPanel.setLayout(new GridLayout(0,3));
		drawingFunctionsPanel.add(clearButton);
		drawingFunctionsPanel.add(drawButton);
		drawingFunctionsPanel.add(eraseButton);
		
		drawingFunctionsPanel.setMaximumSize(new Dimension(800, 40));
		
		this.add(drawingFunctionsPanel);
	}

	private void setupAreaSlider() {
		areaSizeSlider = new JSlider(MIN_AREA_SIZE, MAX_AREA_SIZE, STARTING_AREA_VALUE);
		areaSizeSlider.setMajorTickSpacing(10);
		areaSizeSlider.setMinorTickSpacing(1);
		areaSizeSlider.setPaintTicks(true);
		areaSizeSlider.setPaintLabels(true);
		areaSizeSlider.setSnapToTicks(true);
		
		areaSizeSlider.addChangeListener(new SliderListener());
	}

	private void setupPenSlider() {
		penSizeSlider = new JSlider(MIN_PEN_WIDTH, MAX_PEN_WIDTH, STARTING_PEN_VALUE);
		penSizeSlider.setMajorTickSpacing(2);
		penSizeSlider.setPaintTicks(true);
		penSizeSlider.setPaintLabels(true);
		penSizeSlider.setSnapToTicks(true);
		
		Hashtable<Integer, JLabel> penLabels = new Hashtable<Integer, JLabel>();

		for (int i = MIN_PEN_WIDTH; i <= MAX_PEN_WIDTH; i+= 2){
			penLabels.put(i, new JLabel(new Integer(i).toString()));
		}
		
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
			else if (evt.getSource() == eraseButton){
				eraseButton.setBorder(BorderFactory.createLineBorder(optionSelectedColor, 3));
				eraseButton.setEnabled(false);
				drawButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				drawButton.setEnabled(true);
				parent.erasingStateChanged(true);
			}
			else if (evt.getSource() == drawButton){
				drawButton.setBorder(BorderFactory.createLineBorder(optionSelectedColor, 3));
				drawButton.setEnabled(false);
				eraseButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				eraseButton.setEnabled(true);
				parent.erasingStateChanged(false);
			}
		}
	}
}
