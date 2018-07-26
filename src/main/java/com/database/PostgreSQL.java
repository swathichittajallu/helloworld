package com.database;

import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.service.Logging;

public class PostgreSQL {
	
	public Connection connection() {
		Connection con=null;
		ResultSet rs = null;
		Logging log;
		try {
			Class.forName("org.postgresql.Driver");  
        	con=DriverManager.getConnection(  
        	"jdbc:postgresql://10.138.77.113:5432/postgres","postgres","postgres");
			return con;
		}
		 catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		return con;
	}

	public ArrayList executeCommand(Connection con,String command) {
    	ArrayList al = new ArrayList();
		try {
        	Statement stmt=con.createStatement();  
        	ResultSet rs=stmt.executeQuery(command);  
        	Logging.log.setLevel(Level.DEBUG);
        	Logging.log.debug("Executing the command:" +command);
        	while(rs.next()) {
        		//System.out.println(rs.getString(1)+ " "+rs.getString(2) );
        		al.add(rs.getString(1).trim());
        	con.close();
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return al;
	}
	
	public void insert(Connection con, String tenantid, String servicename,String status ) {
		try {
		Statement stmt = con.createStatement();
		//String insertquery = "INSERT INTO service(tenantid, servicename, status) VALUES('"+tenantid+"','"+servicename+"')";
		String insertquery = "INSERT INTO service(tenantid, servicename, status) VALUES('"+tenantid+"','"+servicename+"','"+status+"')";
			stmt.executeUpdate(insertquery);
			stmt.close();
			con.close();
			System.out.println("Insertion successful");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(Connection con,String servicename) {
		try {
			Statement stmt = con.createStatement();
			String insertquery = "DELETE FROM service WHERE servicename='"+servicename+"'";
				stmt.executeUpdate(insertquery);
				stmt.close();
				con.close();
				System.out.println("Deletion successful");
			} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	public void update(Connection con,String servicename,String status) {
		try {
			Statement stmt = con.createStatement();
			String updatequery = "UPDATE service SET status="+status+" WHERE servicename='"+servicename+"'";
				stmt.executeUpdate(updatequery);
				stmt.close();
				con.close();
				System.out.println("Updation successful");
			} catch (SQLException e) {
				e.printStackTrace();
		}
	}
}
