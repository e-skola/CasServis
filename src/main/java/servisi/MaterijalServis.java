package servisi;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;
import modeli.Materijal;

/**
 *
 * @author zi
 */

@WebService(endpointInterface = "servisi.IMaterijalServis")
public class MaterijalServis implements IMaterijalServis {
	
	@Override
	public List<Materijal> preuzmiMaterijale(int razred, int lekcija) {
		List<Materijal> materijali = new ArrayList();
		
		try {
			materijali.add(new Materijal(1));
			materijali.add(new Materijal(2));
			materijali.get(0).ucitajSliku();
			materijali.get(1).ucitajSliku();
		} catch(Exception ex) {
			throw new WebServiceException(ex.getMessage());
		}
		
		return materijali;
	}
	
	@Override
	public Materijal preuzmiMaterijal(int id) {
		Materijal materijal = new Materijal(1);
		
		try {
			materijal.ucitajSliku();
		} catch(Exception ex) {
			throw new WebServiceException(ex.getMessage());
		}
		
		return materijal;
	}
}
