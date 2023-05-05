package project;

public interface IApplicationRepo extends Repository<Application, Integer> {
    Application findByIds(Integer idV, Integer idP);
}
