<div align="center">
  <h1>The game</h1>
</div>

# Choose your character and fight!!!
This project is for people who want to have some fun!
Choose your character and go into battle against scary monsters.
Player can choose character class and weapon. In the battle area monsters such as: witcher, dragon and baslisk are waiting for you.

# Gameplay

First you should open and run "Server.java", this file contains server which is needed to send message on chat. 
Then open and run "gameTester.java". In this windom player must set name and choose character class. Then click "Play" button.
Now you can send message to server, go to the shop or go fight in the area with monsters. 
You can also save your player attribute and then upload them back.

# Some features of the project

## Monster class
This is abstract class. It is uperclass for Dragon, Basilisk and Witcher classes. Includes normal and abstract methods.

## Witcher, Dragon and Basilisk classes
Based on this class you can create monsters. In constructor you must set monster lvl. They have function call "getDamage" which decides about damage depends on which lvl they have. 

## Weapon
When you want to create new wepon you must set name, attack, defense and its cost. Player can have only one weapon at a time. He can buy it in the store. Defense is added to player health. For example when player choose "dark sword" his health is not equal to 100 but 120. This class also contains some get methods.

## Player
Includes some set and get methods. SetExp method is used to gain experience and reach next levels. SetEquipment method is used to set player attack and defense.

## GraphicalUserInterface
### Functions:
 - set graphical user interface (frame, panels, buttons, txt fields, txt areas)
 - create monsters
 - create player
 - network connection by channels
 - gameplay functions

### Contains:
 - data stream (List<String> characterStream)
 - lambda expressions
 - buttons/ mouse/ list selection listeners
 - graphical panel
 - save and load user data methods
 - inner classes
 - methods such as doNet, configCommunication and sendMessage needed to send messages to the server