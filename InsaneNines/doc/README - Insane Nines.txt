Insane Nines
Authors: Eric Chang, Andy Ding, Samuel Zhou
Revision: 4/22/21


Introduction: 
[In a few paragraphs totaling about ½ page, introduce the high-level concept of your program. What this looks like depends a lot on what type of thing you are making. An introduction for an application will look different than one for a game. In general, your introduction should address questions like these:
What does your program do?
What problem does it solve? Why did you write it?
What is the story?
What are the rules? What is the goal?
Who would want to use your program?
What are the primary features of your program?
You and your friends are looking for a new game to prove your gaming superiority. You stumble upon the card game Insane Nines. Perfect. You call up your friends and start the battle for domination in this ultimate test of skill.


Insane Nines is a turn based card game using the standard 52 card deck. If there are 2 players, each player starts with 7 cards. Otherwise, each player starts with 5 cards. There is initially a randomly chosen card from the draw pile to start the game. The cards played will go into the played pile. A player can either play a card with the same suit or rank, or the number nine no matter what. If they can’t play a card, they will have to draw from the pile until they can play a card. The draw pile will be refilled by the played pile when empty. The first player to discard all their cards wins the game.


Instructions:
[Explain how to use the program. This needs to be specific: 
Which keyboard keys will do what? 
Where will you need to click? 
Will you have menus that need to be navigated? What will they look like? 
Do actions need to be taken in a certain order?


Insane Nines is a turn based game where 2 - 4 players play and draw cards until they run out of cards. Clicking on a card will play the card. Clicking on the draw pile will draw a card. You can play cards that have matching numbers or suits with the last played card or you can play the card 9 no matter what.


Features List (THE ONLY SECTION THAT CANNOT CHANGE LATER):
Must-have Features:
[These are features that we agree you will definitely have by the project due date. A good final project would have all of these completed. At least 5 are required. Each feature should be fully described (at least a few full sentences for each)]
* A screen that tells players the rules of the game
* 2-4 multiplayer game, with each player on different computers
* Menu screen to navigate players in the app
* Interactive GUI to show your cards, other players, and input commands
* The card game itself, with rules in place such as only playing cards during your turn


Want-to-have Features:
[These are features that you would like to have by the project due date, but you’re unsure whether you’ll hit all of them. A good final project would have perhaps half of these completed. At least 5 are required. Again, fully describe each.]
* A single player mode against an AI
* Scoreboard of wins
* Customization, such as names and colors
* Simple chat interface
* More special cards such as reverse and skip


Stretch Features:
[These are features that we agree a fully complete version of this program would have, but that you probably will not have time to implement. A good final project does not necessarily need to have any of these completed at all. At least 3 are required. Again, fully describe each.]
* Account system to keep track of each individual user
* Multiple online games so people can find random games or choose to join a specific game
* Voice chat in game for verbal communication between players in a game


Class List:
[This section lists the Java classes that make up the program and very briefly describes what each represents. It’s totally fine to put this section in list format and not to use full sentences.]


Main - The main class that is ran to show the main menu
Card - One of the 52 cards with a rank and suit
Deck - A deck of cards
Player - A player with a deck of cards
GamePanel - The panel that the game is played on
Server - The hosted server which clients connect to
ClientHandler - A separate thread to handle each client
Client - A client that connects to a server
ClientReader - What the client uses to read data from the server
ClientWriter - What the client uses to write data to the server
DataObject - What clients and servers send to each other
NetworkListener - A listener that processes the messages it receives from the client


Credits:
[Gives credit for project components. This includes both internal credit (your group members) and external credit (other people, websites, libraries). To do this:
* List the group members and describe how each member contributed to the completion of the final program. This could be classes written, art assets created, leadership/organizational skills exercises, or other tasks. Initially, this is how you plan on splitting the work.
* Give credit to all outside resources used. This includes downloaded images or sounds, external java libraries, parent/tutor/student coding help, etc.]


Andy Ding - GUI in Main, Card, Deck, Player, UML Diagram, README
Samuel Zhou - GUI in GamePanel, Card, Deck, Player, images, UML Diagram
Eric Chang - Server, ClientHandler, Client, ClientReader, ClientWriter, DataObject, NetworkListener, JavaDocs


Outside resources:
Shelby’s ServerBasedNetwork demo
Card images
Stack Overflow
Jakob Jenkov youtube channel
Oracle Java Tutorials