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
    private static State state;
    private static Worker worker;

    public App(){
        this.state= new State();
        this.worker= new Worker();
    }

    public static void runApp() {
        App app= new App();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input= "";
            while(!input.equalsIgnoreCase("Exit")){
                System.out.print("Input an action, or input 'Exit' to end the app> ");
                input= reader.readLine();

                worker.interpretAndUpdate(input,getState());
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public static State getState() {
        return state;
    }

    public static void main( String[] args ) {
        runApp();
    }

}
