package it.polito.tdp.ufo.db;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.Year;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Year> getAllYear() {
		
		String sql = "SELECT DISTINCT YEAR(DATETIME) AS year,COUNT(*) AS cnt FROM sighting WHERE country='us' GROUP BY YEAR(DATETIME)" ;
		
		List<Year> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql);	

			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
			
					Year year = new Year(res.getInt("year"), res.getInt("cnt"));
					result.add(year);
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
			
	public List<String> getStati(int year) {
		
		String sql = "SELECT DISTINCT state FROM sighting WHERE country='us' AND Year(datetime)=?" ;
		
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql);	

			st.setInt(1, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
			
					result.add(res.getString("state"));
					
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
    public boolean esisteArco(String s1, String s2,int year) {
		
		final String sql = "SELECT COUNT(*) AS cnt FROM sighting s1, sighting s2 "+
		                   "WHERE Year(s1.datetime)=Year(s2.datetime) AND Year(s1.datetime)=? "+
				           "AND s1.state=? AND s2.state=? AND s1.country='us' AND s2.country='us' "+
		                   "AND s2.datetime>s1.datetime";
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql);	

			st.setInt(1, year);
			st.setString(2, s1);
			st.setString(3, s2);
			
			ResultSet res = st.executeQuery() ;
			
			//flag
			int i = 1;
			
			if(res.next()) {
					
				if ( res.getInt("cnt") > 0 ) 
					i = 0;
			}
			
			conn.close();	
			
			if (i==0) return true;
			else return false ;

		} catch (SQLException e) {
			return false ;
		}
    }
}
