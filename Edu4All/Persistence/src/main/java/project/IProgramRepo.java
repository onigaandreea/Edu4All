package project;

public interface IProgramRepo extends Repository<Program, Integer>{
    Program findByName(String name);
}
