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
            outcome = publishCmd(personName, messageContent, appState);
        } else if (viewMatcher.matches()) {
            String viewerName, vieweeName;
            viewerName= viewMatcher.group(1);
            vieweeName= viewMatcher.group(2);
            outcome = viewCmd(viewerName, vieweeName, appState);
        } else if(followMatcher.matches()) {
            String follower, followee;
            follower= followMatcher.group(1);
            followee= followMatcher.group(2);
            outcome = followCmd(follower,followee,appState);
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

    private String followCmd(String follower, String followee, State appState){
        if(!appState.getNetwork().containsKey(follower)) return "Seems like "+follower+" doesn't exist in the network. Make an account by publishing a msg.";
        else if (!appState.getNetwork().containsKey(followee)) return "Seems like "+followee+" doesn't exist in the network. Make an account by publishing a msg.";
        else {
            // Add the followee to the follower's network, i.e., create an outbound edge from follower's node, to followee's node.
            Person followeeP= appState.getPeople().get(followee);
            appState.getNetwork().get(follower).add(followeeP);
            return "Added "+followee+ " to "+follower+"'s network. You'll view a merged timeline for "+follower+". Try it.";
        }
    }

    private String viewCmd(String viewerName, String vieweeName, State appState){
        if(!appState.getNetwork().containsKey(viewerName)) return "Seems like "+viewerName+" doesn't exist in the network. Make an account by publishing a msg.";
        else if (!appState.getNetwork().containsKey(vieweeName)) return "Seems like "+vieweeName+" doesn't exist in the network. Make an account by publishing a msg.";
        else {
            if(viewerName.equalsIgnoreCase(vieweeName)){
                List<Person> following = appState.getNetwork().get(viewerName);
                // We need to provide a merged view of this person's timeline, and the timelines of those who he/she follows.
                if (following!=null && following.size()>0){
                    List<Message> mergedTimeline= mergedView(viewerName,following, appState);
                    for(Message message: mergedTimeline) System.out.println(message.toString());
                } else {
                    // If they don't follow anyone, then just show their own posts.
                    List<Message> messages= appState.getPeople().get(viewerName).getWall().getTimeline().getMessages();
                    for (Message message: messages) System.out.println(message.toString());
                }
            } else {
                // This person is trying to view someone else' timeline; show them the other person's posts.
                List<Message> messages= appState.getPeople().get(vieweeName).getWall().getTimeline().getMessages();
                for (Message message: messages) System.out.println(message.toString());
            }
            return "End of"+ vieweeName+ "'s timeline feed.";
        }
    }

    private List<Message> mergedView(String follower, List<Person> followees, State state){
        // Initial set of messages consist only of the viewer's feed.
        List<Message> followerMessages= state.getPeople().get(follower).getWall().getTimeline().getMessages();
        // Traverse the network's edges outbound from the follower's node, and merge each followee's timeline, into the follower's.
        for(Person followee: state.getNetwork().get(follower)){
            List<Message> followeeMessages= followee.getWall().getTimeline().getMessages();
            if(followeeMessages.size()>0){
                followerMessages=merge(followerMessages,followeeMessages);
            }
        }
        return followerMessages;
    }

    private List<Message> merge(List<Message> followerMessages, List<Message> followeeMessages){
        int followerMsgIndx, followeeMsgIndx;
        List<Message> result= new ArrayList<Message>();
        for(followerMsgIndx=0,followeeMsgIndx=0; followerMsgIndx<followerMessages.size() && followeeMsgIndx<followeeMessages.size();){
            if(followeeMessages.get(followeeMsgIndx).getPublishedDateTime().isBefore(followerMessages.get(followerMsgIndx).getPublishedDateTime())){
                result.add(followeeMessages.get(followeeMsgIndx));
                followeeMsgIndx++;
            } else {
                result.add(followerMessages.get(followerMsgIndx));
                followerMsgIndx++;
            }
        }
        if(followerMsgIndx<followerMessages.size()) result.addAll(followerMessages.subList(followerMsgIndx,followerMessages.size()));
        else if(followeeMsgIndx<followeeMessages.size()) result.addAll(followeeMessages.subList(followeeMsgIndx, followeeMessages.size()));

        return result;
    }
}
