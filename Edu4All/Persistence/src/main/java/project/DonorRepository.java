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

public class DonorRepository implements IDonorRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private IAccountRepo accountRepo;

    public DonorRepository(Properties properties, IAccountRepo accountRepo) {
        logger.info("Initializing AccountRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
        this.accountRepo = accountRepo;
    }
    @Override
    public void add(Donor elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Donor (firstName, lastName, email, idA) values (?, ?, ?, ?)")){
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setInt(4, elem.getAccount().getId());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Donor elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from Donor where id = ?")){
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
    public void update(Donor elem, Integer id) {
        logger.traceEntry("updating task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Donor set firstName = ?, lastName = ?, email = ?, idA = ? where id = ?")){
            preStmt.setString(1, elem.getFirstName());
            preStmt.setString(2, elem.getLastName());
            preStmt.setString(3, elem.getEmail());
            preStmt.setInt(4, elem.getAccount().getId());
            preStmt.setInt(5, id);
            int result= preStmt.executeUpdate();
            logger.trace("Updated {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Donor findById(Integer id) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Donor where id = ?")){
            preStmt.setInt(1, id);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idD = result.getInt("id");
                    int idA = result.getInt("idA");
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    String email = result.getString("email");

                    Account account = accountRepo.findById(idA);
                    Donor donor = new Donor(firstName, lastName, email, account);
                    donor.setId(idD);
                    return donor;
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
    public Iterable<Donor> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<Donor> donors = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Donor")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idD = result.getInt("id");
                    int idA = result.getInt("idA");
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    String email = result.getString("email");

                    Account account = accountRepo.findById(idA);
                    Donor donor = new Donor(firstName, lastName, email, account);
                    donor.setId(idD);
                    donors.add(donor);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(donors);
        return donors;
    }
}
