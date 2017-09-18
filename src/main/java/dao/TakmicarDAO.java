package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modeli.Takmicar;

/**
 *
 * @author zi
 */

public class TakmicarDAO {
	
	private BazaPodataka baza;
	
	public TakmicarDAO() {
		baza = new BazaPodataka();
	}
	
	public List<Takmicar> preuzmiSve() {
		List<Takmicar> takmicari = new ArrayList();
		String sql = "SELECT id, ime, prezime, bodovi FROM takmicari";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Takmicar takmicar = new Takmicar();
				takmicar.setId(result.getInt("id"));
				takmicar.setIme(result.getString("ime"));
				takmicar.setPrezime(result.getString("prezime"));
				takmicar.setBodovi(result.getInt("bodovi"));
				takmicari.add(takmicar);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return takmicari;
	}
	
	public Takmicar preuzmi(int id) {
		Takmicar takmicar = null;
		String sql = "SELECT id, ime, prezime, bodovi FROM takmicari WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				takmicar = new Takmicar();
				takmicar.setId(result.getInt("id"));
				takmicar.setIme(result.getString("ime"));
				takmicar.setPrezime(result.getString("prezime"));
				takmicar.setBodovi(result.getInt("bodovi"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return takmicar;
	}
	
	public List<Takmicar> preuzmiRangListu() {
		List<Takmicar> takmicari = new ArrayList();
		String sql = "SELECT id, ime, prezime, bodovi FROM takmicari SORT BY bodovi DESC";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Takmicar takmicar = new Takmicar();
				takmicar.setId(result.getInt("id"));
				takmicar.setIme(result.getString("ime"));
				takmicar.setPrezime(result.getString("prezime"));
				takmicar.setBodovi(result.getInt("bodovi"));
				takmicari.add(takmicar);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return takmicari;
	}
	
	public boolean dodaj(Takmicar takmicar) {
		String sql = "INSERT INTO takmicari(ime, prezime, bodovi) VALUES(?, ?, ?)";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, takmicar.getIme());
			stmt.setString(2, takmicar.getPrezime());
			stmt.setInt(3, takmicar.getBodovi());
			stmt.executeUpdate();
			
			try(ResultSet keys = stmt.getGeneratedKeys()) {
				if(keys.next()) {
					takmicar.setId(keys.getInt(1));
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean izmeni(Takmicar takmicar) {
		String sql = "UPDATE takmicari SET ime=?, prezime=?, bodovi=? WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, takmicar.getIme());
			stmt.setString(2, takmicar.getPrezime());
			stmt.setInt(3, takmicar.getBodovi());
			stmt.setInt(4, takmicar.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean obrisi(Takmicar takmicar) {
		String sql = "DELETE FROM takmicari WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, takmicar.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean obrisiSve() {
		String sql = "DELETE FROM takmicari WHERE";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			stmt.executeUpdate(sql);
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
