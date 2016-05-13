package cz.vscht.fchi.oop;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Trida sprajtu
 * 
 * @author Vladislav Solodki
 *
 */
public class Sprajt {
	private Image obrazek; // Obrazek pro sprajt

	public Sprajt(Image obrazek) {
		this.obrazek = obrazek; // Vytvori novy sprajt z obrazku
	}

	public int getSirka() {
		return obrazek.getWidth(null);
	}

	public int getVyska() {
		return obrazek.getHeight(null);
	}

	public void kreslit(Graphics gr, int x, int y) { // Kresli obrazek, gr je
													// graficky kontext
		gr.drawImage(obrazek, x, y, null);
	}
}
