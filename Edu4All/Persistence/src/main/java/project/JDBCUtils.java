package project;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    private Properties jdbcProperties;

    public JDBCUtils(Properties props){
        this.jdbcProperties = props;
    }

    private Connection instance=null;

    private Connection getNewConnection(){
        String url = jdbcProperties.getProperty("jdbc.url");
        String user = jdbcProperties.getProperty("jdbc.user");
        String password = jdbcProperties.getProperty("jdbc.password");
        Connection conn=null;
        try{
            if(user!=null && password!=null){
                conn= DriverManager.getConnection(url, user,password);
            }
            else conn= DriverManager.getConnection(url);
        }catch (SQLException ex){
            System.out.println("Error getting connection: "+ex);
            System.out.println((new File(".")).getAbsolutePath());
        }
        return conn;
    }

    public Connection getConnection(){
        try{
            if(instance==null || instance.isClosed())
                instance = getNewConnection();
        }catch (SQLException ex){
            System.out.println("Error DB: "+ ex);
        }
        return instance;
    }
}
