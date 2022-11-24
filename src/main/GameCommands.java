package main;

import myclasses.Card;
import myclasses.Deck;
import myclasses.Player;
import myclasses.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;

import java.util.ArrayList;

final class GameCommands {

    private GameCommands() {
    }

    private static int backRowIndex = 3;
    private static int maxCardsPerRow = 5;

    /**
     * Commands for statistics and small debugs
     */
    public static void statistics(final ObjectMapper objectMapper, final ArrayNode output,
                                  final int noOfWinsPlayer1, final int noOfWinsPlayer2,
                                  final int i, final Player player1, final Player player2,
                                  final int currentPlayer, final ActionsInput iterator) {
        if (iterator.getCommand().equals("getTotalGamesPlayed")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getTotalGamesPlayed");
            node.put("output", i + 1);
            output.add(node);
        }
        if (iterator.getCommand().equals("getPlayerOneWins")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerOneWins");
            node.put("output", noOfWinsPlayer1);
            output.add(node);
        }

        if (iterator.getCommand().equals("getPlayerTwoWins")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerTwoWins");
            node.put("output", noOfWinsPlayer2);
            output.add(node);
        }

        if (iterator.getCommand().equals("getPlayerMana")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerMana");
            node.put("playerIdx", iterator.getPlayerIdx());
            if (iterator.getPlayerIdx() == 1) {
                node.put("output", player1.getManaPlayer());
            } else {
                node.put("output", player2.getManaPlayer());
            }
            output.add(node);
        }

        if (iterator.getCommand().equals("getPlayerTurn")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerTurn");
            node.put("output", currentPlayer);
            output.add(node);
        }
    }

