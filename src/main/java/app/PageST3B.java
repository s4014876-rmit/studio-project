package app;

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
        r += "abs(PreRanking-"+SelectedPreranking+") As Ranking ";
        
        r += "From ("+AA(Table, Rank, MinYear, MaxYear, Country, CityState)+") Order by Ranking asc";
        return r;
    }




    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        // put context grabs here
        String Rank = "Avg";
        String Table = "Country";
        String Country = "Australia";
        int BeginYear = 2000;
        int Period = 10;
        
        html += CommonElements.DocumentStart("3B");
        html += CommonElements.Header();


        JDBCConnection con = new JDBCConnection();
        String metric = con.execute(AA("Country", "Avg", 2000, 2007, "Australia", "Melbourne"))
                        .getString("PreRanking");
        System.out.println(metric);
        //html += CommonElements.Table(BB(metric, "Country", "Avg", 2000, 2007, "Australia", "Melbourne"));

        int MinYear = con.execute("Select Min(Year) as MIN From "+Table+" Where Country='"+Country+"'").getInt("MIN");

        System.out.println(MinYear);

        int MaxYear = con.execute("Select Max(Year) as MAX From "+Table+" Where Country='"+Country+"'").getInt("MAX");

        System.out.println(MaxYear);
        

        for (int i = 0; i < (MaxYear-MinYear)-Period; i++){
            html += CommonElements.Table(BB(metric, Table, Rank, MinYear+i, MinYear+i+Period, Country, "Melbourne"));
        }


        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

}
