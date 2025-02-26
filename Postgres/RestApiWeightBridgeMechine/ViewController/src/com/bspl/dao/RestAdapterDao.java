package com.bspl.dao;

import java.sql.Connection;

import java.sql.SQLException;

import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.sql.DataSource;

public class RestAdapterDao {
    public Connection getConnection() throws SQLException, NamingException {
        InitialContext initialContext = new InitialContext();
      //  DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/APPLICATIONDBDS");
       // DataSource ds = (DataSource) initialContext.lookup("jdbc/APPLICATIONDBBWD");
       DataSource ds = (DataSource) initialContext.lookup("jdbc/APPLICATIONDBDP1");
        java.sql.Connection conn = ds.getConnection();
        return conn;
    }
    
    public static void closeDataSourceConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
