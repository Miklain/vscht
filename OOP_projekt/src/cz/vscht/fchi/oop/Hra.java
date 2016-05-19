package cz.vscht.fchi.oop;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Hlavni trida hry
 * 
 * @author Vladislav Solodki
 */
public class Hra extends Canvas {

    private static final long serialVersionUID = 1L;
    private boolean hraSpustena = true;
    private ArrayList<Subjekt> subjekty = new ArrayList<Subjekt>(); // dynamicke pole subjektu, zvetsuje se automaticky
    private ArrayList<Subjekt> odstrSezn = new ArrayList<Subjekt>();//  seznam subjektu pro odstraneni
    private Subjekt hrac;
    private BufferStrategy strategie; // moznost zrychleni pohybu sprajtu
    private double rychlost = 300; // rychlost hrace v pixelech
    private long poslVyst = 0; // cas posledniho vystrelu
    private long interval = 100; // interval vystrelu v ms
    private int pocetVetr;
    private String zprava = ""; // zpravy hry
    private boolean cekaStisk = true;
    private boolean leveStisk = false;
    private boolean praveStisk = false;
    private boolean vystrel = false;
    private boolean logCyklus = false; // promenna cyklu pro logiku objektu

    private class OvladacKlaves extends KeyAdapter { // ovladac klaves

	private int pocetStisk = 1;

	public void keyPressed(KeyEvent e) {
	    if (cekaStisk) {
		return;
	    }

	    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		leveStisk = true;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		praveStisk = true;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		vystrel = true;
	    }
	}

