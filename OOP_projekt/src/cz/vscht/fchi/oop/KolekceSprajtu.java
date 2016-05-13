package cz.vscht.fchi.oop;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Trida manipulace zásobami
 * 
 * @author Miklain
 *
 */
public class KolekceSprajtu {
	private static KolekceSprajtu jedinacek = new KolekceSprajtu(); 				// jedina instance teto tridy
						
	public static KolekceSprajtu get() {
		return jedinacek; 															// vrati instanci tridy
	}

	private HashMap<String, Sprajt> sprajty = new HashMap<String, Sprajt>(); 		// pro cache obrazku

	public Sprajt ziskSprajt(String odk) {												

		if (sprajty.get(odk) != null) {												// jestli uz mame sprajt v cache, tak vratime ho
			return (Sprajt) sprajty.get(odk);
		}

		BufferedImage zdrojObrazku = null;											

		try {
			URL odkaz = this.getClass().getClassLoader().getResource(odk); 			// odkaz na obrazky modelu

			if (odkaz == null) {
				chyba("Neni mozne najit soubory" + odk);
			}
			zdrojObrazku = ImageIO.read(odkaz); 									// nacitame obrazek
		} catch (IOException e) {
			chyba("Nejde spustit" + odk);
		}
		GraphicsConfiguration gk = 
				GraphicsEnvironment.getLocalGraphicsEnvironment()					// vytvori zrychlenou verzi obrazku,
				.getDefaultScreenDevice() 											// kde ulozime nas sprajt
				.getDefaultConfiguration();
		Image obrazek = gk.createCompatibleImage(zdrojObrazku.getWidth(), zdrojObrazku.getHeight(),
				Transparency.BITMASK);
		obrazek.getGraphics().drawImage(zdrojObrazku, 0, 0, null);					// kresli obrazek v zrychlenou verzi
		Sprajt sprajt = new Sprajt(obrazek);										// vytvori sprajt, prida ho do cache a vrati
		sprajty.put(odk, sprajt);
		return sprajt;
	}

	private void chyba(String zprava) {					//	V pripade chyby, objevi se zprava a exit
		System.err.println(zprava);
		System.exit(0);
	}
}
