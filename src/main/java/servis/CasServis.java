package servis;

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

@WebService(endpointInterface = "servis.ICasServis")
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
	public Takmicar prijavaTakmicara(Takmicar takmicar) {
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
	public Takmicar proveriResenje(Takmicar takmicar, Pitanje pitanje) {
		if(pitanje.getOdgovor() > 0) {
			if(pitanje.getOdgovor() == pitanje.getTacanOdgovor()) {
				takmicar.setBodovi(takmicar.getBodovi() + 10);
			} else {
				takmicar.setBodovi(takmicar.getBodovi() - 5);
			}
		}
		takmicarDAO.izmeni(takmicar);
		
		return takmicar;
	}

	@Override
	public List<Takmicar> preuzmiRangListu() {
		return takmicarDAO.preuzmiRangListu();
	}
	
	public void pokreniKviz(Kviz kviz) throws IOException {
		this.kviz = kviz;
		pitanja = pitanjeDAO.preuzmi(kviz);
		takmicarDAO.obrisiSve();
		for(Pitanje pitanje : pitanja) {
			pitanje.ucitajSliku();
		}
		kvizPokrenut = true;
	}
	
	public void zaustaviKviz() {
		kvizPokrenut = false;
	}
}
