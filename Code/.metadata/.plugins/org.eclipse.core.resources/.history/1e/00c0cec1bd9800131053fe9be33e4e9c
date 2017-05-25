package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class DrawingArea extends JPanel {
	private final int AREA_HEIGHT = 300;
	private final int AREA_WIDTH = 300;
	
	private final int DEFAULT_HEIGHT = 10;
	private final int DEFAULT_WIDTH = 10;
	private final int DEFAULT_PEN_SIZE = 1;
	
	private int height, width, penSize;
	private float cellWidth, cellHeight;
	private int[][] data;
	
	DrawingPanel parent;
	
	public DrawingArea(DrawingPanel parent){
		setDimensions(DEFAULT_HEIGHT, DEFAULT_WIDTH);
		
		data = new int[height][width];
		penSize = DEFAULT_PEN_SIZE;
		
		this.parent = parent;
		
		this.addMouseMotionListener(new DragListener());
		
		this.setSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
	}
	
	public void setPenSize(int size){
		penSize = size;
	}
	
	public void setDimensions(int height, int width){
		this.height = height;
		this.width = width;
		
		cellWidth = (float)AREA_WIDTH / width;
		cellHeight = (float)AREA_HEIGHT / height;
		
		data = new int[height][width];
		repaint();
	}
	
	public void clearData(){
		data = new int[height][width];
		repaint();
	}
	
	public int[][] getData(){
		return data;
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
		
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				int startingX = (int) (x * cellWidth);
				int endingX = (int)((x+1) * cellWidth) - 1;
				int startingY = (int) (y * cellHeight);
				int endingY = (int) ((y+1) * cellHeight) - 1;
				
				Color cellColor = (data[y][x] == 1) ? Color.BLACK : Color.WHITE;
				g.setColor(cellColor);
				g.drawRect(startingX, startingY, (endingX - startingX), (endingY - startingY));
				g.fillRect(startingX, startingY, (endingX - startingX), (endingY - startingY));
				
				parent.drawingChanged();
			}
		}
	}
	
	private class DragListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent evt) {
			Point mouseLocation = evt.getPoint();
			Point areaLocation = DrawingArea.this.getLocation();
			Point relativeLocation = new Point(mouseLocation.x - areaLocation.x, mouseLocation.y - areaLocation.y);
			
			int cellX = (int) (relativeLocation.x / cellWidth);
			int cellY = (int) (relativeLocation.y / cellHeight);
			
			if (cellY < height && cellX < width){
				drawOnArea(cellX, cellY);
			}
		}

		private void drawOnArea(int cellX, int cellY) {
			int radius = penSize / 2;
			
			int startingX = cellX - radius;
			int endingX = cellX + radius;
			int startingY = cellY - radius;
			int endingY = cellY + radius;
			
			for (int y = startingY; y <= endingY; y++){
				for (int x = startingX; x <= endingX; x++){
					if (x >= 0 && x < width && y >= 0 && y < height){
						data[y][x] = 1;
					}
				}
			}
			
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}
	}
}
