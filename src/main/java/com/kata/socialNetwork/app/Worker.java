package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Message;
import com.kata.socialNetwork.app.models.Person;
import com.kata.socialNetwork.app.models.Wall;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
            outcome = publishCmd(personName, messageContent, appState);
        } else if (viewMatcher.matches()) {
            String viewerName, vieweeName;
            viewerName= viewMatcher.group(1);
            vieweeName= viewMatcher.group(2);
            outcome = viewCmd(viewerName, vieweeName, appState);
        } else if(followMatcher.matches()) {
            return "";
        }
        return outcome;
    }

    private String publishCmd(String personName, String messageContent, State appState){
        Set<String> accountHolders= appState.getNetwork().keySet();
        for(String accountHolder: accountHolders){
            if(accountHolder.equals(personName)) {
                Person holder= appState.getPeople().get(accountHolder);
                holder.getWall().getTimeline().enqueue(messageContent, LocalDateTime.now());
                return "Published '"+messageContent+"' to "+personName+"'s timeline.";
            }
        }
        // person doesn't have account- create one, then enqueue msg.
        appState.addPersonToNetwork(personName);
        Person newcomer= appState.getPeople().get(personName);
        newcomer.getWall().getTimeline().enqueue(messageContent, LocalDateTime.now());
        return "Couldn't find "+personName+" in the network. An account was created, and msg has been published.";
    }

    private String viewCmd(String viewerName, String vieweeName, State appState){
        if(!appState.getNetwork().containsKey(viewerName)) return "Seems like "+viewerName+" doesn't exist in the network. Make an account by publishing a msg.";
        else if (!appState.getNetwork().containsKey(vieweeName)) return "Seems like "+vieweeName+" doesn't exist in the network. Make an account by publishing a msg.";
        else {
            List<Message> messages= appState.getPeople().get(vieweeName).getWall().getTimeline().getMessages();
            for (Message message: messages) System.out.println(message.toString());
            return "End of"+ vieweeName+ "'s timeline feed.";
        }
    }

}
