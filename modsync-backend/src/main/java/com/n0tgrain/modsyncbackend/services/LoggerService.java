package com.n0tgrain.modsyncbackend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    private final Logger logger = LoggerFactory.getLogger(LoggerService.class);

    public void logWarning(String message) {
        logger.warn(message);
    }

    public void logError(String message) {
        logger.error(message);
    }

    public void logInfo(String message) {
        logger.info(message);
    }
}
