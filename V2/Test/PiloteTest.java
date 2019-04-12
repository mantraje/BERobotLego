package Test;

import Code.Graphe;
import Code.Main;
import Code.Pilote;

public class PiloteTest {


    public static void main (String args[]) {

        Main m = new Main();
        Graphe testG=m.inialisationGraphe();
        Pilote p = new Pilote(m.getPosRobotPilote(),testG,null);

        p.startInterface();



    }
}
