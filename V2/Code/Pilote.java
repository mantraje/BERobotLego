package Code;

import org.jfree.layout.CenterLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Pilote extends Robot implements ActionListener {
	private Bluetooth communication;
	private static JFrame fenetre = new JFrame();
	private static JSplitPane layout = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private static JPanel takeDrop = new JPanel();
	private static JPanel direction = new JPanel();
	private static JLabel text = new JLabel("",SwingConstants.CENTER);
	private static JButton up = new JButton(new ImageIcon("src/image/fleche-haut.jpg"));
	private static JButton right = new JButton(new ImageIcon("src/image/fleche-droite.jpg"));
	private static JButton left = new JButton(new ImageIcon("src/image/fleche-gauche.jpg"));
	private static JButton down = new JButton(new ImageIcon("src/image/tourner-fleche.jpg"));
	private static JRadioButton take = new JRadioButton("take",false);
	private static JRadioButton drop = new JRadioButton("drop",false);
	private static JPanel TD = new JPanel();
	private String suivi="Position robot : ";

	public Pilote(int init, Graphe g, Bluetooth blue) {
		super(init, g);
		this.communication = blue;
		suivi = suivi + currentPos;
		text.setText(suivi);
	}

	public void startInterface() {
		up.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		down.addActionListener(this);
		take.addActionListener(this);
		drop.addActionListener(this);
		
		layout.setTopComponent(direction);
		layout.setBottomComponent(takeDrop);
		layout.setDividerSize(5);
		layout.setDividerLocation(400);

		// bouton take et drop
		FlowLayout layTakeDrop = new FlowLayout();
		TD.setLayout(layTakeDrop);
		Dimension d = new Dimension();
		d.setSize(100, 60);
		take.setPreferredSize(d);
		drop.setPreferredSize(d);
		Font font = new Font(Font.DIALOG, Font.BOLD, 15);
		take.setFont(font);
		drop.setFont(font);
		TD.add(take);
		TD.add(drop);

		//text info


		//text ino + take et drop
		takeDrop.setLayout(new BorderLayout());
		takeDrop.add(TD, BorderLayout.SOUTH);
		takeDrop.add(text, BorderLayout.NORTH);


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


	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		boolean takeB=take.isSelected();
		boolean dropB = drop.isSelected();


		if (source==drop) {
			if (takeB) {
				take.setSelected(false);
			}
		}else if (source==take) {
			if (dropB) {
				take.setSelected(false);
			}
		}else {
			boolean effectuer = false;
			effectuer = isEffectuer(source, takeB, dropB, effectuer);
			if (effectuer) {
				suivi = "Position robot : "+currentPos;
				if (takeB) {
					take.setSelected(false);

				}
				if (dropB) {
					drop.setSelected(false);
				}
			}else {
				suivi = "\n MOUVEMENT IMPOSSIBLE || " +"Position robot : "+currentPos+ "\n || MOUVEMENT IMPOSSIBLE";
			}
			text.setText(suivi);
		}


	}

	private boolean isEffectuer(Object source, boolean takeB, boolean dropB, boolean effectuer) {
		if (source==up) {
			//String message = toutDroit(take, drop)
			//fonction sent to robot (message)
			if (graphe.estLigne(currentPos)){
				System.out.println(this.mouvement("s",takeB,dropB));
				effectuer = true;
			}
		}
		if (source==left) {
			//String message = tournerGauche(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(currentPos)){
				System.out.println(this.mouvement("l",takeB,dropB));
				effectuer = true;
			}
		}
		if (source==right) {
			//String message = tournerDroite(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(currentPos)){
				System.out.println(this.mouvement("r",takeB,dropB));
				effectuer = true;
			}
		}
		if (source==down) {
			//String message = demiTour(take, drop)
			//fonction sent to robot (message)
			System.out.println(this.mouvement("u",takeB,dropB));
			effectuer = true;

		}

		if (source==take) {
			if (dropB) {
				drop.setSelected(false);
			}
		}
		return effectuer;
	}


}
