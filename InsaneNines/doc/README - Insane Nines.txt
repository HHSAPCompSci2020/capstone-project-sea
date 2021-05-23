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


Ever since the beginning of the COVID-19 pandemic, people have been required to practice social distancing, preventing them from partaking in many in person social activities such as playing cards. Our game, Insane Nines, is a shedding type card game using the standard 52 card deck that can be played on a local area network. This allows one to keep distance while still enjoying the game. The objective of the game is to be the first player to discard all their cards. Each player starts with 5 cards, or 7 cards in a 2 player game. 1 normal card is placed in the played pile to start the game. The remaining cards are placed face down in the draw pile. If the draw pile is empty, all the cards in the played pile other than the top card are moved to the draw pile. If the draw pile is still empty, the player’s turn is skipped if they cannot play a card. A player can play a card with the same rank or suit as the top card, or the card 9 at any time and declare the suit of the next player’s card. Playing a queen skips the next player’s turn, and playing an ace reverses the direction of play.


Instructions:
[Explain how to use the program. This needs to be specific: 
Which keyboard keys will do what? 
Where will you need to click? 
Will you have menus that need to be navigated? What will they look like? 
Do actions need to be taken in a certain order?


One player must create a server and share the server information with the other players. The other players will join the server using the information. When enough players join the server, the host can start the game. In the game, players take turns to play. A player can click on their cards to play them or click on the draw deck to draw cards.


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
Eric Chang - Connecting GUI with networking, all the networking classes, JavaDocs


Outside resources:
Shelby’s ServerBasedNetwork demo
Card images
Stack Overflow
Jakob Jenkov youtube channel
Oracle Java Tutorials