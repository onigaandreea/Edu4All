package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonationRepository implements IDonationRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private ISocialCaseRepo socialCaseRepo;
    private IDonorRepo donorRepo;

    public DonationRepository(Properties properties, ISocialCaseRepo socilCaseRepo, IDonorRepo donorRepo) {
        logger.info("Initializing AccountRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
        this.socialCaseRepo = socilCaseRepo;
        this.donorRepo = donorRepo;
    }
    @Override
    public void add(Donation elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Donation (idD, idSC, amount, donationTime) values (?, ?, ?, ?)")){
            preStmt.setInt(1, elem.getDonor().getId());
            preStmt.setInt(2, elem.getSocialCase().getId());
            preStmt.setInt(3, elem.getAmount());
            preStmt.setString(4, elem.getDonationTime().toString());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Donation elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from Donation where idD = ? and idSC = ?")){
            preStmt.setInt(1, elem.getDonor().getId());
            preStmt.setInt(2, elem.getSocialCase().getId());
            int result= preStmt.executeUpdate();
            logger.trace("Deleted {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Donation elem, Integer id) {

    }

    @Override
    public Donation findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Donation> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<Donation> donations = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Donation")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idD = result.getInt("idD");
                    int idSC = result.getInt("idSC");
                    int amount = result.getInt("amount");
                    String time = result.getString("donationTime");
                    LocalDateTime donationTime = LocalDateTime.parse(time);

                    Donor donor = donorRepo.findById(idD);
                    SocialCase socialCase = socialCaseRepo.findById(idSC);

                    Donation donation = new Donation(donor, socialCase, amount, donationTime);

                    donations.add(donation);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(donations);
        return donations;
    }

    @Override
    public Donation findByIds(Integer idDonor, Integer idSocialCase) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Donation where idD = ? and idSC = ?")){
            preStmt.setInt(1, idDonor);
            preStmt.setInt(2, idSocialCase);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idD = result.getInt("idD");
                    int idSC = result.getInt("idSC");
                    int amount = result.getInt("amount");
                    String time = result.getString("donationTime");
                    LocalDateTime donationTime = LocalDateTime.parse(time);

                    Donor donor = donorRepo.findById(idD);
                    SocialCase socialCase = socialCaseRepo.findById(idSC);

                    Donation donation = new Donation(donor, socialCase, amount, donationTime);
                    return donation;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
        return null;
    }
}
