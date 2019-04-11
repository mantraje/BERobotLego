package robotLego;

public abstract class Robot {
	private int currentPos;
	private int previousPos;
	private int initPos;
	private Graphe graphe;

	public Robot(int init, Graphe g) {
		this.initPos = init;
		this.currentPos = init;
		this.previousPos = -1;
		this.graphe = g;
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public int getPreviousPos() {
		return previousPos;
	}

	public int getInitPos() {
		return initPos;
	}

	public Graphe getGraphe() {
		return graphe;
	}

	// construit le message à envoyer au robot
	private String construct(boolean take, boolean drop, String action) {
		String toSent = action;
		if (!(take || drop)) {
			toSent += "\n";
			return "20" + toSent;
		}
		if (take) {
			toSent += "t\n";
		}
		if (drop) {
			toSent += "d\n";
		}
		return "30" + toSent;
	}

	// retourne la commande straight et mAj des positions après déplacement
	public String toutDroit(boolean take, boolean drop) {
		int finalPos = graphe.toutDroit(previousPos, currentPos);
		if (take) {
			graphe.rammasserVictime(finalPos);
		}
		previousPos = currentPos;
		currentPos = finalPos;
		return construct(take, drop, "s");
	}

	// retourne la commande turn around et mAj des positions après déplacement
	public String demiTour(boolean take, boolean drop) {
		int finalPos = previousPos;
		if (take) {
			graphe.rammasserVictime(previousPos);
		}
		previousPos = currentPos;
		currentPos = finalPos;
		return construct(take, drop, "u");
	}

	// retourne la commande right et mAj des positions après déplacement
	public String tournerDroite(boolean take, boolean drop) {
		int finalPos = graphe.tournerDroite(previousPos, currentPos);
		if (take) {
			graphe.rammasserVictime(finalPos);
		}
		previousPos = currentPos;
		currentPos = finalPos;
		return construct(take, drop, "r");
	}

	// retourne la commande left et mAj des positions après déplacement
	public String tournerGauche(boolean take, boolean drop) {
		int finalPos = graphe.tournerGauche(previousPos, currentPos);
		if (take) {
			graphe.rammasserVictime(finalPos);
		}
		previousPos = currentPos;
		currentPos = finalPos;
		return construct(take, drop, "l");
	}

}
