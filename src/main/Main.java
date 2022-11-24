package main;

import myclasses.Card;
import myclasses.Deck;
import myclasses.EmpressThorina;
import myclasses.GeneralKocioraw;
import myclasses.KingMudface;
import myclasses.LordRoyce;
import myclasses.Player;
import myclasses.Table;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static main.GameCommands.setCards;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation
        //here I will introduce my program


        int noOfWinsPlayer1 = 0;
        int noOfWinsPlayer2 = 0;
        //I go through the array of games
        for (int i = 0; i < inputData.getGames().size(); ++i) {
            //game with index i
            Player player1 = new Player();
            Player player2 = new Player();
            Table table = new Table();

            int maxMana = 10;

            GameInput currentGame = inputData.getGames().get(i);
            StartGameInput startGame = currentGame.getStartGame();
            int currentPlayer = inputData.getGames().get(i).getStartGame().getStartingPlayer();
            ArrayList<ActionsInput> actions = currentGame.getActions();

            //now we give the decks to the two players: tank boys <3
            Deck player1Deck = new Deck(inputData.getPlayerOneDecks().
                    getDecks().get(startGame.getPlayerOneDeckIdx()));
            Deck player2Deck = new Deck(inputData.getPlayerTwoDecks().
                    getDecks().get(startGame.getPlayerTwoDeckIdx()));

            Random rand = new Random(currentGame.getStartGame().getShuffleSeed());
            Collections.shuffle(player1Deck.getDeck(), rand);

            rand = new Random(currentGame.getStartGame().getShuffleSeed());
            Collections.shuffle(player2Deck.getDeck(), rand);

            //now we set the heroesfor the two players
            Card hero1 = new Card(startGame.getPlayerOneHero());
            Card hero2 = new Card(startGame.getPlayerTwoHero());

            //declare dynamic and static type for OOP principles
            if (hero1.getName().equals("Lord Royce")) {
                Card hero1A = new LordRoyce(hero1);
                player1.setHero(hero1A);
            }
            if (hero1.getName().equals("Empress Thorina")) {
                Card hero1A = new EmpressThorina(hero1);
                player1.setHero(hero1A);
            }
            if (hero1.getName().equals("King Mudface")) {
                Card hero1A = new KingMudface(hero1);
                player1.setHero(hero1A);
            }
            if (hero1.getName().equals("General Kocioraw")) {
                Card hero1A = new GeneralKocioraw(hero1);
                player1.setHero(hero1A);
            }
            //pt 2
            if (hero2.getName().equals("Lord Royce")) {
                Card hero1A = new LordRoyce(hero2);
                player2.setHero(hero1A);
            }
            if (hero2.getName().equals("Empress Thorina")) {
                Card hero1A = new EmpressThorina(hero2);
                player2.setHero(hero1A);
            }
            if (hero2.getName().equals("King Mudface")) {
                Card hero1A = new KingMudface(hero2);
                player2.setHero(hero1A);
            }
            if (hero2.getName().equals("General Kocioraw")) {
                Card hero1A = new GeneralKocioraw(hero2);
                player2.setHero(hero1A);
            }

            setCards(player1Deck, player2Deck);


            int turns = 0;
            int rounds = 0;

            Card inHand1 = player1Deck.getDeck().get(0);
            Card inHand2 = player2Deck.getDeck().get(0);
            //punem si in mana acum cartile scoase
            player1.addHandCard(inHand1);
            player2.addHandCard(inHand2);

            player1Deck.removeFirstElement();
            player2Deck.removeFirstElement();


            int winner = 0;
            int countTurns = 0;
            player1.setManaPlayer(1);
            player2.setManaPlayer(1);
            rounds = 1;
            //iterate through actions
            for (ActionsInput iterator : actions) {

                GameCommands.getPlayerDeck(objectMapper, output, player1Deck, player2Deck, iterator);

                GameCommands.getPlayerHero(objectMapper, output, player1, player2, iterator);

                if (iterator.getCommand().equals("endPlayerTurn")) {
                    if (currentPlayer == 1) {
                        player1.getHero().setHasAttacked(0);
                        currentPlayer = 2;
                        for (int defrost = 2; defrost <= 3; defrost++) {
                            for (Card card : table.getTable().get(defrost)) {
                                card.setIsFrozen(0);
                                card.setHasAttacked(0);
                            }
                        }
                        // dau si defrost la randuri pe aici
                    } else {
                        player2.getHero().setHasAttacked(0);
                        currentPlayer = 1;
                        for (int defrost = 0; defrost <= 1; defrost++) {
                            for (Card card : table.getTable().get(defrost)) {
                                card.setIsFrozen(0);
                                card.setHasAttacked(0);
                            }
                        }
                    }
                    turns++;
                    if (turns == 2) {
                        Card inHand11 = null, inHand21 = null;
                        if (!player1Deck.getDeck().isEmpty()) {
                            inHand11 = new Card(player1Deck.getDeck().get(0));
                            player1.addHandCard(new Card(inHand11));
                            player1Deck.removeFirstElement();
                        }
                        if (!player2Deck.getDeck().isEmpty()) {
                            inHand21 = new Card(player2Deck.getDeck().get(0));
                            player2.addHandCard(new Card(inHand21));
                            player2Deck.removeFirstElement();
                        }
                        rounds++;
                        if (rounds <= maxMana) {
                            player1.addMana(rounds);
                            player2.addMana(rounds);
                        } else {
                            player1.addMana(maxMana);
                            player2.addMana(maxMana);
                        }
                        turns = 0;
                    }
                }
                GameCommands.getCardsInHand(objectMapper, output, player1, player2, iterator);

                GameCommands.placeCard(objectMapper, output, player1, player2, table,
                        currentPlayer, iterator);

                GameCommands.getCardsOnTable(objectMapper, output, table, iterator);

                GameCommands.getEnvironmentCardsInHand(objectMapper, output, player1,
                        player2, iterator);

                GameCommands.getCardAtPosition(objectMapper, output, table, iterator);

                GameCommands.useEnvironmentCard(objectMapper, output, player1, player2, table,
                        currentPlayer, iterator);


                GameCommands.getFrozenCardsOnTable(objectMapper, output, table, iterator);

                GameCommands.cardUsesAttack(objectMapper, output, table, currentPlayer, iterator);


                if (iterator.getCommand().equals("useAttackHero")) {
                    int x = iterator.getCardAttacker().getX();
                    int y = iterator.getCardAttacker().getY();
                    ObjectNode node;
                    if (table.getTable().get(x).size() > y) {
                        if (table.getTable().get(x).get(y).getIsFrozen() == 1) {
                            node = objectMapper.createObjectNode();
                            node.put("command", "useAttackHero");
                            ObjectNode help1 = objectMapper.createObjectNode();
                            help1.put("x", x);
                            help1.put("y", y);
                            node.put("cardAttacker", help1);
                            node.put("error", "Attacker card is frozen.");
                            output.add(node);
                        } else if (table.getTable().get(x).get(y).getHasAttacked() == 1) {
                            node = objectMapper.createObjectNode();
                            node.put("command", "useAttackHero");
                            ObjectNode help1 = objectMapper.createObjectNode();
                            help1.put("x", x);
                            help1.put("y", y);
                            node.put("cardAttacker", help1);
                            node.put("error", "Attacker card has already attacked this turn.");
                            output.add(node);
                        } else {
                            int existsTank = 0;
                            if (currentPlayer == 1) {
                                for (int it = 0; it <= 1; ++it) {
                                    for (Card card : table.getTable().get(it)) {
                                        if (card.getIsTank() == 1) {
                                            existsTank = 1;
                                        }
                                    }
                                }
                            } else {
                                for (int it = 2; it <= 3; ++it) {
                                    for (Card card : table.getTable().get(it)) {
                                        if (card.getIsTank() == 1) {
                                            existsTank = 1;
                                        }
                                    }
                                }
                            }

                            if (existsTank == 1) {
                                node = objectMapper.createObjectNode();
                                node.put("command", "useAttackHero");
                                ObjectNode help1 = objectMapper.createObjectNode();
                                help1.put("x", x);
                                help1.put("y", y);
                                node.put("cardAttacker", help1);
                                node.put("error", "Attacked card is not of type 'Tank'.");
                                output.add(node);
                            } else {
                                Player playerWithHero = null;
                                if (currentPlayer == 1) {
                                    playerWithHero = player2;
                                } else {
                                    playerWithHero = player1;
                                }
                                playerWithHero.getHero().setHealth(playerWithHero.getHero().
                                        getHealth()
                                        - table.getTable().get(x).get(y).getAttackDamage());
                                table.getTable().get(x).get(y).setHasAttacked(1);
                                if (playerWithHero.getHero().getHealth() <= 0) {
                                    if (currentPlayer == 1) {
                                        winner = 1;
                                        node = objectMapper.createObjectNode();
                                        node.put("gameEnded", "Player one killed the enemy hero.");
                                        noOfWinsPlayer1++;
                                        output.add(node);
                                    } else {
                                        winner = 2;
                                        node = objectMapper.createObjectNode();
                                        node.put("gameEnded", "Player two killed the enemy hero.");
                                        output.add(node);
                                        noOfWinsPlayer2++;
                                    }
                                }
                            }
                        }
                    }
                }

                GameCommands.useHeroAbility(objectMapper, output, player1,
                        player2, table, currentPlayer, iterator);
                GameCommands.statistics(objectMapper, output, noOfWinsPlayer1,
                        noOfWinsPlayer2, i, player1, player2, currentPlayer, iterator);
                GameCommands.cardUsesAbility(objectMapper, output, table,
                        currentPlayer, iterator);

            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
