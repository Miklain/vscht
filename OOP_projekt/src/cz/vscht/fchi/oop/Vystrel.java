package cz.vscht.fchi.oop;

/**
 * Subjekt vystrelu
 * 
 * @author Miklain
 *
 */
public class Vystrel extends Subjekt {

	private double rychlost = -600;  							// rychlost vystrelu
	private Hra hra;
	private boolean uzity;										// status uzita zbran nebo ne, jestli vetrelec zmizel

	public Vystrel(Hra hra, String sprajt, int x, int y) {		// konstruktor, sprajt vystrelu a jeho pocatecni poloha
		super(sprajt, x, y);
		this.hra = hra;
		yRychlost = rychlost;
	}

	public void pohyb(long cas) {								// cas od posledniho vystrelu, jestli se dostal do y<1, tak se odstrani
		super.pohyb(cas);
		if (y < 1) {
			hra.odstrSubj(this);
		}
	}

	public void srazkaS(Subjekt tento) {						// srazka s vetrelcem a jeho zabiti
		if (uzity) {
			return;
		}
		if (tento instanceof Vetrelec) {
			hra.odstrSubj(this);
			hra.odstrSubj(tento);
			hra.oznZabVetr();
			uzity = true;
		}
	}

}
