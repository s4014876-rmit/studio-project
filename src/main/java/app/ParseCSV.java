package app;

import java.io.EOFException;

// A script specialized to parse the CSV files, and turn them into an SQL database.

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParseCSV {

	//	c			Character buffer
	static char					c = 0;
	//	s			Strings buffer.
	static String				s = "";
	//	fins		Stream of file input
	static FileInputStream		fins = null;
	//	finr		Input stream reader, handles UTF-8 code points.
	static InputStreamReader	finr = null;
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

	public static void Init(){
		try {
			fout = new FileWriter("database/GenerateDatabase.sql", false);
		} catch (Exception e) {
			System.out.println("ParseCSV.Init() failed.");
		}
	}

	public static void CreateTables() throws IOException {
		fout.write(
		"""
		CREATE TABLE Global {
			Year		int			NOT NULL,
			AVG			float		,
			MIN			float		,
			MAX			float		,
			LOAVG		float		,
			LOMIN		float		,
			LOMAX		float		,
			PRIMARY KEY	(Year)
		};

		CREATE TABLE Country {
			Year		int			NOT NULL,
			AVG			float		,
			MIN			float		,
			MAX			float		,
			Country		varchar(20)	,
			PRIMARY KEY	(Year,Country)
		}

		CREATE TABLE Country {
			Year		int			NOT NULL,
			AVG			float		,
			MIN			float		,
			MAX			float		,
			Country		varchar(20)	,
			PRIMARY KEY	(Year,Country)
		}


		"""
		);
	}

	private static void GenericParse(String table_name, String csv_file) throws IOException{
		//	Open GlobalYearlyLandTempByCity.csv
		/*	Sample of CSV for reference.
		Year,AverageTemperature,MinimumTemperature,MaximumTemperature,City,Country,Latitude,Longitude
		1750,15.909,8.357,25.463,Durrës,Albania,40.99N,19.17E
		1750,15.909,8.357,25.463,Elbasan,Albania,40.99N,19.17E
		1750,15.909,8.357,25.463,Tirana,Albania,40.99N,19.17E
		*/
		try {
			fins = new FileInputStream("database/csv/" + csv_file);
			finr = new InputStreamReader(fins, "UTF-8");
			System.out.println("Begun generating SQL for " + csv_file);
 		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find " + csv_file); 
		}

		// Throw away first line
		c = (char)finr.read();
		while (c != '\n') {
			c = (char)finr.read();
		}

		// Read until EOF
		final char EOF = (char)(-1); 
		
		c = (char)finr.read();
		while (c != EOF) {
			fout.write("INSERT INTO " + table_name + " VALUES ("); 

			//Read until NEWLINE
			while (c != '\n' && c != EOF) {
				//Read until COMMA
				while (c != ',' && c != '\n' && c != EOF) {
					s += c;
					c = (char)finr.read();
				}

				//If it is a long/lat coordinate, remove final letter
				if (s.charAt(s.length() - 4) == '.'){
					char temp = s.charAt(s.length()-1);
					switch(temp){
						case 'N': case 'E':
							s = s.substring(0, s.length()-1);
							break;
						case 'S': case 'W': 
							s = "-" + s.substring(0, s.length()-1);
							break;
						default:
							break;
					}
				}
				
				//	Insert value into INSERT INTO statement
				if (s.length() > 0){
					fout.write("'" + s + "'");
					s = "";
				}
				else{
					fout.write("NULL");
				}

				//	Append extra text for statement.
				switch(c){
					case ',':
						fout.write(",");
						c = (char)finr.read();
						break;
					case '\n': case EOF:
						fout.write(");\n");
						break;
					default:
						System.out.println("switch(c) picked up an unexpected value.");
						break;
				}

			}

			c = (char)finr.read();
		} 


		fins.close();
	}

	// A modification of GenericParse() which must handle Population.csv
	private static void PopulationParse() throws IOException {

	}

	public static void ParseAll() throws IOException{
		Init();

		// Sample with GlobalYearlTemp.csv to ensure the process is thorough.
		GenericParse("TableName", "TEST.csv");




		fout.close();
		return;
	}
}
