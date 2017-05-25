package appTest;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import debug.FeatureExtractionDebug;

public class BinaryImagePanel extends JPanel {
	private int[][] lightValues;
	
	public BinaryImagePanel(int dimensionX, int dimensionY, int width, int height){
		lightValues = new int[dimensionY][dimensionX];
		this.setSize(new Dimension(width, height));
	}
	
	public void setData(int[][] values){		
		if (lightValues.length == values.length && lightValues[0].length == values[0].length){
			lightValues = values;
		}
		else throw new IllegalArgumentException("Wrong dimensions, dumbass...");
	}
	
	@Override
	public void paintComponent(Graphics g){		
		int width = this.getSize().width;
		int height = this.getSize().height;
		int cellHeight = (height / lightValues.length);
		int cellWidth = (width / lightValues[0].length);
		
		for (int row = 0; row < lightValues.length; row++){
			for (int col = 0; col < lightValues[0].length; col++){
				int yPos = this.getLocation().y + (row * cellHeight);
				int xPos = this.getLocation().x + (col * cellWidth);
				
				Color cellColor = (lightValues[row][col] == 1) ? Color.BLACK : Color.WHITE;
				g.setColor(cellColor);
				g.fillRect(xPos, yPos, cellWidth, cellHeight);
				
				g.setColor(Color.RED);
				g.drawRect(xPos, yPos, cellWidth, cellHeight);
			}
		}
	}
	
	private Color getCellColor(float value){
		Color color = null;
		
		if (value > 0.9f){
			color = Color.BLACK;
		}
		else if (value > 0.6f){
			color = Color.DARK_GRAY;
		}
		else if (value > 0.3f){
			color = Color.LIGHT_GRAY;
		}
		else color = Color.WHITE;
		
		return color;
	}
}
