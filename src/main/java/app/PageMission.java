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
        ResultSet personas = con.execute("Select * From Persona");
        ResultSet students = con.execute("Select * From Student");
        
        html += CommonElements.DocumentStart("Mission Statement");
        html += CommonElements.Header();

        // Contributions will mostly go here.
        html += "Testing SQL return values:";
        while(students.next()){
            html += students.getString("StudentNum") + ": " + students.getString("Name") + "\n\n";
        }

        html += CommonElements.Table("Select * From Student");

        html += CommonElements.Footer();
        html += CommonElements.DocumentEnd();

        context.html(html);
        con.close();
    }

}
