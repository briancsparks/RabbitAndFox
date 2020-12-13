package com.jordanshandssparks.code;

//  Rabbit Hunt
/*
 * This simulation contains 9 classes: Animal, Bush, Controller,
 * Field, Fox, Location, Rabbit, RabbitHunt, and View.
 *
 * RabbitHunt is the tester (contains main method).
 *
 * (The following classes are NOT created by ME)
 * The template contains Controller (set up GUI and handle the
 * events), Field(creates a "field" and sets up methods that let
 * animals move and look around), Location (an object to store
 * the row# and column# of animals and bushes), RabbitHunt (the
 * tester class), and View (displays the progress of hunting).
 *
 * (The following classes ARE created by ME)
 * The Animal class, the Bush class, the Fox class, the Rabbit
 * class.
 *
 * Since there are SO MANY lines of code, I decide only to print
 * the classes that are created by ME and the tester.
 * (Because I don't have enough paper :C)
 */

/**
 * Describes a "rabbit hunt", in which a fox moves around a field
 * containing bushes, looking for a rabbit. The rabbit, of course,
 * tries not to be caught by the fox.
 *
 * @author David Matuszek
 * @version October 12, 2001
 */
public class RabbitHunt {
  // class variables
  private static Object[][] field;
  private static Field model;
  private static View view;
  private static Controller controller;
  private static int numberOfRows;
  private static int numberOfColumns;

  public static void main(String args[]) {
    numberOfRows = 20;
    numberOfColumns = 20;
    field = new Object[numberOfRows][numberOfColumns];
    model = new Field(field);
    view = new View(field);
    controller = new Controller(model, view);
  }
}
