package app;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
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

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    public static void GenerateDatabase() throws FileNotFoundException {
        File sql_file = new File("database/GenerateDatabase.sql");

        //  Connect to database
        Connection con = null;
        Statement sql_statement = null;
		try {
            con = DriverManager.getConnection(DATABASE);
            sql_statement = con.createStatement();
            sql_statement.setQueryTimeout(500);
        } catch (SQLException e) {
            System.out.println("Could not connect to database in RunScript()");
        }

        //  Read in line and execute it.
        Scanner sql_scanner = new Scanner(sql_file);
        String s = "";
        while (sql_scanner.hasNextLine()) {

            s = sql_scanner.nextLine();

            try {
                sql_statement.execute(s);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            s = "";
        }

        sql_scanner.close();
    }

}
