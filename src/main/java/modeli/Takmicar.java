package modeli;

/**
 *
 * @author zi
 */

public class Takmicar {
	
	private int id;
	private String ime;
	private String prezime;
	private int bodovi;
	
	public Takmicar() {
	}
	
	public Takmicar(int id) {
		this.id = id;
	}
	
	public Takmicar(int id, String ime, String prezime, int bodovi) {
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.bodovi = bodovi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public int getBodovi() {
		return bodovi;
	}

	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}
}
