## Problem:

The Goal is to implement a game with two independent units – the players – communicating with each other using an API.

##### Description

When a player starts, it intercepts a random (whole) number and sends it to the second player as an approach of starting the game.
The receiving player can now always choose between adding one of​`{­1, 0, 1}` to get to a number that is divisible by​3. Divide it by three. 
The resulting whole number is then sent back to the original sender.
The same rules are applied until one player reaches the number​`1` (after the division).

For each "move", a sufficient output should get generated (mandatory: the added, and the resulting number).

Both players should be able to play automatically without user input. One of the players should optionally be adjustable by a user.

## Solution:

Application works and covers all features and also most cornercases. See `AcceptanceTests.java` for all tested user features.

This was implemented in `vanilla Java` (the only dependencies are Junit, mockito and log4j).

Once the server is started and a `tcp` client is connected, these are the commands available:
```
ADD_PLAYER:your_name  - to add yourself as player
ADD_MACHINE           - to add AI player (play with computer)
START                 - start game (2 players must registred. AI or human)
PLAY:number           - play a number. -1, 0 or 1 following game logic described above
STATE                 - print the current state of the game
EXIT                  - leave the game
``` 

See a little lower an example. 

Adding 2 AI players is also possible. Example:
```
ADD_MACHINE
Added AI player Machine2 to game.
ADD_MACHINE
Added AI player Machine3 to game.
START
Game started. The starting number is 111. Next to play is machine Machine2.
machine Machine2 played number 0. The result is Round result: outputNumber 37, winner false.
machine Machine3 played number -1. The result is Round result: outputNumber 12, winner false.
machine Machine2 played number 0. The result is Round result: outputNumber 4, winner false.
machine Machine3 played number -1. The result is Round result: outputNumber 1, winner true.
```

### Planning

At first I planed to implement this as a P2P (peer to peer) communication where each player would run his/her own version of the game and there would be no state stored anywhere. I would have been just 2 idempotent applications that would ping pong the input number and last round result from one to another.  
At a later stage I decided to switch to a `one server application` that holds the state of the game and players just connect to it. This would be more evolvable.  

##### Game implementation consists of 3 parts:
 - the game logic itself (also separated in 2 modules)
 - the network communication (streams here with separate threads and pubsub)
 - main controller that mediates the game with the network communication
 
 The game is also divided in modules:
 - the `game round` module which processes an `input` and generates the `output`. It has it's own domains, logic, validators, it's a standalone module. Also the `game logic` and `wining logic` are injected (replaceable).
 - the turn based `game` module which joins `game round` with a `player aggregation`.
 
 This part was implemented using immutable classes, using a more `functional programming` style.

 
