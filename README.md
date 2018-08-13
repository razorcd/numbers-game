## Problem:

The Goal is to implement a game with two independent units – the players – communicating with each other using an API.

##### Description

When a player starts, it incepts a random (whole) number and sends it to the second player as an approach of starting the game.
The receiving player can now always choose between adding one of​`{­1, 0, 1}` to get to a number that is divisible by​3. Divide it by three. 
The resulting whole number is then sent back to the original sender.
The same rules are applied until one player reaches the number​`1` (after the division).

For each "move", a sufficient output should get generated (mandatory: the added, and the resulting number).

Both players should be able to play automatically without user input. One of the players should optionally be adjustable by a user.

## Solution:

### Planing

At first I planed to implement this as a P2P (peer to peer) communication where each player would run his/her own version of the game and there would be no state stored anywhere. I would have been just 2 idempotent applications that would ping pong the input number and last round result from one to another.  
At a later stage I decided to switch to a `one server application` that holds the state of the game and players just connect to it. This would be more evolvable.  

##### Game implementation consists of 3 parts:
 - the game logic itself (the gameRounds rounds)
 - the network communication (streams here)
 - main controller mediator that connects the game with the network communication and logs each event
 
 
##### Defining business domains before coding:
  - input number
  - output number
  - game round result
  - player
  
##### Defining game actions:
  - initialise new `round` with input number
  - validate `input number` is greater then 2
  - create `output number` by following action: add -1, 0, 1 to input number to make result divisible by 3 and then divide it by 3.
  - check if winner


### Implementation 

 I did the implementation using a more functional programming approach. Notice there are no `IF` keywords used! This results in a more linear, reusable codebase.
 Everything was done fallowing `TDD` (plan small, red green refactor and then big the refactoring)
 For acceptance tests I used `BDD` so it's easier to work with the business experts.
 For the design of the application followed `DDD` so again it's easier to work with business experts.
 
 The application is separated in `3 main modules/packages`: Controller, Game and Server. 
 Controller being the mediator module that connects Game and server.
 
 To connect the Controller to Server initially I implemented the `Observer` pattern but later I moved to `Streams` because I find it more practical for this case. For example inserting a security middleware here would be very easy, just follow the stream.  
  
 I tried to not add any any big external dependencies/frameworks and and just keep it simple and stupid (KISS).
 
 `Test` coverage is good (and I mean the behaviours coverage, not only the line coverage), Uncle Bob should be proud :)

 ### Setup
 
 ##### Configuration
 - to configure the application look in the `resources/application.properties`
 - to change logging level look in the `resources/log4j.properties`
 
 Notice tests environment has it's own configuration files.
 
 
 For `network transfer protocol` I used TCP/IP.
 For `message protocol` I used my own data format and plain old strings.  (command:data). 
 I also implemented a `deserializer` so it should be easy to update to another protocol like JSON for example.

##### Requirements
- java 8
- maven 3
- a tcp network tool
 
##### Start the server:

- run to build app: `mvn compile` or better `mvn clean install` 
- run to start app: `mvn exec:java -Dexec.mainClass=com.challenge.ServerApp`
- run a TCP/IP networking client interface on the same port as the application. 
For example in linux you can use the popular `netcat`. Run `netcat localhost 9999` to start client and connect to server socket.

Then start typing.

##### Demo
The application is fully functioning and it works like this:
```
➜  ~ nc localhost 9999 
connected

some_wrong_command
unknown command. Available commands are: ADD_PLAYER:player_name, START, PLAY:number, STATE, EXIT.

ADD_PLAYER:player1
Added player player1 to game.

ADD_PLAYER:player2
Added player player2 to game.

START
Player player1 started game and played a random number 119. The result is Round result: outputNumber 40, winner false.

PLAY:119
ERROR: Player player2: Can not play 119 after Round result: outputNumber 40, winner false.

PLAY40
unknown command. Available commands are: ADD_PLAYER:player_name, START, PLAY:number, STATE, EXIT.

PLAY:40
Player player2 played number 40. The result is Round result: outputNumber 13, winner false.

PLAY:13
Player player1 played number 13. The result is Round result: outputNumber 4, winner false.

PLAY:4
Player player2 played number 4. The result is Round result: outputNumber 1, winner true.

PLAY:1
ERROR: Player player1: Can not play after Round result: outputNumber 1, winner true.
``` 
 
 
 
##### Run tests:
```
mvn test
```

### Todo
 - [ ] define a better interface for Game module to reduce the leaking in controller.
 - [ ] allow ip and port to be overwritten with the command args, not only file configuration. 
 - [ ] global properties class
### Other 
 This was done in about 5 days so far, including the small server implementation.
 
 


Design patterns I used on application:
 - observer
 - strategy
 - factory !
 - visitor
 - command
 - chain of responsibility (middleware)
 - singleton (global configurable properties)
 - mediator (like controller)
 - memento (GameManager holds state of games. Can hold history)
 builder?

- and of course composition over inheritance where applicable and di.

Game, gameround are immutable and can be streamed through a reducer. (functional programming style)

reduce functional for readability


Other parts:
- controller parses input and uses command pattern and chain of responsibility pattern to control game   
- each socket connection starts in separate thread and binds socket to controller
- socketChannel works as a pub-sub system to connect to streams and broadcast messages. 

- game is divided in 2 main parts:
  - the game round which is a stateless class representing a round (a turn) in a game. It has it's own domains, logic, validators is a standalone module.
  - the turn based game that mediates the game round and the players. 

- main game logic and AI are two separate classes that are injected by the game factory into the game.

The game logic is very flexible. For example to convert this to a tic-tac-toe game all is needed is to replace the game logic, win logic and AI independent classes.






  

