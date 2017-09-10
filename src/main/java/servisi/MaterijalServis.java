package servisi;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 *
 * @author zi
 */

@WebService(endpointInterface = "servis.IMaterijalServis")
public class MaterijalServis implements IMaterijalServis {
	
	@Override
	public String test() {
		return "Test!";
	}
	
	public static void main(String args[]) {
		Endpoint.publish("http://0.0.0.0:5001/Cas", new MaterijalServis());
	}
}
