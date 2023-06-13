package app;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.sql.ResultSet;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2023. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class PageMission implements Handler {

    // URL of this page relative to http://localhost:7001/
    public static final String URL = "/mission.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "";
        JDBCConnection con = new JDBCConnection();
        
        html += CommonElements.DocumentStart("Mission Statement");
        html += CommonElements.Header();
        
        // Styling
        html +=
        """
        <style>
        .grid-container {
            width:                  80%;
            display:                grid;
            grid-template-rows:     auto 20vh 20vh;
            grid-template-columns:  175px 175px 175px 175px 175px 175px;
            grid-column-gap:        25px;
        }

        .mission {
            grid-area: 1 / 1 / 4 / 4;
        }
        """;
                
        for(int i = 1; i <= 3; i++) {
            html +=
            """
            .persona-% {
                width:          150px;
                grid-row:       1 / 2;
                grid-column:    $ / 7;
                margin-left: 12px;
            }

            .persona-text-% {
                display: none;
                background-color: white;
                overflow: scroll;
                max-height: 40%;
                padding: 0px;
                grid-area: 2 / 4 / 4 / 7;
            }

            .persona-%:hover + .persona-text-%{
                display: block;
            }
            
            .persona-text-%:hover{
                display: block;
            }

            .persona-% img {
                max-width : 150px;
                border: black solid 1px;
            }

            .persona-% h1 {
                font-size : 14pt;
                text-align: center;
            }

            .persona-text-% p {
                float: right;
                text-align: justify;
            }
            
            """
            .replace('%', (char)String.valueOf(i)  .toCharArray()[0])
            .replace('$', (char)String.valueOf(i+3).toCharArray()[0]);
        }
        html += "</style>";



        html += "<div class='grid-container'>";

        ResultSet students = con.execute("Select * From Student");
        //  Mission Statement
        html +=
        """
        <div class='mission'>
        Filler


        </div>
        """;


        //  Personas
        ResultSet personas = con.execute("Select * From Persona");
        for (int i = 1; i <= 3; i++){
            personas.next();
            html +=
            "<div class='persona-"+i+"'><img src='" + personas.getString("ImageURL") + "' alt='" + personas.getString("Name") + "'><br>" +
            "<h1>" + personas.getString("Name") + "</h1></div>" +
            "<div class='persona-text-"+i+"'><p>" + personas.getString("Text") + "</p></div>"
            ;
        }
        html += "</div>";


        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
        con.close();
    }

}
