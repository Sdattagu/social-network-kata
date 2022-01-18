package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Message;
import com.kata.socialNetwork.app.models.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UtilityTest {
    State mockState;
    Worker mockWorker;

    @Before
    public void setUp() throws Exception {
        Map<String, List<Person>> mockNetwork= new HashMap<>();
        Map<String,Person> mockPeople= new HashMap<>();
        mockState= new State(mockNetwork,mockPeople);
        mockWorker= new Worker();
    }

    @Test
    public void merge_follower_single_followee() throws InterruptedException {
        //define
        mockWorker.interpretAndUpdate("Alice publishes \"I love the weather today.\"",mockState);
        try{TimeUnit.SECONDS.sleep(4);} catch (InterruptedException e) {throw e;}
        mockWorker.interpretAndUpdate("Bob publishes \"Darn! We lost!\"",mockState);
        try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {throw e;}
        mockWorker.interpretAndUpdate("Bob publishes \"Good game though.\"",mockState);

        //pre-assertions
        assertTrue(mockState.getNetwork().get("Alice").size()==0);
        //execute
        mockWorker.interpretAndUpdate("Alice follows Bob", mockState);
        assertTrue(mockState.getNetwork().get("Alice").size()==1);
        List<Message> mergedMessages= Utility.mergedView("Alice",mockState.getNetwork().get("Alice"),mockState);

        //post-assertions
        assertTrue(mergedMessages.get(0).getContent().contains("Good game"));
        assertTrue(mergedMessages.get(1).getContent().contains("Darn!"));
        assertTrue(mergedMessages.get(2).getContent().contains("I love the weather"));
    }

    @After
    public void tearDown() throws Exception {
    }
}