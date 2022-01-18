package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Message;
import com.kata.socialNetwork.app.models.Person;
import com.kata.socialNetwork.app.models.Wall;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker {
    public String interpretAndUpdate(String command, State appState) {
        Pattern publish = Pattern.compile("([a-zA-Z]+)\\spublishes\\s(\".*\")");
        Pattern view = Pattern.compile("([a-zA-Z]+)\\sviews\\s([a-zA-Z]+)");
        Pattern follow = Pattern.compile("([a-zA-Z]+)\\sfollows\\s([a-zA-Z]+)");

        Matcher publishMatcher = publish.matcher(command);
        Matcher viewMatcher = view.matcher(command);
        Matcher followMatcher = follow.matcher(command);

        String outcome= "This is an unknown command. Please check syntax.";
        if (publishMatcher.matches()) {
            String personName, messageContent;
            personName = publishMatcher.group(1);
            messageContent = publishMatcher.group(2);
            outcome = Utility.publishCmd(personName, messageContent, appState);
        } else if (viewMatcher.matches()) {
            String viewerName, vieweeName;
            viewerName= viewMatcher.group(1);
            vieweeName= viewMatcher.group(2);
            outcome = Utility.viewCmd(viewerName, vieweeName, appState);
        } else if(followMatcher.matches()) {
            String follower, followee;
            follower= followMatcher.group(1);
            followee= followMatcher.group(2);
            outcome = Utility.followCmd(follower,followee,appState);
        }
        return outcome;
    }
}
