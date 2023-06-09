package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database.
 * Allows SQL queries to be used with the SQLLite Databse in Java.
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class JDBCConnection {

    // Name of database file (contained in database folder)
    public static final String DATABASE = "jdbc:sqlite:database/climate.db";

    Connection  con             = null;
    Statement   sql_statement   = null;

    public JDBCConnection() {
		try {
            con = DriverManager.getConnection(DATABASE);
            sql_statement = con.createStatement();
            sql_statement.setQueryTimeout(10);
            System.out.println("Created JDBCConnection");
        } catch (SQLException e) {
            System.out.println("Could not initialize a JDBCConnection");
        }
    }

    public ResultSet execute(String s) {
        ResultSet r = null;
        try {
            sql_statement.execute(s);
            r = sql_statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.print("Failed to close a JDBC Connection.");
        }
    }

    

}
