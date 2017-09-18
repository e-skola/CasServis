package servisi;

import dao.KvizDAO;
import dao.MaterijalDAO;
import dao.PitanjeDAO;
import dao.TakmicarDAO;
import java.io.IOException;
import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import modeli.Kviz;
import modeli.Materijal;
import modeli.Pitanje;
import modeli.Takmicar;

/**
 *
 * @author zi
 */

@WebService(endpointInterface = "servisi.ICasServis")
public class CasServis implements ICasServis {
	
	private MaterijalDAO materijalDAO;
	
	private boolean kvizPokrenut;
	private Kviz kviz;
	private List<Pitanje> pitanja;
	
	private KvizDAO kvizDAO;
	private PitanjeDAO pitanjeDAO;
	private TakmicarDAO takmicarDAO;

	public CasServis() {
		materijalDAO = new MaterijalDAO();
		kvizDAO = new KvizDAO();
		pitanjeDAO = new PitanjeDAO();
		takmicarDAO = new TakmicarDAO();
	}
	
	@Override
	public List<Materijal> preuzmiMaterijale(int razred, int lekcija) {
		List<Materijal> materijali = materijalDAO.preuzmi(razred, lekcija);
		
		for(Materijal materijal : materijali) {
			try {
				materijal.ucitajSliku();
			} catch (IOException ex) {}
		}
		
		return materijali;
	}
	
	@Override
	public Materijal preuzmiMaterijal(int id) {
		Materijal materijal = materijalDAO.preuzmi(id);
		
		try {
			materijal.ucitajSliku();
		} catch(Exception ex) {
			throw new WebServiceException(ex.getMessage());
		}
		
		return materijal;
	}

	@Override
	public boolean isKvizPokrenut() {
		return kvizPokrenut;
	}
	
	@Override
	public boolean prijavaTakmicara(Takmicar takmicar) {
		return takmicarDAO.dodaj(takmicar);
	}
	
	@Override
	public int preuzmiBrojPitanja() {
		return pitanja.size();
	}

	@Override
	public Pitanje preuzmiPitanje(int index) {
		if(index >= pitanja.size()) {
			throw new WebServiceException("Pitanje sa zadatim indeksom ne posotoji");
		}
		
		return pitanja.get(index);
	}

	@Override
	public boolean proveriResenje(Takmicar takmicar, Pitanje pitanje) {
		if(pitanje.getOdgovor() <= 0)
			return false;
		
		if(pitanje.getOdgovor() == pitanje.getTacanOdgovor()) {
			takmicar.setBodovi(takmicar.getBodovi() + 10);
			return true;
		} else {
			takmicar.setBodovi(takmicar.getBodovi() - 5);
		}
		
		takmicarDAO.izmeni(takmicar);
		
		return false;
	}

	@Override
	public List<Takmicar> preuzmiRangListu() {
		return takmicarDAO.preuzmiRangListu();
	}
	
	public void pokreniKviz(Kviz kviz) {
		this.kviz = kviz;
		pitanja = pitanjeDAO.preuzmi(kviz);
		kvizPokrenut = true;
	}
	
	public void zaustaviKviz() {
		kvizPokrenut = false;
	}
}
