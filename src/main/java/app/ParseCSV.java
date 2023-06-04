package app;

import java.io.EOFException;

// A script specialized to parse the CSV files, and turn them into an SQL database.

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ParseCSV {

	//	c			Character buffer
	static char					c = 0;
	//	statement	Buffer for the statement.
	//static String				statement = "";
	//	s			Strings buffer.
	static String				s = "";
	//	fin			Takes input from various CSV files.
	static FileInputStream		fin = null;
	//	fout		Overwrites resultant data of Java program to an SQL file.
	static FileWriter			fout = null;
	//	p			Holds additional keywords for the table currently being generated.
	static DataEntity			p = null;

	public class DataEntity {
		String	type[];
		String	attributes[];
	}

	public class value_flags {
		Boolean is_varchar	= false;
		Boolean is_float	= false;
		Boolean is_integer	= false;
	}

	public static void Init_fout(){
		try {
			fout = new FileWriter("database/GenerateDatabase.sql", false);
		} catch (Exception e) {
			System.out.println("ParseCSV.Init_fout() failed.");
		}
	}

	public static void CreateTables() {

	}

	public static void SimpleParse(String table_name, String csv_file) throws IOException{
		//	Open GlobalYearlyLandTempByCity.csv
		/*	Sample of CSV for reference.
		Year,AverageTemperature,MinimumTemperature,MaximumTemperature,City,Country,Latitude,Longitude
		1750,15.909,8.357,25.463,Durrës,Albania,40.99N,19.17E
		1750,15.909,8.357,25.463,Elbasan,Albania,40.99N,19.17E
		1750,15.909,8.357,25.463,Tirana,Albania,40.99N,19.17E
		*/
		try {
			fin = new FileInputStream("database/csv/" + csv_file);
			System.out.println("Begun generating SQL for " + csv_file);
 		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find " + csv_file); 
		}

		// Throw away first line
		c = (char)fin.read();
		while (c != '\n') {
			c = (char)fin.read();
		}

		// Read until EOF
		final char EOF = (char)(-1);
		
		c = (char)fin.read();
		while (c != EOF) {
			fout.write("INSERT INTO " + table_name + " VALUES ("); 

			//Read until NEWLINE
			while (c != '\n' && c != EOF) {
				//Read until COMMA
				while (c != ',' && c != '\n' && c != EOF) {
					s += c;
					c = (char)fin.read();
				}

				//If it is a long/lat coordinate, remove final letter
				// This section currently causes an error.
				/*if (s.charAt(s.length() - 4) == '.'){
					char temp = s.charAt(s.length()-1);
					switch(temp){
						case 'N': case 'S': case 'W': case 'E': //Make negative value cases
							s = s.substring(0, s.length()-1);
							break;
						default:
							break;
					}
				}*/
				
				//Insert value
				fout.write("'" + s + "'");
				s = "";

				switch(c){
					case ',':
						fout.write(",");
						c = (char)fin.read();
						break;
					case '\n': case EOF:
						fout.write(");\n");
						break;
					default:
						System.out.println("switch(c) picked up an unexpected value.");
						break;
				}

			}

			c = (char)fin.read();
		} 


		fin.close();
	}

	public static void ParseAll() throws IOException{
		Init_fout();


		SimpleParse("TableName", "TEST.csv");




		fout.close();
		return;
	}
}
