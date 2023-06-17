package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST2B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2B.html";


    /* Temperature Changes Table */
    private static String SQL_Temperature(String CityOrState, String Country, String StartYear, String EndYear) {
        return "Select a.Year, a.Country, a."+CityOrState+", a.Avg - b.Avg as AvgChange, a.Min - b.Min as MinChange, a.Max - b.Max as MaxChange From City a, City b Where a.Country = b.Country and a."+CityOrState+" = b."+CityOrState+" and a.Year-1 = b.Year and a.Year >= "+StartYear+" and a.Year <= "+EndYear+" and a.Country='"+Country+"'";
    }
/* Ranking Of City/State Table */
    private static String SQL_Ranking(String CityOrState, String Country, String StartYear, String EndYear) {
        return "Select Country, "+CityOrState+", Rank() Over(Order by Abs(Avg(AvgChange)) desc, "+CityOrState+") AvgRank, Rank() Over(Order by Abs(Avg(MinChange)) desc, "+CityOrState+") MinRank, Rank() Over(Order by Abs(Avg(MaxChange)) desc, "+CityOrState+") MaxRank From (" + SQL_Temperature(CityOrState, Country, StartYear, EndYear) + ") Group by "+CityOrState+" Order by "+CityOrState+" Desc";
    }

    private static String SQL_Return(String TableV, String CityOrState, String Country, String StartYear, String EndYear) {
        if (TableV == "" || CityOrState == "" || Country == "" || StartYear == "" || EndYear == "") {
            System.out.println("ZERO");
            return null;
        }

        if (TableV == null || CityOrState == null || Country == null || StartYear == null || EndYear == null) {
            System.out.println("NULL");
            return null;
        }

        switch(TableV){
            case "Raw":
                return SQL_Temperature(CityOrState, Country, StartYear, EndYear);
            case "Ranking":
                return SQL_Ranking(CityOrState, Country, StartYear, EndYear);
            default:
                System.out.println("Incorrect TableV in SQL_Return at PageST2B.java");
                return "";
        }
    }
    
    @Override
    public void handle(Context context) throws Exception {
        //String UserWarn;
        String TableV = context.formParam("TableV");
        String CityOrState = context.formParam("CityOrState");
        String CountryName = context.formParam("CountryName");
        String StartYear = context.formParam("StartYear");
        String EndYear = context.formParam("EndYear");
    
        // do error checking



        String html = "";
        
        html += CommonElements.DocumentStart("2B");
        html += CommonElements.Header();

        html +=
        """
        <style>
        .content {
            margin-left:   3%;
            margin-right:  3%;
        }

        .grid-container {
            display:                grid;
            width:                  100%;
            height:                 70vh;
            grid-template-rows:     auto auto;
            grid-template-columns:  25vw auto;
            grid-column-gap:        25px;
        }

        .table-container {
            grid-area: 1 / 2 / 3 / 3;
            max-height: 70vh;
            display: block;
            overflow: scroll;
            border: 1px black solid;
        }

        table {
            width: 100%;
        }
        </style>
        """;

        html += "<div class='grid-container'>";


        html +=
        """
        <form action='/page2B.html' method='post'>
        <label for='TableV'>Data View</label><br>
        <select id='TableV' name='TableV'>
          <option value='Raw'>Temperature Changes</option>
          <option value='Ranking'>Temperature Rankings by Proportion of Change</option>
        </select> <br>

        <label for='CityOrState'>Region Type</label><br>
        <select id='CityOrState' name='CityOrState'>
          <option value='City'>Cities</option>
          <option value='State'>States</option>
        </select> <br>

        <label for='CountryName'>Country Name</label><br>
        <input type='text' id='CountryName' name='CountryName'> <br>

        <label for='StartYear'>Start Year</label><br>
        <input type='text' id='StartYear' name='StartYear'> <br>

        <label for='EndYear'>End Year</label><br>
        <input type='text' id='EndYear' name='EndYear'> <br>

        <input type='submit' value='Submit'>
        </form>
        """;


        html += CommonElements.Table(SQL_Return(TableV, CityOrState, CountryName, StartYear, EndYear));



        html += "</div>";

        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

}
