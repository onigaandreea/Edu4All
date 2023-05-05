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

public class SocialCaseRepository implements ISocialCaseRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public SocialCaseRepository(Properties properties) {
        logger.info("Initializing SocialCaseRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(SocialCase elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into SocialCases (title, description) values (?, ?)")){
            preStmt.setString(1, elem.getTitle());
            preStmt.setString(2, elem.getDescription());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(SocialCase elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from SocialCases where id = ?")){
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
    public void update(SocialCase elem, Integer id) {
        logger.traceEntry("updating task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update SocialCases set title = ?, description = ? where id = ?")){
            preStmt.setString(1, elem.getTitle());
            preStmt.setString(2, elem.getDescription());
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
    public SocialCase findById(Integer id) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from SocialCases where id = ?")){
            preStmt.setInt(1, id);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idSC = result.getInt("id");
                    String title = result.getString("title");
                    String description = result.getString("description");

                    SocialCase sc = new SocialCase(title, description);
                    sc.setId(idSC);
                    return sc;
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
    public Iterable<SocialCase> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<SocialCase> cases = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from SocialCases")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idA = result.getInt("id");
                    String title = result.getString("title");
                    String description = result.getString("description");

                    SocialCase sc = new SocialCase(title, description);
                    sc.setId(idA);
                    cases.add(sc);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(cases);
        return cases;
    }
}

