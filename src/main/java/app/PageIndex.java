package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        JDBCConnection con = new JDBCConnection();
        
        html += CommonElements.DocumentStart("Homepage");
        html += CommonElements.Header();

         // header for the page
        html = html + """
        <div class='header'
            <h1>
                Homepage:)
            </h1>
        </div>
        """;
        

        //TODO make form param for which tab you are on for resubs to not revert to "world"

    //Max container - overall container
    html = html + """
    <div class='Max_Container'>
    """;

        //Data Display Container
        html = html + """
        <div class='Disp_Container'>
            <div class='World_Image'>
                <h3>Picture of world i guess</h3>
                <p>not really sure tho</p>
            </div>
            """;
            //Data Ranges Display
            html = html + """
            <div class='Data_Ranges'>
                """;
                //data range display values
                html = html + "<h4>AvgGlobalTemp</h4>";
                html = html + "<p> |------------------| <p>" + CommonElements.getMinYear("global", "AVG") + " - " +  CommonElements.getMaxYear("global", "AVG");

                html = html + "<h4>LandOceanAvgTemp</h4>";
                html = html + "<p> |------------------| <p>" + CommonElements.getMinYear("global", "LOAVG") + " - " +  CommonElements.getMaxYear("global", "LOAVG");

                html = html + "<h4>TotalPopulation</h4>";
                html = html + "<p> |------------------| " + CommonElements.getMinYear("population", "population") + " - " +  CommonElements.getMaxYear("population", "population");
