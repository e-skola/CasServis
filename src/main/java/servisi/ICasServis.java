package servisi;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import modeli.Materijal;
import modeli.Pitanje;
import modeli.Takmicar;

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
	
	@WebMethod
	public boolean isKvizPokrenut();
	
	@WebMethod
	public boolean prijavaTakmicara(Takmicar takmicar);
	
	@WebMethod
	public int preuzmiBrojPitanja();
	
	@WebMethod
	public Pitanje preuzmiPitanje(int index);
	
	@WebMethod
	public boolean proveriResenje(Takmicar takmicar, Pitanje pitanje);
	
	@WebMethod
	public List<Takmicar> preuzmiRangListu();
}
