package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Person;
import com.kata.socialNetwork.app.models.Wall;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker {
    public String interpretAndUpdate(String command, State appState) {
        Pattern publish = Pattern.compile("([a-zA-Z]+)\\spublishes\\s(.*)");
        Pattern view = Pattern.compile("([a-zA-Z]+)\\sviews\\s([a-zA-Z])");
        Pattern follow = Pattern.compile("([a-zA-Z]+)\\sfollows\\s([a-zA-Z])");

        Matcher publishMatcher = publish.matcher(command);
        Matcher viewMatcher = view.matcher(command);
        Matcher followMatcher = follow.matcher(command);

        String outcome= "This is an unknown command. Please check syntax.";
        if (publishMatcher.matches()) {
            String personName, messageContent;
            personName = publishMatcher.group(1);
            messageContent = publishMatcher.group(2);
            outcome = publishCmd(personName, messageContent, appState);
        } else if (viewMatcher.matches()) {
            return "";
        } else if(followMatcher.matches()) {
            return "";
        }
        return outcome;
    }

    private String publishCmd(String personName, String messageContent, State appState){
        Set<Person> people= appState.getNetwork().keySet();
        for(Person person: people){
            if(person.getName().equals(personName)) {
                person.getWall().getTimeline().enqueue(messageContent, LocalDateTime.now());
                return "Published '"+messageContent+"' to "+personName+"'s timeline.";
            }
        }
        // person doesn't have account- create one, then enqueue msg.
        Person newcomer= new Person(personName);
        appState.addPersonToNetwork(newcomer);
        newcomer.getWall().getTimeline().enqueue(messageContent, LocalDateTime.now());
        return "Couldn't find "+personName+" in the network. An account was created, and msg has been published.";
    }

}