;

                html = html + """
                        
            </div>
        </div>
        """;
        
        //Tabs and Tab content

        //Tab container
        html = html + CommonElements.tabContainer();
            
            //Create World Tab
            html = html + CommonElements.tabLabel("World", "checked");
                //Content for world tab

                    html = html + CommonElements.dropdownFormOpen("World", "/");
                        html = html + CommonElements.dropDownOptionOpen("WorldYear", "World", " -- Select a Year -- ");
                            
                            ResultSet WorldYears = con.execute("Select Year from Global ORDER BY Year DESC;");
                            while(WorldYears.next()){
                                html = html + "<option value='" + WorldYears.getString("Year") + "'>" + WorldYears.getString("Year") + "</option>";
                            }     

                        html = html + CommonElements.dropDownOptionClose();
                    html = html + CommonElements.dropDownSubmit("View data");
                    

                    //form param for year selection
                    String WorldYear_drop = context.formParam("WorldYear_drop");
                    
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

                    if (WorldYear_drop == null){
                        html = html + "<div class='Tab_Data_Container'>";
                            html = html + "<p>AvgTemp: " + CommonElements.nodata() + "</p>";
                            html = html + "<p>LandOCeanAvg: " + CommonElements.nodata() + "</p>";
                            html = html + "<p>Population: " + CommonElements.nodata() + "</p>";
                        html = html + "</div>";
                    html = html + "</div>";
                    }
                    else {
                        html = html + """                                
                        <div class='Tab_Data_Container'>
                            """;
                            html = html + "<p>AvgTemp: " + getAvgTemp_World(WorldYear_drop) + " degrees</p>";
                            html = html + "<p>LandOcreanAvg: " + getLandOceanAvg_World(WorldYear_drop) + " degrees</p>";
                            html = html + "<p>Population: " + getPopulation_World(WorldYear_drop);
                                    
                            html = html + """
                                <p>This is where all the <i>World</i> data will go:)</p>
                            </div>
                        </div>
                    """;
                    }   
                    
                //CREATE Country Tab
                html = html + CommonElements.tabLabel("Country", "not checked");
                    //Content for Country Tab
                    html = html + CommonElements.dropdownFormOpen("Country", "/");
                        html = html + CommonElements.dropDownOptionOpen("Country", "Country", " -- Select a Country -- ");
                                
                            ResultSet CountryList = con.execute("Select DISTINCT Country from Country Order BY Country ASC;");
                            while(CountryList.next()){
                                html = html + "<option value='" + CountryList.getString("Country") + "'>" + CountryList.getString("Country") + "</option>";
                            }
                        html = html + CommonElements.dropDownOptionClose();

                        html = html + CommonElements.dropDownOptionOpen("CountryYear", "Year", " -- Select a Year -- ");

                            ResultSet CountryYearList = con.execute("Select DISTINCT Year from Country Order BY Year DESC;");
                            while(CountryYearList.next()){
                                html = html + "<option value='" + CountryYearList.getString("Year") + "'>" + CountryYearList.getString("Year") + "</option>";
                            }
                        html = html + CommonElements.dropDownOptionClose();
                    html = html + CommonElements.dropDownSubmit("View data");

                    //form param for country selection
                    String Country_drop = context.formParam("Country_drop");
                    String CountryYear_drop = context.formParam("CountryYear_drop");

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

                    if (Country_drop == null || CountryYear_drop == null){
                        html = html + "<div class='Tab_Data_Container'>";
                            html = html +"<p>AvgTemp:" + CommonElements.nodata() + "</p>";
                            html = html + "<p>Population:" + CommonElements.nodata() + "</p>";
                        html= html + "</div>";
                    html = html + "</div>";
                    }
                    else {
                        html = html + """
                        <div class='Tab_Data_Container'>
                            """;
                            html = html + "<p>AvgTemp: " + getAvgTemp_CountryYear(Country_drop, CountryYear_drop) + " degrees</p>";
                            html = html + "<p>Population: " + getpopulation_CountryYear(Country_drop, CountryYear_drop);

                            html = html + """        
                                <p>This is where all the <i>Country</i> data will go:)</p>
                        </div>
                    </div>
                """;   
                }
                    
            
                //Create State Tab
                html = html + CommonElements.tabLabel("State", "not checked");
                    //Content for State Tab
                    html = html + CommonElements.dropdownFormOpen("State", "/");
                        html = html + CommonElements.dropDownOptionOpen("State", "State", " -- Select a State -- ");

                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                ResultSet StateList = con.execute("Select DISTINCT State from State Order BY State ASC;");

                                while(StateList.next()){
                                    html = html + "<option value='" + StateList.getString("State") + "'>" + StateList.getString("State") + "</option>";

                                }
                        html = html + CommonElements.dropDownOptionClose();
                        
                        html = html + CommonElements.dropDownOptionOpen("StateYear", "Year", " -- Select a Year -- ");

                                ResultSet StateYearList = con.execute("Select DISTINCT Year from State Order BY Year DESC;");

                                while(StateYearList.next()){
                                    html = html + "<option value='" + StateYearList.getString("Year") + "'>" + StateYearList.getString("Year") + "</option>";
                                }
                                
                        html = html + CommonElements.dropDownOptionClose();
                    html = html + CommonElements.dropDownSubmit("View data");

                    
                        
                    //form param for state selection
                    String State_drop = context.formParam("State_drop");
                    String StateYear_drop = context.formParam("StateYear_drop");
                    html = html + """
                        <class='Tab_Content_Graph_Container'>
                            <div class='Tab_Content_Graph'>
                                <h4>StateAvgTemp</h4>
                            </div
                        </div>
                        """;

                    if (State_drop == null || StateYear_drop == null){
                        html = html + "<div class='Tab_Data_Container'>";
                            html = html + "<p>AvgTemp:" + CommonElements.nodata() + "</p>";       
                        html = html + "</div>";
                    html = html + "</div>";
                    }
                    else{
                        html = html + """
                            
                        <div class='Tab_Data_Container'>
                            """;
                            html = html + "<p>AvgTemp: " + getAvgTemp_StateYear(State_drop, StateYear_drop) + " degrees</p>";
                            
                            html = html + """
                                <p>This is where all the <i>State</i> data will go:)</p>
                        </div>
                    </div>
                    """;
                    }
                   
            //Create City Tab
            html = html + CommonElements.tabLabel("City", "not checked");
                //Content for City Tab
                html = html + CommonElements.dropdownFormOpen("City","/");
                    html = html + CommonElements.dropDownOptionOpen("City", "City", " -- Select a City -- ");

                        ResultSet CityList = con.execute("Select DISTINCT City from City Order BY City ASC;");
                        while(CityList.next()){
                            html = html + "<option value='" + CityList.getString("City") + "'>" + CityList.getString("City") + "</option>";
                        }
                    html = html + CommonElements.dropDownOptionClose();

                    html = html + CommonElements.dropDownOptionOpen("CityYear", "Year", " -- Select a Year -- ");

                        ResultSet CityYearList = con.execute("Select DISTINCT Year from City Order BY Year DESC;");
                        while(CityYearList.next()){
                            html = html + "<option value='" + CityYearList.getString("Year") + "'>" + CityYearList.getString("Year") + "</option>";
                        }
                                
                    html = html + CommonElements.dropDownOptionClose();
                html = html + CommonElements.dropDownSubmit("View data");

                //form param for city selection
                String City_drop = context.formParam("City_drop");
                String CityYear_drop = context.formParam("CityYear_drop");
                html = html + """
                    <div class='Tab_Content_Graph_Container'>
                        <div class='Tab_Content_Graph'>
                            <h4>CityAvgTemp</h4>
                        </div>
                    </div>
                    """;
                    
                    if (City_drop == null || CityYear_drop == null){
                        html = html + "<div class='Tab_Data_Container'>";
                            html = html + "<p>AvgTemp:" + CommonElements.nodata() + "</p>";
                            html = html + "</div>";
                        html = html + "</div>";
                        }
                    else{
                        html = html + """ 
                        <div class='Tab_Data_Container'>
                            """;
                            html = html + "<p>AvgTemp: " + getAvgTemp_CityYear(City_drop, CityYear_drop) + " degrees</p>";
                            
                            html = html + """
                        </div>
                    </div>
                    """;
                    }
            //close tabs
            html = html + "</div>";
        //close max conatianer
        html = html + "</div>";




        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

    public static String getAvgTemp_World(String Year) throws Exception{
        
        JDBCConnection con = new JDBCConnection();
        String query = "Select AVG from Global where year = " + Year;
        ResultSet AvgTempDB = con.execute(query);
        String AvgTemp = "does not exist";
        while (AvgTempDB.next()){
            AvgTemp = AvgTempDB.getString("AVG");
        }
        return AvgTemp;
    }

    public static String getLandOceanAvg_World(String Year) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "Select LOAVG from Global where year = " + Year;
        ResultSet LandOceanAvgDB = con.execute(query);
        String LandOceanAvg = "doesnt exist";
        while (LandOceanAvgDB.next()){
            LandOceanAvg = LandOceanAvgDB.getString("LOAVG");
        } 
        return LandOceanAvg;
    }

    public static String getPopulation_World(String Year) throws Exception{
        String population = CommonElements.nodata();
        if(Double.parseDouble(Year) <= Double.parseDouble(CommonElements.getMaxYear("Population", Year)) && Double.parseDouble(Year) >= Double.parseDouble(CommonElements.getMinYear("Population", Year)) ){
            JDBCConnection con = new JDBCConnection();
            String query = "Select SUM(population) AS population from population where year = " + Year;
            ResultSet populationDB = con.execute(query);
            while (populationDB.next()){
                population = populationDB.getString("population");
            }
        double amount = Double.parseDouble(population);
        DecimalFormat formatter = new DecimalFormat("#,###");
        
        return formatter.format(amount);
        }
        return population;
    }

    public static String getAvgTemp_CountryYear(String Country, String Year) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "Select AVG from Country WHERE Country = \"" + Country + "\" AND year = " + Year + ";";
        ResultSet AvgTemp_CountryDB = con.execute(query);
        String AvgTemp_Country = "Does not exist";
        while (AvgTemp_CountryDB.next()){
            AvgTemp_Country = AvgTemp_CountryDB.getString("AVG");
        }
        return AvgTemp_Country;
    }

    public static String getpopulation_CountryYear(String Country, String Year) throws Exception{
        String population = CommonElements.nodata();
        if(Double.parseDouble(Year) <= Double.parseDouble(CommonElements.getMaxYear("Population", Year)) && Double.parseDouble(Year) >= Double.parseDouble(CommonElements.getMinYear("Population", Year)) ){
            JDBCConnection con = new JDBCConnection();
            String query = "Select population from population WHERE Country = \"" + Country + "\" AND year = " + Year + ";";
            ResultSet population_countryDB = con.execute(query);
            String population_country = "does not exist";
            while (population_countryDB.next()){
                population_country = population_countryDB.getString("Population");
            }
            double amount = Double.parseDouble(population_country);
            DecimalFormat formatter = new DecimalFormat("#,###");
            
            return formatter.format(amount);
        }
        return population;
    }

    public String getAvgTemp_StateYear(String State, String Year) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "Select AVG from State WHERE State = \"" + State + "\" AND year = " + Year + ";";
        ResultSet AvgTemp_StateDB = con.execute(query);
        String AvgTemp_State = "Does not exist";
        while (AvgTemp_StateDB.next()){
            AvgTemp_State = AvgTemp_StateDB.getString("AVG");
        }
        return AvgTemp_State;
    }
    
    public String getAvgTemp_CityYear(String City, String Year) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "Select AVG from City WHERE City = \"" + City + "\" AND year = " + Year + ";";
        ResultSet AvgTemp_CityDB = con.execute(query);
        String AvgTemp_City = "Does not exist";
        while (AvgTemp_CityDB.next()){
            AvgTemp_City = AvgTemp_CityDB.getString("AVG");
        }
        return AvgTemp_City;
    }            
}