package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class VolunteerRepository implements IVolunteerRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public VolunteerRepository(Properties properties) {
        logger.info("Initializing VolunteerRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(Volunteer elem) {
        logger.traceEntry("saving volunteer {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Volunteer (firstName, lastName, email, password, birthDate, phone) values (?, ?, ?, ?, ?, ?")){
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setString(4, elem.getPassword());
            preStmt.setString(5, elem.getBirthDate().toString());
            preStmt.setString(6, elem.getPhone());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Volunteer elem) {

    }

    @Override
    public void update(Volunteer elem, Integer id) {

    }

    @Override
    public Volunteer findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Volunteer> findAll() {
        return null;
    }

    @Override
    public Volunteer findByEmailPassword(String email, String password) {
        return null;
    }

}
