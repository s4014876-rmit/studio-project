/*	Database.java
 * A Java program specialized for parsing the particular CSV files given for this project,
 * and turns them into an SQL database.
 */

package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;


public class Database {

	//	fins		Stream of file input
	private static FileInputStream		fins = null;
	//	finr		Input stream reader, handles specific encoding formats.
	private static InputStreamReader	finr = null;
	//	fout		Overwrites resultant data of Java program to an SQL file.
	private static FileWriter			fout = null;
	//	c			Character buffer
	private static char					c = 0;
	//	c_read()	Reads a character into the character buffer from finr
	private static void					c_read() throws IOException{
		c = (char)finr.read();
	}
	//	s			Strings buffer and initialization function.
	private static String[]				s;
	//	EOF			Defines the End-Of-File character.
	private static final char			EOF = (char)(-1); 

	private static void CreateTables() throws IOException {
		fout.write(
		"""
		PRAGMA foreign_keys = ON ;
		CREATE TABLE Global ( Year int NOT NULL, AVG float, MIN float , MAX float , LOAVG float , LOMIN float , LOMAX float , PRIMARY KEY (Year) ) ;
		CREATE TABLE Country ( Year int NOT NULL, Country varchar(20) , AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country) ) ;
		CREATE TABLE City ( Year int NOT NULL, Country varchar(20) NOT NULL, City varchar(20) NOT NULL, AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country,City), FOREIGN KEY (Year) REFERENCES Country(Year), FOREIGN KEY (Country) REFERENCES Country(Country) ) ;
		CREATE TABLE State ( Year int NOT NULL, Country varchar(20) NOT NULL, State varchar(20) NOT NULL, AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country,State), FOREIGN KEY (Year) REFERENCES Country(Year), FOREIGN KEY (Country) REFERENCES Country(Country) ) ;
		CREATE TABLE Population ( Year int NOT NULL, Country varchar(20) NOT NULL, CountryCode varchar(3), Population int, PRIMARY KEY (Year,Country) ) ;
		CREATE TABLE Students ( StudentNum int NOT NULL, Name varchar(20) , PRIMARY KEY (StudentNum) ) ;
		CREATE TABLE Personas ( Name varchar(20) NOT NULL, ImageURL varchar(30) , Text varchar(1200) , PRIMARY KEY (Name) ) ;
		"""
		);
	}

	private static void MiscTables() throws IOException {
		fout.write(
		"""
		INSERT INTO Students VALUES ('Aleksei Eaves', 4014876);
		INSERT INTO Students VALUES ('Shane Knight', 3785357);
		INSERT INTO Personas VALUES ('Jane Doe', 'url/to/image.jpg', 'So and so');
		INSERT INTO Personas VALUES ('Dane Doe', 'url/to/image.jpg', 'So and so');
		INSERT INTO Personas VALUES ('Jane Joe', 'url/to/image.jpg', 'So and so');
		""");
	}

