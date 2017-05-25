package ui;

import io.Logger;
import io.NeuralNetworkIO;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.INeuralNetwork;
import neuralNetwork.ITrainingProgressHandler;
import neuralNetwork.MatrixBackpropTrainer;
import neuralNetwork.MatrixNeuralNetwork;
import app.AlphaNumericCharacterConverter;
import app.NetworkFactory;

public class CustomNetworkTab extends JPanel implements ITrainingProgressHandler {
	private JTextField numberLayersBox, numberNeuronsBox, iterationsField, mseField;
	private JButton trainButton, saveButton;
	
	private INeuralNetwork customNetwork;
	private int numberNeurons, numberLayers, numberIterations;
	private float mse;
	
	private TrainingSetSelectionPanel setSelectionPanel;
	private LoadingScreen loading;
	
	private final int NUMBER_ELEMENTS = 11;
	private final int DEFAULT_WIDTH = 350;
	private final int DEFAULT_HEIGHT = 350;
	
	private Font defaultFont, fieldFont;
	
	private float errorAchieved;
	private int iterationsPerformed;
	private boolean success;
	private long trainingTime;
	
	public CustomNetworkTab(){
		defaultFont = new Font("Arial", Font.BOLD, 22);
		fieldFont = new Font("Arial", Font.PLAIN, 20);
		
		this.setLayout(new GridLayout(NUMBER_ELEMENTS, 0));
		setupTextFieldPanels();
		setupTrainingDataSelectionPanel();
		setupButtons();
		
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setBorder(BorderFactory.createEmptyBorder(50, 100, 100, 100));
		this.setVisible(true);
	}

	private void setupTrainingDataSelectionPanel() {
		setSelectionPanel = new TrainingSetSelectionPanel(false);
		setSelectionPanel.setSelectedFolder(new File("trainingImages/ASCII2"));
		this.add(setSelectionPanel);
	}

	private void setupButtons() {
		trainButton = new JButton("Train Network");
		saveButton = new JButton("Save Network");
		
		trainButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		saveButton.setFont(HomeWindow.DEFAULT_BUTTON_FONT);
		
		trainButton.addActionListener(new ButtonListener());
		saveButton.addActionListener(new ButtonListener());
		
		saveButton.setEnabled(false);
		
		this.add(trainButton);
		this.add(saveButton);
	}

	private void setupTextFieldPanels() {
		JPanel numberLayersPanel = new JPanel();
		JPanel numberNeuronsPanel = new JPanel();
		
		JLabel layersLabel = new JLabel("Number of hidden layers:");
		JLabel neuronsLabel = new JLabel("Number of neurons per hidden layer:");
		
		layersLabel.setFont(defaultFont);
		neuronsLabel.setFont(defaultFont);
		
		numberLayersPanel.setLayout(new BoxLayout(numberLayersPanel, BoxLayout.X_AXIS));
		numberNeuronsPanel.setLayout(new BoxLayout(numberNeuronsPanel, BoxLayout.X_AXIS));
		
		numberLayersBox = new JTextField();
		numberNeuronsBox = new JTextField();
		
		numberLayersBox.setFont(fieldFont);
		numberNeuronsBox.setFont(fieldFont);
		
		numberLayersPanel.add(layersLabel);
		numberLayersPanel.add(numberLayersBox);
		
		numberNeuronsPanel.add(neuronsLabel);
		numberNeuronsPanel.add(numberNeuronsBox);
		
		JPanel iterationsPanel = new JPanel();
		JPanel msePanel = new JPanel();
		
		iterationsPanel.setLayout(new BoxLayout(iterationsPanel, BoxLayout.X_AXIS));
		msePanel.setLayout(new BoxLayout(msePanel, BoxLayout.X_AXIS));
		
		iterationsField = new JTextField();
		mseField = new JTextField();
		
		iterationsField.setFont(fieldFont);
		mseField.setFont(fieldFont);
		
		JLabel iterationsLabel = new JLabel("iterations");
		JLabel mseLabel = new JLabel("mean squared error");
		
		iterationsLabel.setFont(defaultFont);
		mseLabel.setFont(defaultFont);
		
		iterationsPanel.add(iterationsField);
		iterationsPanel.add(iterationsLabel);
		
		
		msePanel.add(mseField);
		msePanel.add(mseLabel);
		
		this.add(numberLayersPanel);
		this.add(numberNeuronsPanel);
		this.add(new JPanel());
		
		JLabel stopLabel = new JLabel("Stop training network at:");
		stopLabel.setFont(defaultFont);
		
		this.add(stopLabel);
		this.add(iterationsPanel);
		
		JLabel orLabel = new JLabel("OR");
		orLabel.setFont(defaultFont);
		
		this.add(orLabel);
		this.add(msePanel);
		this.add(new JPanel());
	}
	
