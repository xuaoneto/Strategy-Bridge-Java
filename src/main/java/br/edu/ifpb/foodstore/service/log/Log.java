package br.edu.ifpb.foodstore.service.log;

public interface Log {
    //    padrao bridge que consiste em dividir classes em 2 fases abstração e implementação, aqui está a abstração...
    void debug(String message);
    void info(String message);
    void error(String message);
}
