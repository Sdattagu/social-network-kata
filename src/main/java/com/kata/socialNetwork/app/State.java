package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Person;
import com.kata.socialNetwork.app.models.Wall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<Person, List<Person>> network;

    public State() {
        this.network= new HashMap<Person,List<Person>>();
    }

    public Map<Person, List<Person>> getNetwork() {
        return network;
    }

    public void addPersonToNetwork(Person person) {
        if(!this.network.containsKey(person)){
            this.network.put(person, new ArrayList<Person>());
        }
    }

    // Returns a list of people who a given person is following.
    // The set of outbound edges from person node.
    public List<Person> getWhoAmIFollowing(Person person){
        if(this.network.containsKey(person)) return this.network.get(person);
        else return null;
    }
}
