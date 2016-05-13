package cz.vscht.fchi.oop;

/**
 * Subjekt hrace
 * 
 * @author Vladislav Solodki
 *
 */
public class Hrac extends Subjekt {

	private Hra hra;

	public void pohyb(long cas) {		
		if ((xRychlost < 0) && (x < 5)) { 				// jestli se hrac dostal leve strany a prave strany, zastavi se 
			return;
		}
		if ((xRychlost > 0) && (x > 1020)) {
			return;
		}
		super.pohyb(cas);
	}

	public void srazkaS(Subjekt tento) {		// jestli se hrac srazil s vetrelcem - konec hry
		if (tento instanceof Vetrelec) {
			hra.oznSmrt();
		}
	}

	public Hrac(Hra hra, String odk, int x, int y) {			//konstruktor,	odkaz na sprajt, X a Y souradnice pocatecni polohy
		super(odk, x, y);
		this.hra = hra;
	}
}
