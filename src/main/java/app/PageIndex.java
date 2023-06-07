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
        
        html += CommonElements.DocumentStart();
        html += CommonElements.Header();

         // header for the page
         html = html + """
            <div class='header'
                <h1>
                    Homepage:)
                </h1>
            </div>
        """;
        
         // CREATE Tabs
        html = html + """
        <div class='tabs'>
            <input type='radio' class='tabs__radio' name='regions' id='WorldTab' checked>
            <label for='WorldTab' class='tabs__label'>World</label>
                <div class='tabs__content'>
                    <!--CONTENT for Tab World-->
                        <h3>World</h3>
                        <p>This is where all the <i>World</i> data will go:)</p>
                </div>
        
        <!--CREATE Country Tab-->
            <input type='radio' class='tabs__radio' name='regions' id='CountryTab'>
            <label for='CountryTab' class='tabs__label'>Country</label>
                <div class='tabs__content'>
                    <!--CONTENT for Tab CountryTab-->
                        <h3>Country</h3>
                        <p>This is where all the <i>Country</i> data will go:)</p>
                </div>
        
        <!--CREATE State Tab-->
            <input type='radio' class='tabs__radio' name='regions' id='StateTab'>
            <label for='StateTab' class='tabs__label'>State</label>
                <div class='tabs__content'>
                    <!--CONTENT for Tab StateTab-->
                        <h3>State</h3>
                        <p>This is where all the <i>State</i> data will go:)</p>
                </div>

        <!--CREATE City Tab-->
            <input type='radio' class='tabs__radio' name='regions' id='CityTab'>
            <label for='CityTab' class='tabs__label'>City</label>
                <div class='tabs__content'>
                    <!--CONTENT for Tab CityTab-->
                        <h3>City</h3>
                        <p>This is where all the <i>City</i> data will go:)</p>
                </div>
            </div>
        """;

        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
    }

}
