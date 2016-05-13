package cz.vscht.fchi.oop;

import java.awt.Graphics;
import org.eclipse.swt.graphics.Rectangle;


/**
 * Trida subjektu hry
 * 
 * @author Vladislav Solodki
 *
 */
public abstract class Subjekt {

	protected double x;      				//	X a Y souradnice, rychlosti xRychlost a yRychlost, sprajt subjektu, double kvuli presnosti pohybu
	protected double y;
	protected double xRychlost;
	protected double yRychlost;
	protected Sprajt sprajt;		

	public Subjekt(String odk, int x, int y) {						// sprajt a pocatecni poloha subjektu
		this.sprajt = KolekceSprajtu.get().ziskSprajt(odk);
		this.x = x;
		this.y = y;
	}
	

	public boolean srazka(Subjekt jiny) {						// zkontroluje, jestli se stala srazka
		Rectangle prvni = new Rectangle((int) x, (int) y, sprajt.getSirka(), sprajt.getVyska());
		Rectangle druhy = new Rectangle((int) jiny.x, (int) jiny.y, jiny.sprajt.getSirka(), jiny.sprajt.getVyska());
		return prvni.intersects(druhy);
	}

	public abstract void srazkaS(Subjekt jiny);					// Oznameni o srazce

	public void kreslit(Graphics gr) {								// kresli subjekt
		sprajt.kreslit(gr, (int) x, (int) y);
	}

	public void pohyb(long cas) {							// obnovi polohu subjektu vzhledem k rychlosti a casu
		x += (cas * xRychlost) / 1000;
		y += (cas * yRychlost) / 1000;
	}
	

	public void delejTo() { 									// logika subjektu

	}

	public void nastVodorHnuti(double dx) {
		this.xRychlost = dx;
	}

	public double ziskVodorHnuti() {
		return xRychlost;
	}

	public void nastSvislHnuti(double dy) {
		this.yRychlost = dy;
	}

	public double ziskSvislHnuti() {
		return yRychlost;
	}

	public int ziskX() {
		return (int) x;
	}

	public int ziskY() {
		return (int) y;
	}
}
