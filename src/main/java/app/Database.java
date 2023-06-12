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
		CREATE TABLE Global ( Year integer NOT NULL, AVG float, MIN float , MAX float , LOAVG float , LOMIN float , LOMAX float , PRIMARY KEY (Year) ) ;
		CREATE TABLE Country ( Year integer NOT NULL, Country varchar(20) , AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country) ) ;
		CREATE TABLE City ( Year integer NOT NULL, Country varchar(20) NOT NULL, City varchar(20) NOT NULL, Longitude float, Latitude float, AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country,City) ) ;
		CREATE TABLE State ( Year integer NOT NULL, Country varchar(20) NOT NULL, State varchar(20) NOT NULL, AVG float , MIN float , MAX float , PRIMARY KEY (Year,Country,State) ) ;
		CREATE TABLE Population ( Year integer NOT NULL, Country varchar(20) NOT NULL, CountryCode varchar(3), Population integer, PRIMARY KEY (Year,Country) ) ;
		CREATE TABLE Student ( Name varchar(20) , StudentNum integer NOT NULL , PRIMARY KEY (StudentNum) ) ;
		CREATE TABLE Persona ( Name varchar(20) NOT NULL, ImageURL varchar(30) , Text varchar(4000) , PRIMARY KEY (Name) ) ;
		"""
		);
	}

	private static void MiscTables() throws IOException {
		fout.write(
		"""
		INSERT INTO Student VALUES ('Aleksei Eaves', 4014876);
		INSERT INTO Student VALUES ('Shane Knight', 3785357);
		INSERT INTO Persona VALUES ('Michael Wiseau', 'Michael.png', '<b>Description/Attributes:</b> Michael Is a 45-year-old primary school teacher, who has been teaching students for over 20 years. He is passionate about educating young people and teaching them about the importance of taking care of the planet. He teaches a wide variety of subjects from English to science but finds himself excited to teach the children about the world around them, from the plants and animals in the school yard, to the natural wonders that exist on our planet and even to outer space.<br><b>Needs:</b> Michael needs a tool that can help him easily access and present data about the climate to his students. He is not particularly tech savvy and needs user-friendly and visually engaging experience so that his students can easily understand the information. He also needs reliable and up to date information so that he can be confident in the accuracy of his presentations.<br><b>Goals:</b> Michaels main goal is to find a web app that assists him in teaching his class about climate change in an informative and engaging way. The app will need to present user appropriate surface level information as well as an opportunity to read further into the data so he can have a better understanding before presenting it to his class.<br><b>Skills & experience:</b> Michael has a loose grasp on understanding and navigating computers. However, he recognizes that technology can be a powerful tool for classroom activities and is keen to learn how to integrate technology into his lessons so that his students can have a more interactive and exciting learning experience.');
		INSERT INTO Persona VALUES ('Sophia Maynard', 'Sophia.png', '<b>Description:</b> Sophia is a 1st year university student studying environmental science and like many other young people, is concerned about the environment. Sophia is particularly interested in keeping up with climate related news, attends local demonstrations and protests, and often talks about it with other people. She is even currently working on a project for one of her classes where she will develop her analytical and research skills so that she can have an informed opinion and a better chance of working towards a positive impact on the climate in the future.<br><b>Needs:</b> Sophia needs an accessible but comprehensive data resource to build on her knowledge and add substance and validity to her presentation. She needs credible and reliable information so she can make justified and accurate points to talk about in her project while not being overwhelmed with excessive detail of data or spending lots of time learning how to use the platform.<br><b>Goals:</b> Sophia wants to do well on her assessment while also gaining a basic understanding of data analysis and a broad view of world temperature trends and its relevance to the climate crisis. This way she will enrich her own understanding, and then be able to enrich the understanding of other people by explaining the effects of climate change in a concrete way.<br><b>Skills & Experience:</b> Sophia has basic computer literacy and is familiar with simple data concepts to where she can consider some ways in which to get meaning from data. She is generally familiar with climate concepts.');
		INSERT INTO Persona VALUES ('Sarah Moore', 'Sarah.png', '<b>Description:</b> Sarah is an environmental scientist working with a team of data analysts for the state to outline strategies for Victoria to use to combat their own environmental impact. She has a doctorate in climate change, so she has an advanced understanding of climate data. She believes that climate change is the most vital issue of the 21st century and is deeply committed to finding solutions for global climate issues.<br><b>Needs:</b> Sarah needs a digital platform which will let her utilize nuanced and high-quality climate data without needing to go through the regular boilerplate of setting up software and document files on her computer. She also needs to be able to reference this data and allow her colleagues to access the same site and collect their own data so they can have discussions about their individual findings. She requires data outlining trends from different countries to show state officials what countries and states are doing to combat climate change and how effective it has been so far. So highly adjustable and filtered information needs to be easily accessed and referenced for her presentations.<br><b>Goals:</b>Sarah''s goal is to identify whether certain countries actions on the climate crisis is having a positive effect o so that she can propose justified and proved strategies to the state on how to address this issue at a state level. Providing data and calculated values on trending and correlated statistics for the purpose of validating or voiding current strategies.<br><b>Skills and Experience:</b>Sarah has published her own research about climate change published before, so she is adept at understanding data about the climate and its implications. She is comfortable with using a computer, as she does so regularly in her job.');
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
				if (c == '\'')
					CountryName += '\'';
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
		int lines = 0;

        //  Connect to database
        JDBCConnection con = new JDBCConnection();

        //  Read in line and execute it.
        Scanner sql_scanner = new Scanner(sql_file);
        String s = "";

		System.out.println("Beginning database generation."); 
        while (sql_scanner.hasNextLine()) {
            s = sql_scanner.nextLine();
            con.execute(s);
            s = "";
			
			if (lines % 8136 == 0){
				System.out.print("\n" + (lines / 8136) + "%"); 
			}
			if (lines % 400 == 0){
				System.out.print("."); 
			}
			lines++;
        }
		System.out.println("Finished database generation."); 

        sql_scanner.close();
        con.close();
    }


}
