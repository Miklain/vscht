package cz.vscht.fchi.oop;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
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
 *
 */
public class Hra extends Canvas {

	private static final long serialVersionUID = 1L;					
	private boolean hraSpustena = true;
	private ArrayList<Subjekt> subjekty = new ArrayList<Subjekt>();		// seznam subjektu hry
	private ArrayList<Subjekt> odstrSezn = new ArrayList<Subjekt>();	// seznam odstranenych subjektu
	private Subjekt hrac;
	private BufferStrategy strategie;									//moznost akselerace pohybu sprajtu
	private double rychlost = 300;										// rychlost hrace v pixelech
	private long poslVyst = 0;											// cas posledniho vystrelu
	private long interval = 100;										// interval vystrelu v ms
	private int pocetVetr;
	private String zprava = "";
	private boolean cekaStisk = true;
	private boolean leveStisk = false;
	private boolean praveStisk = false;
	private boolean vystrel = false;
	private boolean logCyklus = false;									//promenna cyklu pro logiku objektu
	
	private class OvladacKlaves extends KeyAdapter {					// ovladac klaves

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

		public void keyTyped(KeyEvent e) {

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
		JFrame kontejner = new JFrame("Vesmirni vetrelci aka Space Invaders");			
		JPanel panel = (JPanel) kontejner.getContentPane();
		panel.setPreferredSize(new Dimension(1024, 768));
		panel.setLayout(null);
		setBounds(0, 0, 1024, 768);													// nastavi rozmery a prida this
		panel.add(this);
		setIgnoreRepaint(true);
		kontejner.pack();															// objevi se okno
		kontejner.setResizable(false);
		kontejner.setVisible(true);
		kontejner.addWindowListener(new WindowAdapter() {							// listener pro zavirani okna
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addKeyListener(new OvladacKlaves());										// listener pro stisknuti klaves
		requestFocus();
		createBufferStrategy(5);
		strategie = getBufferStrategy();
		inicSubj();																	//vyzva inicializaci subjektu
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
				Subjekt vetrelec = new Vetrelec(this, "util/vetrelec.png", 100 + (sloupec * 50), (50) + radek * 30);
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
				subjekt.nastVodorHnuti(subjekt.ziskVodorHnuti() * 1.03);    // vetrelci se zrychluji o 1%
			}
		}
	}

	public void zkusVystrel() {
		if (System.currentTimeMillis() - poslVyst < interval) {
			return;
		}

		poslVyst = System.currentTimeMillis();
		Vystrel vystrel = new Vystrel(this, "util/vystrel.png", hrac.ziskX() + 10, hrac.ziskY() - 30);
		subjekty.add(vystrel);
	}

	public void cyklus() {													// hlavni cyklus hry
		long casPoslCykl = System.currentTimeMillis();

		while (hraSpustena) {

			long cas = System.currentTimeMillis() - casPoslCykl;
			casPoslCykl = System.currentTimeMillis();

			Graphics2D grafika = (Graphics2D) strategie.getDrawGraphics();
			grafika.setColor(Color.DARK_GRAY);
			grafika.fillRect(0, 0, 1024, 768);

			if (!cekaStisk) {
				for (int i = 0; i < subjekty.size(); i++) {
					Subjekt subjekt = (Subjekt) subjekty.get(i);

					subjekt.pohyb(cas);
				}
			}
			for (int i = 0; i < subjekty.size(); i++) {
				Subjekt subjekt = (Subjekt) subjekty.get(i);

				subjekt.kreslit(grafika);
			}
			for (int p = 0; p < subjekty.size(); p++) {
				for (int d = p + 1; d < subjekty.size(); d++) {
					Subjekt prvni = (Subjekt) subjekty.get(p);
					Subjekt druhy = (Subjekt) subjekty.get(d);

					if (prvni.srazka(druhy)) {
						prvni.srazkaS(druhy);
						druhy.srazkaS(prvni);
					}
				}
			}
			subjekty.removeAll(odstrSezn);
			odstrSezn.clear();

			if (logCyklus) {
				for (int i = 0; i < subjekty.size(); i++) {
					Subjekt subjekt = (Subjekt) subjekty.get(i);
					subjekt.delejTo();
				}

				logCyklus = false;
			}
			if (cekaStisk) {
				grafika.setColor(Color.WHITE);
				grafika.drawString(zprava, 450 , 250);
				grafika.drawString("POUZIJTE PRAVOU KLAVESU pro pohyb VPRAVO, LEVOU KLAVESU pro pohyb VLEVO,  MEZERNIK pro VYSTREL",200, 500 );
			}
			grafika.dispose();
			strategie.show();
			hrac.nastVodorHnuti(0);

			if ((leveStisk) && (!praveStisk)) {
				hrac.nastVodorHnuti(-rychlost);
			} else if ((praveStisk) && (!leveStisk)) {
				hrac.nastVodorHnuti(rychlost);
			}
			if (vystrel) {
				zkusVystrel();
			}
			try {
				Thread.sleep(10);
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
