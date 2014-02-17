package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import networkIOtranslation.AlphaNumericIOTranslator;
import networkIOtranslation.INetworkIOTranslator;
import neuralNetwork.CharacterReader;
import neuralNetwork.INeuralNetwork;
import app.CharacterResult;

public class CharacterRecognitionPanel extends JPanel implements INetworkSelectionHandler{
	private INeuralNetwork selectedNetwork;
	private NetworkSelectionPanel networkPanel;
	private JButton recognizeButton;
	private JLabel translationLabel, confidenceLabel;
	
	private BufferedImage characterImage;
	private INetworkIOTranslator translator;
	
	private DigitalDrawingWindow window;
	
	public CharacterRecognitionPanel(DigitalDrawingWindow window){
		this.window = window;
		translator = new AlphaNumericIOTranslator();
		setupPanel();
	}
	
	public void setImage(BufferedImage image){
		characterImage = image;
	}
	
	private void setupPanel(){
		networkPanel = new NetworkSelectionPanel(this);
		recognizeButton = new JButton("Recognize Current Image");
		recognizeButton.addActionListener(new ButtonListener());
		
		JLabel resultLabel = new JLabel("RESULT:");
		translationLabel = new JLabel("Network thinks it's: ---");
		confidenceLabel = new JLabel("Translation Confidence: ---");
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(networkPanel);
		this.add(recognizeButton);
		this.add(resultLabel);
		this.add(translationLabel);
		this.add(confidenceLabel);
	}
	
	@Override
	public void networkSelected(String networkName, INeuralNetwork network) {
		selectedNetwork = network;
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent evt) {
			if (evt.getSource() == recognizeButton){
				window.recognizeClicked();
				
				if (selectedNetwork == null){
					selectedNetwork = networkPanel.getNetwork();
				}
				
				CharacterReader reader = new CharacterReader(selectedNetwork, translator);
				CharacterResult result = reader.readCharacter(characterImage);
				
				char translation = result.getResult().getCharacter();
				float confidence = result.getResult().getConfidence();
				float confidencePercentage = confidence * 100;
				
				String percentageString = convertFloatToRoundedString(2, confidencePercentage) + "%";
				
				translationLabel.setText("Network thinks it's: " + translation);
				confidenceLabel.setText("Translation confidence: " + percentageString);
			}
		}
		
		private String convertFloatToRoundedString(int placesToRound, float value){
			String floatString = new Float(value).toString();
			String[] floatParts = floatString.split("[.]");
			
			String roundedString = floatParts[1].substring(0, placesToRound);
			return floatParts[0] + "." + roundedString;
		}
	}
}