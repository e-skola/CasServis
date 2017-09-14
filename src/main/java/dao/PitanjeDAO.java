package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modeli.Kviz;
import modeli.Pitanje;

/**
 *
 * @author zi
 */

public class PitanjeDAO {
	
	private BazaPodataka baza;
	
	public PitanjeDAO() {
		baza = new BazaPodataka();
	}
	
	public List<Pitanje> preuzmiSve() {
		List<Pitanje> pitanja = new ArrayList();
		String sql = "SELECT id, id_kviz, tacan_odgovor FROM pitanja";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Pitanje pitanje = new Pitanje();
				pitanje.setId(result.getInt("id"));
				pitanje.setIdKviz(result.getInt("id_kviz"));
				pitanje.setTacanOdgovor(result.getInt("tacan_odgovor"));
				pitanja.add(pitanje);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return pitanja;
	}
	
	public Pitanje preuzmi(int id) {
		Pitanje pitanje = null;
		String sql = "SELECT id, id_kviz, tacan_odgovor FROM pitanja WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				pitanje = new Pitanje();
				pitanje.setId(result.getInt("id"));
				pitanje.setIdKviz(result.getInt("id_kviz"));
				pitanje.setTacanOdgovor(result.getInt("tacan_odgovor"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return pitanje;
	}
	
	public List<Pitanje> preuzmi(Kviz kviz) {
		List<Pitanje> pitanja = new ArrayList();
		String sql = "SELECT id, id_kviz, tacan_odgovor FROM pitanja WHERE id_kviz=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, kviz.getId());
			
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Pitanje pitanje = new Pitanje();
				pitanje.setId(result.getInt("id"));
				pitanje.setIdKviz(result.getInt("id_kviz"));
				pitanje.setTacanOdgovor(result.getInt("tacan_odgovor"));
				pitanja.add(pitanje);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return pitanja;
	}
	
	public boolean dodaj(Pitanje pitanje) {
		String sql = "INSERT INTO pitanja(id_kviz, tacan_odgovor) VALUES(?, ?)";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, pitanje.getIdKviz());
			stmt.setInt(2, pitanje.getTacanOdgovor());
			stmt.executeUpdate();
			
			try(ResultSet keys = stmt.getGeneratedKeys()) {
				if(keys.next()) {
					pitanje.setId(keys.getInt(1));
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean izmeni(Pitanje pitanje) {
		String sql = "UPDATE pitanja SET id_kviz=?, tacan_odgovor=? WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, pitanje.getIdKviz());
			stmt.setInt(2, pitanje.getTacanOdgovor());
			stmt.setInt(3, pitanje.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean obrisi(Pitanje pitanje) {
		String sql = "DELETE FROM pitanja WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, pitanje.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
