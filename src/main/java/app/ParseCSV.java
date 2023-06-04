package app;

import java.io.EOFException;

// A script specialized to parse the CSV files, and turn them into an SQL database.

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ParseCSV {

	//	c			Character buffer
	static char					c = 0;
	//	s			Strings buffer and initialization function.
	static String[]				s;
	//	fins		Stream of file input
	static FileInputStream		fins = null;
	//	finr		Input stream reader, handles UTF-8 code points.
	static InputStreamReader	finr = null;
	//	fout		Overwrites resultant data of Java program to an SQL file.
	static FileWriter			fout = null;
	//	EOF			Defines the End-Of-File character.
	static final char			EOF = (char)(-1); 


	private static void Init() throws IOException {
		fout = new FileWriter("database/GenerateDatabase.sql", false);
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
		c = (char)finr.read();
		while (c != '\n') {
			c = (char)finr.read();
		}

		// Read until EOF
		c = (char)finr.read(); 
		while (c != EOF) {
			fout.write("INSERT INTO " + table_name + " VALUES ("); 

			//	Initialize variables.
			int k = -1;
			s = new String[10];
			Arrays.fill(s, "");

			//Read until NEWLINE
			while (c != '\n' && c != EOF) {
				k++;
				//Read until COMMA
				while (c != ',' && c != '\n' && c != EOF) {
					s[k] += c;
					c = (char)finr.read();
				}
				
				//If it is a long/lat coordinate, remove final letter
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

				if (c == ',')
					c = (char)finr.read();
			}
			//	Insert value into INSERT INTO statement

			for(int i = 0; i <= k; i++){
				int j = Integer.parseInt(String.valueOf(column.charAt(i)));

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
		GenericParse("TableName", "TEST2.csv", "76543210");




		fout.close();
		return;
	}
}
