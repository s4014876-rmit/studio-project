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
public class PageST2A implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/page2A.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        JDBCConnection con = new JDBCConnection();
        
        html += CommonElements.DocumentStart("2A");
        html += CommonElements.Header();

        // Contributions will mostly go here.

        // header for page 
        html = html + """
        <div class'header'
            <h1>Compare</h1>
        </div
        """;

        html = html + """
        <link rel='stylesheet' type='text/css' href='TabsCommon.css'>
        """;

        //query for table later
        String query = "";

        //Max container = overall container 
        html = html + """
        <div class='Max_Container'
        """;

        //Tabs and tab content 
        //Tab container
            
        html = html + CommonElements.tabContainer();
        html = html + CommonElements.tabContainer();
            //Create World Tab
            html = html + CommonElements.tabLabel("World", "checked");
                
                //Content for world tab
                //dropdown menus
                html = html + CommonElements.dropdownFormOpen("World", "/page2A.html");

                    html = html + CommonElements.dropDownOptionOpen("WorldYearMin", "YearMin", " -- Select a start Year -- ");

                    ResultSet WorldYearMin = con.execute("Select DISTINCT Year from Global ORDER BY Year ASC;");
                    while(WorldYearMin.next()){
                        html = html + "<option value='" + WorldYearMin.getString("Year") + "'>" + WorldYearMin.getString("Year") + "</option>";
                    }

                    html = html + CommonElements.dropDownOptionClose();
                                     
                    html = html + CommonElements.dropDownOptionOpen("WorldYearMax", "YearMax", " -- Select an end Year -- ");
                        
                        ResultSet WorldYearMax = con.execute("Select DISTINCT Year from Global ORDER BY Year DESC;");
                        while(WorldYearMax.next()){
                            html = html + "<option value='" + WorldYearMax.getString("Year") + "'>" + WorldYearMax.getString("Year") + "</option>";
                        }

                    html = html + CommonElements.dropDownOptionClose();
                html = html + CommonElements.dropDownSubmit("View data");

                //form param for world year selection 
                String WorldYearMin_drop = context.formParam("WorldYearMin_drop");
                String WorldYearMax_drop = context.formParam("WorldYearMax_drop");

                html = html + """
                <div class='Tab_Content_Graph_Container'>
                    <div class='Tab_Content_Graph'>
                        <h4>WorldAvgTemp</h4>
                    </div>
                    <div class='Tab_Content_Graph'>
                        <h4>WorldOceanLandTemp</h4>
                    </div>
                    <div class='Tab_Content_Graph'>
                        <h4>WorldPopulation</h1>
                    </div>
                </div>

                """;

                if(WorldYearMin_drop == null || WorldYearMax == null){

                html = html + "<div class='Tab_Data_Container'>";           
                    
                         html = html + "<p>AvgTemp diff: " + CommonElements.nodata() + "</p>";
                         html = html + "<p>LandOCeanAvg diff: " + CommonElements.nodata() + "</p>";
                         html = html + "<p>Population diff: " + CommonElements.nodata() + "</p>";
                     html = html + "</div>";
                }
                else {
                    html = html + """                                
                    <div class='Tab_Data_Container'>
                    """;
                        html = html + "<p>Year selected: " + WorldYearMin_drop + " - " + WorldYearMax_drop + "</p>";
                        html = html + "<p>Average temp: " + PageIndex.getAvgTemp_World(WorldYearMin_drop) + " - " + PageIndex.getAvgTemp_World(WorldYearMax_drop);
                        html = html + "<p>Average temp difference: " + getAvgTempDiff("global", WorldYearMin_drop, WorldYearMax_drop) + "</p><br></br>";

                        html = html + "<p>LandOceanAverage temp: " + PageIndex.getAvgTemp_World(WorldYearMin_drop) + " - " + PageIndex.getAvgTemp_World(WorldYearMax_drop);
                        html = html + "<p>Average land Ocean temp difference: " + getLandOceanAvgDiff("World", WorldYearMin_drop, WorldYearMax_drop) + " degrees</p><br></br>";

                        html = html + "<p>Population Year: " + WorldYearMin_drop + " = " + PageIndex.getPopulation_World(WorldYearMin_drop) + " people";
                        html = html + "<p>Population Year: " + WorldYearMax_drop + " = " + PageIndex.getPopulation_World(WorldYearMax_drop) + " people";
                        html = html + "<p>Population change: " + getPopulationDiff("World", WorldYearMin_drop, WorldYearMax_drop) + " people";
                        
                        query = "Select * from global Where Year Between " + WorldYearMin_drop + " AND " + WorldYearMax_drop;

                        html = html + """
                    </div>
                    """;
                }
            html = html + "</div>";

            //Create country tab
            html = html + CommonElements.tabLabel("Country", "not checked");

                //Content for Country Tab
                html = html + CommonElements.dropdownFormOpen("Country", "/page2A.html");

                    html = html + CommonElements.tabContainer_sort();

                        html = html + CommonElements.tabLabel_sort("Alphabetical", "not checked");

                            html = html + CommonElements.dropDownOptionOpen("Country", "Country", " -- Select a Country -- ");

                                ResultSet CountryList = con.execute("Select DISTINCT Country from Country Order BY Country ASC;");
                                while(CountryList.next()){
                                    html = html + "<option value='" + CountryList.getString("Country") + "'>" + CountryList.getString("Country") + "</option>";
                                }
                        
                            html = html + CommonElements.dropDownOptionClose();
                        html = html + "</div>";
                        
                        html = html + CommonElements.tabLabel_sort("Temperature", "not checked");

                            html = html + CommonElements.dropDownOptionOpen("Country", "Country", " -- Select a Country -- ");

                                ResultSet CountryList2 = con.execute("Select DISTINCT Country from Country Order BY avg DESC;");
                                while(CountryList2.next()){
                                    html = html + "<option value='" + CountryList2.getString("Country") + "'>" + CountryList.getString("Country") + "</option>";
                                }
                        
                                html = html + CommonElements.dropDownOptionClose();
                            html = html + "</div>";
                        
                        html = html + CommonElements.tabLabel_sort("Population", "not checked");

                            html = html + CommonElements.dropDownOptionOpen("Country", "Country", " -- Select a Country -- ");

                            ResultSet CountryList3 = con.execute("Select MAX(population), country from population GROUP BY country Order By population desc;");
                            while(CountryList3.next()){
                                html = html + "<option value='" + CountryList3.getString("Country") + "'>" + CountryList3.getString("Country") + "</option>";
                            }
                        
                    html = html + CommonElements.dropDownOptionClose();
                html = html + "</div>";
                    html = html + "</div>";

                    html = html + "</div>";

                    html = html + CommonElements.dropDownOptionOpen("CountryYearMin", "YearMin", " -- Select a start Year -- ");

                        ResultSet CountryYearMin = con.execute("Select DISTINCT Year from Country ORDER BY Year ASC;");
                        while(CountryYearMin.next()){
                            html = html + "<option value='" + CountryYearMin.getString("Year") + "'>" + CountryYearMin.getString("Year") + "</option>";
                        }

                    html = html + CommonElements.dropDownOptionClose();

                    html = html + CommonElements.dropDownOptionOpen("CountryYearMax", "YearMax", " -- Select an end Year -- ");

                        ResultSet CountryYearMax = con.execute("Select DISTINCT Year from Country ORDER BY Year DESC;");
                        while(CountryYearMax.next()){
                            html = html + "<option value='" + CountryYearMax.getString("Year") + "'>" + CountryYearMax.getString("Year") + "</option>";
                        }

                    html = html + CommonElements.dropDownOptionClose();
                html = html + CommonElements.dropDownSubmit("View data");


                //form param for country year selection
                String Country_drop = context.formParam("Country_drop");
                String CountryYearMin_drop = context.formParam("CountryYearMin_drop");
                String CountryYearMax_drop = context.formParam("CountryYearMax_drop");

                html = html + """
                    <div class='Tab_Content_Graph_Container'>
                        <div class='Tab_Content_Graph'>
                            <h4>CountryAvgTemp<h4>
                        </div>
                        <div class='Tab_Content_Graph'>
                            <h4>CountryPopulation<h4>
                        </div>
                    </div>
                    """;
                            
                if(Country_drop == null|| CountryYearMin_drop == null || CountryYearMax_drop == null){
                    html = html + "<div class='Tab_Data_Container'>";
                            html = html + "<p>AvgTemp: " + CommonElements.nodata() + "</p>";
                            html = html + "<p>Population: " + CommonElements.nodata() + "</p>";
                        html = html + "</div>";
                    html = html + "</div>"; 
                }

                else {   
                html = html + """
                    <div class='Tab_Data_Container'>
                        """;
                        html = html + "<p>Country selected: " + Country_drop + "</p>";
                        html = html + "<p>Year selected: " + CountryYearMin_drop + " - " + CountryYearMax_drop + "</p><br>";
                        html = html + "<p>Average Temp: " + PageIndex.getAvgTemp_CountryYear(Country_drop, CountryYearMin_drop) + " - " + PageIndex.getAvgTemp_CountryYear(Country_drop, CountryYearMax_drop) + "</p>";                                
                        //html = html + "<p>AvgTemp Diff: " + getAvgTempDiff_CountryYear(CountryYearMin_drop, CountryYearMax_drop) + " </p><br>";

                        html = html + "<p>Population Year: " + CountryYearMin_drop + " = " + PageIndex.getpopulation_CountryYear(Country_drop, CountryYearMin_drop) + " people";
                        html = html + "<p>Population Year: " + CountryYearMax_drop + " = " + PageIndex.getpopulation_CountryYear(Country_drop, CountryYearMax_drop) + " people";
                        html = html + "<p>Population change: " + getPopulationDiff(Country_drop, WorldYearMin_drop, WorldYearMax_drop) + " people";

                        query = "Select IFNULL(c.year, \"no data\") AS Year, IFNULL(c.country, \"no data\") AS Country, IFNULL(c.avg, \"no data\") AS Avg, IFNULL(c.min,\"no data\") AS Min, IFNULL(c.max, \"no data\") AS Max, IFNULL(p.population, \"no data\") AS Population from Country c LEFT JOIN population p ON p.country = c.country AND p.year = c.year Where c.country = '" + Country_drop + "' AND c.Year Between " + CountryYearMin_drop + " AND " + CountryYearMax_drop;

                        html = html + """
                            
                    </div>
                </div>
                """;
                }
                

            //Create state tab
            html = html + CommonElements.tabLabel("State", "not checked");

                //Content state tab
                html = html + CommonElements.dropdownFormOpen("State", "/page2A.html");
                    html = html + CommonElements.dropDownOptionOpen("State", "State", " -- Select a State -- ");

                        ResultSet StateList = con.execute("Select DISTINCT State from State Order BY State ASC;");
                        while(StateList.next()){
                            html = html + "<option value='" + StateList.getString("State") + "'>" + StateList.getString("State") + "</option>";
                        }
                    
                    html = html + CommonElements.dropDownOptionClose();

                    html = html + CommonElements.dropDownOptionOpen("StateYearMin", "YearMin", " -- Select a Year -- ");

                        ResultSet StateYearMin = con.execute("Select DISTINCT Year from State ORDER BY State DESC;");
                        while(StateYearMin.next()){
                            html = html + "<option value='" + StateYearMin.getString("Year") + "'>" + StateYearMin.getString("Year") + "</option>";
                        }

                    html = html + CommonElements.dropDownOptionClose();       
                    
                    html = html + CommonElements.dropDownOptionOpen("StateYearMax", "YearMax", " -- Select a Year -- ");

                        ResultSet StateYearMax = con.execute("Select DISTINCT Year from State ORDER BY Year DESC;");
                        while(StateYearMax.next()){
                            html = html + "<option value='" + StateYearMax.getString("Year") + "'>" + StateYearMax.getString("Year") + "</option>";
                        }
                    
                    html = html + CommonElements.dropDownOptionClose();       
                html = html + CommonElements.dropDownSubmit("View Data");

                //form param for State year selection
                String State_drop = context.formParam("State_drop");
                String StateYearMin_drop = context.formParam("StateYearMin_drop");
                String StateYearMax_drop = context.formParam("StateYearMax_drop");
               
                html = html + """
                    <div class='Tab_Content_Graph_Container'>
                        <div class='Tab_Content_Graph'>
                            <h4>StateAvgTemp<h4>
                        </div>
                    </div>
                    """;
                
                if(State_drop == null|| StateYearMin_drop == null || StateYearMax_drop == null){
                    html = html + "<div class='Tab_Data_Container'>";
                            html = html + "<p>AvgTemp: " + CommonElements.nodata() + "</p>";
                        html = html + "</div>";
                    html = html + "</div>";
                }
                else {
                html = html + """
                    <div class='Tab_Data_Container'
                        """;
                        html = html + "<p>State selected: " + State_drop + "</p>";
                        html = html + "<p>Year selected: " + StateYearMin_drop + " - " + StateYearMax_drop + "</p><br>";

                        //html = html + "<p>AvgTemp Diff: " + getAvgTempDiff("State", State_drop, StateYearMin_drop, StateYearMax_drop) + " degrees</p>";
                        // query for table
                        query = "Select * from State Where State = '" + State_drop + "' AND Year Between " + StateYearMin_drop + " AND " + StateYearMax_drop;
                    
                        html = html + """
                            
                    </div>
                </div>
                """;
                }

            //Create City Tab
            html = html + CommonElements.tabLabel("City", "not checked");

                //Content City tab
                html = html + CommonElements.dropdownFormOpen("City", "/page2A.html");
                    html = html + CommonElements.dropDownOptionOpen("City", "City", " -- Select a City -- ");
            
                        ResultSet CityList = con.execute("Select DISTINCT City from City Order BY City ASC;");
                        while(CityList.next()){
                            html = html + "<option value='" + CityList.getString("City") + "'>" + CityList.getString("City") + "</option>";
                        }

                    html = html + CommonElements.dropDownOptionClose();

                    html = html + CommonElements.dropDownOptionOpen("CityYearMin", "YearMin", " -- Select a start Year -- ");

                        ResultSet CityYearMin = con.execute("Select DISTINCT Year from City ORDER BY Year DESC;");
                        while(CityYearMin.next()){
                            html = html + "<option value='" + CityYearMin.getString("Year") + "'>" + CityYearMin.getString("Year") + "</option>";
                        }
                    
                    html = html + CommonElements.dropDownOptionClose();

                    html = html + CommonElements.dropDownOptionOpen("CityYearMax", "YearMax", " -- Select an end Year -- ");

                        ResultSet CityYearMax = con.execute("Select DISTINCT Year from City ORDER BY Year DESC;");
                        while(StateYearMax.next()){
                            html = html + "<option value='" + CityYearMax.getString("Year") + "'>" + CityYearMax.getString("Year") + "</option>";
                        }
                    
                    html = html + CommonElements.dropDownOptionClose();
                html = html + CommonElements.dropDownSubmit("View Data");


                //form param for State year selection
                String City_drop = context.formParam("City_drop");
                String CityYearMin_drop = context.formParam("CityYearMin_drop");
                String CityYearMax_drop = context.formParam("CityYearMax_drop");
                
                html = html + """
                <div class='Tab_Content_Graph_Container'>
                    <div class='Tab_Content_Graph'>
                        <h4>CityAvgTemp<h4>
                    </div>
                </div>
                """;

                if(City_drop == null|| CityYearMin_drop == null || CityYearMax_drop == null){
                    html = html + "<div class='Tab_Data_Container'>";
                        html = html + "<p>AvgTemp:" + CommonElements.nodata() + "</p>";
                    html = html + "</div>";
                html = html + "</div>";
                }
                else {
                    html = html + """
                    <div class='Tab_Data_Container'
                        """;
                    html = html + "<p>City selected: " + City_drop + "</p>";
                    html = html + "<p>Year selected: " + CityYearMin_drop + " - " + CityYearMax_drop + "</p><br>";

                    //html = html + "<p>AvgTemp Diff: " + getAvgTempDiff("City", City_drop, CityYearMin_drop, CityYearMax_drop) + " degrees</p>";
                    
                    // query for table
                    query = "Select * from City Where City = '" + City_drop + "' AND Year Between " + CityYearMin_drop + " AND " + CityYearMax_drop;
                    html = html + """
                    </div>
                </div>
                """;
                }
                //close tabs
                html = html + "</dv>";
                html = html + "</div>";
            //close tab container
            html = html + "</div>";
            html = html + "</div>";
        html = html + """
            <div class='table_container'>
            """;
            
            html = html + CommonElements.Table(query);
                

            html = html + "</div>";
     
            
        //close table conatianer
        html = html + "</div>";



        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

    public String getAvgTempDiff(String region, String YearMin, String YearMax) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "SELECT ((SELECT AVG FROM " + region + " WHERE year BETWEEN " + YearMin + " AND " + YearMax + " ORDER BY Year DESC LIMIT 1) - (SELECT AVG FROM "  + region + " WHERE year BETWEEN " + YearMin + " AND " + YearMax + " ORDER BY Year ASC LIMIT 1)) / (SELECT AVG FROM " + region + " WHERE year BETWEEN " + YearMin + " AND " + YearMax + " ORDER BY Year ASC LIMIT 1) * 100 AS AVG";
        ResultSet AvgTempDiffDB = con.execute(query);
        String AvgTempDiff = "does not exist";
        while(AvgTempDiffDB.next()){
            AvgTempDiff = AvgTempDiffDB.getString("AVG");
        } 
        if (Double.parseDouble(AvgTempDiff) > 0){
            return AvgTempDiff + "% increase";
        }
        else if (Double.parseDouble(AvgTempDiff) < 0){
            return AvgTempDiff + "% decrease";
        }
        else{
            return AvgTempDiff + "%";
        }
        
    }
    public String getLandOceanAvgDiff(String region, String YearMin, String YearMax){
        return YearMin;
    }

    public String getPopulationDiff(String region, String YearMin, String YearMax){
        return YearMin;
    }

    public String getAvgTempDiff_CountryYear(String YearMin, String YearMax){
        return YearMin;
    }

    public String populationDiff_CountryYear(String YearMin, String YearMax){
        return YearMin;
    }

    public String getAvgTempDiff_StateYear(String state, String YearMin, String YearMax){
        return YearMin;
    }

    public String getAvgTempDiff_CityYear(String city, String YearMin, String YearMax){
        return YearMax;
    }

}
