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

        // reference page 2A style sheet 

    //Max container = overall container 
    html = html + """
    <div class='Max_Container'
    """;




        //Tabs and tab content 

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
                    <h3>World comparison<h3>
                        <form action='/page2A.html' method='post'>
                            <div class='form-group'>
                                <label for='WorldYearMin_drop'>YearMin:</label>
                                <select id='WorldYearMin_drop' name='WorldYearMin_drop'>
                                <option value='' selected disabled> -- Select a start Year -- </option>
                                """;

                                ResultSet WorldYearMin = con.execute("Select DISTINCT Year from Global ORDER BY Year DESC;");

                                while(WorldYearMin.next()){
                                    html = html + "<option value='" + WorldYearMin.getString("Year") + "'>" + WorldYearMin.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='WorldYearMax_drop'>YearMax:</label>
                                <select id='WorldYearMax_drop' name='WorldYearMax_drop'>
                                <option value='' selected disabled> -- Select an end Year -- </option>
                                """;

                                ResultSet WorldYearMax = con.execute("Select DISTINCT Year from Global ORDER BY Year DESC;");

                                while(WorldYearMax.next()){
                                    html = html + "<option value='" + WorldYearMax.getString("Year") + "'>" + WorldYearMax.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                            //form param for world year selection 
                            String WorldYearMin_drop = context.formParam("WorldYearMin_drop");
                            String WorldYearMax_drop = context.formParam("WorldYearMax_drop");

                            if(WorldYearMin_drop == null || WorldYearMax == null){
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
                                    <p>AvgTemp diff: XX.X*</p>
                                    <p>LandOCeanAvg diff: XX.X*</p>
                                    <p>Population diff: XXXXXXX
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
                                html = html + "<p>AvgTemp Year: " + WorldYearMin_drop + " = " + PageIndex.getAvgTemp_World(WorldYearMin_drop) + "degrees</p>";
                                html = html + "<p>AvgTemp Diff: " + getAvgTempDiff(WorldYearMin_drop, WorldYearMax_drop) + " degrees</p>";

                                html = html + "<p>LandOceanAvg Year: " + WorldYearMin_drop + " = " + PageIndex.getLandOceanAvg_World(WorldYearMin_drop) + "degrees</p>";
                                html = html + "<p>LandOcreanAvg: " + getLandOceanAvgDiff_World(WorldYearMin_drop, WorldYearMax_drop) + " degrees</p>";

                                html = html + "<p>Population Year: " + WorldYearMin_drop + " = " + PageIndex.getPopulation_World(WorldYearMin_drop) + "people";
                                html = html + "<p>Population: " + getPopulationDiff_World(WorldYearMin_drop, WorldYearMax_drop) + " people";
                                    
                                html = html + """
                                <p>This is where all the <i>World</i> data will go:)</p>
                            </div>
                        </div>
                    """;
                    }

                    //Create country tab
                    html = html + """
                    <input type='radio' class='tabs_radio' name='regions' id='CountryTab'>
                    <label for='CountryTab' class='tabs_label'>Country</label>
                        <div class='tabs_content'>
                        """;
                        //Content for Country Tab
                        html = html + """
                        <h3>Country comparison<h3>
                            <form action'/page2A.html' method='post'>
                                <div class='form-group'>
                                    <label for='Country_drop'>Country:</label>
                                    <select id='Country_drop' name='Country_drop'>
                                    <option value='' selected disabled> -- Select a Country -- </option>
                                    """;
                                    //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                    ResultSet CountryList = con.execute("Select DISTINCT Country from Country Order BY Country DESC;");
                                    
                                    while(CountryList.next()){
                                        html = html + "<option value='" + CountryList.getString("Country") + "'>" + CountryList.getString("Country") + "</option>";
                                    }
                                    html = html + """
                                </select>
                            </div>
                            """;
                            
                            html = html + """
                            <div class='form-group'>
                                <label for='CountryYearMin_drop'>YearMin:</label>
                                <select id='CountryYearMin_drop' name='CountryYearMin_drop'>
                                <option value='' selected disabled> -- Select a start Year -- </option>
                                """;

                                ResultSet CountryYearMin = con.execute("Select DISTINCT Year from Country ORDER BY Year DESC;");

                                while(CountryYearMin.next()){
                                    html = html + "<option value='" + CountryYearMin.getString("Year") + "'>" + CountryYearMin.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='CountryYearMax_drop'>YearMax:</label>
                                <select id='CountryYearMax_drop' name='CountryYearMax_drop'>
                                <option value='' selected disabled> -- Select an end Year -- </option>
                                """;

                                ResultSet CountryYearMax = con.execute("Select DISTINCT Year from Country ORDER BY Year DESC;");

                                while(CountryYearMax.next()){
                                    html = html + "<option value='" + CountryYearMax.getString("Year") + "'>" + CountryYearMax.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                            //form param for country year selection
                            String Country_drop = context.formParam("Country_drop");
                            String CountryYearMin_drop = context.formParam("CountryYearMin_drop");
                            String CountryYearMax_drop = context.formParam("CountryYearMax_drop");

                            if(Country_drop == null|| CountryYearMin_drop == null || CountryYearMax_drop == null){
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
                                    //html = html + "<p>AvgTemp Year: " + CountryYearMin_drop + " = " + PageIndex.getAvgTemp_CountryYear(Country_drop, CountryYearMin_drop) + "degrees</p>";                                
                                    html = html + "<p>AvgTemp Diff: " + getAvgTempDiff_CountryYear(CountryYearMin_drop, CountryYearMax_drop) + " degrees</p>";

                                    //html = html + "<p>Population Year: " + CountryYearMin_drop + " = " + PageIndex.getAvgTemp_CountryYear(Country_drop,CountryYearMin_drop) + "people</p>";
                                    html = html + "<p>Population Diff: " + populationDiff_CountryYear(CountryYearMin_drop, CountryYearMax_drop) + " people</p>";

                                    html = html + """
                                    <p>This is where all the <i>Country</i> data will go:)</p>
                                </div>
                            </div>
                            """;
                            }

                    //Create state tab
                    html = html + """
                    <input type='radio' class='tabs_radio' name='regions' id='StateTab'>
                    <label for='StateTab' class='tabs_label'>State</label>
                        <div class='tabs_content'>
                        """;
                        //Content state tab
                        html = html + """
                         <h3>State comparison<h3>
                            <form action'/page2A.html' method='post'>
                                <div class='form-group'>
                                    <label for='State_drop'>State:</label>
                                    <select id='State_drop' name='State_drop'>
                                    <option value='' selected disabled> -- Select a State -- </option>
                                    """;

                                    ResultSet StateList = con.execute("Select DISTINCT State from State Order BY State ASC;");
                                    
                                    while(StateList.next()){
                                        html = html + "<option value='" + StateList.getString("State") + "'>" + StateList.getString("State") + "</option>";
                                    }
                                    html = html + """
                                </select>
                            </div>
                            """;
                            html = html + """
                            <div class='form-group'>
                                <label for='StateYearMin_drop'>YearMin:</label>
                                <select id='StateYearMin_drop' name='StateYearMin_drop'>
                                <option value='' selected disabled> -- Select a start Year -- </option>
                                """;

                                ResultSet StateYearMin = con.execute("Select DISTINCT Year from State ORDER BY State DESC;");

                                while(StateYearMin.next()){
                                    html = html + "<option value='" + StateYearMin.getString("Year") + "'>" + StateYearMin.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;
                            
                            html = html + """
                            <div class='form-group'>
                                <label for='StateYearMax_drop'>YearMax:</label>
                                <select id='StateYearMax_drop' name='StateYearMax_drop'>
                                <option value='' selected disabled> -- Select an end Year -- </option>
                                """;

                                ResultSet StateYearMax = con.execute("Select DISTINCT Year from State ORDER BY Year DESC;");

                                while(StateYearMax.next()){
                                    html = html + "<option value='" + StateYearMax.getString("Year") + "'>" + StateYearMax.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                            //form param for State year selection
                            String State_drop = context.formParam("State_drop");
                            String StateYearMin_drop = context.formParam("StateYearMin_drop");
                            String StateYearMax_drop = context.formParam("StateYearMax_drop");

                            if(State_drop == null|| StateYearMin_drop == null || StateYearMax_drop == null){
                                html = html + """
                                <div class='Tab_Content_Graph_Container'>
                                    <div class='Tab_Content_Graph'>
                                        <h4>StateAvgTemp<h4>
                                    </div>
                                </div>

                                    <div class='Tab_Data_Container'>
                                        <p>AvgTemp:XX.X*</p>
                                    </div>
                                </div>
                                """;
                            }
                            else {
                                html = html + """
                                <div class='Tab_Content_Graph_Container'>
                                    <div class='Tab_Content_Graph'>
                                        <h4>StateAvgTemp<h4>
                                    </div>
                                </div>
                                
                                <div class='Tab_Data_Container'
                                    """;
                                html = html + "<p>AvgTemp Diff: " + getAvgTempDiff_StateYear(State_drop, StateYearMin_drop, StateYearMax_drop) + " degrees</p>";
                                html = html + """
                                <p>This is where all the <i>State</i> data will go:)</p>
                                </div>
                            </div>
                            """;
                            }

                    //Create City Tab
                    html = html + """
                    <input type='radio' class='tabs_radio' name='regions' id='CityTab'>
                    <label for='CityTab' class='tabs_label'>City</label>
                        <div class='tabs_content'>
                        """;
                        //Content City tab
                        html = html + """
                         <h3>City comparison<h3>
                            <form action'/page2A.html' method='post'>
                                <div class='form-group'>
                                    <label for='City'>City:</label>
                                    <select id='City_drop' name='City_drop'>
                                    <option value='' selected disabled> -- Select a City -- </option>
                                    """;

                                    ResultSet CityList = con.execute("Select DISTINCT City from City Order BY City ASC;");
                                    
                                    while(CityList.next()){
                                        html = html + "<option value='" + CityList.getString("City") + "'>" + CityList.getString("City") + "</option>";
                                    }
                                    html = html + """
                                </select>
                            </div>
                            """;
                            html = html + """
                            <div class='form-group'>
                                <label for='CityYearMin_drop'>CityMin:</label>
                                <select id='CityYearMin_drop' name='CityYearMin_drop'>
                                <option value='' selected disabled> -- Select a start Year -- </option>
                                """;

                                ResultSet CityYearMin = con.execute("Select DISTINCT Year from City ORDER BY Year DESC;");

                                while(CityYearMin.next()){
                                    html = html + "<option value='" + CityYearMin.getString("Year") + "'>" + CityYearMin.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='CityYearMax_drop'>YearMax:</label>
                                <select id='CityYearMax_drop' name='CityYearMax_drop'>
                                <option value='' selected disabled> -- Select an end Year -- </option>
                                """;

                                ResultSet CityYearMax = con.execute("Select DISTINCT Year from City ORDER BY Year DESC;");

                                while(StateYearMax.next()){
                                    html = html + "<option value='" + CityYearMax.getString("Year") + "'>" + CityYearMax.getString("Year") + "</option>";
                                }
                                    html = html + """
                                </select>
                            </div>
                            """;
                            
                            html = html + """
                                <button type 'submit' class'btn'>View data</button>
                            </form>
                            """;

                            //form param for State year selection
                            String City_drop = context.formParam("City_drop");
                            String CityYearMin_drop = context.formParam("CityYearMin_drop");
                            String CityYearMax_drop = context.formParam("CityYearMax_drop");


                            if(City_drop == null|| CityYearMin_drop == null || CityYearMax_drop == null){
                                html = html + """
                                <div class='Tab_Content_Graph_Container'>
                                    <div class='Tab_Content_Graph'>
                                        <h4>CityAvgTemp<h4>
                                    </div>
                                </div>

                                    <div class='Tab_Data_Container'>
                                        <p>AvgTemp:XX.X*</p>
                                    </div>
                                </div>
                                """;
                            }
                            else {
                                html = html + """
                                <div class='Tab_Content_Graph_Container'>
                                    <div class='Tab_Content_Graph'>
                                        <h4>CityAvgTemp<h4>
                                    </div>
                                </div>
                                
                                <div class='Tab_Data_Container'
                                    """;
                                html = html + "<p>AvgTemp Diff: " + getAvgTempDiff_CityYear(City_drop, CityYearMin_drop, CityYearMax_drop) + " degrees</p>";
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
    public String getAvgTempDiff(String YearMin, String YearMax){
        return YearMin;
    }
    public String getLandOceanAvgDiff_World(String YearMin, String YearMax){
        return YearMin;
    }

    public String getPopulationDiff_World(String YearMin, String YearMax){
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
