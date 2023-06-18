package app;

import java.sql.ResultSet;
import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;



/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageST3B implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page3B.html";


    public static String AA(String Table, String Rank, int MinYear, int MaxYear/*, String Country, String CityState*/){
        String r = "";
        r += "Select *, ";
        
        if (Table != "Country"){
            r += Table + ",";
        }

        r += "Abs(Avg("+Rank+")) PreRanking\nFrom";
        r +=
        """
        (Select
            a.Year, a.Country,
            a.Avg, a.Min, a.Max, 
            a.Avg - b.Avg as AvgChange,
            a.Min - b.Min as MinChange,
            a.Max - b.Max as MaxChange,

        """;
        if (Table == "Country" && Rank.substring(0,3) == "Pop") {
            r += "p.Population as Pop, p.Population - q.Population as PopChange";
        }
        r += " From "+Table+" a, "+Table+" b ";
        if (Table == "Country" && Rank.substring(0,3) == "Pop") {
            r += "JOIN Population p, Population q";
            r += " ON a.Year = p.Year and a."+Table+" = p."+Table+" and p.Year-1 = q.Year and p."+Table+" = q."+Table;
        }
        r += " Where a."+Table+" = b."+Table+" and a.Year-1 = b.Year";
        r += " and a.Year >= "+MinYear+" and a.Year <= "+MaxYear;
        /*r += " and a.Country='"+Country+"'";
        if (Table != "Country"){
            r += "and "+Table+"='"+CityState+"'";
        }*/
        r += ") Group by "+Table+" Order by "+Table+" Asc";
        return r;
    }

    public static String AA(String Table, String Rank, int MinYear, int MaxYear, String Country, String CityState){
        String r = "";
        r += "Select *, ";
        
        if (Table != "Country"){
            r += Table + ",";
        }

        r += "Abs(Avg("+Rank+")) PreRanking\nFrom";
        r +=
        """
        (Select
            a.Year, a.Country,
            a.Avg, a.Min, a.Max, 
            a.Avg - b.Avg as AvgChange,
            a.Min - b.Min as MinChange,
            a.Max - b.Max as MaxChange

        """;
        if (Table == "Country" && Rank.substring(0,3) == "Pop") {
            r += ", p.Population as Pop, p.Population - q.Population as PopChange";
        }
        r += " From "+Table+" a, "+Table+" b ";
        if (Table == "Country" && Rank.substring(0,3) == "Pop") {
            r += "JOIN Population p, Population q";
            r += " ON a.Year = p.Year and a."+Table+" = p."+Table+" and p.Year-1 = q.Year and p."+Table+" = q."+Table;
        }
        r += " Where a."+Table+" = b."+Table+" and a.Year-1 = b.Year";
        r += " and a.Year >= "+MinYear+" and a.Year <= "+MaxYear;
        r += " and a.Country='"+Country+"'";
        if (Table != "Country"){
            r += "and "+Table+"='"+CityState+"'";
        }
        r += ") Group by "+Table+" Order by "+Table+" Asc";
        return r;
    }  

    private static String BB(String SelectedPreranking, String Table, String Rank, int MinYear, int MaxYear, String Country, String CityState){
        String r = "";
        r += "Select Year, Country, ";
        if (Rank.substring(3) == "Change"){
            r += Rank.substring(0, 3) + ", ";
        }
        r += Rank + ", ";
        r += "ROUND(abs(PreRanking-"+SelectedPreranking+"),3) As Ranking ";
        
        r += "From ("+AA(Table, Rank, MinYear, MaxYear, Country, CityState)+") Order by Ranking asc";
        return r;
    }




    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        // put context grabs here
        String Rank = context.formParam("RankA") + context.formParam("RankB"); // avg, min, max, pop, *change
        String Table = context.formParam("Table");
        String Country = context.formParam("Country");
        String CityState = context.formParam("CityState");
        int BeginYear;
        int Period;
        int Limit;
        try {
            BeginYear = Integer.parseInt(context.formParam("BeginYear"));
            Period = Integer.parseInt(context.formParam("Period"));
            Limit = Integer.parseInt(context.formParam("Limit"));
        } catch (NumberFormatException e) {
            BeginYear = 0;
            Period = 0;
            Limit = 0;
        }
        html += CommonElements.DocumentStart("3B");
        html += CommonElements.Header();

        boolean run = true;
        if (Rank == null || Table == null || Country == null ){
            run = false;
        }

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
            background-color:      #eee;
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


        JDBCConnection con = new JDBCConnection();
        int MinYear = con.execute("Select Min(Year) as MIN From Global").getInt("MIN");
        int MaxYear = con.execute("Select Max(Year) as MAX From Global").getInt("MAX");

        if (run){
            String metric = con.execute(AA(Table, Rank, BeginYear, BeginYear+Period, Country, CityState)).getString("PreRanking");

            String execute = "Create Table TempTable (Year Integer, Country varchar(20)";
            if(Table != "Country") {
                execute += ", "+Table+" varchar(20)";
            }
            execute += ", "+Rank+" float, Ranking float)";
            System.out.println(execute);
            con.execute(execute);

            for (int i = 0; i < (MaxYear-MinYear)-Period; i++){
                //html += CommonElements.Table(BB(metric, Table, Rank, MinYear+i, MinYear+i+Period, Country, CityState));
                ResultSet sort = con.execute(BB(metric, Table, Rank, MinYear+i, MinYear+i+Period, Country, CityState));
                String query = "Insert into TempTable Values(";
                query += sort.getString("Year")+", '"+sort.getString("Country")+"', ";
                if(Table != "Country"){
                    query += "'"+sort.getString(CityState)+"'', ";
                }
                query += sort.getString(Rank)+", "+sort.getString("Ranking")+")";
                con.execute(query);
                System.out.println(query);
            }System.out.println(execute);
        }

        html +=
        """
        <form action='/page3B.html' method='post'>

        <label for='Table'>Dataset</label><br>
        <select id='Table' name='Table'>
            <option value='Country'>Country</option>
            <option value='City'>City</option>
            <option value='State'>State</option>
        </select> <br>
        
        <label for='RankA'>Data Type</label><br>
        <select id='RankA' name='RankA'>
          <option value='Avg'>Average</option>
          <option value='Min'>Minimum</option>
          <option value='Max'>Maximum</option>
          <option value='Pop'>Population (only for Country)</option>
        </select> <br>

        <label for='RankB'>Data View</label><br>
        <select id='RankB' name='RankB'>
          <option value=''>Absolute</option>
          <option value='Change'>Relative</option>
        </select> <br>

        <label for='Country'>Country</label><br>
        <select id='Country' name='Country'>
        """;
        ResultSet Countries = con.execute("Select Distinct Country From Country Order by Country");
        while (Countries.next()){
            html +=
            "<option value='" + Countries.getString("Country") +
            "'>" + Countries.getString("Country") + "</option>";
        }
        html +=
        """
        </select> <br>

        <label for='CityState'>City/State Name (only for City or State)</label><br>
        <input type='text' id='CityState' name='CityState'> <br>

        <label for='BeginYear'>Starting Year</label><br>
        <input type='number' id='BeginYear' name='BeginYear' min='"""
            +MinYear+
        """
        ', max='"""
            +MaxYear+
        """
        ' value='"""
           +MinYear+ 
        """
        '> <br>

        <label for='Period'>Period of Time</label><br>
        <input type='number' id='Period' name='Period' min='"""
            +1+
        """
        ', max='"""
            +150+
        """
        ' value='"""
            +1+ 
        """
        '> <br>
        
        <label for='Limit'>Number of Entries</label><br>
        <input type='number' id='Limit' name='Limit' min='"""
            +1+
        """
        ', max='"""
            +150+
        """
        ' value='"""
            +1+ 
        """
        '> <br>

        <input type='submit' value='Submit'>
        </form>
        """;

        if (run) {
            html += CommonElements.Table("Select * From TempTable Order By Ranking Asc", Limit, con);
            con.execute("Drop Table TempTable");
        } else {
            html += "<div class='table-container'></div>";
        }

        html += "</div>";
        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        con.close();
        context.html(html);
    }

}
