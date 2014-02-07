package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import app.ImageReadMethod;

public class AdvancedOptionsWindow extends JFrame {
	private JButton saveChangesButton;
	private Map<String, ImageReadMethod> readMethods;
	private MainWindow window;
	
	private JCheckBox nnCheckBox, ldCheckBox;
	
	List<ImageReadMethod> selectedReadMethods;
	
	public AdvancedOptionsWindow(MainWindow window, List<ImageReadMethod> selectedMethods){
		this.window = window;
		
		selectedReadMethods = new ArrayList<ImageReadMethod>();
		if(selectedMethods != null){
			for (ImageReadMethod nextMethod : selectedMethods){
				selectedReadMethods.add(nextMethod);
			}
		}
		
		
		initializeReadMethods();
		setupClassificationOptions();
		setupSaveButton();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(nnCheckBox);
		this.getContentPane().add(ldCheckBox);
		this.getContentPane().add(saveChangesButton);
		
		this.pack();
		this.setVisible(true);
	}
	
	private void setupSaveButton(){
		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.addActionListener(new ButtonListener());
	}
	
	private void setupClassificationOptions() {
		nnCheckBox = new JCheckBox("Neural Network");
		ldCheckBox = new JCheckBox("Least Distance");
		
		if(selectedReadMethods.contains(ImageReadMethod.NEURAL_NETWORK)){
			nnCheckBox.setSelected(true);
		}
		if(selectedReadMethods.contains(ImageReadMethod.LEAST_DISTANCE)){
			ldCheckBox.setSelected(true);
		}
		
		nnCheckBox.addActionListener(new ButtonListener());
		ldCheckBox.addActionListener(new ButtonListener());
	}

	private void initializeReadMethods(){
		readMethods = new HashMap<String, ImageReadMethod>();
		readMethods.put("Neural Network", ImageReadMethod.NEURAL_NETWORK);
		readMethods.put("Least Distance", ImageReadMethod.LEAST_DISTANCE);
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveChangesButton){
				if (selectedReadMethods.size() > 0){
					window.advancedOptionsSaveChangesClicked(selectedReadMethods);
					AdvancedOptionsWindow.this.dispose();
				}
				else JOptionPane.showMessageDialog(AdvancedOptionsWindow.this, "Must select at least one method", "Selection Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (evt.getSource().getClass() == JCheckBox.class){
				JCheckBox clickedBox = (JCheckBox)evt.getSource();
				
				if (clickedBox.isSelected()){
					selectedReadMethods.add(readMethods.get(clickedBox.getText()));
				}
				else selectedReadMethods.remove(readMethods.get(clickedBox.getText()));
			}
		}
		
	}
}
