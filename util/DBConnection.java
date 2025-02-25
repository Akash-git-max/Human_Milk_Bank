package com.util;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBConnection {
	public static Connection getconnection() throws SQLException
	{
		Connection conn = null;		
		String url="jdbc:mysql://localhost:3306/tarasa_mconnect";
		String drivername="com.mysql.jdbc.Driver";
		String username="tarasaadmin";
		String password="tarasaadmin";
		try
		{
			Class.forName(drivername);
			conn = (Connection) DriverManager.getConnection(url,username,password);
			if (conn != null)
			{
				System.out.println("Connection Connected");
			}
			else
			{
				System.out.println("No Connection");
			}
		}
		catch (ClassNotFoundException  e)
		{
			e.printStackTrace();
		}
		/* finally
	        {
	        	conn.close();
	        }*/
		return conn;
	}
	
	/*public static void main(String[] a) throws SQLException {
		Connection db=DBConnection.getconnection();
	}*/
	
}
