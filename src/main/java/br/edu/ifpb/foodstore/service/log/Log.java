package br.edu.ifpb.foodstore.service.log;

public interface Log {
    void debug(String message);
    void info(String message);
    void error(String message);
}
