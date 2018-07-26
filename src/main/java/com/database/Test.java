package com.database;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Session;
import com.service.JSCH;

public class Test {

    public static void main(String[] args) throws IOException {

     JSCH j=new JSCH("tcs","10.138.77.172","tcs@12345");
    Session s=j.connect();
     System.out.println(j.executeCommand(s,"ls -a"));
    }
    }