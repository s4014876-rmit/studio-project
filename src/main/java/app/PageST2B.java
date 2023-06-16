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
    private static String SQL_Temperature(String CityOrState, String Country, int StartYear, int EndYear) {
        return "Select a.Year, a.Country, a."+CityOrState+", a.Avg - b.Avg as AvgChange, a.Min - b.Min as MinChange, a.Max - b.Max as MaxChange From City a, City b Where a.Country = b.Country and a."+CityOrState+" = b."+CityOrState+" and a.Year-1 = b.Year and a.Year >= "+StartYear+" and a.Year <= "+EndYear+" and a.Country='"+Country+"'";
    }
/* Ranking Of City/State Table */
    private static String SQL_Ranking(String CityOrState, String Country, int StartYear, int EndYear) {
        return "Select Country, "+CityOrState+", Rank() Over(Order by Abs(Avg(AvgChange)) desc, "+CityOrState+") AvgRank, Rank() Over(Order by Abs(Avg(MinChange)) desc, "+CityOrState+") MinRank, Rank() Over(Order by Abs(Avg(MaxChange)) desc, "+CityOrState+") MaxRank From (" + SQL_Temperature(CityOrState, Country, StartYear, EndYear) + ") Group by "+CityOrState+" Order by "+CityOrState+" Desc";
    }

    //private static String SQL_Return(String CityOrState, String Country, int StartYear, int EndYear) {
    //
    //}
    
    @Override
    public void handle(Context context) throws Exception {
        
        
        String html = "";
        
        html += CommonElements.DocumentStart("2B");
        html += CommonElements.Header();

        html +=
        """
        <style>
        .grid-container {
            display:                grid;
            width:                  100%;
            grid-template-rows:     auto auto;
            grid-template-columns:  25vh auto;
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

        html += CommonElements.Table("Select * From Country Where Country='United States'");




        html += "</div>";

        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

}
