package robotLego;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Pilote extends Robot implements ActionListener {
	private Bluetooth communication;
	private static JFrame fenetre = new JFrame();
	private static JSplitPane layout = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private static JPanel takeDrop = new JPanel();
	private static JPanel direction = new JPanel();
	private static JButton up = new JButton(new ImageIcon("src/robotLego/image/fleche-haut.jpg"));
	private static JButton right = new JButton(new ImageIcon("src/robotLego/image/fleche-droite.jpg"));
	private static JButton left = new JButton(new ImageIcon("src/robotLego/image/fleche-gauche.jpg"));
	private static JButton down = new JButton(new ImageIcon("src/robotLego/image/tourner-fleche.jpg"));
	private static JRadioButton take = new JRadioButton("take",false);
	private static JRadioButton drop = new JRadioButton("drop",false);

	public Pilote(int init, Graphe g, Bluetooth blue) {
		super(init, g);
		this.communication = blue;
	}

	public void startInterface() {
		up.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		down.addActionListener(this);
		
		layout.setTopComponent(direction);
		layout.setBottomComponent(takeDrop);
		layout.setDividerSize(5);
		layout.setDividerLocation(400);

		// bouton take et drop
		FlowLayout layTakeDrop = new FlowLayout();
		takeDrop.setLayout(layTakeDrop);
		Dimension d = new Dimension();
		d.setSize(100, 60);
		take.setPreferredSize(d);
		drop.setPreferredSize(d);
		Font font = new Font(Font.DIALOG, Font.BOLD, 15);
		take.setFont(font);
		drop.setFont(font);
		takeDrop.add(take);
		takeDrop.add(drop);

		// bouton de direction
		Dimension d2 = new Dimension();
		d2.setSize(100, 100);
		up.setPreferredSize(d2);
		left.setPreferredSize(d2);
		right.setPreferredSize(d2);
		down.setPreferredSize(d2);
		direction.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 2.0;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		direction.add(up, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		direction.add(left, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 1;
		direction.add(right, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		direction.add(down, c);
		
		fenetre.add(layout);
		fenetre.setSize(500, 550);
		fenetre.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source==up) {
			//String message = toutDroit(take, drop)
			//fonction sent to robot (message)
			if (take.isSelected()) {
				take.setSelected(false);
			}
			if (drop.isSelected()) {
				drop.setSelected(false);
			}
		}
		if (source==left) {
			//String message = tournerGauche(take, drop)
			//fonction sent to robot (message)
			if (take.isSelected()) {
				take.setSelected(false);
			}
			if (drop.isSelected()) {
				drop.setSelected(false);
			}
		}
		if (source==right) {
			//String message = tournerDroite(take, drop)
			//fonction sent to robot (message)
			if (take.isSelected()) {
				take.setSelected(false);
			}
			if (drop.isSelected()) {
				drop.setSelected(false);
			}
		}
		if (source==down) {
			//String message = demiTour(take, drop)
			//fonction sent to robot (message)
			if (take.isSelected()) {
				take.setSelected(false);
			}
			if (drop.isSelected()) {
				drop.setSelected(false);
			}
		}
	}
	
	
}
