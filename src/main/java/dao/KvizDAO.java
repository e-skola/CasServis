package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modeli.Kviz;

/**
 *
 * @author zi
 */

public class KvizDAO {
	
	private BazaPodataka baza;
	
	public KvizDAO() {
		baza = new BazaPodataka();
	}
	
	public List<Kviz> preuzmiSve() {
		List<Kviz> kvizovi = new ArrayList();
		String sql = "SELECT id, naziv FROM kvizovi";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Kviz kviz = new Kviz();
				kviz.setId(result.getInt("id"));
				kviz.setNaziv(result.getString("naziv"));
				kvizovi.add(kviz);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		return kvizovi;
	}
	
	public Kviz preuzmi(int id) {
		Kviz kviz = null;
		String sql = "SELECT id, naziv FROM kvizovi WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
				kviz = new Kviz();
				kviz.setId(result.getInt("id"));
				kviz.setNaziv(result.getString("naziv"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return kviz;
	}
	
	public boolean dodaj(Kviz kviz) {
		String sql = "INSERT INTO kvizovi(naziv) VALUES(?)";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, kviz.getNaziv());
			stmt.executeUpdate();
			
			try(ResultSet keys = stmt.getGeneratedKeys()) {
				if(keys.next()) {
					kviz.setId(keys.getInt(1));
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean izmeni(Kviz kviz) {
		String sql = "UPDATE kvizovi SET naziv=? WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, kviz.getNaziv());
			stmt.setInt(2, kviz.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean obrisi(Kviz kviz) {
		String sql = "DELETE FROM kvizovi WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, kviz.getId());
			stmt.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
