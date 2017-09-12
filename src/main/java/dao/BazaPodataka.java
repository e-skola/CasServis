package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author zi
 */

public class BazaPodataka {
	
	public static final String URL = "jdbc:sqlite:res/cas.db";
	
	public BazaPodataka() {
	}
	
	public Connection otvoriKonekciju() throws SQLException {
		Connection konekcija = null;
		
		konekcija = DriverManager.getConnection(URL);
		
		return konekcija;
	}
}
