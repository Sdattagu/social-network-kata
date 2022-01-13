package com.kata.socialNetwork.app.models;

import java.util.Objects;
import java.util.Set;

public class Person {
    String name;
    Wall wall;

    public Person(String name) {
        this.name = name;
        this.wall = new Wall();
    }

    public String getName() {
        return name;
    }
    public Wall getWall() {
        return wall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(wall, person.wall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, wall);
    }
}
