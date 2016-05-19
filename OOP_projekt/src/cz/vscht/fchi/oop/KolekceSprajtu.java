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
 * Trida manipulace zásobami, realizace Singleton třída bude mít jedinou instanci a 
 * poskytne k ní globální přístupový bod
 * 
 * @author Vladislav Solodki
 *
 */
public class KolekceSprajtu {
        private static KolekceSprajtu jedinacek = new KolekceSprajtu(); // jedina instance tridy
						
	public static KolekceSprajtu get() {  // vrati jedinou instanci tridy
		return jedinacek; 			
	}

	private HashMap<String, Sprajt> sprajty = new HashMap<String, Sprajt>();  // pro cache obrazku,
										// spoji odkazy a obrazky
	public Sprajt ziskSprajt(String odk) {			// ziska sprajt z KolekceSpajtu									

		if (sprajty.get(odk) != null) {			// jestli uz mame sprajt v cache, tak vratime ho
			return (Sprajt) sprajty.get(odk);
		}

		BufferedImage zdrojObrazku = null;		// zavadec, ovlada a zpracovava datu obrazku									

		try {
		     URL odkaz = this.getClass().getClassLoader().getResource(odk); // odkaz na obrazky modelu
		     								    // vrati zdroj 
		     if (odkaz == null) {
			chyba("Neni mozne najit soubory " + odk);
			}
		     
		zdrojObrazku = ImageIO.read(odkaz); 	 // nacitame obrazek
		} catch (IOException e) {
			chyba("Nejde spustit " + odk);
		}
		GraphicsConfiguration grafKonfig = 
				GraphicsEnvironment.getLocalGraphicsEnvironment()// vytvori zrychlenou verzi obrazku,
				.getDefaultScreenDevice() 		// kde ulozime nas sprajt
				.getDefaultConfiguration();        // alokujeme grafickou pamet bez pouziti procesoru
		Image obrazek = grafKonfig.createCompatibleImage(zdrojObrazku.getWidth(), zdrojObrazku.getHeight(),
				Transparency.BITMASK);    //pruhlednost
		obrazek.getGraphics().drawImage(zdrojObrazku, 0, 0, null); // kresli nahrany obrazek
		Sprajt sprajt = new Sprajt(obrazek);			// vytvori sprajt, prida ho do cache
		sprajty.put(odk, sprajt);
		return sprajt;
	}

	private void chyba(String zprava) {	//V pripade chyby, objevi se zprava a exit
		System.err.println(zprava);
		System.exit(0);
	}
}
