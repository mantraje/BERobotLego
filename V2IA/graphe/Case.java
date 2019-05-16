package graphe;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Case {
	private Case nord=null;
	private Case sud=null;
	private Case ouest=null;
	private Case est=null;
	private String[] voisin;
	int x,y;

	public Case (Case nord,Case ouest,String[] voisin,int x,int y) {
		this.nord = nord;
		this.ouest=ouest;
		this.voisin = voisin;
		this.x = x;
		this.y = y;
	}

	public void setSud(Case sud) {
		this.sud = sud;
	}

	public void setEst(Case est) {
		this.est = est;
	}

	//retourne tous les voisins de cette Case
	public List<Case> getVoisin(){
		List<Case> vois = new LinkedList<>();
		for(String v : voisin) {
			switch (v) {
				case ("N"):
					vois.add(nord);
					break;
				case ("S"):
					vois.add(sud);
					break;
				case ("O"):
					vois.add(ouest);
					break;
				case ("E"):
					vois.add(est);
					break;
			}
		}
		return vois;
	}

	//retourne les voisin possible selon une direction 
	public List<Case> getVoisinPossible(String dir){
		List<Case> vois = new LinkedList<>();
		if(isIntersect()) {
			Case next = next(dir);
			vois.add(next);
			return vois;
		}else {
			vois.add(trad(dir));
			vois.add(trad(autreVoisin(dir)));
		}
		return vois;
	}

	public String autreVoisinIntersect(String un, String deux) {
		if(isIntersect()) {
			List<Case> unEtDeux = new LinkedList<>() ;
			unEtDeux.add(trad(un));
			unEtDeux.add(trad(deux));
			List<Case> all = getVoisin();
			all.removeAll(unEtDeux);
			return tradCase(all.get(0));
		}
		return "e";
	}

	public List<String> getDirectionPossible(String entre) {
		List<String> result = new LinkedList<>();
		for (int i = 0; i < voisin.length; i++) {
			if (!entre.equals(voisin[i])) {
				result.add(voisin[i]);
			}
		}
		return result;
	}

	public boolean isIntersect() {
		return voisin.length==3;
	}

	//Uniquement pour ligneDroite/virage : retourne le voisin qui n'est pas "v"
	public String autreVoisin(String v) {
			for(int i=0 ; i<=voisin.length; i++) {
				if(!v.equals(voisin[i])) {
					return voisin[i];
				}
			}
		return null;
	}

	//retourne les voisins qui ne sont pas dans vu
	public List<Case> voisinNonVu(List<Case> vu) {
		List<Case> retour = getVoisin();
		retour.removeAll(vu);
		return retour;
	}

	//traduit une string (O/E/S/N) vers une case
	private Case trad(String dir) {
		switch (dir) {
			case ("N"):
				return nord;
			case ("S"):
				return sud;
			case ("O"):
				return ouest;
			case ("E"):
				return est;
		}
		return null;
	}

	//traduit une case vers un string
	private String tradCase(Case x) {
		if(x.equals(nord)) {return "N";}
		if(x.equals(sud)) {return "S";}
		if(x.equals(ouest)) {return "O";}
		if(x.equals(est)) {return "E";}
		return "e";
	}

	//retourne la prochaine case selon une direction
	// en supposant que la direction implique un voisin differant de null soit existant
	public Case next(String dir) {
		return trad(dir);
	}

	private boolean contain(String orient) {
		for(int i=0;i<voisin.length;i++) {
			if(voisin[i].equals(orient)) {
				return true;
			}
		}
		return false;
	}

	//retourne la direction qui se situe un quart � gauche de dir
	public Case quartGauche(String dir) {
		switch (dir) {
			case ("N"):
				if(contain("O")) {
					return trad("O");
				}
				break;
			case ("S"):
				if(contain("E")) {
					return trad("E");
				}
				break;
			case ("O"):
				if(contain("S")) {
					return trad("S");
				}
				break;
			case ("E"):
				if(contain("N")) {
					return trad("N");
				}
				break;
		}
		return null;
	}

	//retourne la direction qui se situe un quart a droite de dir
	public Case quartDroite(String dir) {
		switch (dir) {
			case ("N"):
				if(contain("E")) {
					return trad("E");
				}
				break;
			case ("S"):
				if(contain("O")) {
					return trad("O");
				}
				break;
			case ("O"):
				if(contain("N")) {
					return trad("N");
				}
				break;
			case ("E"):
				if(contain("S")) {
					return trad("S");
				}
				break;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Case{" +
				"voisin=" + Arrays.toString(voisin) +
				", x=" + x +
				", y=" + y +
				'}';
	}

	//Pour IA: retourne le message a envoyer selon le voisin d'entree et de sortie
	public String messageToSent(String entree,String sortie) {
		if(isIntersect()) {
			//arrive par neutral
			if(quartGauche(entree)!=(null) && quartDroite(entree)!=(null)) {
				if(trad(sortie).equals(quartDroite(entree))) {
					return "l";
				}else {
					return "r";
				}
			}else {
				//arrive par la gauche
				if(quartGauche(entree)!=(null)) {
					if(trad(sortie).equals(quartGauche(entree))) {
						return "r";
					}else {
						return "l";
					}
					//arrive par la droite
				}else {
					if(trad(sortie).equals(trad(inverse(entree)))) {
						return "r";
					}else {
						return "l";
					}
				}
			}
		}else {
			return "s";
		}
	}

	//Pour pilote: retourne la nouvelle orientation du robot apres avoir effectue mouvement en arrivant par entree
	public String newOrientation(String entree,String mouvement) {
		if(isIntersect()) {
			if(mouvement.equals("u")||mouvement.equals("s")) {return "e";}
			//arrive par neutral
			if(quartDroite(entree)!=(null) && quartGauche(entree)!=(null)) {
				if(mouvement.equals("l")) {return tradCase(quartDroite(entree));}
				else {return tradCase(quartGauche(entree));}
			}else {
				//arrive par gauche
				if(quartGauche(entree)!=null) {
					if(mouvement.equals("r")) {return tradCase(quartGauche(entree));}
					//arrive par droite
				}else {
					if(mouvement.equals("l")) {return tradCase(quartDroite(entree));}
				}
				//inverse de entree
				return inverse(entree);
			}
		}else {
			if(mouvement.equals("l")||mouvement.equals("r")) {return "e";}
			return autreVoisin(entree);
		}
	}

	//retourne true si le mouvement est possible
	public boolean mouvementPossible(String entree,String mouvement) {
		return newOrientation(entree,mouvement)!="e";
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Case aCase = (Case) o;
		return x==aCase.getX() && y==aCase.getY() && voisin.equals(aCase.voisin);
	}

}