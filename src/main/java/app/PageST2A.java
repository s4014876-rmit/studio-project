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




        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

}
