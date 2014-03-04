package ui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MenuWindow extends JFrame {
	private final int DEFAULT_HEIGHT = 750;
	private final int DEFAULT_WIDTH = 875;
	
	public MenuWindow(){
		setupWindow();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		this.setVisible(true);
	}

	private void setupWindow() {
		JTabbedPane pane = new JTabbedPane();
		pane.setFont(HomeWindow.DEFAULT_LABEL_FONT);
		pane.addTab("Image Translation", new ImageTranslationTab());
		pane.addTab("Folder Translation", new FolderTranslationTab());
		pane.addTab("Neural Network Training", new CustomNetworkTab());
		pane.addTab("Handwritten Characters", new DigitalDrawingTab());
		
		this.getContentPane().add(pane);
	}
}