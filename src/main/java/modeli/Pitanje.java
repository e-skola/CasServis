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
	private int odgovor;
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
	
	public Pitanje(int id, int idKviz, int tacanOdgovor, int odgovor, byte[] slika) {
		this.id = id;
		this.idKviz = idKviz;
		this.tacanOdgovor = tacanOdgovor;
		this.odgovor = odgovor;
		this.slika = slika;
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

	public int getOdgovor() {
		return odgovor;
	}

	public void setOdgovor(int odgovor) {
		this.odgovor = odgovor;
	}

	public byte[] getSlika() {
		return slika;
	}

	public void setSlika(byte[] slika) {
		this.slika = slika;
	}
}
