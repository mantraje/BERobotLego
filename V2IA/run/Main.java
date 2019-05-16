package run;

import communication.Bluetooth;
import graphe.Case;
import graphe.Graphe;
import robot.Ia;
import robot.Pilote;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {
    private static int XPilote = -1, YPilote = -1, XIA = -1, YIA = -1;
    private static String orPilote, orIA;
    private static File repertoire = new File("src/configuration");
    private static File fichier = popInterface();
    private static boolean BRIA = false;
    private static boolean BRP = false;
    private static int capaciteCofrre = 1;
    private static String nomIA = null, nomPilote = null, portIA = null, portPilote = null;
    private static boolean blue = false;
    private static Semaphore sem = new Semaphore(1);


    /**
     * Permet la demande du fichier de configuration
     */
    public static File popInterface() {
        JOptionPane pop = new JOptionPane();
        String[] listConfig = repertoire.list();
        String chemin = (String) JOptionPane.showInputDialog(null,
                "Entrez le nom du fichier de configuration :",
                "Parametre",
                JOptionPane.QUESTION_MESSAGE, null, listConfig, null);
        File fichierCur = new File("src/configuration/" + chemin);
        return fichierCur;
    }


    /**
     * Lance le pilote dans un threads separe
     */
    public static void lancerRobot(Graphe graphe) {
        new Thread(()-> {
        if(!blue) {
                new Pilote(graphe, XPilote, YPilote, orPilote, nomPilote, capaciteCofrre, nomIA, sem);
            } else

            {
                new Pilote(graphe, XPilote, YPilote, orPilote, nomPilote, capaciteCofrre, nomIA, new Bluetooth(nomPilote, portPilote), sem);
            }
        }).start();
    }

    /**
     * Lance l'IA dans un threads separe
     */
    public static void lancerIA(Graphe graphe) {
        if (!blue) {
            new Ia(graphe, XIA, YIA, orIA, nomIA, capaciteCofrre, nomPilote, sem);
        } else {
            new Ia(graphe, XIA, YIA, orIA, nomIA, capaciteCofrre, nomPilote, new Bluetooth(nomIA, portIA), sem);
        }

    }

    public static void main(String[] args) {
        Graphe graphe = inialisation();
        if (!(graphe == null)) {
            if (BRIA) {
                lancerIA(graphe);
            }
            if (BRP) {
                lancerRobot(graphe);
            }

        }
    }

    /**
     * initialise le graphe en fonction du fichier recupere dans popInterface()
     */
    private static Graphe inialisation() {
        Scanner scanner;
        String current;
        Case[][] matriceG = new Case[0][];
        LinkedList<Case> victimes = null, hopitaux = null;
        int tailleX = 0, tailleY = 0;
        //ouverture fichier
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return null;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
            switch (current) {
                //construction de la matrice ( en haut a gauche -> 0,0 )
                case ("G"):
                    String[] ligne;
                    tailleX = Integer.parseInt(scanner.nextLine());
                    tailleY = Integer.parseInt(scanner.nextLine());
                    matriceG = new Case[tailleX][tailleY];
                    for (int x = 0; x < tailleX; x++) {
                        ligne = scanner.nextLine().split(" ");
                        for (int y = 0; y < tailleY; y++) {
                            if (ligne[y].equals("_")) {
                                matriceG[x][y] = null;
                            } else {
                                Case nord = x - 1 >= 0 ? matriceG[x - 1][y] : null;
                                Case ouest = y - 1 >= 0 ? matriceG[x][y - 1] : null;
                                Case cur = new Case(nord, ouest, ligne[y].split(""), x, y);
                                if (nord != null) {
                                    nord.setSud(cur);
                                }
                                if (ouest != null) {
                                    ouest.setEst(cur);
                                }
                                matriceG[x][y] = cur;
                            }
                        }
                    }
                    break;
                case ("B"):
                    blue = true;
                    break;
                case ("C"):
                    current = scanner.nextLine();
                    capaciteCofrre = Integer.parseInt(current);
                    break;
                case ("RP"):
                    String[] donneBlue = scanner.nextLine().split(",");
                    nomPilote = donneBlue[0];
                    portPilote = donneBlue[1];
                    String[] coordonne = scanner.nextLine().split(",");
                    XPilote = Integer.parseInt(coordonne[0]);
                    YPilote = Integer.parseInt(coordonne[1]);
                    orPilote = coordonne[2];
                    BRP = true;
                    break;
                case ("RIA"):
                    String[] donneBlueIA = scanner.nextLine().split(",");
                    nomIA = donneBlueIA[0];
                    portIA = donneBlueIA[1];
                    String[] coordonneIA = scanner.nextLine().split(",");
                    XIA = Integer.parseInt(coordonneIA[0]);
                    YIA = Integer.parseInt(coordonneIA[1]);
                    orIA = coordonneIA[2];
                    BRIA = true;
                    break;
                case ("V"):
                    victimes = construcListAvecString(scanner.nextLine().split(":"), matriceG);
                    break;
                case ("H"):
                    hopitaux = construcListAvecString(scanner.nextLine().split(":"), matriceG);
                    break;
            }
        }
        return (new Graphe(tailleX, tailleY, matriceG, victimes, hopitaux,sem));
    }

    /**
     * prends un Strin[] des coordonne des victime ou hopitaux
     * et renvoie une LinkedList de Case correspondant aux coordonne
     */
    private static LinkedList<Case> construcListAvecString(String[] split, graphe.Case[][] matriceG) {
        LinkedList<Case> result = new LinkedList<>();
        int x, y;
        for (String current : split) {
            String[] coordonne = current.split(",");
            x = Integer.parseInt(coordonne[0]);
            y = Integer.parseInt(coordonne[1]);
            result.add(matriceG[x][y]);
        }
        return result;
    }
}
