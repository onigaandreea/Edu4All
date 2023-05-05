package project;

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

    }

    @Override
    public void update(Program elem, Integer id) {

    }

    @Override
    public Program findById(Integer id) {
        return null;
    }

    @Override
    public Iterable<Program> findAll() {
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
}
