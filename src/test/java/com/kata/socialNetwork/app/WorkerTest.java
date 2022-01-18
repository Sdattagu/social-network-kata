package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WorkerTest {
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
    public void publishCmd_first_time_creates_person_in_network(){
        //define
        String command= "Alice publishes \"I love the weather today.\"";

        //pre-assertions
        assertTrue(!mockState.getNetwork().containsKey("Alice"));
        assertTrue(!mockState.getPeople().containsKey("Alice"));

        //execute
        mockWorker.interpretAndUpdate(command,mockState);

        //post-assertions
        assertTrue(mockState.getNetwork().containsKey("Alice") && mockState.getNetwork().get("Alice").size()==0);
        assertTrue(mockState.getPeople().containsKey("Alice"));
        assertTrue(mockState.getPeople().get("Alice").getWall().getTimeline().getMessages().size()!=0);
    }

    @Test
    public void viewCmd_nonexistent_person_returns_msg(){
        //define
        String command1= "Alice publishes \"I love the weather today.\"";
        String command2= "Alice views Bob";
        //pre-assertions
        assertFalse(mockState.getPeople().containsKey("Bob"));
        //execute
        mockWorker.interpretAndUpdate(command1,mockState);
        String outcome= mockWorker.interpretAndUpdate(command2,mockState);
        //post-assertions
        assertTrue(outcome.equalsIgnoreCase("Seems like Bob doesn't exist in the network. Make an account by publishing a msg."));
    }

    @After
    public void tearDown() throws Exception {
    }
}