package test;

import robotLego.*;

public class PiloteTest {
	public static void main(String args[]) {
		Bluetooth b = new Bluetooth();
		Graphe g = new Graphe(null,null,null);
		Pilote hum = new Pilote(1,g,b);
		hum.startInterface();
	}
}
