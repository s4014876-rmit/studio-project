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
        
    
        html = html + """
        <link rel='stylesheet' type='text/css' href='PageIndexStyle.css'>
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
                html = html + "<p> |------------------| <p>" + getDispRange("AVG");

                html = html + "<h4>LandOceanAvgTemp</h4>";
                html = html + "<p> |------------------| <p>" + getDispRange("LOAVG") + "</p>";

                html = html + "<h4>TotalPopulation</h4>";
                html = html + "<p> |------------------| " + getDispRange("TotalPopulation") + "</p>";

                html = html + """
                        
            </div>
        </div>
        """;
        
        //Tabs and Tab content

        //Tab container
        html = html + """
        <div class='Tabs_Container'>
            <div class='tabs'>
                """;
                
                //Create World Tab
                html = html + """
                <input type='radio' class='tabs_radio' name='regions' id='WorldTab' checked>
                <label for='WorldTab' class='tabs_label'>World</label>
                    <div class='tabs_content'>
                        """;
                        //Content for world tab
                        html = html + """
                        <h3>World</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='WorldYear_drop'>World:</label>
                                <select id='WorldYear_drop' name='WorldYear_drop'>
                                <option value='' selected disabled> -- Select a Year -- </option>
                                """;
                                    //TODO work out which type of diplay (drop down/scroll/spin a wheel)
                                    ResultSet WorldYears = con.execute("Select Year from Global ORDER BY Year DESC;");

                                    while(WorldYears.next()){
                                        html = html + "<option value='" + WorldYears.getString("Year") + "'>" + WorldYears.getString("Year") + "</option>";
                                    }
                        
                                    html = html + """
                                </select>
                            </div>
                            
                            <button type 'submit' class'btn'>View data</button>
                        </form>
                            """;
                    //form param for year selection
                    String WorldYear_drop = context.formParam("WorldYear_drop");
                    if (WorldYear_drop == null){
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
                        
                        <div class='Tab_Data_Container'>
                            <p>AvgTemp: XX.X*</p>
                            <p>LandOCeanAvg: XX.X*</p>
                            <p>Population: XXXXXXX
                        </div>
                    </div>
                    """;
                    }
                    else {
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

                        <div class='Tab_Data_Container'>
                            """;
                            html = html + "<p>AvgTemp: " + getAvgTemp_World(WorldYear_drop) + " degrees</p>";
                            html = html + "<p>LandOcreanAvg: " + getLandOceanAvg_World(WorldYear_drop) + " degrees</p>";
                            html = html + "<p>Population: " + getPopulation_World(WorldYear_drop) + " people";
                                    
                            html = html + """
                                <p>This is where all the <i>World</i> data will go:)</p>
                            </div>
                        </div>
                    """;
                    }
                  
                        
                    
                //CREATE Country Tab
                html = html + """
                <input type='radio' class='tabs_radio' name='regions' id='CountryTab'>
                <label for='CountryTab' class='tabs_label'>Country</label>
                    <div class='tabs_content'>
                        """;
                        //Content for Country Tab
                        html = html + """
                        <h3>Country</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='Country_drop'>Country:</label>
                                <select id='Country_drop' name='Country_drop'>
                                <option value='' selected disabled> -- Select a Country -- </option>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                ResultSet CountryList = con.execute("Select DISTINCT Country from Country Order BY Country ASC;");
                                
                                while(CountryList.next()){
                                    html = html + "<option value='" + CountryList.getString("Country") + "'>" + CountryList.getString("Country") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;
                                    
                            html = html + """
                            <div class='form-group'>
                                <label for='CountryYear_drop'>Year:</label>
                                <select id='CountryYear_drop' name='CountryYear_drop'>
                                <option value='' selected disabled> -- Select a Year -- </option>
                                """;
                                
                                ResultSet CountryYearList = con.execute("Select DISTINCT Year from Country Order BY Year DESC;");

                                while(CountryYearList.next()){
                                    html = html + "<option value='" + CountryYearList.getString("Year") + "'>" + CountryYearList.getString("Year") + "</option>";
                                }
                                
                                html = html + """
                                </select>
                            </div>
                            """;
                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;
                                
                        //form param for country selection
                        String Country_drop = context.formParam("Country_drop");
                        String CountryYear_drop = context.formParam("CountryYear_drop");
                        if (Country_drop == null || CountryYear_drop == null){
                            html = html + """
                            <div class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>CountryAvgTemp<h4>
                                </div>
                                <div class='Tab_Content_Graph'>
                                    <h4>CountryPopulation<h4>
                                </div>
                            </div>

                            <div class='Tab_Data_Container'>
                                <p>AvgTemp:XX.X*</p>
                                <p>Population: XXXXXXX</p>
                            </div>
                        </div>
                        """;
                        }
                        else {
                            html = html + """
                            <div class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>CountryAvgTemp<h4>
                                </div>
                                <div class='Tab_Content_Graph'>
                                    <h4>CountryPopulation<h4>
                                </div>
                            </div>
                                
                            <div class='Tab_Data_Container'>
                                """;
                                html = html + "<p>AvgTemp: " + getAvgTemp_CountryYear(Country_drop, CountryYear_drop) + " degrees</p>";
                                html = html + "<p>Population: " + population_CountryYear(Country_drop, CountryYear_drop) + " people</p>";

                                html = html + """        
                                    <p>This is where all the <i>Country</i> data will go:)</p>
                            </div>
                        </div>
                    """;   
                    }
                    
            
                //Create State Tab
                html = html + """
                <input type='radio' class='tabs_radio' name='regions' id='StateTab'>
                <label for='StateTab' class='tabs_label'>State</label>
                    <div class='tabs_content'>
                        """;
                        //Content for State Tab
                        html = html + """
                        <h3>State</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='State_drop'>State: Select a State(Dropdown):</label>
                                <select id='State_drop' name='State_drop'>
                                <option value='' selected disabled> -- Select a State -- </option>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                ResultSet StateList = con.execute("Select DISTINCT State from State Order BY State ASC;");

                                while(StateList.next()){
                                    html = html + "<option value='" + StateList.getString("State") + "'>" + StateList.getString("State") + "</option>";

                                }
                                    html = html + """
                                </select
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='StateYear_drop'>Year: Select a Year(Dropdown):</label>
                                <select id='StateYear_drop' name='StateYear_drop'>
                                <option value='' selected disabled> -- Select a Year -- </option>
                                """;
                                
                                ResultSet StateYearList = con.execute("Select DISTINCT Year from State Order BY Year DESC;");

                                while(StateYearList.next()){
                                    html = html + "<option value='" + StateYearList.getString("Year") + "'>" + StateYearList.getString("Year") + "</option>";
                                }
                                
                                html = html + """
                                </select>
                            </div>
                            """;
                    
                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                        //form param for state selection
                        String State_drop = context.formParam("State_drop");
                        String StateYear_drop = context.formParam("StateYear_drop");
                        if (State_drop == null || StateYear_drop == null){
                            html = html + """
                            < class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>StateAvgTemp</h4>
                                </div
                            </div>
                                    
                            <div class='Tab_Data_Container'>
                                <p>AvgTemp: XX.X*</p>       
                            </div>
                        </div>
                        """;
                        }
                        else{
                            html = html + """
                            <div class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>StateAvgTemp</h4>
                                </div>
                                <div class='Tab_Content_Graph'>
                                    <h4>StatePopulation</h4>
                                </div>
                            </div>
                                
                            <div class='Tab_Data_Container'>
                                """;
                                html = html + "<p>AvgTemp: " + getAvgTemp_StateYear(State_drop, StateYear_drop) + " degrees</p>";
                                
                                html = html + """
                                    <p>This is where all the <i>State</i> data will go:)</p>
                            </div>
                        </div>
                        """;
                        }
                    //end tab
                    html = html + "</div>";
                        

                
                //Create City Tab
                html = html + """
                <input type='radio' class='tabs_radio' name='regions' id='CityTab'>
                <label for='CityTab' class='tabs_label'>City</label>
                    <div class='tabs_content'>
                        """;
                        //Content for City Tab
                        html = html + """
                        <h3>City</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='City_drop'>City: Select a City(Dropdown):</label>
                                <select id='City_drop' name='City_drop'>
                                <option value='' selected disabled> -- Select a City -- </option>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                ResultSet CityList = con.execute("Select DISTINCT City from City Order BY City ASC;");

                                while(CityList.next()){
                                    html = html + "<option value='" + CityList.getString("City") + "'>" + CityList.getString("City") + "</option>";

                                }
                                    html = html + """
                                </select
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='CityYear_drop'>Year: Select a Year(Dropdown):</label>
                                <select id='CityYear_drop' name='CityYear_drop'>
                                <option value='' selected disabled> -- Select a Year -- </option>
                                """;
                                ResultSet CityYearList = con.execute("Select DISTINCT Year from City Order BY Year DESC;");

                                while(CityYearList.next()){
                                    html = html + "<option value='" + CityYearList.getString("Year") + "'>" + CityYearList.getString("Year") + "</option>";
                                }
                                
                                html = html + """
                                </select>
                            </div>
                            """;
                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                        //form param for city selection
                        String City_drop = context.formParam("City_drop");
                        String CityYear_drop = context.formParam("CityYear_drop");
                        if (City_drop == null || CityYear_drop == null){
                            html = html + """
                            <div class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>CityAvgTemp</h4>
                                </div>
                                <div class='Tab_Content_Graph'>
                                    <h4>CityPopulation</h4>
                                </div>
                            </div>

                                <div class='Tab_Data_Container'>
                                    <p>AvgTemp: XX.X*</p>                                        
                                    <p>Population: XXXXXXX</p>
                                </div>
                            </div>
                            """;
                        }
                        else{
                            html = html + """
                            <div class='Tab_Content_Graph_Container'>
                                <div class='Tab_Content_Graph'>
                                    <h4>CityAvgTemp</h4>
                                </div>
                                <div class='Tab_Content_Graph'>
                                    <h4>CityPopulation</h4>
                                </div>
                            </div>
                                
                            <div class='Tab_Data_Container'>
                                """;
                                html = html + "<p>AvgTemp: " + getAvgTemp_CityYear(City_drop, CityYear_drop) + " degrees</p>";
                                
                                html = html + """
                                    <p>This is where all the <i>City</i> data will go:)</p>
                            </div>
                        </div>
                        """;
                        }
                    //end tab
                    html = html + "</div>";
                //close tabs
                html = html + "</div>";
            //close tab conatianer
            html = html + "</div>";


        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

    public String getDispRange(String rangeType) throws Exception{
        JDBCConnection con = new JDBCConnection();
        String query = "";
        if (rangeType == "AVG"){
            query = "Select MIN(Year), MAX(Year) FROM Global;";
        }
        else if (rangeType == "LOAVG"){
            query = "Select MIN(Year), MAX(Year) FROM Global where LOAVG IS NOT NULL;";
        }
        else if (rangeType == "TotalPopulation"){
            query = "Select MIN(Year), MAX(Year) from Population";
        }
        ResultSet rangeDB = con.execute(query);
        String rangeMAX = "does not exist";
        String rangeMIN = "does not exist";
        while (rangeDB.next()){
            rangeMAX = rangeDB.getString("MAX(Year)");
            rangeMIN = rangeDB.getString("MIN(Year)");
        }    

        return "<p> " + rangeMIN + " - " + rangeMAX + "</p>";    
            
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
        JDBCConnection con = new JDBCConnection();
        String query = "Select SUM(population) AS population from population where year = " + Year;
        ResultSet populationDB = con.execute(query);
        String population = "does not exist";
        while (populationDB.next()){
            population = populationDB.getString("population");
        }
        double amount = Double.parseDouble(population);
        DecimalFormat formatter = new DecimalFormat("#,###");
        
        return formatter.format(amount);
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

    public static String population_CountryYear(String Country, String Year) throws Exception{
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