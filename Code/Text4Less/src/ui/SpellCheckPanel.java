package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SpellCheckPanel extends JPanel{
	private boolean useSpellCheck = false;
	private JRadioButton yesSpellCheckButton, noSpellCheckButton;
	
	public SpellCheckPanel(){
		setupDisplay();
	}
	
	private void setupDisplay() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel spellCheckLabel = new JLabel("Translation Spell Checking:");
		spellCheckLabel.setFont(HomeWindow.SUB_LABEL_FONT);
		
		this.add(spellCheckLabel);
		
		yesSpellCheckButton = new JRadioButton("Yes");
		noSpellCheckButton = new JRadioButton("No");
		
		yesSpellCheckButton.setFont(HomeWindow.SUB_LABEL_FONT);
		noSpellCheckButton.setFont(HomeWindow.SUB_LABEL_FONT);
		
		SpellCheckListener listener = new SpellCheckListener();
		yesSpellCheckButton.addActionListener(listener);
		noSpellCheckButton.addActionListener(listener);
		
		noSpellCheckButton.setSelected(true);
		
		ButtonGroup group = new ButtonGroup();
		group.add(yesSpellCheckButton);
		group.add(noSpellCheckButton);
		
		this.add(yesSpellCheckButton);
		this.add(noSpellCheckButton);
	}

	public boolean isSpellCheckEnabled(){
		return useSpellCheck;
	}
	
	public void setSpellCheckEnabled(boolean isEnabled){
		useSpellCheck = isEnabled;
		
		if (useSpellCheck){
			yesSpellCheckButton.setSelected(true);
		}
		else noSpellCheckButton.setSelected(true);
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
}
