/*
 * Copyright 2023, DataJek.io
 * All Rights Reserved
 */

package io.datajek.ftpservice.server;

import io.datajek.ftpservice.utils.ServerConfiguration;
import io.datajek.ftpservice.utils.ServerConstants;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

/**
 * This class manages the lifecycle of the gRPC server.
 */
public class FTPServer {

    private static final Logger logger = LogManager.getLogger(FTPServer.class.getName());
    private final Server server;

    private final String mode;

    /**
     * @param serverConfiguration
     */
    public FTPServer(ServerConfiguration serverConfiguration)
            throws IOException, GeneralSecurityException {

        server = ServerBuilder.forPort(ServerConstants.INSECURE_MODE_PORT)
                .addService(new FTPService(serverConfiguration))
                .build();
        mode = "IN-secure";

    }

    /**
     * This method starts the server and adds shutdown hooks for logging. In
     * case of an abrupt shutdown, the server waits thirty seconds for all
     * channels to cleanly shutdown before a forced shutdown is executed.
     */
    public void start() throws IOException {
        server.start();

        logger.info("FTP Server started in " + mode + " mode");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM
            // shutdown hook.
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            try {
                if (server != null) {
                    server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("gRPC server shut down");
        }));
    }

    /**
     * Shuts down the gRCP server.
     */
    public void shutDown() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Graceful shutdown. Await termination on the main thread since the gRPC
     * library uses daemon threads. This method can hang indefinitely if
     * existing channels don't shutdown.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}