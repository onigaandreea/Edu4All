package project;

public class Application extends Entity<Integer>{
    private Program program;
    private Volunteer volunteer;

    public Application(Program program, Volunteer volunteer) {
        this.program = program;
        this.volunteer = volunteer;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    @Override
    public String toString() {
        return "Application{" +
                "program=" + program +
                ", volunteer=" + volunteer +
                '}';
    }
}
