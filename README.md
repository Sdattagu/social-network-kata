# Social Network Kata
This is a web application which allows multiple users to: 
- Publish messages to a timeline
- View their own and other users' messages, and 
- Follow other users, which integrates the followed users' messages into the users' own view of their timeline.

## How to Build the Project

The project is built with Maven 3.8.4. The junit framework version is 4.11.

First, open a shell in the root directory of the project. Then, execute the following cmd:

_mvn package_

This will compile the project and generate an executable JAR file. You may run that jar file like so:

_java -cp target/social-network-app-1.0-SNAPSHOT.jar com.kata.socialNetwork.app.App_

This should yield a prompt of the following form: 

**"Input an action, or input 'Exit' to end the app. >"**

You're ready to start interacting with the app. You can try the following commands:

- [User A] publishes "[Phrase A]". If the user doesn't exist in the network yet, you'll see a message stating that the user was created, and that message is published.
- [User A] views [User B]. This will output a list, ordered by post time, of User B's timeline, regardless of whether User A is following User B or not. Note that User A can view their own timeline using this command!
- [User A] follows [User B]. By following a user, and THEN viewing your own posts, you'll see a 'merged' view of your own timeline.

## Generic Description of Code

The project does not use any in-memory database, so all state is maintained within the top-level application context. This means that if the application is ended, you lose the state.

There is a Worker class which uses 'command' methods to update the state. Commands are Strings which are parsed with regular expressions.

The underlying data structure of the State is a hash-map. It represents a directed unweighted graph represented as an adjacency list.

The most interesting part of the application is how it handles the result of a "follow" command. Once "follow" is executed, the adjacency list of the follower is updated.

When "view" command is executed after a "follow", the network graph is traversed, and timelines of the traversed nodes (followees) are merged, according to the post time.

The result is a single message list which contains the user's posts, and the posts of those he/she has followed- in time-order, with elapsed time stated in minutes/seconds/hours.

It's important to note that the user's set of messages are not updated within the state; merging does not modify the user's own timeline internally, it just aggregates the posts of all followees using a merge algorithm, in time linear to the total number of messages of user+followees.
