package com.example.login;

public class adminmodel {
    private static Integer id_admin;
    private static adminmodel instance;
    public  adminmodel(Integer id_admin){
       adminmodel.id_admin = id_admin;
    }
    public static adminmodel getInstance(Integer id_admin) {
        if(instance == null ){
            instance = new adminmodel(id_admin);
        }
        return instance;
    }
    public static Integer getId_admin() {
        return id_admin;
    }
}
