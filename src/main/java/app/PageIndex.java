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
public class PageIndex implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        
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
                <h4>AvgGlobalTemp</h4>
                <p>|------------------|XXX - XXX</p>
                
                <h4>LandOceanAvgTemp</h4>
                <p>|------------------|XXX - XXX</p>
                
                <h4>TotalPopulation</h4>
                <p>|------------------|XXX - XXX</p>
                
                <h4>TotalPopulation</h4>
                <p>|------------------|XXX - XXX</p>
                <br></br>
            </div>
        </div>
        """;
        
        //Tabs and Tab content

        //Tab container
        html = html + """
        <div class='Tabs_Container'>
            <div class='tabs'>
                """;
                
                //CREATE World Tab
                html = html + """
                <input type='radio' class='tabs__radio' name='regions' id='WorldTab' checked>
                <label for='WorldTab' class='tabs__label'>World</label>
                    <div class='tabs__content'>
                        """;
                        //Content for world tab
                        html = html + """
                        <h3>World</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='WorldYear_drop'>World: Select a year(Dropdown):</label>
                                <select id='WorldYear_drop' name='WorldYear_drop'>
                                """;
                                    //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                    //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> YEARSOFWORLD = jdbc.getYEARSOFWORLD();
                                    // for (int i = 0; i <select YEARSOFWORLD.size(); i++){
                                    //     html = html + "         <option>" + YEARSOFWORLD.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params

                                    html = html + """
                                    <option value='2000'>2000</option>
                                    <option value='2001'>2001</option>
                                    <option value='2002'>2002</option>
                                    <option value='2003'>2003</option>
                                </select>
                            </div>
                            <button type 'submit' class'btn'>Get year data:)</button>
                        </form>
                            """;
                        
                        //form param for year selection
                        String WorldYear_drop = context.formParam("WorldYear_drop");
                        if (WorldYear_drop == null){
                            html = html + """                                
                            <div class='World_Graph_Container'>
                                <div class='WorldAvgTempGraph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='WorldOceanLandTempGraph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='WorldPopulationGraph'>
                                    <h4>A graph I guess?</h1>
                                </div>
                                </div>
                                        <br></br>
                                        <p>AvgTemp:XX.X*</p>
                                        <p>LandOCeanAvg:XX.X*</p>
                                        <p>Population:XXXXXXX
                                </div>
                            """;
                        }
                        else {
                            html = html + """                                
                            <div class='World_Graph_Container'>
                                <div class='WorldAvgTempGraph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='WorldOceanLandTempGraph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='WorldPopulationGraph'>
                                    <h4>A graph I guess?</h1>
                                </div>
                                </div>
                                        <br></br>
                                        """;
                                        html = html + "<p>AvgTemp:" + getAvgTemp_World(WorldYear_drop) + " degrees</p>";
                                        html = html + "<p>LandOcreanAvg:" + getLandOceanAvg_World(WorldYear_drop) + " degrees</p>";
                                        html = html + "<p>Population:" + getPopulation_World(WorldYear_drop) + " degrees";
                                    
                                    html = html + """
                                    <p>This is where all the <i>World</i> data will go:)</p>
                            </div>
                            """;
                        }
                        
                    
                //CREATE Country Tab
                html = html + """
                <input type='radio' class='tabs__radio' name='regions' id='CountryTab'>
                <label for='CountryTab' class='tabs__label'>Country</label>
                    <div class='tabs__content'>
                        """;
                        //Content for Country Tab
                        html = html + """
                        <h3>Country</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='Country_drop'>Country: Select a Country(Dropdown):</label>
                                <select id='Country_drop' name='Country_drop'>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYLIST = jdbc.getCOUNTRYLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params
                                    
                                    html = html + """
                                    <option value='Brazil'>Brazil</option>
                                    <option value='Croatia'>Croatia</option>
                                    <option value='Pvislavia'>Pvislavia</option>
                                    <option value='Chicoglio'>Chicoglio</option>
                                </select>
                            </div>
                            """;
                                    
                            html = html + """
                            <div class='form-group'>
                                <label for='CountryYear_drop'>Year: Select a Year(Dropdown):</label>
                                <select id='CountryYear_drop' name='CountryYear_drop'>
                                """;
                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYYEARLIST = jdbc.getCOUNTRYYEARLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params
                                    html = html + """
                                    <option value='2000'>2000</option>
                                    <option value='2001'>2001</option>
                                    <option value='2002'>2002</option>
                                    <option value='2003'>2003</option>
                                </select>
                            </div>
                            """;
                            html = html + """
                                <button type 'submit' class'btn'>Get year data:)</button>
                            </form>
                            """;
                                
                        //form param for country selection
                        String Country_drop = context.formParam("Country_drop");
                        String CountryYear_drop = context.formParam("CountryYear_drop");
                        if (Country_drop == null || CountryYear_drop == null){
                            html = html + """
                            <div class='Country_Graph_Container'>
                                <div class='CountryAvgTemp_Graph'>
                                    <h4>A graph I guess?<h4>
                                </div>
                                <div class='CountryPopulation_Graph'>
                                    <h4>A graph I guess?<h4>
                                </div>

                                
                                <div class='Country_data_wrapper'>
                                    <br></br>
                                    <p>AvgTemp:XX.X*</p>
                                    <p>Population:XXXXXXX</p>
                                </div>
                            </div>
                            """;
                        }
                        else {
                            html = html + """
                            <div class='Country_Graph_Container'>
                                <div class='CountryAvgTemp'>
                                    <h4>A graph I guess?<h4>
                                </div>
                                <div class='CountryPopulation'>
                                    <h4>A graph I guess?<h4>
                                </div>
                            </div>
                                
                            <div class='Country_data_wrapper'>
                                <br></br>
                                """;
                                html = html + "<p>AvgTemp:" + getAvgTemp_CountryYear(Country_drop, CountryYear_drop) + " degrees</p>";
                                html = html + "<p>Population" + population_CountryYear(Country_drop, CountryYear_drop) + " people</p>";

                                html = html + """        
                                    <p>This is where all the <i>Country</i> data will go:)</p>
                                </div>
                            </div>
                        """;   
                        }
                    //end tab
                    html = html + "</div>";
                    
            
                //Create State Tab
                html = html + """
                <input type='radio' class='tabs__radio' name='regions' id='StateTab'>
                <label for='StateTab' class='tabs__label'>State</label>
                    <div class='tabs__content'>
                        """;
                        //Content for State Tab
                        html = html + """
                        <h3>State</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='State_drop'>State: Select a State(Dropdown):</label>
                                <select id='State_drop' name='State_drop'>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYLIST = jdbc.getCOUNTRYLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params     
                                    html = html + """
                                    <option value='Victoria'>Victoria</option>
                                    <option value='Western Australia'>Western Australia</option>
                                    <option value='Tasmania'>Tasmania</option>
                                    <option value='South Australia'>South Australia</option>
                                </select>
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='StateYear_drop'>Year: Select a Year(Dropdown):</label>
                                <select id='StateYear_drop' name='StateYear_drop'>
                                """;
                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYYEARLIST = jdbc.getCOUNTRYYEARLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params
                                    html = html + """
                                    <option value='2000'>2000</option>
                                    <option value='2001'>2001</option>
                                    <option value='2002'>2002</option>
                                    <option value='2003'>2003</option>
                                </select>
                            </div>
                            """;
                            html = html + """
                                <button type 'submit' class'btn'>Get year data:)</button>
                            </form>
                            """;

                        //form param for state selection
                        String State_drop = context.formParam("State_drop");
                        String StateYear_drop = context.formParam("StateYear_drop");
                        if (State_drop == null || StateYear_drop == null){
                            html = html + """
                            <div class='State_Graph_Container'>
                                <div class='StateAvgTemp_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='StatePopulation_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                    
                                <div class='State_data_wrapper'>
                                    <br></br>
                                    <p>AvgTemp:XX.X*</p>                                        
                                    <p>Population:XXXXXXX</p>
                                </div>
                            </div>
                            """;
                        }
                        else{
                            html = html + """
                            <div class='State_Graph_Container'>
                                <div class='StateAvgTemp_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='StatePopulation_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                            </div>
                                
                            <div class='State_data_wrapper'>
                                <br></br>
                                """;
                                html = html + "<p>AvgTemp:" + getAvgTemp_StateYear(State_drop, StateYear_drop) + " degrees</p>";
                                html = html + "<p>Population" + population_StateYear(State_drop, StateYear_drop) + " people</p>";
                                
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
                <input type='radio' class='tabs__radio' name='regions' id='CityTab'>
                <label for='CityTab' class='tabs__label'>City</label>
                    <div class='tabs__content'>
                        """;
                        //Content for City Tab
                        html = html + """
                        <h3>City</h3>
                        <form action='/' method='post'>
                            <div class='form-group'>
                                <label for='City_drop'>City: Select a City(Dropdown):</label>
                                <select id='City_drop' name='City_drop'>
                                """;
                                //TODO work out which type of diplay (drop down/scroll/spin a wheel)

                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYLIST = jdbc.getCOUNTRYLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params     
                                    html = html + """
                                    <option value='Melbourne'>Melbourne</option>
                                    <option value='Hobart'>Hobart</option>
                                    <option value='Perth'>Perth</option>
                                    <option value='Sydney'>Sydney</option>
                                </select>
                            </div>
                            """;

                            html = html + """
                            <div class='form-group'>
                                <label for='CityYear_drop'>Year: Select a Year(Dropdown):</label>
                                <select id='CityYear_drop' name='CityYear_drop'>
                                """;
                                //JDBCConnection jdbc = new JDBCConnection();
                                    //ArrayList<String> COUNTRYYEARLIST = jdbc.getCOUNTRYYEARLIST();
                                    // for (int i = 0; i <select COUNTRYLIST.size(); i++){
                                    //     html = html + "         <option>" + COUNTRYLIST.get(i) + "</option>";
                                    // }
                                    // this is all yoinked from studio workshop 6 and reworked with suitible params
                                    html = html + """
                                    <option value='2000'>2000</option>
                                    <option value='2001'>2001</option>
                                    <option value='2002'>2002</option>
                                    <option value='2003'>2003</option>
                                </select>
                            </div>
                            """;
                            html = html + """
                                <button type 'submit' class'btn'>Get year data:)</button>
                            </form>
                            """;

                        //form param for state selection
                        String City_drop = context.formParam("City_drop");
                        String CityYear_drop = context.formParam("CityYear_drop");
                        if (City_drop == null || CityYear_drop == null){
                            html = html + """
                            <div class='City_Graph_Container'>
                                <div class='CityAvgTemp_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='CityPopulation_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>

                                <div class='City_data_wrapper'>
                                    <br></br>
                                    <p>AvgTemp:XX.X*</p>                                        
                                    <p>Population:XXXXXXX</p>
                                </div>
                            </div>
                            """;
                        }
                        else{
                            html = html + """
                            <div class='City_Graph_Container'>
                                <div class='CityAvgTemp_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                                <div class='CityPopulation_Graph'>
                                    <h4>A graph I guess?</h4>
                                </div>
                            </div>
                                
                            <div class='City_data_wrapper'>
                                <br></br>
                                """;
                                html = html + "<p>AvgTemp:" + getAvgTemp_CityYear(City_drop, CityYear_drop) + " degrees</p>";
                                html = html + "<p>Population" + population_CityYear(City_drop, CityYear_drop) + " people</p>";
                                
                                html = html + """
                                    <p>This is where all the <i>City</i> data will go:)</p>
                            </div>
                        </div>
                        """;
                        }
                    html = html + """
                    </div>
                </div>
            </div>
        </div>
   
    """;

        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }


    public String getAvgTemp_World(String Year){
        return Year;
    }

    public String getLandOceanAvg_World(String Year){
        return Year;
    }

    public String getPopulation_World(String Year){
        return Year;
    }

    public String getAvgTemp_CountryYear(String Country, String Year){
        return Year;

    }

    public String population_CountryYear(String Country, String Year){
        return Year;
    }

    public String getAvgTemp_StateYear(String State, String Year){
        return Year;
    }

    public String population_StateYear(String State, String Year){
        return Year;
    }    

    public String getAvgTemp_CityYear(String City, String Year){
        return Year;
    }

    public String population_CityYear(String City, String Year){
        return Year;
    }             
}