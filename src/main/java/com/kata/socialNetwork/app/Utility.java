package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Message;
import com.kata.socialNetwork.app.models.Person;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.*;

public final class Utility {

    // This utility class contains static 'command' methods which describe operations within the application that are invoked by worker.
    // Also contains some methods for manipulating LocalTime.

    public static String publishCmd(String personName, String messageContent, State appState){
        Set<String> accountHolders= appState.getNetwork().keySet();
        for(String accountHolder: accountHolders){
            if(accountHolder.equals(personName)) {
                Person holder= appState.getPeople().get(accountHolder);
                holder.getWall().getTimeline().enqueue(messageContent, LocalTime.now(), holder.getName());
                return "Published '"+messageContent+"' to "+personName+"'s timeline.";
            }
        }
        // person doesn't have account- create one, then enqueue msg.
        appState.addPersonToNetwork(personName);
        Person newcomer= appState.getPeople().get(personName);
        newcomer.getWall().getTimeline().enqueue(messageContent, LocalTime.now(), newcomer.getName());
        return "Couldn't find "+personName+" in the network. An account was created, and msg has been published.";
    }

    public static String followCmd(String follower, String followee, State appState){
        if(!appState.getNetwork().containsKey(follower)) return "Seems like "+follower+" doesn't exist in the network. Make an account by publishing a msg.";
        else if (!appState.getNetwork().containsKey(followee)) return "Seems like "+followee+" doesn't exist in the network. Make an account by publishing a msg.";
        else {
            // Add the followee to the follower's network, i.e., create an outbound edge from follower's node, to followee's node.
            Person followeeP= appState.getPeople().get(followee);
            appState.getNetwork().get(follower).add(followeeP);
            return "Added "+followee+ " to "+follower+"'s network. You'll view a merged timeline for "+follower+". Try it.";
        }
    }

    public static String viewCmd(String viewerName, String vieweeName, State appState){
        if(!appState.getNetwork().containsKey(viewerName)) return "Seems like "+viewerName+" doesn't exist in the network. Make an account by publishing a msg.";
        else if (!appState.getNetwork().containsKey(vieweeName)) return "Seems like "+vieweeName+" doesn't exist in the network. Make an account by publishing a msg.";
        else {
            if(viewerName.equalsIgnoreCase(vieweeName)){
                List<Person> following = appState.getNetwork().get(viewerName);
                // We need to provide a merged view of this person's timeline, and the timelines of those who he/she follows.
                if (following!=null && following.size()>0){
                    List<Message> mergedTimeline= mergedView(viewerName,following, appState);
                    prettyPrintView(mergedTimeline);
                } else {
                    // If they don't follow anyone, then just show their own posts.
                    List<Message> messages= appState.getPeople().get(viewerName).getWall().getTimeline().getMessages();
                    prettyPrintView(messages);
                }
            } else {
                // This person is trying to view someone else' timeline; show them the other person's posts.
                List<Message> messages= appState.getPeople().get(vieweeName).getWall().getTimeline().getMessages();
                prettyPrintView(messages);
            }
            return "End of "+ vieweeName+ "'s timeline feed.";
        }
    }

    public static List<Message> mergedView(String follower, List<Person> followees, State state){
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

    private static List<Message> merge(List<Message> followerMessages, List<Message> followeeMessages){
        int followerMsgIndx, followeeMsgIndx;
        List<Message> result= new ArrayList<Message>();
        for(followerMsgIndx=0,followeeMsgIndx=0; followerMsgIndx<followerMessages.size() && followeeMsgIndx<followeeMessages.size();){
            if(followeeMessages.get(followeeMsgIndx).getPublishedTime().isAfter(followerMessages.get(followerMsgIndx).getPublishedTime())){
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

    private static void prettyPrintView(List<Message> messages){
        LocalTime currentTime= LocalTime.now();
        for(Message message: messages) {
            String formattedMsg= ""+message.getOwner()+" - "+message.getContent()+" ("+getElapsedTimeBetween(currentTime,message.getPublishedTime())+")";
            System.out.println(formattedMsg);
        }
    }

    private static String getElapsedTimeBetween(LocalTime t1, LocalTime t2){
        long elapsed= Math.abs(SECONDS.between(t1,t2));

        if(elapsed>3600) return ""+Math.floor(elapsed/3600)+" hours ago";
        else if(elapsed>60) return ""+Math.floor(elapsed/60)+" minutes, "+Math.floor(elapsed%60)+"s ago";
        else return ""+Math.floor(elapsed)+" seconds ago";

    }
}