	/*	GenericParse()
	The typical parsing function for most of the CSV files.

	The variable 'column' is a String of digits which each represent a table attribute indexed from zero.
	It specifies the order which attributes will be output.
	To illustrate how it works here are some examples.
		01234	Normal order.
		43210	Reverse order.
		10234	First two attributes swapped.
	 */
	private static void GenericParse(String table_name, String csv_file, String column) throws IOException{
		//	Initialize variables
		try {
			fins = new FileInputStream("database/csv/" + csv_file);
			finr = new InputStreamReader(fins, "UTF-8");
			System.out.println("Begun generating SQL for " + csv_file);
 		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find " + csv_file); 
		}
		
		// Throw away first line
		c_read();
		while (c != '\n') {
			c_read();
		}

		// Read until EOF
		c_read(); 
		while (c != EOF) {
			fout.write("INSERT INTO " + table_name + " VALUES ("); 

			//	Initialize variables.
			int k = -1;
			s = new String[10];
			Arrays.fill(s, "");

			//Read until NEWLINE / EOF
			while (c != '\n' && c != EOF) {
				k++;
				//Read until COMMA / NEWLINE / EOF
				while (c != ',' && c != '\n' && c != EOF) {
					s[k] += c;
					if (c == '\'')
						s[k] += '\'';
					c_read();
				}

				// Trim newline if accidentally placed in.
				s[k] = s[k].trim();
				
				//If it is a long/lat coordinate, remove final letter
				try {
					if (s[k].charAt(s[k].length() - 4) == '.'){
					switch(s[k].charAt(s[k].length()-1)){
						case 'N': case 'E':
							s[k] = s[k].substring(0, s[k].length()-1);
							break;
						case 'S': case 'W': 
							s[k] = "-" + s[k].substring(0, s[k].length()-1);
							break;
						default:
							break;
					}
					}
				} catch (Exception e) {
				}

				if (c == ',')
					c_read();
			}

			//	Insert value into INSERT INTO statement
			for(int i = 0; i <= k; i++){
				int j = Integer.parseInt(String.valueOf(column.charAt(i)));

				// If neither of the two functions fail, then write normally, else write with apostrophes.
				try {  
					Float.parseFloat(s[j]);
					Integer.parseInt(s[j]);
					fout.write(s[j]);
				}
				catch (NumberFormatException e) {
					fout.write("'" + s[j] + "'");
				}
				if (i != k) fout.write(",");
			}

			k = -1;

			fout.write(");\n");

			if (c == '\n')
				c_read();
		} 


		fins.close();
	}

	// A different parsing method for Population.csv
	private static void PopulationParse(String table_name, String csv_file) throws IOException {
		//	Initialize variables
		try {
			fins = new FileInputStream("database/csv/" + csv_file);
			finr = new InputStreamReader(fins, "UTF-8");
			System.out.println("Begun generating SQL for " + csv_file);
 		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find " + csv_file); 
		}
		
		// Throw away first line
		c_read();
		while (c != '\n') {
			c_read();
		}

		// Read until EOF
		c_read();
		while (c != EOF) {
			// Read into CountryName
			String CountryName = "";
			while (c != ','){
				CountryName += c;
				c_read(); 
			}

			// Read into CountryCode
			c_read();
			String CountryCode = "";
			while (c != ','){
				CountryCode += c;
				c_read(); 
			}

			// Read rest of line
			int Year = 1960;
			String Population = "";
			c_read(); 
			while (c != '\n' && c != EOF){
				// Read until COMMA / NEWLINE
				while (c != ',' && c != '\n'){
					Population += c;
					c_read(); 
				}
				
				// Output INSERT INTO statement
				fout.write("INSERT INTO " + table_name + " VALUES (" + Integer.toString(Year)
				+ ",'" + CountryName + "','" + CountryCode + "'," + Population.trim() + ");\n");

				Year++;
				Population = "";
				
				if (c == ',')
					c_read(); 
			}

			if (c == '\n')
				c_read();
		} 

		fins.close();
		System.out.println("Finished SQL for " + csv_file);
	}

	public static void ParseDatabase() throws IOException{
		fout = new FileWriter("database/GenerateDatabase.sql", false);

		CreateTables();

		GenericParse	("Global",		"GlobalYearlyTemp.csv",					"0123456");
		GenericParse	("Country",		"GlobalYearlyLandTempByCountry.csv",	"04123");
		GenericParse	("City",		"GlobalYearlyLandTempByCity.csv",		"05467123");
		GenericParse	("State",		"GlobalYearlyLandTempByState.csv",		"054123");
		PopulationParse	("Population",	"Population.csv");

		MiscTables();

		fout.close();
	}

	public static void GenerateDatabase() throws FileNotFoundException {
        File sql_file = new File("database/GenerateDatabase.sql");

        //  Connect to database
        JDBCConnection con = new JDBCConnection();

        //  Read in line and execute it.
        Scanner sql_scanner = new Scanner(sql_file);
        String s = "";
        while (sql_scanner.hasNextLine()) {
            s = sql_scanner.nextLine();
            con.execute(s);
            s = "";
        }

        sql_scanner.close();
        con.close();
    }


}
