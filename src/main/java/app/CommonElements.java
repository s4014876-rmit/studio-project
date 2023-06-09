package app;

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
		""";
	}


	public static String Footer() {
		return
		"""
		<div class='footer'>
			<p>COSC2803 - Studio Project Starter Code (Apr23)</p>
		</div>
		""";
	}

}
