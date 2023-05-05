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

public class ApplicationRepository implements IApplicationRepo{

    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private IVolunteerRepo volunteerRepo;
    private IProgramRepo programRepo;

    public ApplicationRepository(Properties properties) {
        logger.info("Initializing ApplicationRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(Application elem) {
        logger.traceEntry("saving application {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Application(idV, idP) values (?, ?)")) {
            preStmt.setInt(1, elem.getVolunteer().getId());
            preStmt.setInt(2, elem.getProgram().getId());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Application elem) {
        logger.traceEntry("deleting application {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Application where idV=? AND idP=?")) {
            preStmt.setInt(1, elem.getVolunteer().getId());
            preStmt.setInt(2, elem.getProgram().getId());
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Application elem, Integer id) {

    }

    @Override
    public Application findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Application> findAll() {
        logger.traceEntry("finding all applications");
        Connection con= dbUtils.getConnection();
        List<Application> applications = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Application")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    Integer idV= result.getInt("idV");
                    Integer idP = result.getInt("idP");
                    Volunteer volunteer = volunteerRepo.findById(idV);
                    Program program = programRepo.findById(idP);
                    Application application = new Application(program, volunteer);
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(applications);
        return applications;
    }

    @Override
    public Application findByIds(Integer idV, Integer idP) {
        logger.traceEntry("finding application with the ids {} {} ", idV, idP);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Application where idV=? and idP=?")) {
            preStmt.setInt(1, idV);
            preStmt.setInt(2, idP);
            try(ResultSet result=preStmt.executeQuery()){
                Volunteer volunteer = volunteerRepo.findById(idV);
                Program program = programRepo.findById(idP);
                Application application = new Application(program, volunteer);
                logger.trace("Found {} instance", result);
                return application;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }
}