	public void keyReleased(KeyEvent e) {
	    if (cekaStisk) {
		return;
	    }

	    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		leveStisk = false;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		praveStisk = false;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		vystrel = false;
	    }
	}

	public void keyTyped(KeyEvent e) {			// zacneme hru po stisknuti

	    if (cekaStisk) {
		if (pocetStisk == 1) {

		    cekaStisk = false;
		    startHry();
		    pocetStisk = 0;
		} else {
		    pocetStisk++;
		}
	    }
	    if (e.getKeyChar() == 27) {
		System.exit(0);
	    }
	}
    }

    public Hra() {
	JFrame kontejner = new JFrame("Vesmirni Vetrelci");
	JPanel panel = (JPanel) kontejner.getContentPane(); 	// tabulka obsahu
	panel.setPreferredSize(new Dimension(1024, 768));
	panel.setLayout(null);
	setBounds(0, 0, 1024, 768); // nastavi rozmery a prida this
	panel.add(this);
	setIgnoreRepaint(true);		// zakazka premalovani 
	kontejner.pack(); 		// objevi se okno
	kontejner.setResizable(false);
	kontejner.setVisible(true);
	kontejner.addWindowListener(new WindowAdapter() { // listener pro
							  // zavirani okna
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	addKeyListener(new OvladacKlaves()); // poslouchac pro stisknuti klaves
	requestFocus();				
	createBufferStrategy(4);		// 4 vyrovnavace pro zrychleni
	strategie = getBufferStrategy();
	inicSubj(); // vyzva inicializaci subjektu
    }

    private void startHry() {
	subjekty.clear();
	inicSubj();
	leveStisk = false;
	praveStisk = false;
	vystrel = false;
    }

    private void inicSubj() {
	hrac = new Hrac(this, "util/hrac.png", 500, 740);
	subjekty.add(hrac);

	pocetVetr = 0;
	for (int radek = 0; radek < 10; radek++) {
	    for (int sloupec = 0; sloupec < 18; sloupec++) {
		Subjekt vetrelec = new Vetrelec(this, "util/vetrelec.png", 70 + (sloupec * 50), 50 + radek * 30);
		subjekty.add(vetrelec);
		pocetVetr++;
	    }
	}
    }

    public void obnLogiku() {
	logCyklus = true;
    }

    public void odstrSubj(Subjekt subjekt) {
	odstrSezn.add(subjekt);
    }

    public void oznSmrt() {
	zprava = "Zkuste jeste jednou";
	cekaStisk = true;
    }

    public void oznVyhra() {
	zprava = "Vyhral jste, gratulujeme!";
	cekaStisk = true;
    }

    public void oznZabVetr() {
	pocetVetr--;
	if (pocetVetr == 0) {
	    oznVyhra();
	}

	for (int i = 0; i < subjekty.size(); i++) {
	    Subjekt subjekt = (Subjekt) subjekty.get(i);
	    if (subjekt instanceof Vetrelec) {
		subjekt.nastVodorHnuti(subjekt.ziskVodorHnuti() * 1.01); // vetrelci
									 // se
									 // zrychluji
									 // o %
	    }
	}
    }

    public void zkusVystrel() {
	if (System.currentTimeMillis() - poslVyst < interval) {      // korekce frekvence
	    return;
	}
	poslVyst = System.currentTimeMillis();
	Vystrel vystrel = new Vystrel(this, "util/vystrel.png", hrac.ziskX() + 10, hrac.ziskY() - 30);
	subjekty.add(vystrel);
    }

    public void cyklus() { // hlavni cyklus hry
	long casPoslCykl = System.currentTimeMillis();

	while (hraSpustena) {

	    long cas = System.currentTimeMillis() - casPoslCykl;  // uplynuly cas
	    casPoslCykl = System.currentTimeMillis();

	    Graphics2D grafika = (Graphics2D) strategie.getDrawGraphics();   // Sehnat graficky kontext pro zrychlenou plochu
	    grafika.setColor(Color.DARK_GRAY);
	    grafika.fillRect(0, 0, 1024, 768);

	    if (!cekaStisk) {							// cyklus pro pohyb
		for (int i = 0; i < subjekty.size(); i++) {
		    Subjekt subjekt = (Subjekt) subjekty.get(i);

		    subjekt.pohyb(cas);
		}
	    }
	    for (int i = 0; i < subjekty.size(); i++) {				// cyklus pro kresleni
		Subjekt subjekt = (Subjekt) subjekty.get(i);

		subjekt.kreslit(grafika);
	    }
	    
	    for (int p = 0; p < subjekty.size(); p++) {				// cyklus pro srazky
		for (int d = p + 1; d < subjekty.size(); d++) {
		    Subjekt prvni = (Subjekt) subjekty.get(p);
		    Subjekt druhy = (Subjekt) subjekty.get(d);

		    if (prvni.srazka(druhy)) {
			prvni.srazkaS(druhy);
			druhy.srazkaS(prvni);
		    }
		}
	    }
	    subjekty.removeAll(odstrSezn);				// vyprazdni nutne subjekty
	    odstrSezn.clear();

	    if (logCyklus) {					// cyklus pro logiku subjektu v pripade, ze hra oznamila o tom
		for (int i = 0; i < subjekty.size(); i++) {
		    Subjekt subjekt = (Subjekt) subjekty.get(i);
		    subjekt.logVetr();
		}
		logCyklus = false;
	    }
	    
	    if (cekaStisk) {						// zpravy, kdy ceka na stisknuti klaves
		grafika.setColor(Color.RED);
		grafika.setFont(new Font("Garamond", Font.BOLD, 17));
		grafika.drawString(zprava, 350, 250);
		grafika.drawString(
			"POUZIJTE PRAVOU KLAVESU pro pohyb VPRAVO, LEVOU KLAVESU pro pohyb VLEVO,  MEZERNIK pro VYSTREL",
			50, 500);
	    }
	    grafika.dispose();				// ukoncili jsme kresleni, uklidi grafiku a prepne vyrovnavace
	    strategie.show();
	    
	    hrac.nastVodorHnuti(0);				// jesli hrac se nepohybuje
	    if ((leveStisk) && (!praveStisk)) {			// stiskneme levo -> leva strana a naopak
		hrac.nastVodorHnuti(-rychlost);
	    } else if ((praveStisk) && (!leveStisk)) {
		hrac.nastVodorHnuti(rychlost);
	    }
	    if (vystrel) {					// zkusime vystrelit
		zkusVystrel();
	    }
	   try {
		Thread.sleep(100);			// vyrovna cas a je to pauza pred spustenim
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public static void main(String[] argc) {
	Hra hra = new Hra();
	hra.cyklus();
    }
}
