package com.bm.financial.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.bm.financial.bussiness.exceptions.DBConnectionException;
import com.bm.financial.controller.Login;

// TO DO : check if we need to close the connection or not -- use try with resource  or not 
public class DBManger {

	private static DBManger instance= null;
	private static Connection connection = null;
	static Logger logger = Logger.getLogger(DBManger.class);
	
	private DBManger() throws DBConnectionException, IOException {
	
	}
	
	public static DBManger getInstance() throws DBConnectionException, IOException {
		if (instance == null) {
			instance = new DBManger();
			initalizeDBConnection();
		}
		return instance;
		
	}
	public static Connection getConnection() throws DBConnectionException {
		if(connection == null) {
			logger.log(Priority.ERROR,"DB Connection :Connection is not establised yet");
			throw new DBConnectionException("DB Connection :Connection is not establised yet");
		}
	    return connection;
	
	}
	private static void initalizeDBConnection () throws DBConnectionException, IOException {

		try {
				Properties prop=new Properties();
				InputStream in = instance.getClass().getClassLoader().getResourceAsStream("dbConnection.properties");
	            prop.load(in);
	            in.close();

	            String driver = prop.getProperty("driverClassName").trim();
	            String dbUrl = prop.getProperty("db-url").trim();

	            Class.forName(driver);
	           // URL u= instance.getClass().getResource("accountsdb.accdb");
	            String url= "jdbc:ucanaccess://" + dbUrl;
				
				connection = DriverManager.getConnection(url);
				
			} catch (ClassNotFoundException e) {
				logger.log(Priority.ERROR,"DB Connection :UcanaccessDriver is not defined, check MS driver");
				throw new DBConnectionException("DB Connection :UcanaccessDriver is not defined");
			} 
			catch (SQLException e) {
				logger.log(Priority.ERROR,"DB Connection : Url of the database is not correct");
				throw new DBConnectionException("DB Connection : Url of the database is not correct");
			}
			
		
	  
	}
	
}
