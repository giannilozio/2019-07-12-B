package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.PD;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Food> getVertici(int porzioni, Map<Integer, Food> idMap) {
		
		String sql = "SELECT DISTINCT f.food_code, f.display_name,AVG(p.saturated_fats) as media " + 
					 "FROM portion p, food f " + 
					 "WHERE f.food_code=p.food_code " + 
					 "GROUP BY f.food_code, f.display_name " + 
					 "HAVING COUNT(p.portion_id)>= ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f = new Food(res.getInt("food_code"),res.getString("display_name"),res.getDouble("media"));
					idMap.put(res.getInt("food_code"), f);
					list.add(f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
		
	}

	public List<PD> getArchi(int porzioni, Map<Integer, Food> idMap) {
		
		String sql = "SELECT p1.food_code, p2.food_code, AVG (p1.saturated_fats)-AVG (p2.saturated_fats) media " + 
				 	 "FROM portion p1, portion p2 " + 
				 	 "WHERE p1.food_code <> p2.food_code " + 
				 	 "AND p1.food_code IN (SELECT DISTINCT p.food_code " + 
				 	 "				     FROM portion p " + 
				 	 "				     GROUP BY p.food_code " + 
				 	 "				     HAVING COUNT(p.food_code) >=?) " + 
				 	 "AND p2.food_code IN (SELECT DISTINCT p.food_code " + 
				 	 "				     FROM portion p " + 
				 	 "				     GROUP BY p.food_code " + 
				 	 "				     HAVING COUNT(p.food_code) >=?) " + 
				 	 "GROUP BY p1.food_code, p2.food_code " + 
				 	 "HAVING AVG (p1.saturated_fats) - AVG(p2.saturated_fats)>0";
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, porzioni);
		st.setInt(2, porzioni);
		List<PD> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
				Food f1 = idMap.get(res.getInt("p1.food_code"));
				Food f2 = idMap.get(res.getInt("p2.food_code"));
				double media = res.getDouble("media");
				
				PD p = new PD(f1,f2,media);
				list.add(p);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		conn.close();
		return list ;

	} catch (SQLException e) {
		e.printStackTrace();
		return null ;
	} 
		
		
	}
}
