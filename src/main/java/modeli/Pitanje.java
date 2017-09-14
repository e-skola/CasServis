package modeli;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import utils.Konverter;

/**
 *
 * @author zi
 */

public class Pitanje {
	
	private int id;
	private int idKviz;
	private int tacanOdgovor;
	private byte[] slika;
	
	public Pitanje() {
	}
	
	public Pitanje(int id) {
		this.id = id;
	}
	
	public Pitanje(int id, int idKviz, int tacanOdgovor) {
		this.id = id;
		this.idKviz = idKviz;
		this.tacanOdgovor = tacanOdgovor;
	}
	
	public void ucitajSliku() throws IOException {
		slika = Konverter.imageToByteArray(ImageIO.read(new File("./res/kviz/" + id + ".jpg")));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdKviz() {
		return idKviz;
	}

	public void setIdKviz(int idKviz) {
		this.idKviz = idKviz;
	}

	public int getTacanOdgovor() {
		return tacanOdgovor;
	}

	public void setTacanOdgovor(int tacanOdgovor) {
		this.tacanOdgovor = tacanOdgovor;
	}

	public byte[] getSlika() {
		return slika;
	}

	public void setSlika(byte[] slika) {
		this.slika = slika;
	}
}