	private boolean parseUserInput(){
		String numberLayersText = numberLayersBox.getText();
		String numberNeuronsText = numberNeuronsBox.getText();
		String iterationsText = iterationsField.getText();
		String mseText = mseField.getText();
		String numericalPattern = "^[0-9]+$";
		String floatNumericalPattern = "^[0-9]+([.][0-9]+)?$";
		
		boolean isValidInput = (numberLayersText.matches(numericalPattern) &&
				numberNeuronsText.matches(numericalPattern) &&
				iterationsText.matches(numericalPattern) &&
				mseText.matches(floatNumericalPattern));
		
		if (isValidInput){
			numberLayers = Integer.parseInt(numberLayersText);
			numberNeurons = Integer.parseInt(numberNeuronsText);
			numberIterations = Integer.parseInt(iterationsText);
			mse = Float.parseFloat(mseText);
		}
		else JOptionPane.showMessageDialog(this, "Input must be non-negative integer values \r\n" +
				"(except for mse, which must be a decimal)", "Invalid Input", JOptionPane.ERROR_MESSAGE);
		
		return isValidInput;
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == trainButton){
				if (parseUserInput()){
					new Thread(){
						public void run(){
							loading = new LoadingScreen("Training Neural Network", true, CustomNetworkTab.this, 400, 250);
							
							try {
								trainButton.setEnabled(false);
								saveButton.setEnabled(false);
								
								INetworkIOTranslator translator = new AlphaNumericIOTranslator();
								INeuralNetwork network = new MatrixNeuralNetwork(((AlphaNumericIOTranslator)translator).getInputLength(),
										numberLayers, numberNeurons, AlphaNumericCharacterConverter.NUMBER_CLASSES, true);
								
								File selectedTrainingSet = setSelectionPanel.getSelectedFolder();
								
								customNetwork = NetworkFactory.getTrainedNetwork(network, translator, selectedTrainingSet, 
										new MatrixBackpropTrainer(0.05f, 0.02f, CustomNetworkTab.this), numberIterations, mse);
								
								saveButton.setEnabled(true);
								
								String message = (success ? "Network was successfully trained.\n" : "Network could NOT be successfully trained.\n");
								message += "MSE achieved: " + errorAchieved +"\nNumber iterations performed: " + iterationsPerformed +
										"\nTraining time: " + trainingTime + " sec.";
								
								JOptionPane.showMessageDialog(CustomNetworkTab.this, message, "Network Training Summary", JOptionPane.INFORMATION_MESSAGE);
								
							} catch (IOException e) {
								Logger.logMessage("Error occurred while training neural network. Message: " + e.getMessage() + "; Source: " + Arrays.toString(e.getStackTrace()));
								JOptionPane.showMessageDialog(CustomNetworkTab.this, "An error occurred while training the neural network, and training has stopped.", 
										"Error", JOptionPane.ERROR_MESSAGE);
							}
							finally{
								trainButton.setEnabled(true);
								loading.closeScreen();
							}
						}
					}.start();
				}
			}
			else if (evt.getSource() == saveButton){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("savedNetworks/customNetworks"));
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Artifical Neural Networks", "ann"));
				int result = chooser.showSaveDialog(CustomNetworkTab.this);
				
				if (result == JFileChooser.APPROVE_OPTION){
					File networkFile = chooser.getSelectedFile();
					String networkName = networkFile.getName();
					String[] nameParts = networkName.split("[.]");
					
					if (isValidFilename(nameParts)){
						String networkPath = networkFile.getAbsolutePath();
						
						if (!networkPath.endsWith(".ann"))
							networkPath += ".ann";
						
						NeuralNetworkIO.writeNetworkToPath(customNetwork, networkPath);
					}
					else{
						String errorMessage = getErrorMessage(nameParts);
						JOptionPane.showMessageDialog(CustomNetworkTab.this, errorMessage, "Save Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

		private boolean isValidFilename(String[] nameParts) {
			return (nameParts.length == 1 || (nameParts.length == 2 && nameParts[1].equals("ann"))) && nameParts[0].length() > 0;
		}

		private String getErrorMessage(String[] nameParts) {
			String errorMessage = "Error saving the file.";
			
			if (nameParts.length > 2){
				errorMessage = "File cannot have more than one extension.";
			}
			else if (nameParts[0].length() == 0){
				errorMessage = "File name must have at least one character.";
			}
			else if (!nameParts[1].equals("ann")){
				errorMessage = "File must be an artificial neural network.";
			}
			
			return errorMessage;
		}
	}

	@Override
	public void progressUpdate(float mse, int iterationsPerformed) {
		loading.setSubMessage("Current MSE: " + mse + "\nNumber iterations performed: " + iterationsPerformed);
	}

	@Override
	public void setTrainingSummary(float mse, int iterationsPerformed,
			boolean success, long secs) {
		errorAchieved = mse;
		this.iterationsPerformed = iterationsPerformed;
		this.success = success;
		trainingTime = secs;
	}
}
