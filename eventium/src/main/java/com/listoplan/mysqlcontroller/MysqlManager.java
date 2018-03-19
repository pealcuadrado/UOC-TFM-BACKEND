package com.listoplan.mysqlcontroller;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.listoplan.utils.ConfigurationManager;

import java.sql.Connection;


public class MysqlManager {

	
	private Connection connection;
	private static MysqlManager mysqlManager=null;
	
	private MysqlManager(){
		if(this.connection==null){
			this.connect();
		}
	};
	
	public static MysqlManager getInstance(){
		if (mysqlManager==null){
			mysqlManager= new MysqlManager();
		}
		return mysqlManager;
	}
	
	public void connect(){
		String uri=ConfigurationManager.getInstance().getCM().getString("mysql.uri");
		String password=ConfigurationManager.getInstance().getCM().getString("mysql.password");
		String user=ConfigurationManager.getInstance().getCM().getString("mysql.user");
		try{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(uri, user, password);
		    System.out.println("Database connected!");
		} catch (SQLException e) {
		    throw new IllegalStateException("No ha sido posible conectar con la Base de Datos MySQL", e);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("No se puede encontrar el controlador MySQL",e);
		}
	}
	
	private Connection getConnection(){
		try {
			if(this.connection.isClosed()|| !this.connection.isValid(10)) {
				// reconnect
				this.connect();
			};
		} catch (SQLException e) {
			// reconnect
			this.connect();
		}
		return this.connection;
	}
	
	public ResultSet query(String sql){
		ResultSet rs;
		try {
			Statement statement= this.getConnection().createStatement();
			rs= statement.executeQuery(sql);
		} catch (SQLException e) {
			rs=null;
			System.out.println("Error ejecutando consulta en BDD");
			e.printStackTrace();
		}
		return rs;
	}
	
	public void execute(String sql){
		try {
			PreparedStatement statement= this.getConnection().prepareStatement(sql);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("Error ejecutando consulta en BDD");
			e.printStackTrace();
		}
	}
	
	public void quit(){
		try {
			this.connection.close();
			System.out.println("Desconexion realizada con exito");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
