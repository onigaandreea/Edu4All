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

public class AccountRepository implements IAccountRepo{
    private JDBCUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public AccountRepository(Properties properties) {
        logger.info("Initializing AccountRepository with properties: {} ",properties);
        this.dbUtils = new JDBCUtils(properties);
    }

    @Override
    public void add(Account elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Account (accountNr, CVV, expDate) values (?, ?, ?)")){
            preStmt.setString(1, elem.getAccountNr());
            preStmt.setString(2, elem.getCVV());
            preStmt.setString(3, elem.getExpDate());
            int result= preStmt.executeUpdate();
            logger.trace("Saved {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Account elem) {
        logger.traceEntry("deleting task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete * from Account where idA = ?")){
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
    public void update(Account elem, Integer id) {
        logger.traceEntry("updating task {} ", elem);
        Connection con= dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Account set accountNr = ?, CVV = ? , expDate = ? where idA = ?")){
            preStmt.setString(1, elem.getAccountNr());
            preStmt.setString(2, elem.getCVV());
            preStmt.setString(3, elem.getExpDate());
            preStmt.setInt(4, elem.getId());
            int result= preStmt.executeUpdate();
            logger.trace("Updated {} instances", elem);
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Account findById(Integer id) {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Account where idA = ?")){
            preStmt.setInt(1, id);
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idA = result.getInt("idA");
                    String accountNr = result.getString("accountNr");
                    String CVV = result.getString("CVV");
                    String expDate = result.getString("expDate");

                    Account account = new Account(accountNr, CVV, expDate);
                    account.setId(idA);
                    return account;
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
    public Iterable<Account> findAll() {
        logger.traceEntry();
        Connection con= dbUtils.getConnection();
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preStmt=con.prepareStatement("select * from Account")){
            try (ResultSet result= preStmt.executeQuery()){
                while (result.next()){
                    int idA = result.getInt("idA");
                    String accountNr = result.getString("accountNr");
                    String CVV = result.getString("CVV");
                    String expDate = result.getString("expDate");

                    Account account = new Account(accountNr, CVV, expDate);
                    account.setId(idA);
                    accounts.add(account);
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit(accounts);
        return accounts;
    }
}
