package br.edu.ifpb.foodstore.service.log.impl;

import br.edu.ifpb.foodstore.service.log.Log;
import br.edu.ifpb.foodstore.service.log.impl.LogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService implements Log {

    private final LogHandler logHandler;

    @Override
    public void debug(String message) {
        logHandler.log("debug");
        logHandler.log(message);
    }
    @Override
    public void info(String message) {
        logHandler.log(message);
    }

    @Override
    public void error(String message) {
        logHandler.log("error");
        logHandler.log(message);
    }

}
