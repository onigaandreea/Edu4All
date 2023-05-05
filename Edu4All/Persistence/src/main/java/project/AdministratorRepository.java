package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AdministratorRepository implements IAdministratorRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public AdministratorRepository(Properties properties) {
        logger.info("Initializing AdministratorRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(Administrator elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Administrators (email, password) values (?, ?)")){
            preStmt.setString(1, elem.getEmail());
            preStmt.setString(2, elem.getPassword());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Administrator elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from Administrators where id = ?")){
            preStmt.setInt(1, elem.getId());
            int result= preStmt.executeUpdate();
            logger.trace("Deleted {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Administrator elem, Integer id) {
        logger.traceEntry("updating task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Administrators set email = ?, password = ? where id = ?")){
            preStmt.setString(1, elem.getEmail());
            preStmt.setString(2, elem.getPassword());
            preStmt.setInt(3, id);
            int result= preStmt.executeUpdate();
            logger.trace("Updated {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Administrator findById(Integer id) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Administrators where id = ?")){
            preStmt.setInt(1, id);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idA = result.getInt("id");
                    String email = result.getString("email");
                    String password = result.getString("password");

                    Administrator admin = new Administrator(email, password);
                    admin.setId(idA);
                    return admin;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Administrator> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<Administrator> admins = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Administrators")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idA = result.getInt("id");
                    String email = result.getString("email");
                    String password = result.getString("password");

                    Administrator admin = new Administrator(email, password);
                    admin.setId(idA);
                    admins.add(admin);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(admins);
        return admins;
    }
}
