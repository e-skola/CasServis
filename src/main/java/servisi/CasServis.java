package servisi;

import dao.MaterijalDAO;
import java.io.IOException;
import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import modeli.Materijal;

/**
 *
 * @author zi
 */

@WebService(endpointInterface = "servisi.ICasServis")
public class CasServis implements ICasServis {
	
	private MaterijalDAO materijalDAO;

	public CasServis() {
		materijalDAO = new MaterijalDAO();
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
}
