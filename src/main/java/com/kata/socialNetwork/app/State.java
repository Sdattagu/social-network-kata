package com.kata.socialNetwork.app;

import com.kata.socialNetwork.app.models.Person;
import com.kata.socialNetwork.app.models.Wall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private Map<String, List<Person>> network;
    private Map<String,Person> people;

    public State() {
        this.network= new HashMap<String,List<Person>>();
        this.people= new HashMap<>();
    }

    public Map<String, List<Person>> getNetwork() {
        return network;
    }
    public Map<String, Person> getPeople() { return people; }

    public void addPersonToNetwork(String name) {
        if(!this.network.containsKey(name)){
            this.network.put(name, new ArrayList<Person>());
            this.people.put(name, new Person(name));
        }
    }

    // The set of outbound edges from person node.
    public List<Person> getWhoAmIFollowing(String name){
        if(this.network.containsKey(name)) return this.network.get(name);
        else return new ArrayList<>();
    }
}
