package com.example.login;
import java.sql.Connection;
import java.sql.DriverManager;

public class db {
    public static Connection databaseLink;
    public static Connection getConnection(){
        String databaseName="facturation";
        String databaseUser="root";
        String databasePassword="";
        String url="jdbc:mysql://localhost:3306/"+databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink=DriverManager.getConnection(url,databaseUser,databasePassword);
            System.out.println("connected ");

        }catch (Exception e){
            e.printStackTrace();

        }
        return databaseLink;


    }
}
