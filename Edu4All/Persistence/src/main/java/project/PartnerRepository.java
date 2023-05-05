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

public class PartnerRepository implements IPartnerRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public PartnerRepository(Properties properties) {
        logger.info("Initializing PartnerRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(Partner elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Partners (name, email, telephone) values (?, ?, ?)")){
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getEmail());
            preStmt.setString(3, elem.getPhone());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Partner elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from Partners where id = ?")){
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
    public void update(Partner elem, Integer id) {
        logger.traceEntry("updating task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Partners set name = ?, email = ?, telephone = ? where id = ?")){
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getEmail());
            preStmt.setString(3, elem.getPhone());
            preStmt.setInt(4, id);
            int result= preStmt.executeUpdate();
            logger.trace("Updated {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Partner findById(Integer id) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Partners where id = ?")){
            preStmt.setInt(1, id);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idP = result.getInt("id");
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String phone = result.getString("telephone");

                    Partner p = new Partner(name, email, phone);
                    p.setId(idP);
                    return p;
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
    public Iterable<Partner> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<Partner> partners = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Partners")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idP = result.getInt("idP");
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String phone = result.getString("telephone");

                    Partner p = new Partner(name, email, phone);
                    p.setId(idP);
                    partners.add(p);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(partners);
        return partners;
    }
}
