/*
 * Copyright 2023, Datajek.io
 */

package io.datajek.ftpservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class manages the configuration settings for the server.
 */
public class ServerConfiguration {

    private static final Logger logger = LogManager.getLogger(ServerConfiguration.class);
    private Properties properties;

    //default constructor or testing purposes
    public ServerConfiguration() {
        properties = new Properties();
    }
    //parameterized constructor to read properties from a config file
    public ServerConfiguration(String configFileName) {
        properties = new Properties();
        File file = new File(configFileName);

        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Cannot read in config file", e);
        }
    }
    //method to retrive the DESTINATION_DIRECTORY_ON_SERVER property
    public String getDestinationDirectoryOnServer() {
        return properties.getProperty(ServerConstants.DESTINATION_DIRECTORY_ON_SERVER);
    }
    //method to retrive the TEMP_WRITE_PATH property
    public String getTempWritePath() {
        return properties.getProperty(ServerConstants.TEMP_WRITE_PATH);
    }
}
