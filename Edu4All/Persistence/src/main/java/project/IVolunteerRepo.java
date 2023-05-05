package project;

public interface IVolunteerRepo extends Repository<Volunteer, Integer>{
    Volunteer findByEmailPassword(String email, String password);
}
