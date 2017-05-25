package debug;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import appTest.CharacterViewerPanel;

public class CharacterViewDebug {
	public static void displayCharacterView(BufferedImage img, int[][] zoneValues, int dimensionX, int dimensionY){
		CharacterViewerPanel panel = new CharacterViewerPanel(dimensionX, dimensionY);
		panel.setData(img, zoneValues);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(panel);
		frame.setSize(new Dimension(600, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
