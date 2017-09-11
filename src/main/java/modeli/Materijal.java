package modeli;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import utils.Konverter;

/**
 *
 * @author zi
 */

public class Materijal {
	
	private int id;
	private int razred;
	private int lekcija;
	private byte[] slika;
	
	public Materijal() {
	}
	
	public Materijal(int id) {
		this.id = id;
	}
	
	public Materijal(int id, int razred, int lekcija, byte[] slika) {
		this.id = id;
		this.razred = razred;
		this.lekcija = lekcija;
		this.slika = slika;
	}
	
	public void ucitajSliku() throws IOException {
		slika = Konverter.imageToByteArray(ImageIO.read(new File("./res/materijal/" + id + ".jpg")));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRazred() {
		return razred;
	}

	public void setRazred(int razred) {
		this.razred = razred;
	}

	public int getLekcija() {
		return lekcija;
	}

	public void setLekcija(int lekcija) {
		this.lekcija = lekcija;
	}

	public byte[] getSlika() {
		return slika;
	}

	public void setSlika(byte[] slika) {
		this.slika = slika;
	}
}
