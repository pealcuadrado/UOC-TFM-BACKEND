package com.eventium.eventium;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DataBase {
   public static String result;
   public static java.sql.Connection connection=null;
   
    
	public static void startDataBase() {
		try {
			
			

		    String url = "jdbc:mysql://192.168.1.20:3306/eventium";
		    String username = "eventium";
		    String password = "eventium";
		    
		    if (connection == null) {
		    		connection = DriverManager.getConnection(url, username, password);
		    		System.out.println("Conexion realizada");
		    }
		    
		    java.sql.Statement statement = connection.createStatement();
		    ResultSet rs = statement.executeQuery("SELECT greeting FROM eventium.hello");

		    while (rs.next()) {

		        String hello = rs.getString("greeting");
		        result=hello;

		        System.out.println(String.format("%s", hello));
		    }

		    rs.close();
		    statement.close();
		    //connection.close();

		} catch (SQLException ex) {
		    System.out.println(ex);
		}
	}
	
}
