package graphe;

import robot.Couple;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Graphe {
	private List<Triplet4Heuris> Hvictimes;
	private List<Triplet4Heuris> Hhopitaux;
	private Case[][] map;
	private List<Case> victimes;
	private List<Case> hopitaux;
	private Semaphore sem=null;
	
	public Graphe(int tailleX,int tailleY,Case[][] matrice,List<Case> vic,List<Case> hop,Semaphore sem) {
		this.sem = sem;
		this.map = matrice;
		this.victimes=vic;
		this.hopitaux=hop;
		Hhopitaux = initHeuristique(hopitaux);
        Hvictimes = initHeuristique(victimes);
	}
	
	private List<Case> matriceToList(){
		List<Case> matPlate = new LinkedList<>();
		for(int i=0;i<map.length;i++) {
			for(Case c : map[i]) {
			    if (c!=null) {
                    matPlate.add(c);
                }
			}
		}
		return matPlate;
	}
	
	public Case getCase(int x, int y) {
	    if (x==-1 || y ==-1){
	        return null;
        }
		return map[x][y];
	}
	
    private List<Triplet4Heuris> initHeuristique(List<Case> listBut){
        List<Case> caseGraphe = matriceToList();
        List<Triplet4Heuris> lTriplet =  new LinkedList<>();
        for (Case cur : listBut){
            for(Case som : caseGraphe) {
                lTriplet.add(new Triplet4Heuris(som,cur,this));
            }
        }
        return lTriplet;
    }
    
    public void  rammasserVictime (int x, int y){
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (victimes.contains(getCase(x,y))){
            victimes.remove(getCase(x,y));
        }
        sem.release();
    }

    public boolean estHopital(int x,int y){
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	boolean res = hopitaux.contains(getCase(x,y));
    	sem.release();
        return res;
    }

    public boolean estHopital(Case c){
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	boolean res = hopitaux.contains(c);
    	sem.release();
        return res;
    }
    
    public boolean estVictime(int x,int y){
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	boolean res = victimes.contains(getCase(x,y));
    	sem.release();
        return res;
    }

    public boolean estVictime(Case c){
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	boolean res = victimes.contains(c);
    	sem.release();
        return res;
    }

    public List<Case> getVictimes() {
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<Case> res = new LinkedList<>(victimes);
    	sem.release();
        return res;
    }

    public List<Case> getHopitaux() {
    	try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	List<Case> res = new LinkedList<>(hopitaux);
    	sem.release();
        return res;
    }

    public String toString (){
        return map.toString();
    }
    
    public List<Case> getVoisinCouple(Couple currentPos) {
        return currentPos.getCase().getVoisin();
    }
    
    public float heuristique(Case sommet, Case but) {
        List<Triplet4Heuris> listBut;
        if (victimes.contains(but)){
            listBut = Hvictimes;
        }else{
            listBut = Hhopitaux;
        }
        for (Triplet4Heuris c : listBut){
            if (c.getBut()==but){
                if (c.getSommetDepart()==sommet){
                    return c.getCout();
                }
            }
        }
        return 100;
    }
    
    public List<Case> voisinPasVu(Case current,List<Case> vu){
    	return current.voisinNonVu(vu);
    }
    
    public String inverse(String dir) {
    	switch (dir) {
    		case ("N"):
    			return "S";
    		case ("S"):
    			return "N";
    		case ("O"):
    			return "E";
    		case ("E"):
    			return "O";
    		default:
    			return null;
    	}
    }
    
    public String nextOrientation(int x, int y,String dir ,String mouvement) {
    	Case current = map[x][y];
        System.out.println(dir);
        if (mouvement=="u"){
            return current.autreVoisin(dir);
        }
    	Case next = current.next(dir);
    	return next.newOrientation(inverse(dir),mouvement );
    }
    
    public String nextMouv(int x, int y, String dir,String dirSortie) {
    	Case current = map[x][y];
    	Case next = current.next(dir);
    	return next.messageToSent(inverse(dir), dirSortie);
    }
    
    public boolean isMouvementPossible(int x, int y, String dir, String mouvement) {
    	Case current = map[x][y];
    	if (mouvement=="u"){
    	    return !current.isIntersect();
        }
    	Case next = current.next(dir);
    	return next.mouvementPossible(inverse(dir), mouvement);
    }
    
}