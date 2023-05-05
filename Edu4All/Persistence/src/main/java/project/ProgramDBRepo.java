package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static project.utils.Constants.DATE_TIME_FORMATTER;

public class ProgramDBRepo implements IProgramRepo{

    private JDBCUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ProgramDBRepo(Properties props){
        System.out.println("Initializing DB with properties: " + props);
        dbUtils = new JDBCUtils(props);
    }

    @Override
    public void add(Program elem) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement ps = conn.prepareStatement("insert into Programs(name, description, startDate, endDate, location) values (?, ?, ?, ?, ?)")){
            ps.setString(1, elem.getName());
            ps.setString(2, elem.getDescription());
            ps.setString(3, elem.getStartDate().format(DATE_TIME_FORMATTER));
            ps.setString(4, elem.getEndDate().format(DATE_TIME_FORMATTER));
            ps.setString(5, elem.getLocation());

            ps.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error ProgramDBRepo: "+ ex);
        }
    }

    @Override
    public void delete(Program elem) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("delete from Programs where id=?")){
                ps.setInt(1, elem.getId());

                ps.executeUpdate();
        }catch (SQLException ex){
            System.out.println("Error ProgramDBRepo: "+ ex);
        }
    }

    @Override
    public void update(Program elem, Integer id) {
        Connection con = dbUtils.getConnection();
        try(PreparedStatement ps = con.prepareStatement("update programs set name=? where id=?")){
            ps.setString(1, elem.getName());
            ps.setInt(2, id);
           ps.executeUpdate();
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Program findById(Integer id) {
        Connection con = dbUtils.getConnection();
        Program f = null;
        try(PreparedStatement ps = con.prepareStatement("select * from Programs where id=?")){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    int idF = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    LocalDateTime sDate = LocalDateTime.parse(rs.getString("startDate"), DATE_TIME_FORMATTER);
                    LocalDateTime eDate = LocalDateTime.parse(rs.getString("endDate"), DATE_TIME_FORMATTER);
                    String location = rs.getString("location");

                    f = new Program(name, description, sDate, eDate, location);
                    f.setId(idF);
                }
            }
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return f;
    }
    @Override
    public Iterable<Program> findAll() {
        logger.traceEntry("[ProgramDBRepo]: Entering findAll...");
        Connection con = dbUtils.getConnection();
        List<Program> allT= new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement("select * from Programs")){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String descr = rs.getString("description");
                    LocalDateTime stDate = LocalDateTime.parse(rs.getString("startDate"), DATE_TIME_FORMATTER);
                    LocalDateTime eDate = LocalDateTime.parse(rs.getString("endDate"), DATE_TIME_FORMATTER);
                    String location = rs.getString("location");
                    Program p = new Program(name, descr, stDate, eDate, location);
                    p.setId(id);
                    allT.add(p);
                }
            }
        }catch (SQLException ex){
            System.out.println("Error ProgramDBRepo: "+ex.getMessage());
        }
        return allT;
    }

    @Override
    public Program findByName(String name) {
        Connection con = dbUtils.getConnection();
        Program found = null;
        try(PreparedStatement ps = con.prepareStatement("select * from Programs where name=?")){
            ps.setString(1, name);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String nameP = rs.getString("name");
                    String description = rs.getString("description");
                    LocalDateTime sDate = LocalDateTime.parse(rs.getString("startDate"), DATE_TIME_FORMATTER);
                    LocalDateTime eDate = LocalDateTime.parse(rs.getString("endDate"), DATE_TIME_FORMATTER);
                    String location = rs.getString("location");
                    found = new Program(nameP, description, sDate, eDate, location);
                    found.setId(id);
                }
            }
        }catch (SQLException ex){
            System.out.println("Error ProgramDBRepo: "+ex.getMessage());
        }
        return found;
    }
}
