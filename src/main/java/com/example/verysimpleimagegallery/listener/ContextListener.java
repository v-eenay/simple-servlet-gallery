package com.example.verysimpleimagegallery.listener;

import com.example.verysimpleimagegallery.util.CreateTagTables;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Application context listener that initializes resources when the application starts.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Initializes resources when the application starts.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application starting up...");
        
        // Create tag tables if they don't exist
        boolean tablesCreated = CreateTagTables.createTables();
        if (tablesCreated) {
            System.out.println("Tag tables initialized successfully.");
        } else {
            System.err.println("Failed to initialize tag tables.");
        }
    }

    /**
     * Cleans up resources when the application shuts down.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application shutting down...");
    }
}
