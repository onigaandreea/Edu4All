package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        try(PreparedStatement preStmt=con.prepareStatement("insert into Volunteers (firstName, lastName, email, password, birthDate, phone) values (?, ?, ?, ?, ?, ?")){
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
        logger.traceEntry("deleting volunteer {} ", elem);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Volunteers where id=?")) {
            preStmt.setInt(1, elem.getId());
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Volunteer elem, Integer id) {
        logger.traceEntry("updating volunteer with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Volunteers set firstName=?, lastName=?, email=?, password=?, birthDate=?, phone=? where id=?")) {
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setString(4, elem.getPassword());
            preStmt.setString(5, elem.getBirthDate().toString());
            preStmt.setString(6, elem.getPhone());
            preStmt.setInt(7, id);
            int result=preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }

    @Override
    public Volunteer findById(Integer id) {
        logger.traceEntry("finding volunteer with the id {} ", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Volunteers where id=?")) {
            preStmt.setInt(1, id);
            try(ResultSet result=preStmt.executeQuery()){
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String email = result.getString("email");
                String password = result.getString("password");
                String birth_date_string = result.getString("birthDate");
                LocalDateTime birthDate = LocalDateTime.parse(birth_date_string);
                String phone = result.getString("phone");
                Volunteer volunteer = new Volunteer(firstName, lastName, email, password, birthDate, phone);
                volunteer.setId(id);
                logger.trace("Found {} instance", result);
                return volunteer;
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Volunteer> findAll() {
        logger.traceEntry("finding all the volunteers");
        Connection con= dbUtils.getConnection();
        List<Volunteer> volunteers = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Volunteers")) {
            try(ResultSet result=preSmt.executeQuery()) {
                while(result.next()) {
                    int id = result.getInt("id_swimmer");
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    String birth_date_string = result.getString("birthDate");
                    LocalDateTime birthDate = LocalDateTime.parse(birth_date_string);
                    String phone = result.getString("phone");
                    Volunteer volunteer = new Volunteer(firstName, lastName, email, password, birthDate, phone);
                    volunteer.setId(id);
                    volunteers.add(volunteer);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(volunteers);
        return volunteers;
    }

    @Override
    public Volunteer findByEmailPassword(String email, String password) {
        logger.traceEntry("finding volunteer with the email {} and password {}", email, password);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Volunteers where email=? and password=?")) {
            preStmt.setString(1, email);
            preStmt.setString(2, password);
            try(ResultSet result=preStmt.executeQuery()){
                Integer id = result.getInt("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String birth_date_string = result.getString("birthDate");
                LocalDateTime birthDate = LocalDateTime.parse(birth_date_string);
                String phone = result.getString("phone");
                if(firstName!=null && lastName!=null && birthDate!=null && phone!=null) {
                    Volunteer volunteer = new Volunteer(firstName, lastName, email, password, birthDate, phone);
                    volunteer.setId(id);
                    logger.trace("Found {} instance", result);
                    return volunteer;
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
        return null;
    }

}
