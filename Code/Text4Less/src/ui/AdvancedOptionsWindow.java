package ui;

import imageHandling.ImageReadMethod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import neuralNetwork.INeuralNetwork;

public class AdvancedOptionsWindow extends JFrame implements INetworkSelectionHandler {
	private JButton saveChangesButton;
//	private Map<String, ImageReadMethod> readMethods;
	private MainWindow window;
	
	private JRadioButton nnButton, ldButton, treeButton;
	
	private ImageReadMethod selectedReadMethod;
	private INeuralNetwork chosenNetwork;
	private String networkName;
	
	private NetworkSelectionPanel networkPanel;
	private JRadioButton yesSpellCheckButton, noSpellCheckButton;
	private boolean useSpellCheck = false;
	
	public AdvancedOptionsWindow(MainWindow window, ImageReadMethod selectedMethod, INeuralNetwork chosenNetwork, String networkName,
			boolean useSpellCheck){		
		this(window, selectedMethod, useSpellCheck);
		
		this.useSpellCheck = useSpellCheck;
		
		if (chosenNetwork != null && networkName != null){
			this.chosenNetwork = chosenNetwork;
			this.networkName = networkName;
			networkPanel.setNetwork(chosenNetwork, networkName);
		}
	}
	
	public AdvancedOptionsWindow(MainWindow window, ImageReadMethod selectedMethod, boolean useSpellCheck){
		this.window = window;
		
		selectedReadMethod = selectedMethod;		
		
		setupClassificationOptions();
		setupSpellCheckOptions(useSpellCheck);
		setupSaveButton();
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		setupReadMethodsPanel();
		setupNetworkToUsePanel();
		
		this.getContentPane().add(saveChangesButton);
		
		this.pack();
		this.setVisible(true);
	}
	
	private void setupSpellCheckOptions(boolean isSpellCheckOn) {
		JPanel spellCheckPanel = new JPanel();
		spellCheckPanel.setLayout(new BoxLayout(spellCheckPanel, BoxLayout.X_AXIS));
		spellCheckPanel.add(new JLabel("Translation Spell Checking:"));
		
		yesSpellCheckButton = new JRadioButton("Yes");
		noSpellCheckButton = new JRadioButton("No");
		
		SpellCheckListener listener = new SpellCheckListener();
		yesSpellCheckButton.addActionListener(listener);
		noSpellCheckButton.addActionListener(listener);
		
		if (isSpellCheckOn){
			yesSpellCheckButton.setSelected(true);
		}
		else noSpellCheckButton.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(yesSpellCheckButton);
		group.add(noSpellCheckButton);
		
		spellCheckPanel.add(yesSpellCheckButton);
		spellCheckPanel.add(noSpellCheckButton);
		
		this.getContentPane().add(spellCheckPanel);
	}

	private void setupNetworkToUsePanel() {
		networkPanel = new NetworkSelectionPanel(this);
		networkPanel.setBorder(BorderFactory.createTitledBorder("Chosen Neural Network"));
		this.getContentPane().add(networkPanel);
	}

	private void setupReadMethodsPanel() {
		JPanel readMethodsPanel = new JPanel();
		readMethodsPanel.setLayout(new BoxLayout(readMethodsPanel, BoxLayout.Y_AXIS));
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(nnButton);
		group.add(ldButton);
		group.add(treeButton);
		
		readMethodsPanel.add(nnButton);
		readMethodsPanel.add(ldButton);
		readMethodsPanel.add(treeButton);
		
		readMethodsPanel.setBorder(BorderFactory.createTitledBorder("Classification Methods"));
		this.getContentPane().add(readMethodsPanel);
	}

	private void setupSaveButton(){
		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.addActionListener(new ButtonListener());
	}
	
	private void setupClassificationOptions() {
		nnButton = new JRadioButton("Neural Network");
		ldButton = new JRadioButton("Least Distance");
		treeButton = new JRadioButton("Decision Tree");
		
		ButtonListener listener = new ButtonListener();
		nnButton.addActionListener(listener);
		ldButton.addActionListener(listener);
		treeButton.addActionListener(listener);
		
		if (selectedReadMethod == ImageReadMethod.NEURAL_NETWORK){
			nnButton.setSelected(true);
		}
		else if (selectedReadMethod == ImageReadMethod.LEAST_DISTANCE){
			ldButton.setSelected(true);
		}
		else if (selectedReadMethod == ImageReadMethod.DECISION_TREE){
			treeButton.setSelected(true);
		}
	}
	
	private class SpellCheckListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == yesSpellCheckButton){
				useSpellCheck = true;
			}
			else if (evt.getSource() == noSpellCheckButton){
				useSpellCheck = false;
			}
		}
	}
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveChangesButton){
				window.advancedOptionsSaveChangesClicked(selectedReadMethod, chosenNetwork, networkName, useSpellCheck);
				AdvancedOptionsWindow.this.dispose();
			}
			else if (evt.getSource() == nnButton){
				selectedReadMethod = ImageReadMethod.NEURAL_NETWORK;
			}
			else if (evt.getSource() == ldButton){
				selectedReadMethod = ImageReadMethod.LEAST_DISTANCE;			
			}
			else if (evt.getSource() == treeButton){
				selectedReadMethod = ImageReadMethod.DECISION_TREE;
			}
		}
		
	}

	@Override
	public void networkSelected(String networkName, INeuralNetwork network) {
		chosenNetwork = network;
		this.networkName = networkName;
	}
}
