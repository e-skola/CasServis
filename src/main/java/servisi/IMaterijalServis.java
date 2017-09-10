package servisi;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author zi
 */

@WebService
public interface IMaterijalServis {
	
	@WebMethod
	public String test();
}
