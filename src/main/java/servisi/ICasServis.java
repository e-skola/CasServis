package servisi;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import modeli.Materijal;

/**
 *
 * @author zi
 */

@WebService
public interface ICasServis {
	
	@WebMethod
	public List<Materijal> preuzmiMaterijale(int razred, int lekcija);
	
	@WebMethod
	public Materijal preuzmiMaterijal(int id);
}
