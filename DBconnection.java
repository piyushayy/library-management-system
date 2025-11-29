/*
 * Updated for MySQL
 */
// Keep this only if your file is inside a folder named 'JFrame'
import java.sql.*;

public class DBconnection {
    
    public static Connection getConnection() {
        try {
            // 1. Load the MySQL Driver (Not Oracle)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Define the Connection URL
            // "library_ms" is the name of the database we will create in Step 2.
            String url = "jdbc:mysql://localhost:3306/library_ms";
            
            // 3. YOUR MySQL Username and Password
            // CHANGE "YOUR_PASSWORD_HERE" to the password you created during installation!
            Connection con = DriverManager.getConnection(url, "username", "password");
            
            return con;
            
        } catch (Exception e) {
            e.printStackTrace(); // This prints more detailed errors
            return null;
        }
    }

}
