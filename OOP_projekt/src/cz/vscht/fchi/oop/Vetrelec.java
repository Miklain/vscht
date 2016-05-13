package cz.vscht.fchi.oop;

/**
 * Subjekt vetrelce
 * 
 * @author Vladislav Solodki
 *
 */
public class Vetrelec extends Subjekt {

	private double rychlost = 100; 								// rychlost vetrelce v pixelech
	private Hra hra;

	public Vetrelec(Hra hra, String odk, int x, int y) {  		//konstruktor, odkaz sprajtu vetrelce, pocatecni souradnice X a Y
		super(odk, x, y);
		this.hra = hra;
		xRychlost = -rychlost;
	}

	public void pohyb(long cas) {  			// pohyb vetrelcu vlevo a vpravo, cas od posledniho pohybu
		if ((xRychlost < 0) && (x < 1)) {
			hra.obnLogiku(); 				// obnovit logiku vetrelce
		}
		if ((xRychlost > 0) && (x > 980)) {
			hra.obnLogiku();
		}
		super.pohyb(cas);
	}

	public void delejTo() { 						// logika vetrelce, pohyb dolu
		xRychlost = -xRychlost;
		y += 3;
		if (y > 950) {
			hra.oznSmrt();
		}
	}

	public void srazkaS(Subjekt jiny) {
		// TODO Auto-generated method stub
	}
}
