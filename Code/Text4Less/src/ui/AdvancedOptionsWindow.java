package ui;

import imageHandling.ImageReadMethod;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import neuralNetwork.INeuralNetwork;

public class AdvancedOptionsWindow extends JFrame implements INetworkSelectionHandler {
	private JButton saveChangesButton;
//	private Map<String, ImageReadMethod> readMethods;
	private ImageTranslationTab window;
	
	private JRadioButton nnButton, ldButton, treeButton;
	
	private ImageReadMethod selectedReadMethod;
	private INeuralNetwork chosenNetwork;
	private String networkName;
	
	private NetworkSelectionPanel networkPanel;
	private SpellCheckPanel spellCheckPanel;
	
	private final int NUMBER_ELEMENTS = 4;
	
	public AdvancedOptionsWindow(ImageTranslationTab window, ImageReadMethod selectedMethod, INeuralNetwork chosenNetwork, String networkName,
			boolean useSpellCheck){		
		this(window, selectedMethod, useSpellCheck);
		
		if (chosenNetwork != null && networkName != null){
			this.chosenNetwork = chosenNetwork;
			this.networkName = networkName;
			networkPanel.setNetwork(chosenNetwork, networkName);
		}
	}
	
	public AdvancedOptionsWindow(ImageTranslationTab window, ImageReadMethod selectedMethod, boolean useSpellCheck){
		this.window = window;
		
		selectedReadMethod = selectedMethod;		
		
		setupClassificationOptions();
		setupSpellCheckOptions(useSpellCheck);
		setupSaveButton();
		
		GridLayout layout = new GridLayout(NUMBER_ELEMENTS, 0);
		this.getContentPane().setLayout(layout);
		
		setupReadMethodsPanel();
		setupNetworkToUsePanel();
		
		this.getContentPane().add(saveChangesButton);
		
		this.setTitle("Advanced Options");
		this.pack();
		this.setVisible(true);
	}
	
	private void setupSpellCheckOptions(boolean isSpellCheckOn) {
		spellCheckPanel = new SpellCheckPanel();
		spellCheckPanel.setSpellCheckEnabled(isSpellCheckOn);
		this.getContentPane().add(spellCheckPanel);
	}

	private void setupNetworkToUsePanel() {
		networkPanel = new NetworkSelectionPanel(this);
		
		TitledBorder border = BorderFactory.createTitledBorder("Chosen Neural Network");
		border.setTitleFont(HomeWindow.DEFAULT_LABEL_FONT);
		
		networkPanel.setBorder(border);
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
		
		TitledBorder border = BorderFactory.createTitledBorder("Classification Methods");
		border.setTitleFont(HomeWindow.DEFAULT_LABEL_FONT);
		
		readMethodsPanel.setBorder(border);
		this.getContentPane().add(readMethodsPanel);
	}

	private void setupSaveButton(){
		saveChangesButton = new JButton("Save Changes");
		saveChangesButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
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
		
		nnButton.setFont(HomeWindow.SUB_LABEL_FONT);
		ldButton.setFont(HomeWindow.SUB_LABEL_FONT);
		treeButton.setFont(HomeWindow.SUB_LABEL_FONT);
		
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
	
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == saveChangesButton){
				window.advancedOptionsSaveChangesClicked(selectedReadMethod, chosenNetwork, networkName, spellCheckPanel.isSpellCheckEnabled());
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
