package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    }

    @Override
    public void update(Account elem, Integer id) {

    }

    @Override
    public Account findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Account> findAll() {
        return null;
    }
}
