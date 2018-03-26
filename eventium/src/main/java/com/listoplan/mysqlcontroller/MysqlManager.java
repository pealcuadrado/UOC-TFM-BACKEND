package com.listoplan.mysqlcontroller;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.listoplan.utils.ConfigurationManager;

import java.sql.Connection;


public class MysqlManager {

	Logger logger;
	
	private Connection connection;
	private static MysqlManager mysqlManager=null;
	
	private MysqlManager(){
		if(this.connection==null){
			logger=Logger.getLogger(MysqlManager.class);
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
		    logger.info("Conexión a la BBDD realizada correctamente");
		} catch (SQLException e) {
		    logger.error("No ha sido posible conectar con la Base de Datos MySQL", e);
		} catch (ClassNotFoundException e) {
			logger.error("No se puede encontrar el controlador MySQL",e);
		}
	}
	
	private Connection getConnection(){
		try {
			if(this.connection.isClosed()|| !this.connection.isValid(10)) {
				logger.warn("Conexión con la BBDD perdida. Intentando reconectar...");
				this.connect();
			};
		} catch (SQLException e) {
			logger.warn("Conexión con la BBDD perdida. Intentando reconectar...");
			this.connect();
		}
		return this.connection;
	}
	
	public ResultSet query(String sql) throws SQLException{
		ResultSet rs;
		Statement statement= this.getConnection().createStatement();
		rs= statement.executeQuery(sql);
		return rs;
	}
	
	public void execute(String sql) throws SQLException{
		PreparedStatement statement= this.getConnection().prepareStatement(sql);
		statement.execute();
	}
	
	public void quit(){
		try {
			this.connection.close();
			logger.info("Desconexion realizada con exito");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