    /**
     * Method for using card ability
     */
    public static void cardUsesAbility(final ObjectMapper objectMapper,
                                       final ArrayNode output, final Table table,
                                       final int currentPlayer,
                                       final ActionsInput iterator) {
        if (iterator.getCommand().equals("cardUsesAbility")) {
            int xAttacked, yAttacked, xAttacker, yAttacker;
            xAttacked = iterator.getCardAttacked().getX();
            yAttacked = iterator.getCardAttacked().getY();
            xAttacker = iterator.getCardAttacker().getX();
            yAttacker = iterator.getCardAttacker().getY();
            Card attacker = table.getTable().get(xAttacker).get(yAttacker);
            Card attacked = table.getTable().get(xAttacked).get(yAttacked);

            if (attacker.getIsFrozen() == 1) {
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                ObjectNode help1 = objectMapper.createObjectNode();
                help1.put("x", xAttacked);
                help1.put("y", yAttacked);
                node.put("cardAttacked", help1);
                ObjectNode help2 = objectMapper.createObjectNode();
                help2.put("x", xAttacker);
                help2.put("y", yAttacker);
                node.put("cardAttacker", help2);
                node.put("error", "Attacker card is frozen.");
                output.add(node);
            } else if (attacker.getHasAttacked() == 1) {
                ObjectNode node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAbility");
                ObjectNode help1 = objectMapper.createObjectNode();
                help1.put("x", xAttacked);
                help1.put("y", yAttacked);
                node.put("cardAttacked", help1);
                ObjectNode help2 = objectMapper.createObjectNode();
                help2.put("x", xAttacker);
                help2.put("y", yAttacker);
                node.put("cardAttacker", help2);
                node.put("error", "Attacker card has already attacked this turn.");
                output.add(node);
            } else if (attacker.getName().equals("Disciple")) {
                //Disciple only on its own cards
                int esteOk = 0;
                if (currentPlayer == 1 && (xAttacked == 2 || xAttacked == backRowIndex)) {
                    esteOk = 1;
                }
                if (currentPlayer == 2 && (xAttacked == 0 || xAttacked == 1)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAbility");
                    ObjectNode help1 = objectMapper.createObjectNode();
                    help1.put("x", xAttacked);
                    help1.put("y", yAttacked);
                    node.put("cardAttacked", help1);
                    ObjectNode help2 = objectMapper.createObjectNode();
                    help2.put("x", xAttacker);
                    help2.put("y", yAttacker);
                    node.put("cardAttacker", help2);
                    node.put("error", "Attacked card does not belong to the current player.");
                    output.add(node);
                } else {
                    // good, now apply Disciple
                    attacker.setHasAttacked(1);
                    attacked.setHealth(2 + attacked.getHealth());
                }
            } else if (attacker.getName().equals("The Ripper")) {
                int esteOk = 0;
                if (currentPlayer == 1 && (xAttacked == 0 || xAttacked == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 2 && (xAttacked == 2 || xAttacked == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAbility");
                    ObjectNode help1 = objectMapper.createObjectNode();
                    help1.put("x", xAttacked);
                    help1.put("y", yAttacked);
                    node.put("cardAttacked", help1);
                    ObjectNode help2 = objectMapper.createObjectNode();
                    help2.put("x", xAttacker);
                    help2.put("y", yAttacker);
                    node.put("cardAttacker", help2);
                    node.put("error", "Attacked card does not belong to the enemy.");
                    output.add(node);
                } else {
                    // also check for tank
                    int existaTank = 0;
                    if (currentPlayer == 1) {
                        for (Card it : table.getTable().get(1)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    } else {
                        for (Card it : table.getTable().get(2)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    }
                    if (existaTank == 1 && attacked.isTank() == 0) {
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAbility");
                        ObjectNode help1 = objectMapper.createObjectNode();
                        help1.put("x", xAttacked);
                        help1.put("y", yAttacked);
                        node.put("cardAttacked", help1);
                        ObjectNode help2 = objectMapper.createObjectNode();
                        help2.put("x", xAttacker);
                        help2.put("y", yAttacker);
                        node.put("cardAttacker", help2);
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                    } else {
                        //Apply The Ripper
                        attacker.setHasAttacked(1);
                        attacked.setAttackDamage(attacked.getAttackDamage() - 2);
                        if (attacked.getAttackDamage() <= 0) {
                            attacked.setAttackDamage(0);
                        }
                    }
                }
            } else if (attacker.getName().equals("Miraj")) {
                int esteOk = 0;
                if (currentPlayer == 1 && (xAttacked == 0 || xAttacked == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 2 && (xAttacked == 2 || xAttacked == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAbility");
                    ObjectNode help1 = objectMapper.createObjectNode();
                    help1.put("x", xAttacked);
                    help1.put("y", yAttacked);
                    node.put("cardAttacked", help1);
                    ObjectNode help2 = objectMapper.createObjectNode();
                    help2.put("x", xAttacker);
                    help2.put("y", yAttacker);
                    node.put("cardAttacker", help2);
                    node.put("error", "Attacked card does not belong to the enemy.");
                    output.add(node);
                } else {
                    // check tank
                    int existaTank = 0;
                    if (currentPlayer == 1) {
                        for (Card it : table.getTable().get(1)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    } else {
                        for (Card it : table.getTable().get(2)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    }
                    if (existaTank == 1 && attacked.isTank() == 0) {
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAbility");
                        ObjectNode help1 = objectMapper.createObjectNode();
                        help1.put("x", xAttacked);
                        help1.put("y", yAttacked);
                        node.put("cardAttacked", help1);
                        ObjectNode help2 = objectMapper.createObjectNode();
                        help2.put("x", xAttacker);
                        help2.put("y", yAttacker);
                        node.put("cardAttacker", help2);
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                    } else {
                        //Apply Miraj
                        attacker.setHasAttacked(1);
                        int aux = attacked.getHealth();
                        attacked.setHealth(attacker.getHealth());
                        attacker.setHealth(aux);
                        if (attacked.getHealth() == 0) {
                            table.getTable().get(xAttacked).remove(yAttacked);
                        }
                    }
                }
            } else if (attacker.getName().equals("The Cursed One")) {
                int esteOk = 0;
                if (currentPlayer == 2 && (xAttacked == 2 || xAttacked == backRowIndex)) {
                    esteOk = 1;
                }
                if (currentPlayer == 1 && (xAttacked == 0 || xAttacked == 1)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("command", "cardUsesAbility");
                    ObjectNode help1 = objectMapper.createObjectNode();
                    help1.put("x", xAttacked);
                    help1.put("y", yAttacked);
                    node.put("cardAttacked", help1);
                    ObjectNode help2 = objectMapper.createObjectNode();
                    help2.put("x", xAttacker);
                    help2.put("y", yAttacker);
                    System.out.println(xAttacker + " " + yAttacker);
                    node.put("cardAttacker", help2);
                    node.put("error", "Attacked card does not belong to the enemy.");
                    output.add(node);
                } else {
                    // also in this case check for tank
                    int existaTank = 0;
                    if (currentPlayer == 1) {
                        for (Card it : table.getTable().get(1)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    } else {
                        for (Card it : table.getTable().get(2)) {
                            if (it.isTank() == 1) {
                                existaTank = 1;
                            }
                        }
                    }
                    if (existaTank == 1 && attacked.isTank() == 0) {
                        ObjectNode node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAbility");
                        ObjectNode help1 = objectMapper.createObjectNode();
                        help1.put("x", xAttacked);
                        help1.put("y", yAttacked);
                        node.put("cardAttacked", help1);
                        ObjectNode help2 = objectMapper.createObjectNode();
                        help2.put("x", xAttacker);
                        help2.put("y", yAttacker);
                        node.put("cardAttacker", help2);
                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                    } else {
                        //Apply The Cursed One
                        attacker.setHasAttacked(1);
                        int aux = attacked.getHealth();
                        attacked.setHealth(attacked.getAttackDamage());
                        if (attacked.getHealth() <= 0) {
                            table.getTable().get(xAttacked).remove(yAttacked);
                        }
                        attacked.setAttackDamage(aux);
                    }
                }
            }
        }
    }

    /**
     * Method for using the ability of the hero
     */
    public static void useHeroAbility(final ObjectMapper objectMapper, final ArrayNode output,
                                      final Player player1, final Player player2,
                                      final Table table, final int currentPlayer,
                                      final ActionsInput iterator) {
        if (iterator.getCommand().equals("useHeroAbility")) {
            int affectedRow = iterator.getAffectedRow();
            ObjectNode node;
            Player playerNow = null;
            if (currentPlayer == 1) {
                playerNow = player1;
            } else {
                playerNow = player2;
            }
            if (playerNow.getManaPlayer() < playerNow.getHero().getMana()) {
                node = objectMapper.createObjectNode();
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Not enough mana to use hero's ability.");
                output.add(node);
            } else if (playerNow.getHero().getHasAttacked() == 1) {
                node = objectMapper.createObjectNode();
                node.put("command", "useHeroAbility");
                node.put("affectedRow", affectedRow);
                node.put("error", "Hero has already attacked this turn.");
                output.add(node);
            } else if (playerNow.getHero().getName().equals("Lord Royce")) {
                //apply on enemy
                int esteOk = 0;
                if (currentPlayer == 1 && (affectedRow == 0 || affectedRow == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 2 && (affectedRow == 2 || affectedRow == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    node = objectMapper.createObjectNode();
                    node.put("command", "useHeroAbility");
                    node.put("affectedRow", affectedRow);
                    node.put("error", "Selected row does not belong to the enemy.");
                    output.add(node);
                } else {
                    //now we have permission to use
                    playerNow.getHero().setHasAttacked(1);

                    playerNow.setManaPlayer(playerNow.getManaPlayer()
                            - playerNow.getHero().getMana());
                    int maxAttack = 0;
                    for (Card it : table.getTable().get(affectedRow)) {
                        if (it.getAttackDamage() > maxAttack) {
                            maxAttack = it.getAttackDamage();
                        }
                    }
                    for (Card it : table.getTable().get(affectedRow)) {
                        if (it.getAttackDamage() == maxAttack) {
                            it.setIsFrozen(1);
                        }
                    }
                }
            } else if (playerNow.getHero().getName().equals("Empress Thorina")) {
                //harakiri on enemy
                int esteOk = 0;
                if (currentPlayer == 1 && (affectedRow == 0 || affectedRow == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 2 && (affectedRow == 2 || affectedRow == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    node = objectMapper.createObjectNode();
                    node.put("command", "useHeroAbility");
                    node.put("affectedRow", affectedRow);
                    node.put("error", "Selected row does not belong to the enemy.");
                    output.add(node);
                } else {
                    //now we can use
                    playerNow.getHero().setHasAttacked(1);

                    playerNow.setManaPlayer(playerNow.getManaPlayer()
                            - playerNow.getHero().getMana());
                    int maxHealth = 0;
                    for (Card it : table.getTable().get(affectedRow)) {
                        if (it.getHealth() > maxHealth) {
                            maxHealth = it.getHealth();
                        }
                    }
                    Card destroyIt = null;
                    for (Card it : table.getTable().get(affectedRow)) {
                        if (it.getHealth() == maxHealth) {
                            destroyIt = it;
                        }
                    }
                    table.getTable().get(affectedRow).remove(destroyIt);
                }
            } else if (playerNow.getHero().getName().equals("General Kocioraw")) {
                int esteOk = 0;
                if (currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 1 && (affectedRow == 2 || affectedRow == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    node = objectMapper.createObjectNode();
                    node.put("command", "useHeroAbility");
                    node.put("affectedRow", affectedRow);
                    node.put("error", "Selected row does not belong to the current player.");
                    output.add(node);
                } else {
                    playerNow.setManaPlayer(playerNow.getManaPlayer()
                            - playerNow.getHero().getMana());
                    //now we can use
                    playerNow.getHero().setHasAttacked(1);
                    for (Card it : table.getTable().get(affectedRow)) {
                        it.setAttackDamage(it.getAttackDamage() + 1);
                    }
                }
            } else if (playerNow.getHero().getName().equals("King Mudface")) {
                int esteOk = 0;
                if (currentPlayer == 2 && (affectedRow == 0 || affectedRow == 1)) {
                    esteOk = 1;
                }
                if (currentPlayer == 1 && (affectedRow == 2 || affectedRow == backRowIndex)) {
                    esteOk = 1;
                }
                if (esteOk == 0) {
                    node = objectMapper.createObjectNode();
                    node.put("command", "useHeroAbility");
                    node.put("affectedRow", affectedRow);
                    node.put("error", "Selected row does not belong to the current player.");
                    output.add(node);
                } else {
                    //now use
                    playerNow.setManaPlayer(playerNow.getManaPlayer()
                            - playerNow.getHero().getMana());
                    playerNow.getHero().setHasAttacked(1);
                    for (Card it : table.getTable().get(affectedRow)) {
                        it.setHealth(it.getHealth() + 1);
                    }
                }
            }
        }
    }

    /**
     * Method for using the attack of the card
     */
    public static void cardUsesAttack(final ObjectMapper objectMapper, final ArrayNode output,
                                      final Table table, final int currentPlayer,
                                      final ActionsInput iterator) {
        if (iterator.getCommand().equals("cardUsesAttack")) {
            int xAttacker = iterator.getCardAttacker().getX();
            int yAttacker = iterator.getCardAttacker().getY();
            Card attacker = table.getTable().get(xAttacker).get(yAttacker);

            int xAttacked = iterator.getCardAttacked().getX();
            int yAttacked = iterator.getCardAttacked().getY();

            ObjectNode node;
            if ((currentPlayer == 1 && (xAttacked == 2 || xAttacked == backRowIndex))
                    || (currentPlayer == 2 && (xAttacked == 0 || xAttacked == 1))) {
                node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAttack");
                ObjectNode help1 = objectMapper.createObjectNode();
                help1.put("x", xAttacked);
                help1.put("y", yAttacked);
                node.put("cardAttacked", help1);

                ObjectNode help2 = objectMapper.createObjectNode();
                help2.put("x", xAttacker);
                help2.put("y", yAttacker);
                node.put("cardAttacker", help2);
                node.put("error", "Attacked card does not belong to the enemy.");
                output.add(node);
            } else if (attacker.getHasAttacked() == 1) {
                node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAttack");
                ObjectNode help1 = objectMapper.createObjectNode();
                help1.put("x", xAttacked);
                help1.put("y", yAttacked);
                node.put("cardAttacked", help1);

                ObjectNode help2 = objectMapper.createObjectNode();
                help2.put("x", xAttacker);
                help2.put("y", yAttacker);
                node.put("cardAttacker", help2);
                node.put("error", "Attacker card has already attacked this turn.");
                output.add(node);
            } else if (attacker.getIsFrozen() == 1) {
                node = objectMapper.createObjectNode();
                node.put("command", "cardUsesAttack");
                ObjectNode help1 = objectMapper.createObjectNode();
                help1.put("x", xAttacked);
                help1.put("y", yAttacked);
                node.put("cardAttacked", help1);

                ObjectNode help2 = objectMapper.createObjectNode();
                help2.put("x", xAttacker);
                help2.put("y", yAttacker);
                node.put("cardAttacker", help2);
                node.put("error", "Attacker card is frozen.");
                output.add(node);
            } else {
                // check for tank
                int existsTank = 0;
                if (currentPlayer == 2) {
                    for (int iter = 2; iter <= backRowIndex; ++iter) {
                        for (Card card : table.getTable().get(iter)) {
                            if (card.getIsTank() == 1) {
                                existsTank = 1;
                            }
                        }
                    }
                } else {
                    for (int iter = 0; iter <= 1; ++iter) {
                        for (Card card : table.getTable().get(iter)) {
                            if (card.getIsTank() == 1) {
                                existsTank = 1;
                            }
                        }
                    }
                }
                if (table.getTable().get(xAttacked).size() > yAttacked) {
                    Card attacked = table.getTable().get(xAttacked).get(yAttacked);
                    if (existsTank == 1 && attacked.getIsTank() == 0) {
                        node = objectMapper.createObjectNode();
                        node.put("command", "cardUsesAttack");

                        ObjectNode help1 = objectMapper.createObjectNode();
                        help1.put("x", xAttacked);
                        help1.put("y", yAttacked);
                        node.put("cardAttacked", help1);

                        ObjectNode help2 = objectMapper.createObjectNode();
                        help2.put("x", xAttacker);
                        help2.put("y", yAttacker);
                        node.put("cardAttacker", help2);

                        node.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(node);
                    } else {
                        //time for war
                        attacked.setHealth(attacked.getHealth() - attacker.getAttackDamage());
                        if (attacked.getHealth() <= 0) {
                            table.getTable().get(xAttacked).remove(yAttacked);
                        }
                        attacker.setHasAttacked(1);
                    }
                }

            }

        }
    }

    /**
     * Method for printing all the frozen cards from the table.
     */
    public static void getFrozenCardsOnTable(final ObjectMapper objectMapper,
                                             final ArrayNode output, final Table table,
                                             final ActionsInput iterator) {
        if (iterator.getCommand().equals("getFrozenCardsOnTable")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getFrozenCardsOnTable");
            ArrayNode help = objectMapper.createArrayNode();

            for (int line = 0; line <= backRowIndex; ++line) {
                for (Card card : table.getTable().get(line)) {
                    if (card.getIsFrozen() == 1) {

                        ObjectNode aux = objectMapper.createObjectNode();
                        aux.put("mana", card.getMana());
                        aux.put("attackDamage", card.getAttackDamage());
                        aux.put("health", card.getHealth());
                        aux.put("description", card.getDescription());
                        ArrayNode outColors = objectMapper.createArrayNode();
                        for (String iteratorColors : card.getColors()) {
                            outColors.add(iteratorColors);
                        }
                        aux.set("colors", outColors);
                        aux.put("name", card.getName());
                        help.add(aux);

                    }
                }
            }
            node.set("output", help);
            output.add(node);
        }
    }

    /**
     * Method for using the environment card(s).
     */
    public static void useEnvironmentCard(final ObjectMapper objectMapper,
                                          final ArrayNode output, final Player player1,
                                          final Player player2,
                                          final Table table, final int currentPlayer,
                                          final ActionsInput iterator) {
        if (iterator.getCommand().equals("useEnvironmentCard")) {
            int handIdx = iterator.getHandIdx();
            int blastedRow = iterator.getAffectedRow();
            Player playerNow;

            Card cardForAttack = null;
            if (currentPlayer == 1) {
                cardForAttack = player1.getHandCards().get(handIdx);
                playerNow = player1;
            } else {
                cardForAttack = player2.getHandCards().get(handIdx);
                playerNow = player2;
            }
            ArrayList<Card> deepCopyHandCards = new ArrayList<>();
            for (Card iterate : playerNow.getHandCards()) {
                deepCopyHandCards.add(new Card(iterate));
            }
            ObjectNode node;
            if (!((cardForAttack.getName().equals("Heart Hound")
                    || cardForAttack.getName().equals("Firestorm")
                    || cardForAttack.getName().equals("Winterfell")))) {
                node = objectMapper.createObjectNode();
                node.put("command", "useEnvironmentCard");
                node.put("handIdx", handIdx);
                node.put("affectedRow", blastedRow);
                node.put("error", "Chosen card is not of type environment.");
                output.add(node);
            } else if (playerNow.getManaPlayer() < cardForAttack.getMana()) {
                node = objectMapper.createObjectNode();
                node.put("command", "useEnvironmentCard");
                node.put("handIdx", handIdx);
                node.put("affectedRow", blastedRow);
                node.put("error", "Not enough mana to use environment card.");
                output.add(node);
            } else if (((blastedRow == 0 || blastedRow == 1) && currentPlayer == 2)
                    || (((blastedRow == 2 || blastedRow == backRowIndex)
                    && currentPlayer == 1))) {
                node = objectMapper.createObjectNode();
                node.put("command", "useEnvironmentCard");
                node.put("handIdx", handIdx);
                node.put("affectedRow", blastedRow);
                node.put("error", "Chosen row does not belong to the enemy.");
                output.add(node);
            } else if (cardForAttack.getName().equals("Heart Hound")) {
                node = objectMapper.createObjectNode();
                int myRow = backRowIndex - blastedRow;
                if (table.getTable().get(myRow).size() >= maxCardsPerRow) {
                    node.put("command", "useEnvironmentCard");
                    node.put("handIdx", handIdx);
                    node.put("affectedRow", blastedRow);
                    node.put("error", "Cannot steal enemy card since the player's row is full.");
                    output.add(node);
                } else {
                    Card newCard = null;
                    int maxHealth = 0;
                    for (Card searchMaxMinion : table.getTable().get(blastedRow)) {
                        if (searchMaxMinion.getHealth() > maxHealth) {
                            maxHealth = searchMaxMinion.getHealth();
                            newCard = searchMaxMinion;
                        }
                    }
                    Card addCard = new Card(newCard);
                    table.getTable().get(backRowIndex - blastedRow).add(addCard);
                    table.getTable().get(blastedRow).remove(addCard);
                    playerNow.setManaPlayer(playerNow.getManaPlayer() - cardForAttack.getMana());
                    playerNow.removeFromHand(handIdx);

                }
            } else {
                // Winterfell and Firestorn
                if (cardForAttack.getName().equals("Winterfell") && cardForAttack != null) {
                    for (Card iterateCard : table.getTable().get(blastedRow)) {
                        iterateCard.setIsFrozen(1);
                    }
                    playerNow.setManaPlayer(playerNow.getManaPlayer() - cardForAttack.getMana());
                    playerNow.removeFromHand(handIdx);


                } else {
                    //Firestorm
                    for (Card iterateCard : table.getTable().get(blastedRow)) {
                        iterateCard.setHealth(iterateCard.getHealth() - 1);
                    }
                    int help = 0;
                    while (help < table.getTable().get(blastedRow).size()) {
                        if (table.getTable().get(blastedRow).get(help).getHealth() == 0) {
                            table.getTable().get(blastedRow).remove(help);
                        } else {
                            help++;
                        }
                    }
                    playerNow.setManaPlayer(playerNow.getManaPlayer() - cardForAttack.getMana());
                    playerNow.removeFromHand(handIdx);
                }
            }
        }
    }

    /**
     * Method for getting a card at a certain position.
     */
    public static void getCardAtPosition(final ObjectMapper objectMapper,
                                         final ArrayNode output, final Table table,
                                         final ActionsInput iterator) {
        if (iterator.getCommand().equals("getCardAtPosition")) {
            int x = iterator.getX();
            int y = iterator.getY();
            ObjectNode node;
            ArrayList<Card> row = null;
            if (x > backRowIndex && x <= 0) {
                node = objectMapper.createObjectNode();
                node.put("command", "getCardAtPosition");
                node.put("x", x);
                node.put("y", y);
                node.put("output", "No card available at that position.");
                output.add(node);
            } else {
                row = table.getTable().get(x);
            }
            if (row.size() <= y) {
                node = objectMapper.createObjectNode();
                node.put("command", "getCardAtPosition");
                node.put("x", x);
                node.put("y", y);
                node.put("output", "No card available at that position.");
                output.add(node);
            } else {
                Card searchedCard = row.get(y);
                node = objectMapper.createObjectNode();
                node.put("command", "getCardAtPosition");
                node.put("x", x);
                node.put("y", y);

                ObjectNode aux = objectMapper.createObjectNode();

                aux.put("mana", searchedCard.getMana());
                if (searchedCard.getName().equals("The Cursed One")) {
                    searchedCard.setAttackDamage(0);
                }
                if (searchedCard.getName().equals("Disciple")) {
                    searchedCard.setAttackDamage(0);
                }
                aux.put("attackDamage", searchedCard.getAttackDamage());
                aux.put("health", searchedCard.getHealth());
                aux.put("description", searchedCard.getDescription());
                ArrayNode outColors = objectMapper.createArrayNode();
                for (String iteratorColors : searchedCard.getColors()) {
                    outColors.add(iteratorColors);
                }
                aux.set("colors", outColors);
                aux.put("name", searchedCard.getName());
                node.set("output", aux);
                output.add(node);
            }
        }
    }

    /**
     * Print in the json format all the environment cards in hand
     */
    public static void getEnvironmentCardsInHand(final ObjectMapper objectMapper,
                                                 final ArrayNode output, final Player player1,
                                                 final Player player2,
                                                 final ActionsInput iterator) {
        if (iterator.getCommand().equals("getEnvironmentCardsInHand")) {
            // all environment cards from hand
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getEnvironmentCardsInHand");
            node.put("playerIdx", iterator.getPlayerIdx());
            ArrayNode help = objectMapper.createArrayNode();
            if (iterator.getPlayerIdx() == 1) {
                for (Card card : player1.getHandCards()) {
                    if (card.getName().equals("Firestorm")
                            || card.getName().equals("Winterfell")
                            || card.getName().equals("Heart Hound")) {
                        ObjectNode aux = objectMapper.createObjectNode();
                        aux.put("mana", card.getMana());
                        aux.put("description", card.getDescription());
                        ArrayNode outColors = objectMapper.createArrayNode();
                        for (String iteratorColors : card.getColors()) {
                            outColors.add(iteratorColors);
                        }
                        aux.set("colors", outColors);
                        aux.put("name", card.getName());
                        help.add(aux);
                    }
                }
                node.set("output", help);
            }

            if (iterator.getPlayerIdx() == 2) {
                for (Card card : player2.getHandCards()) {
                    if (card.getName().equals("Firestorm")
                            || card.getName().equals("Winterfell")
                            || card.getName().equals("Heart Hound")) {
                        ObjectNode aux = objectMapper.createObjectNode();
                        aux.put("mana", card.getMana());
                        aux.put("description", card.getDescription());
                        ArrayNode outColors = objectMapper.createArrayNode();
                        for (String iteratorColors : card.getColors()) {
                            outColors.add(iteratorColors);
                        }
                        aux.set("colors", outColors);
                        aux.put("name", card.getName());
                        help.add(aux);
                    }
                }
                node.set("output", help);
            }
            output.add(node);
        }
    }

    /**
     * Method for getting all cards from the table.
     */
    public static void getCardsOnTable(final ObjectMapper objectMapper,
                                       final ArrayNode output,
                                       final Table table,
                                       final ActionsInput iterator) {
        if (iterator.getCommand().equals("getCardsOnTable")) {
            ObjectNode node = objectMapper.createObjectNode();

            ArrayNode outputTable = objectMapper.createArrayNode();
            node.put("command", "getCardsOnTable");

            for (int line = 0; line <= backRowIndex; ++line) {
                ArrayNode help = objectMapper.createArrayNode();
                for (Card card : table.getTable().get(line)) {
                    ObjectNode aux = objectMapper.createObjectNode();
                    aux.put("mana", card.getMana());
                    aux.put("attackDamage", card.getAttackDamage());
                    aux.put("health", card.getHealth());
                    aux.put("description", card.getDescription());
                    ArrayNode outColors = objectMapper.createArrayNode();
                    for (String iteratorColors : card.getColors()) {
                        outColors.add(iteratorColors);
                    }
                    aux.set("colors", outColors);
                    aux.put("name", card.getName());
                    help.add(aux);
                }
                outputTable.add(help);
            }
            node.set("output", outputTable);
            output.add(node);
        }
    }

    /**
     * Method for placing a card at the correct coordinates.
     */
    public static void placeCard(final ObjectMapper objectMapper,
                                 final ArrayNode output,
                                 final Player player1,
                                 final Player player2,
                                 final Table table,
                                 final int currentPlayer,
                                 final ActionsInput iterator) {
        if (iterator.getCommand().equals("placeCard")) {
            int handIndex = iterator.getHandIdx();
            int firstRow;
            int secondRow;
            Card card = null;
            Player playerNow = null;
            if (currentPlayer == 1) {
                card = player1.getHandCards().get(handIndex);
                playerNow = player1;
                firstRow = 2;
                secondRow = backRowIndex;
            } else {
                card = player2.getHandCards().get(handIndex);
                playerNow = player2;
                firstRow = 1;
                secondRow = 0;
            }
            ObjectNode node = null;

            if (card.getIsEnvironment() == 1) {
                node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", handIndex);
                node.put("error", "Cannot place environment card on table.");
                output.add(node);

            } else if (playerNow.getManaPlayer() < card.getMana()) {
                node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", handIndex);
                node.put("error", "Not enough mana to place card on table.");
                output.add(node);

            } else if (card.is1stRow() == 1 && (table.getTable().get(firstRow).size()
                    == maxCardsPerRow)) {
                node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", handIndex);
                node.put("error", "Cannot place card on table since row is full.");
                output.add(node);
            } else if (card.is2ndRow() == 1 && (table.getTable().get(secondRow).size()
                    == maxCardsPerRow)) {
                node = objectMapper.createObjectNode();
                node.put("command", "placeCard");
                node.put("handIdx", handIndex);
                node.put("error", "Cannot place card on table since row is full.");
                output.add(node);
            } else {
                playerNow.setManaPlayer(playerNow.getManaPlayer() - card.getMana());
                if (card.is1stRow() == 1) {
                    table.addMinion(firstRow, card);
                } else {
                    table.addMinion(secondRow, card);
                }
                playerNow.removeFromHand(handIndex);
            }


        }
    }

    /**
     * Method for getting all the cards from the player's hand.
     */
    public static void getCardsInHand(final ObjectMapper objectMapper,
                                      final ArrayNode output,
                                      final Player player1,
                                      final Player player2,
                                      final ActionsInput iterator) {
        if (iterator.getCommand().equals("getCardsInHand")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getCardsInHand");
            node.put("playerIdx", iterator.getPlayerIdx());
            ArrayNode help = objectMapper.createArrayNode();

            if (iterator.getPlayerIdx() == 1) {
                for (Card card : player1.getHandCards()) {
                    ObjectNode aux = objectMapper.createObjectNode();
                    aux.put("mana", card.getMana());
                    if (!(card.getName().equals("Firestorm")
                            || card.getName().equals("Winterfell")
                            || card.getName().equals("Heart Hound"))) {
                        aux.put("attackDamage", card.getAttackDamage());
                        aux.put("health", card.getHealth());
                    }
                    aux.put("description", card.getDescription());
                    ArrayNode outColors = objectMapper.createArrayNode();
                    for (String iteratorColors : card.getColors()) {
                        outColors.add(iteratorColors);
                    }
                    aux.set("colors", outColors);
                    aux.put("name", card.getName());
                    help.add(aux);
                }
                node.set("output", help);
            }

            if (iterator.getPlayerIdx() == 2) {
                for (Card card : player2.getHandCards()) {
                    ObjectNode aux = objectMapper.createObjectNode();
                    aux.put("mana", card.getMana());
                    if (!(card.getName().equals("Firestorm")
                            || card.getName().equals("Winterfell")
                            || card.getName().equals("Heart Hound"))) {
                        aux.put("attackDamage", card.getAttackDamage());
                        aux.put("health", card.getHealth());
                    }
                    aux.put("description", card.getDescription());
                    ArrayNode outColors = objectMapper.createArrayNode();
                    for (String iteratorColors : card.getColors()) {
                        outColors.add(iteratorColors);
                    }
                    aux.set("colors", outColors);
                    aux.put("name", card.getName());
                    help.add(aux);
                }
                node.set("output", help);
            }
            output.add(node);
        }
    }

    /**
     * Get the hero of the player.
     */
    public static void getPlayerHero(final ObjectMapper objectMapper,
                                     final ArrayNode output,
                                     final Player player1,
                                     final Player player2,
                                     final ActionsInput iterator) {
        if (iterator.getCommand().equals("getPlayerHero")) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerHero");
            ObjectNode help = objectMapper.createObjectNode();
            if (iterator.getPlayerIdx() == 1) {
                ArrayNode outColors = objectMapper.createArrayNode();
                for (String iteratorColors : player1.getHero().getColors()) {
                    outColors.add(iteratorColors);
                }
                help.set("colors", outColors);
                help.put("description", player1.getHero().getDescription());
                help.put("health", player1.getHero().getHealth());
                help.put("mana", player1.getHero().getMana());
                help.put("name", player1.getHero().getName());
            }
            if (iterator.getPlayerIdx() == 2) {
                ArrayNode outColors = objectMapper.createArrayNode();
                for (String iteratorColors : player2.getHero().getColors()) {
                    outColors.add(iteratorColors);
                }
                help.set("colors", outColors);
                help.put("description", player2.getHero().getDescription());
                help.put("health", player2.getHero().getHealth());
                help.put("mana", player2.getHero().getMana());
                help.put("name", player2.getHero().getName());
            }
            node.set("output", help);
            node.put("playerIdx", iterator.getPlayerIdx());
            output.add(node);
        }
    }

    /**
     * Method for getting the deck of the player.
     */
    public static void getPlayerDeck(final ObjectMapper objectMapper,
                                     final ArrayNode output,
                                     final Deck player1Deck,
                                     final Deck player2Deck,
                                     final ActionsInput iterator) {
        if (iterator.getCommand().equals("getPlayerDeck")) {

            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "getPlayerDeck");
            node.put("playerIdx", iterator.getPlayerIdx());
            ArrayNode help = objectMapper.createArrayNode();

            if (iterator.getPlayerIdx() == 1) {
                for (Card card : player1Deck.getDeck()) {
                    ObjectNode aux = objectMapper.createObjectNode();
                    aux.put("mana", card.getMana());
                    if (!(card.getName().equals("Winterfell")
                            || card.getName().equals("Firestorm")
                            || card.getName().equals("Heart Hound"))) {
                        aux.put("attackDamage", card.getAttackDamage());
                        aux.put("health", card.getHealth());
                    }
                    aux.put("description", card.getDescription());
                    ArrayNode outColors = objectMapper.createArrayNode();
                    for (String iteratorColors : card.getColors()) {
                        outColors.add(iteratorColors);
                    }
                    aux.set("colors", outColors);
                    aux.put("name", card.getName());
                    help.add(aux);
                }
                node.set("output", help);
            }

            if (iterator.getPlayerIdx() == 2) {
                for (Card card : player2Deck.getDeck()) {
                    ObjectNode aux = objectMapper.createObjectNode();
                    aux.put("mana", card.getMana());
                    if (!(card.getName().equals("Winterfell")
                            || card.getName().equals("Firestorm")
                            || card.getName().equals("Heart Hound"))) {
                        aux.put("attackDamage", card.getAttackDamage());
                        aux.put("health", card.getHealth());
                    }
                    aux.put("description", card.getDescription());
                    ArrayNode outColors = objectMapper.createArrayNode();
                    for (String iteratorColors : card.getColors()) {
                        outColors.add(iteratorColors);
                    }
                    aux.set("colors", outColors);
                    aux.put("name", card.getName());
                    help.add(aux);
                }
                node.set("output", help);
            }
            output.add(node);
        }
    }

    /**
     * For setting the cards accordingly
     * @param player1Deck
     * @param player2Deck
     */
    public static void setCards(Deck player1Deck, Deck player2Deck) {
        for (Card it : player1Deck.getDeck()) {
            if (it.getName().equals("Winterfell")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Heart Hound")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Fire Storm")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Goliath")) {
                it.setIsTank(1);
            }
            if (it.getName().equals("Warden")) {
                it.setIsTank(1);
            }
        }

        for (Card it : player2Deck.getDeck()) {
            if (it.getName().equals("Winterfell")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Heart Hound")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Fire Storm")) {
                it.setIsEnvironment(1);
            }
            if (it.getName().equals("Goliath")) {
                it.setIsTank(1);
            }
            if (it.getName().equals("Warden")) {
                it.setIsTank(1);
            }
        }
    }
}
