package robot;

import communication.Bluetooth;
import graphe.Graphe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;


public class Pilote extends Robot implements ActionListener {
    private static JFrame fenetre = new JFrame();
    private static JSplitPane layout = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private static JPanel takeDrop = new JPanel();
    private static JPanel direction = new JPanel();
    private static JLabel text = new JLabel("",SwingConstants.CENTER);
    private static JButton up = new JButton(new ImageIcon("src/image/fleche-haut.jpg"));
    private static JButton right = new JButton(new ImageIcon("src/image/fleche-droite.jpg"));
    private static JButton left = new JButton(new ImageIcon("src/image/fleche-gauche.jpg"));
    private static JButton down = new JButton(new ImageIcon("src/image/tourner-fleche.jpg"));
    private static JButton take = new JButton("TAKE");
    private static JButton drop = new JButton("DROP");
    private static JButton stop = new JButton("STOP");
    private static JPanel TD = new JPanel();
    private static JPanel STOPL = new JPanel();
    private String suivi="Position robot : ";

    public Pilote(Graphe graphe, int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Semaphore sem) {
        super(graphe,x,y,orientation,monNxt,capaciteCofrre,nomCoop,sem);
        suivi = suivi + "["+x + ","+y+"]";
        text.setText(suivi);
        startInterface();
    }
    public Pilote(Graphe graphe,int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Bluetooth communication, Semaphore sem) {
        super(graphe,x,y,orientation,monNxt,capaciteCofrre,nomCoop,communication,sem);
        suivi = suivi + "["+x + ","+y+"]";
        text.setText(suivi);
        startInterface();
    }


    private void startInterface() {
        up.addActionListener(this);
        left.addActionListener(this);
        right.addActionListener(this);
        down.addActionListener(this);
        take.addActionListener(this);
        drop.addActionListener(this);
        stop.addActionListener(this);

        layout.setTopComponent(direction);
        layout.setBottomComponent(takeDrop);
        layout.setDividerSize(5);
        layout.setDividerLocation(400);

        // bouton take et drop
        FlowLayout layTakeDrop = new FlowLayout();
        TD.setLayout(layTakeDrop);
        STOPL.setLayout(layTakeDrop);
        Dimension d = new Dimension();
        d.setSize(220, 60);
        take.setPreferredSize(d);
        drop.setPreferredSize(d);
        stop.setPreferredSize(d);
        Font font = new Font(Font.DIALOG, Font.BOLD, 15);
        take.setFont(font);
        drop.setFont(font);
        stop.setFont(font);
        TD.add(take);
        TD.add(drop);
        STOPL.add(stop);


        //text ino + take et drop + Stop
        takeDrop.setLayout(new BorderLayout());
        takeDrop.add(TD, BorderLayout.CENTER);
        takeDrop.add(STOPL, BorderLayout.SOUTH);
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
        fenetre.setSize(500, 600);
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String msg;
        int oldX,oldY;
        if (source==drop) {
            if (deposer()) {
                suivi = "Position robot : "+"["+x + ","+y+"]" + "  Victime bien depose !";
            }else {
                    suivi = "Position robot : "+"["+x + ","+y+"]" +" : Depot impossible";
            }
        }else if (source==take) {
            if (rammasser()) {
                System.out.println(graphe.getCase(x,y));
                suivi = "Position robot : "+"["+x + ","+y+"]" + "  Victime ramassee !";
            }else {
                suivi = "Position robot : "+"["+x + ","+y+"]" +" : Ramassage impossible";
            }
        }else if (source==stop){
            Rstop();
        }else {
            msg = trad(source);
            if (graphe.isMouvementPossible(x,y,orientation,msg)) {
                mouvement(msg);
                suivi = "Position robot : "+"["+x + ","+y+"]";
            }else {
                suivi = "\n MOUVEMENT IMPOSSIBLE || " +"Position robot : "+"["+x + ","+y+"]"+ "\n || MOUVEMENT IMPOSSIBLE";
            }
        }
        text.setText(suivi);
    }


    private String trad(Object source) {
        if (source==up) {
            return "s";
        }else if (source==down) {
            return "u";
        }else if (source==left) {
            return "l";
        }else {
            return "r";
        }
    }

    @Override
    protected void Rstop(){
        log.close();
        if (communication!=null) {
            communication.deconnexion();
        }
        fenetre.dispose();
    }
}
