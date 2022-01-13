package com.kata.socialNetwork.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void runApp() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input= "";
            while(!input.equalsIgnoreCase("Exit")){
                System.out.print("Input an action, or input 'Exit' to end the app> ");
                input= reader.readLine();
                System.out.println(input);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public static void main( String[] args ) {
        runApp();
    }
}
