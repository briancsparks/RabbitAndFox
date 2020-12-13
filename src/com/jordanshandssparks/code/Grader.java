package com.jordanshandssparks.code;

import java.awt.*;

/**
 * Executes a number of "rabbit hunts", in which a fox moves around a field
 * containing bushes, looking for a rabbit, and prints out the percentage
 * of times that the rabbit escapes.
 *
 * @author David Matuszek
 * @version October 24, 2001
 */
public class Grader {

  // class variables
  private static Object[][] field;
  private static Field model;
  private static View view;
  private static Controller controller;
  private static int numberOfRows;
  private static int numberOfColumns;

  public void doNTests(int n) {
    numberOfRows = numberOfColumns = 20;
    field = new Object[numberOfRows][numberOfColumns];
    model = new Field(field);
    countGames(n);
  }

  /**
   * Runs NUMBER_OF_TRIALS rabbit hunts, and prints out the
   * results as a percentage of times the rabbit escapes.
   */
  private static void countGames(int NUMBER_OF_TRIALS) {

    // compute base score as percent of rabbit escapes
    int numberOfEscapes = 0;
    for (int i = 0; i < NUMBER_OF_TRIALS; i++) {
      model.reset();
      while (!model.gameIsOver) {
        model.allowSingleMove();
      }
      if (model.rabbitIsAlive) {
        numberOfEscapes++;
      }
    }
    double percent = (100 * numberOfEscapes) / NUMBER_OF_TRIALS;
    int roundedPercent = (int) (percent + 0.5);
    System.out.println("Rabbit escapes: " + numberOfEscapes +
      " times out of " + NUMBER_OF_TRIALS +
      ", or " + roundedPercent + "%");

    // check for problems with assumptions about named constants
    Field.NUMBER_OF_ROWS = Field.NUMBER_OF_COLUMNS = 15;
    try {
      for (int i = 0; i < 10; i++) {
        model.reset();
        while (!model.gameIsOver) {
          model.allowSingleMove();
        }
      }
    } catch (Throwable e) {
      System.out.println("Error detected in use of constants.");
      e.printStackTrace();
    }
  }
}
