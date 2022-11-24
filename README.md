

# Tema POO  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

## Short description of my program
* The program is organized according to the OOP principles. Some essential classes are Card, Decks, Deck, Player and Table which provide a smart and efficient way of working with the input classes and the data
* In order to have a well-organized code there are the hero cards Empress Thorina, General Kocioraw, King Mudface and Lord Royce
* These classes are extensions of the Card class, and represent all the heroes in the game. This approach offers a possibility of adding new heroes if needed.
* Also, even if there are no classes for the other cards (I didn't consider this approach to be efficient) in the Card class there are methods that help catgorize the cards into their coresponding types.
* If needed these methods can be completed and have their functionalities expanded to incorporate new cards.
* In the auxiliary class there are methods for most of the commands and each function can be easily expanded.
* In the Main program there is an iteration through the games (as provided by default from the framework) and for each game we declare a new table, new players and proceed accordingly to the game
* The players get each their deck, their hero and then the game can actually begin by iterating through the commands
* The commands are modularized and provide expansion for further improvements.
* ENJOY the game!

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Tests

1. test01_game_start - 3p
2. test02_place_card - 4p
3. test03_place_card_invalid - 4p
4. test04_use_env_card - 4p
5. test05_use_env_card_invalid - 4p
6. test06_attack_card - 4p
7. test07_attack_card_invalid - 4p
8. test08_use_card_ability - 4p
9. test09_use_card_ability_invalid -4p
10. test10_attack_hero - 4p
11. test11_attack_hero_invalid - 4p
12. test12_use_hero_ability_1 - 4p
13. test13_use_hero_ability_2 - 4p
14. test14_use_hero_ability_1_invalid - 4p
15. test15_use_hero_ability_2_invalid - 4p
16. test16_multiple_games_valid - 5p
17. test17_multiple_games_invalid - 6p
18. test18_big_game - 10p


<div align="center"><img src="https://tenor.com/view/homework-time-gif-24854817.gif" width="500px"></div>