##### Defining business domains before coding:
  - input number  (for main game)
  - output number  (for main game)
  - game round input  (for a game round)
  - game round result  (for a game round)
  - player aggregate (players that play main game with player who's next as aggregate root)
  
##### Defining game actions:
  - initialise new `round` with input number
  - validate `input number` is -1, 0 or 1
  - create `output number` by following action: add -1, 0, 1 to input number to make result divisible by 3
  - game logic to divide by 3
  - game logic to win when output number is 1
  - add player
  - start game again
  - add AI player
  - play with AI player
  - let 2 AI players play together

These are all implemented.

### Implementation 

 I did the implementation using a more `functional programming` approach. Notice there are very few `if` keywords used. This results in a more linear, reusable codebase, immutable objects and avoiding `null`.
 Everything was done fallowing `TDD` (plan small, red green refactor and then the big refactoring)
 I also followed `BDD` style, making tests easier to work with business experts.
 For the design of the application followed `DDD` so again it's easier to work with business experts.
 
 The application is separated in `3 main modules/packages`: Controller, Game and Server. 
 Controller being the mediator module that connects Game and server.
 
 To connect the Controller to Server initially I implemented the `Observer` pattern but later I moved to `Streams` because I find it more practical for this case. 
 For example inserting a security middleware here would be very easy, just follow the stream and put it before the controller.
 
 The message sending between controller and server is using pub-sub system. So messages are broadcast to all threads (to all connected users).   
  
 I tried to not add any any big external `dependencies/frameworks` and and just keep it simple and stupid (KISS).
 
 `Test` coverage is good, (and I mean the behaviours coverage, not only the line coverage) acceptance tests cover everything (both happy/sad paths), unit tests are partially done, Uncle Bob should be still happy.


`Design patterns` I used for designing this: `observer`(messaging - pubsub - open stream), `iterator`(cyclic - PlayerAggregate), `strategy`(di), `factory`(build games), `visitor`(validators), 
`command`(human commands mapped to classes), `chain of responsibility`(chained commands), 
`mediator`(controller, services), `memento` (GameService holds state of games. Can also hold entire history of game objects).
- and of course composition over inheritance.

`Game` and `Gameround` are immutable and because of functional approach, these can be streamed through a `reducer` like this:
```java
Game init = new Game(gameRoundServiceMock)
        .addPlayer(player1)
        .addPlayer(player2)
        .startGame();

Game finalStage = Stream.of(
            new InputNumber(-1), 
            new InputNumber(0), 
            new InputNumber(-1), 
            new InputNumber(1))
        .reduce(init, Game::play, (a , b) -> null);

finalStage.getGameRoundResult().isWinner() #=> true
```

The game architecture is very flexible. For example to convert this to a tic-tac-toe game all is needed is to replace the `game logic`, `win logic` and `AI`, these are independent classes.

## Setup
 
##### Configuration
 - to configure the application look in the `resources/application.properties`
 - to change logging level look in the `resources/log4j.properties`
 
 Notice tests have their own configuration files.
 
 
 For `network transfer protocol` I used `TCP/IP`.
 For `message protocol` I used my own format, plain old strings.  Message format: `command:data`. 
 I also implemented a `deserializer` so it should be easy to update to another protocol like JSON for example.

##### Requirements
- java 8
- maven 3
- a tcp network tool
 
##### Start the server:

You have to start the application first. This will be the server. Default port is `9999`. Then use any networking client that supports `tcp` and connect to it. You will immediately see `cnonnected` message. 

- run to build app: `mvn compile` or better `mvn clean install` to also run tests.
- run tests: `mvn test`
- run to start server app: `mvn exec:java -Dexec.mainClass=com.challenge.ServerApp`
- run a TCP/IP networking client interface on the same port as the application. Each client represents a player so you can connect more. 
For example in linux you can use the popular `netcat`. Run `netcat localhost 9999` to start client and connect to server socket. You should see `connected` on the client terminal once successfully connected. Then just start typing.

## Demo

The application is fully functioning and it works like this:

```
➜  ~ nc localhost 9999
connected

ADD_PLAYER:P1
Added player P1 to game.

ADD_MACHINE
Added AI player Machine2 to game.

START
Game started. The starting number is 91. Next to play is player P1.

-1
unknown command. Available commands are: ADD_PLAYER:player_name, ADD_MACHINE, START, PLAY:number, STATE, EXIT.

PLAY:-1
player P1 played number -1. The result is Round result: outputNumber 30, winner false.

machine Machine2 played number 0. The result is Round result: outputNumber 10, winner false.

PLAY:2
ERROR: can not play game because 2 is not within [-1, 0, 1]

PLAY:1
ERROR: could not play this round because of invalid input last round output was 10 and your input was 1

PLAY:XXXXX
ERROR: For input string: "XXXXX"
ERROR: can not play game because null is not within [-1, 0, 1]

PLAY:-1
player P1 played number -1. The result is Round result: outputNumber 3, winner false.

machine Machine2 played number 0. The result is Round result: outputNumber 1, winner true.

STATE
player P1 is next. Last Round result: outputNumber 1, winner true.

START
Game started. The starting number is 88. Next to play is player P1.

PLAY:-1
player P1 played number -1. The result is Round result: outputNumber 29, winner false.

machine Machine2 played number 1. The result is Round result: outputNumber 10, winner false.

PLAY:-1
player P1 played number -1. The result is Round result: outputNumber 3, winner false.

machine Machine2 played number 0. The result is Round result: outputNumber 1, winner true.

EXIT
Goodbye.
``` 


### Other 
 This was done in about 5 days so far, including the small multithreaded server implementation.
 

Thank you!




  

