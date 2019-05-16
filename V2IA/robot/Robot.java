package robot;

import communication.Bluetooth;
import communication.Log;
import graphe.Graphe;

import java.util.concurrent.Semaphore;

public abstract class Robot extends Thread {
    protected int x;
    protected int y;
    protected String orientation;
    protected String monNxt;
    protected int coffre = 0 ;
    protected int capaciteCofrre;
    protected String nomCoop;
    protected Log log;
    protected Bluetooth communication;
    protected Semaphore sem;
    protected Graphe graphe;

    public Robot(Graphe graphe, int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Semaphore sem) {
        this.graphe = graphe;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.monNxt = monNxt;
        this.capaciteCofrre = capaciteCofrre;
        this.nomCoop = nomCoop;
        this.sem = sem;
        this.log = new Log(this,sem);
    }

    public Robot(Graphe graphe,int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Bluetooth communication, Semaphore sem) {
        this.graphe = graphe;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.monNxt = monNxt;
        this.capaciteCofrre = capaciteCofrre;
        this.nomCoop = nomCoop;
        this.communication = communication;
        //communication.connexion();
        this.sem = sem;
        this.log = new Log(this,sem);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getMonNxt() {
        return monNxt;
    }

    private String construct(String action) {
        return "20" + action + "\n";
    }

    public boolean verifTake() { return graphe.estVictime(x,y) && coffre<capaciteCofrre; }

    public boolean verifDrop() { return graphe.estHopital(x,y) && coffre>0; }

    public boolean rammasser() {
        if (verifTake()) {
            graphe.rammasserVictime(x, y);
            coffre++;
            sent("t");
            return true;
        }
        return false;
    }

    public boolean deposer() {
        if (verifDrop()) {
            coffre--;
            sent("d");
            return true;
        }
        return false;
    }

    public void sent(String msg){
        log.ecrire(msg);
        if (communication!=null) {
            communication.sent(construct(msg));
            communication.receive();
            if (msg=="u"){
                communication.sent(construct("s"));
                communication.receive();
            }
        }
        log.ecrire("mouv=non");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void Rstop();

    public void majCoordonne(String msg){
        if (msg != "u"){
            switch (orientation){
                case("N"):
                    x--;
                    break;
                case("S"):
                    x++;
                    break;
                case("E"):
                    y++;
                    break;
                case("O"):
                    y--;
                    break;
            }
        }
    }

    public void mouvement(String msg) {
        int oldX;
        int oldY;
        oldX=x;
        oldY=y;
        majCoordonne(msg);
        orientation=graphe.nextOrientation(oldX,oldY,orientation,msg);
        sent(msg);
    }

}
