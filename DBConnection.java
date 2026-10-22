
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/payroll_system_fresh_2026";

    private static final String USER = "root";
    private static final String PASSWORD = "arjun@2006";

  
    private static final DBConnection instance =
            new DBConnection();

  
    private DBConnection() {
    }

   
    public static DBConnection getInstance() {
        return instance;
    }

 
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                URL, USER, PASSWORD);
    }

  
    public static Connection getConnection() throws SQLException {
        return instance.createConnection();
    }
}
