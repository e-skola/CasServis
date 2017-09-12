package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modeli.Materijal;

/**
 *
 * @author zi
 */

public class MaterijalDAO {
	
	private BazaPodataka baza;
	
	public MaterijalDAO() {
		baza = new BazaPodataka();
	}
	
	public List<Materijal> preuzmiSve() {
		List<Materijal> materijali = new ArrayList();
		String sql = "SELECT id, razred, lekcija FROM materijali";
		
		try(Connection con = baza.otvoriKonekciju();
				Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()) {
				Materijal materijal = new Materijal();
				materijal.setId(result.getInt("id"));
				materijal.setRazred(result.getInt("razred"));
				materijal.setLekcija(result.getInt("lekcija"));
				materijali.add(materijal);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return materijali;
	}
	
	public Materijal preuzmi(int id) {
		Materijal materijal = null;
		String sql = "SELECT id, razred, lekcija FROM materijali WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, id);
			
			ResultSet result = stmt.executeQuery(sql);
			if(result.next()) {
				materijal = new Materijal();
				materijal.setId(result.getInt("id"));
				materijal.setRazred(result.getInt("razred"));
				materijal.setLekcija(result.getInt("lekcija"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return materijal;
	}
	
	public List<Materijal> preuzmi(int razred, int lekcija) {
		List<Materijal> materijali = new ArrayList();
		String sql = "SELECT id, razred, lekcija FROM materijali WHERE razred=? AND lekcija=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, razred);
			stmt.setInt(2, lekcija);
			
			ResultSet result = stmt.executeQuery();
			while(result.next()) {
				Materijal materijal = new Materijal();
				materijal.setId(result.getInt("id"));
				materijal.setRazred(result.getInt("razred"));
				materijal.setLekcija(result.getInt("lekcija"));
				materijali.add(materijal);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return materijali;
	}
	
	public boolean dodaj(Materijal materijal) {
		String sql = "INSERT INTO materijali(razred, lekcija) VALUES(?, ?)";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, materijal.getRazred());
			stmt.setInt(2, materijal.getLekcija());
			stmt.executeUpdate();
			
			try(ResultSet keys = stmt.getGeneratedKeys()) {
				if(keys.next()) {
					materijal.setId(keys.getInt(1));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean izmeni(Materijal materijal) {
		String sql = "UPDATE materijal SET razred=?, lekcija=? WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, materijal.getRazred());
			stmt.setInt(2, materijal.getLekcija());
			stmt.setInt(3, materijal.getId());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean obrisi(Materijal materijal) {
		String sql = "DELETE FROM materijal WHERE id=?";
		
		try(Connection con = baza.otvoriKonekciju();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, materijal.getId());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
