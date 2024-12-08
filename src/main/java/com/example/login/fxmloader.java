package com.example.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.io.FileNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class fxmloader {


            public Pane getPage(String pagename) {
                try {
                    // Get the URL of the FXML file
                    URL fileurl = fxmloader.class.getResource("/com/example/login/" + pagename + ".fxml");
                    System.out.println("File URL: " + fileurl);


                    // Check if the FXML file exists
                    if (fileurl == null) {
                        throw new FileNotFoundException("FXML not found for: " + pagename +
                                "\nMake sure the file is in the correct location and the name is correct.");
                    }

                    // Load the FXML file and set the result to the 'view' variable
                    FXMLLoader fxmlLoader = new FXMLLoader(fileurl);
                    Pane view = fxmlLoader.<Pane>load();
                    System.out.println("hey");


                    // Additional setup or processing if needed

                    return view;
                } catch (IOException e) {
                    System.out.println("Error loading FXML for page " + pagename + ": " + e.getMessage());
                    e.printStackTrace(); // Print the stack trace for more detailed information
                    return null; // or handle the error in a way that makes sense for your application
                }

        }

    }


