package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{	
	private DrawingArea area;
	private ControlsPanel controls;
	
	public DrawingPanel(){
		area = new DrawingArea(this);
		controls = new ControlsPanel(this);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(area);
		this.add(controls);
	}
	
	public void drawingChanged(){
		controls.repaint();
		this.repaint();
	}
	
	public void saveClicked(){
		
	}
	
	public void clearClicked(){
		area.clearData();
	}
	
	public void penSizeChanged(int penSize){
		area.setPenSize(penSize);
	}
	
	public void areaSizeChanged(int areaSize){
		area.setDimensions(areaSize, areaSize);
	}
}
