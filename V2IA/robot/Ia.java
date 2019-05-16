package robot;

import communication.Bluetooth;
import graphe.Case;
import graphe.Graphe;

import javax.swing.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

public class Ia extends Robot {
    public Ia(Graphe graphe, int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Semaphore sem) {
        super(graphe, x, y, orientation, monNxt, capaciteCofrre, nomCoop, sem);
        start();
    }

    public Ia(Graphe graphe, int x, int y, String orientation, String monNxt, int capaciteCofrre, String nomCoop, Bluetooth communication, Semaphore sem) {
        super(graphe, x, y, orientation, monNxt, capaciteCofrre, nomCoop, communication, sem);
        start();
    }

    @Override
    protected void Rstop() {

    }


    public void run() {
        JOptionPane jop1,jop3;
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "GOOOOOO", "Information", JOptionPane.INFORMATION_MESSAGE);
        executionIA();
        /*TreeSet<Couple> attente = new TreeSet<>();
        attente.add(new Couple(0, graphe.getCase(x,y), graphe,graphe.getCase(4,4), new LinkedList<>(), orientation));
        System.out.println(AEtoile(attente,new LinkedList<>(),graphe.getCase(4,4)));
        System.out.println("hihi :" +calculCheminTotalSimulation(x,y,orientation));
        attente = new TreeSet<>();
        attente.add(new Couple(0,graphe.getCase(4,4), graphe,graphe.getCase(2,0), new LinkedList<>(), "O"));
        System.out.println(AEtoile(attente,new LinkedList<>(),graphe.getCase(2,0)));*/
        jop3 = new JOptionPane();
        jop3.showMessageDialog(null, "Fini", "Information", JOptionPane.INFORMATION_MESSAGE);
        log.close();
    }

    public void executionIA(){
        boolean fini = false;
        List<String> chemin=calculCheminTotalSimulation(x,y,orientation);
        while(!fini){
            System.out.println("## JE BOUGE DEPART ## en x : " + x +" y : "+y+"orienttaion : " + orientation);
            envoieUnMessage(chemin);
            System.out.println("## JE BOUGE POUR FINR ## en x : " + x +" y : "+y+"orienttaion : " + orientation);
            chemin = calculCheminTotalSimulation(x,y,orientation);
            System.out.println(chemin);
            fini = (chemin.size()==1 && coffre !=0);
        }
        System.out.println("g,nrzogngzrkpln,mb,mng");
        envoieUnMessage(chemin);
        takeOrDrop();
        esquive(2);
    }


    public void envoieUnMessage ( List<String> listDep){
        String[] currentAction=listDep.get(0).split(" ");
        List<Integer> coordPilote=log.dernierPosCoop(nomCoop);
        Case next = graphe.getCase(x,y);
        System.out.println("1");
        if (currentAction[0].equals("U")){
            System.out.println("2");
            //secu colision
            if (!next.equals(null)) {
                while (next.next(graphe.getCase(x,y).autreVoisin(orientation)).equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    coordPilote=log.dernierPosCoop(nomCoop);
                }
            }
            mouvement("u");
            mouvement(graphe.nextMouv(x,y,orientation,currentAction[1]));
        }else{
            System.out.println("3");
            if (!next.equals(null)) {
                while (next.next(orientation).equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))) {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    coordPilote=log.dernierPosCoop(nomCoop);
                }
            }
            mouvement(graphe.nextMouv(x,y,orientation,currentAction[0]));
        }
        System.out.println(" 4     :::::"+listDep);
        if (listDep.size()>1) {
            currentAction = listDep.get(1).split(" ");
            System.out.println(currentAction[0]);
            if (currentAction[0].equals("A")) {
                System.out.println("5");
                takeOrDrop();
            }
        }



    }

    private void takeOrDrop() {
        System.out.println("6");
        System.out.println(graphe.getCase(x,y));
        if (coffre>=capaciteCofrre){
            while(coffre>0){
                deposer();
            }
        }

        System.out.println("8");
        rammasser();
        System.out.println("9");
    }


    public List<String> calculCheminTotalSimulation(int xDepart,int yDepart,String orientationDepart){
        List<Case> listHopitaux = new LinkedList<>();
        listHopitaux.addAll(graphe.getHopitaux());
        List<Case> listVictimes = new LinkedList<>();
        listVictimes.addAll(graphe.getVictimes());
        //System.out.println( " depart H : " +listHopitaux);
        //System.out.println( " depart V : " +listVictimes);
        List<String> cheminResult = new LinkedList<>();
        List<String> cheminInter;
        Boolean fini = false;
        int coffreHypo=coffre,xSimulation=xDepart,ySimulation=yDepart;
        /*System.out.println( " coffre : " +coffreHypo);
        System.out.println( " xDepart : " +xDepart);
        System.out.println( " yDepart : " +yDepart);
        System.out.println( " orientationDepart : " +orientationDepart);*/
        String orientationSimulation=orientationDepart;
        Case but = null,currentCaseSimulation= graphe.getCase(xSimulation,ySimulation);
        coffreHypo = gestionCoffreSimulation(listVictimes, coffreHypo,currentCaseSimulation);
        //choix but suivant
        if (listVictimes.isEmpty()){
            if (coffreHypo>0){
                but = fristListHopital(listHopitaux,currentCaseSimulation);
            }else fini=true;
        }else {
            but = choixBut(listHopitaux, listVictimes, coffreHypo,currentCaseSimulation);
        }
        TreeSet<Couple> attente = new TreeSet<>();
        attente.add(new Couple(0, currentCaseSimulation, graphe,but, new LinkedList<>(), orientationSimulation));

        while(!fini){
            //System.out.println("but : "+ but);
            cheminInter = AEtoile(attente, new LinkedList<Couple> (), but);
            //System.out.println("cheminInter " +cheminInter);
            //gestion derniere case
            currentCaseSimulation=but;
            String[] currentAction=cheminInter.get(cheminInter.size()-1).split(" ");
            if (currentAction[0].equals("U")) {
                orientationSimulation = currentAction[1];
            }else{
                orientationSimulation = currentAction[0];
            }

            cheminResult.addAll(cheminInter);

            //System.out.println("    cheminresult " +cheminResult);
            coffreHypo = gestionCoffreSimulation(listVictimes, coffreHypo,currentCaseSimulation);
            //choix but suivant
            attente = new TreeSet<>();
            if (listVictimes.isEmpty()){
                if (coffreHypo>0){
                    but = fristListHopital(listHopitaux,currentCaseSimulation);
                    attente = initialisationAttente(cheminResult, orientationSimulation, but, currentCaseSimulation);
                }else
                    fini=true;
            }else {
                but = choixBut(listHopitaux, listVictimes, coffreHypo,currentCaseSimulation);
                attente = initialisationAttente(cheminResult, orientationSimulation, but, currentCaseSimulation);
            }
        }
        return cheminResult;
    }

    private TreeSet<Couple> initialisationAttente(List<String> cheminResult, String orientationSimulation, Case but, Case currentCaseSimulation) {
        TreeSet<Couple> attente = new TreeSet<>();
        List<String> cheminDepart = new LinkedList<>();
        cheminDepart.add("A");
        attente.add(new Couple(0,currentCaseSimulation, graphe,but, cheminDepart,orientationSimulation));
        System.out.println(currentCaseSimulation);
        if (currentCaseSimulation.isIntersect() && !currentCaseSimulation.equals(graphe.getCase(x,y))){
            String pourInverse = cheminResult.get(cheminResult.size()-1);
            if ( pourInverse == "A"){
                pourInverse = cheminResult.get(cheminResult.size()-2);
            }
            String[] currentAction=pourInverse.split(" ");
            if (currentAction[0].equals("U")) {
                pourInverse = currentAction[1];
            }else{
                pourInverse = currentAction[0];
            }
            String autreOrientation = currentCaseSimulation.autreVoisinIntersect(orientationSimulation,graphe.inverse(pourInverse));
            attente.add(new Couple(0,currentCaseSimulation, graphe,but,cheminDepart,autreOrientation));
        } return attente;
    }

    private int gestionCoffreSimulation(List<Case> listVictimes, int coffreHypo,Case current) {
        if (graphe.estHopital(current)){
            while(coffreHypo>0){
                coffreHypo--;
            }
        }
        if (graphe.estVictime(current)){
            listVictimes.remove(current);
            coffreHypo++;
        } return coffreHypo;
    }



    private Case choixBut(List<Case> listHopitaux, List<Case> listVictimes, int coffreHypo, Case current) {
        Case but;
        if (coffreHypo>=capaciteCofrre) {
            but = fristListHopital(listHopitaux,current);
        }else {
            but =  fristListVictime(listVictimes,current);
            listVictimes.remove(but);}
        return but;
    }




    private Case fristListHopital(List<Case> listBut, Case currentCase){
        final float[] malus1 = {0};
        final float[] malus2 = {0};
        TreeSet<Case> result =new TreeSet<>(
                new Comparator<Case>() {
                    @Override
                    public int compare(Case o1, Case o2) {
                        List<Integer> coordPilote=log.dernierPosCoop(nomCoop);
                        if (o1.equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))){ malus1[0] =100;}else {
                            malus1[0] =0;}
                        if (o2.equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))){ malus2[0] =100;}else {
                            malus1[0] =0;}
                        if ((graphe.heuristique(currentCase,o1)+ malus1[0])< (malus2[0] +(graphe.heuristique(currentCase, o2)))) {
                            return -1;
                        }
                        return 1;
                    }
                }
        );
        for (Case cur : listBut){
            result.add(cur);
        }
        return result.first();
    }

    private Case fristListVictime(List<Case> listBut, Case currentCase){
        TreeSet<Case> result =new TreeSet<>(
                new Comparator<Case>() {
                    @Override
                    public int compare(Case o1, Case o2) {
                        List<Integer> coordPilote=log.dernierPosCoop(nomCoop);
                        if (graphe.heuristique(currentCase,o1) == (graphe.heuristique(currentCase,o2))){
                            if (graphe.heuristique(o1,fristListHopital(graphe.getHopitaux(),o1))+(graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o1)+10) < (graphe.heuristique(o2,fristListHopital(graphe.getHopitaux(),o2))+(graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o2)+10))){
                                return 1;
                            }
                            return -1;
                        }else {
                            //System.out.println( "   Pour "+ o1 + " h : " +(graphe.heuristique(graphe.getCase(x, y),o1)-graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o1))+ " < "+" Pour "+ o2 + " h : "+(graphe.heuristique(graphe.getCase(x, y),o2)-(graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o2))));
                            if ((graphe.heuristique(graphe.getCase(x, y),o1)-(graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o1)+10)) <= (graphe.heuristique(graphe.getCase(x, y),o2))-(graphe.heuristique(graphe.getCase(coordPilote.get(0), coordPilote.get(1)),o2)+10)){
                                return -1;
                            }
                            return 1;}
                    }
                }
        );
        for (Case cur : listBut){
            result.add(cur);
        }
        //System.out.println(result);
        return result.first();
    }

    private List<String> AEtoile(TreeSet<Couple> attente, List<Couple> vu, Case but) {
        if (attente.isEmpty()){
            List<String> result = new LinkedList<>();
            result.add("erreur");
            return result;
        }

        Couple current = attente.first();
        attente.remove(current);
        if(current.getCase()==but){
            return current.getChemin();
        }

        List<Couple> filss = creerFils(current,but,vu);
        //System.out.println("\n je suis en "+ current + "mon but est : " + but );
        //System.out.println("	 fils de" + current + " : " + filss+"\n");
        //System.out.println("	 VU" + current + " : " + filss+"\n");
        for(Couple cur : filss){
            attente.add(cur);
        }
        //System.out.println("#attente : "+ attente+ "\n \n");
        /*try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return AEtoile(attente,vu,but);
    }

    private List<Couple> creerFils(Couple current, Case but, List<Couple> vu) {
        List<Couple> result = new LinkedList<>();
        boolean contains;
        Case curCase = current.getCase();
        String curDirection = current.getOrientation();
        float g;
        Couple fils;
        List<String> cheminPere;

        cheminPere = (current.getChemin());

        List<Case> voisinPossible = new LinkedList<>(curCase.getVoisinPossible(curDirection));

        for (Case curFils : voisinPossible) {
            float gfils;
            List<String> direcPossible = curFils.getDirectionPossible(graphe.inverse(curDirection));
            //pour le demi tour
            if (curCase.next(curDirection)!=curFils){
                direcPossible = curFils.getDirectionPossible(graphe.inverse(curCase.autreVoisin(curDirection)));
            }
            //System.out.println("  pour "+curFils + "  @@"+direcPossible);
            for (String curDirectionFils : direcPossible) {
                contains = false;
                g = current.getG();
                List<String> cheminFils = new LinkedList<>(cheminPere);
                if (curCase.next(curDirection) != curFils) {
                    gfils = g + 2;
                    cheminFils.add("U " + curDirectionFils);
                } else {
                    gfils = g + 1;
                    cheminFils.add(curDirectionFils);
                }
                if (curFils.isIntersect()) gfils += 0.25;
                List<Integer> coordPilote=log.dernierPosCoop(nomCoop);
                /*if (curFils.next(curDirectionFils).equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1))) && !graphe.estHopital(graphe.getCase(coordPilote.get(0), coordPilote.get(1))) && curFils.isIntersect()) {
                   gfils += 20;
                }*/
                //System.out.println(graphe.getCase(coordPilote.get(0), coordPilote.get(1)));
                if (curFils.equals(graphe.getCase(coordPilote.get(0), coordPilote.get(1))) && !graphe.estHopital(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))&& !graphe.estVictime(graphe.getCase(coordPilote.get(0), coordPilote.get(1)))){
                    gfils += 100;
                    System.out.println("ta mereeeeeeee");
                }
                fils = new Couple(gfils, curFils, graphe, but, cheminFils, curDirectionFils);
                for (Couple curCoupleVu : vu) {
                    if (curCoupleVu.getCase().equals(fils.getCase())) {
                        if (fils.compareTo(curCoupleVu) < 0) {
                            contains = true;
                        } else {
                            vu.remove(curCoupleVu);
                        }
                    }
                }
                if (!contains) {
                    result.add(fils);
                }
            }
        }
        return result;
    }

    public void esquive(int maxDist) {
        while (!log.estFiniCoop(nomCoop)) {
            List<String> cheminPilote = getDistancePilote();
            if(cheminPilote.size()<=maxDist) {
               String lastDirPilote = cheminPilote.get(cheminPilote.size()-2);
                String voisinPiloteArrive = graphe.inverse(lastDirPilote);
                Case butIA = graphe.getCase(x, y).next(graphe.getCase(x, y).autreVoisin(voisinPiloteArrive));
                TreeSet<Couple> attenteIA = new TreeSet<>();
                attenteIA.add(new Couple(0,graphe.getCase(x, y),graphe,butIA,new LinkedList<>(),orientation));
                List<String> cheminEsquive = AEtoile(attenteIA,new LinkedList<>(),butIA);
                envoieUnMessage(cheminEsquive);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getDistancePilote() {
        TreeSet<Couple> attente;
        List<Integer> lastPosPilote = log.dernierPosCoop(nomCoop);
        String lastOrientPilote = log.lastOrientCoop(nomCoop);
        attente = new TreeSet<>();
        attente.add(new Couple(0, graphe.getCase(lastPosPilote.get(0),lastPosPilote.get(1)), graphe,graphe.getCase(x,y), new LinkedList<>(), lastOrientPilote));
        return AEtoile(attente,new LinkedList<>(),graphe.getCase(x,y));
    }

}
