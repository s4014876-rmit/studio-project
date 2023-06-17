package app;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonElements {

	//	Title is the string variable which gives the document its title.
	public static String DocumentStart(String Title){
		return
		"""
		<html>
		<head>
			<title>
		"""
		+ Title + 
		"""
			</title>
			<link rel='stylesheet' type='text/css' href='common.css' />
		</head>
		<body>
		""";
	}

	//	All appended HTML of a page.
	public static String DocumentEnd(){
		return
		"""
		</body>
		</html>
		""";
	}

	//	Common navbar header element
	public static String Header() {
		return
		"""
		<div class='topnav'>
			<a href='/'>Homepage</a>
			<a href='mission.html'>Our Mission</a>
			<a href='page2A.html'>Sub Task 2.A</a>
			<a href='page2B.html'>Sub Task 2.B</a>
			<a href='page3A.html'>Sub Task 3.A</a>
			<a href='page3B.html'>Sub Task 3.B</a>
		</div>
		<div class='content'>
		""";
	}


	public static String Footer() {
		return
		"""
		</div>
		<div class='footer'>
			<p>COSC2803 - Studio Project Starter Code (Apr23)</p>
		</div>
		""";
	}


	public static String Table(String query) {
		if(query == null || query.isEmpty()){
			return "<div class='table-container'></div>";
		}
		
		String html = "";
		JDBCConnection con = new JDBCConnection();
		ResultSet r = con.execute(query);

		int columnsCount = 0;
		



		try {
			columnsCount = r.getMetaData().getColumnCount();
		} catch (SQLException e) {
			System.out.print("Failed getColumnCount()");
		}

		//The while(true) loops in the try-catch blocks depend on the fact that SQLException occurs if the index passed to ResultSet.getString(int) is out of range.

		// table-container can be defined by a local stylesheet, to provide block formatting
		// for tables, which is necessary for some height adjustments.
		html += "<div class='table-container'><table>";
		//	Headers of Table
		html += "<tr>";
		
		for (int i = 1; i <= columnsCount; i++) {
			String temp = "";
			try {
				temp = r.getMetaData().getColumnName(i);
			} catch (SQLException e) {
				System.out.println("ResultSet.getMetaData().getColumnName() failed in CommonElements.Table()");
			}
			html += "<th>" + temp + "</th>";
		}
		html += "</tr>";

		//	Contents of Table
		try {
			while(r.next()){
				html += "<tr>";
				for (int i = 1; i <= columnsCount; i++){
					String temp = "";
					try {
						temp = r.getString(i);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					html += "<td>" + temp + "</td>";
				}
				html += "</tr>";
			}
		} catch (SQLException e) {
			System.out.println("ResultSet.next() failed in CommonElements.Table()");
		}
		html += "</table></div>";

		return html;
	}

	//returns the minimum Year of any table value using the "table" title and the type of data
	public static String getMinYear(String Table, String rangeType) throws Exception{
		JDBCConnection con = new JDBCConnection();
		String query = "Select Min(Year) from " + Table + " WHERE " + rangeType + " IS NOT NULL;" ;
		ResultSet MinYearDB = con.execute(query);
		String MinYear = "does not exist";
		while (MinYearDB.next()){
		 	MinYear = MinYearDB.getString("Min(Year)");
		}
		return MinYear;
	}
	//returns maximum Year same as above^
	public static String getMaxYear(String Table, String rangeType) throws Exception{
		JDBCConnection con = new JDBCConnection();
		String query = "Select Max(Year) from " + Table + " WHERE " + rangeType + " IS NOT NULL;" ;
		ResultSet MaxYearDB = con.execute(query);
		String MaxYear = "does not exist";
		while (MaxYearDB.next()){
		 	MaxYear = MaxYearDB.getString("Max(Year)");
		}
		return MaxYear;
	}


	//Tab container open
	public static String tabContainer(){
		 		
		return """ 
			<div class='Tabs_Container'>
            <div class='tabs'>
                """;
	}

	//Create tab label
	public static String tabLabel(String tabType, String checked){
		String html = "";
		if(checked == "checked"){
			html = "<input type='radio' class='tabs_radio' name='regions' id='" + tabType + "Tab'" + checked + "> <label for='" + tabType + "Tab' class='tabs_label'>" + tabType +"</label><div class='tabs_content'>";
		}
		else 
		{
			html = "<input type='radio' class='tabs_radio' name='regions' id='" + tabType + "Tab'> <label for='" + tabType + "Tab' class='tabs_label'>" + tabType +"</label><div class='tabs_content'>";
		}
		return html;
			
	}

	//create dropdownform open (for tabs)
	public static String dropdownFormOpen(String region, String post){
		
		String html = "<h3>" + region + " <h3> <form action'" + post + "' method='post'>";
        return html;           
	}

	//dropdown form option open (for tabs)
	public static String dropDownOptionOpen(String id, String label, String menuName){
		String html = "<div class='form-group'><label for='" + id + "_drop'>" + label + ":</label><select id='" + id + "_drop'name='" + id + "_drop'><option value='' selected disabled>" + menuName + "</option>";
 
		return html;
	}
	//dropdown form option close (for tabs)
	public static String dropDownOptionClose(){
		return "</select></div>";
	}
	//dropdown button submit (for tabs)
	public static String dropDownSubmit(String label){
		return "<button type 'submit' class'btn'>" + label + "</button></form>";
	}
	
}
