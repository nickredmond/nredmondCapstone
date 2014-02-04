package appTest;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RejectedImagesPanel extends JPanel {
	
	public RejectedImagesPanel(List<BufferedImage> images){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("REJECTED IMAGES");
		this.add(title);
		
		for (BufferedImage nextImage : images){
			JLabel nextLabel = new JLabel(new ImageIcon(nextImage));
			this.add(nextLabel);
		}
	}
}
