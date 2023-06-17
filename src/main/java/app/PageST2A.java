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

}
