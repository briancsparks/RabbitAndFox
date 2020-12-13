package com.jordanshandssparks.code;


import java.util.Random;

class Animal {

  // instance variables -- every animal has a location
  private Field model;
  int row;
  int column;
  static Random randomNumberGenerator = new Random();

  public Animal(Field model, int row, int column) {
    this.model = model;
    this.row = row;
    this.column = column;
  }

  static int random(int min, int max) {
    return Field.random(min, max);
  }

  int getObjectInDirection(int direction) {
    return model.getObjectInDirection
      (new Location(row, column), direction);
  }

  int distanceToObject(int direction) {
    return model.distanceToObject
      (new Location(row, column), direction);
  }

  public Location locationInDirection(int direction) {
    return new Location
      (row + model.rowChange(direction), column
        + model.columnChange(direction));
  }

  boolean canMove(int direction) {
    if (direction == Field.STAY)
      return true;
    if (distanceToObject(direction) > 1)
      return true;
    int object = getObjectInDirection(direction);
    if (object == Field.EDGE || object == Field.BUSH)
      return false;
    return true;
  }

  int decideMove() {
    return Field.STAY;
  }
}

class Fox extends Animal {
  // instance variables
  private boolean haveSeenRabbit = false;
  private boolean canSeeRabbitNow = false;
  private int distanceToRabbit;
  private int directionToRabbit;
  private int currentDirection;

  public Fox(Field model, int row, int column) {
    super(model, row, column);
    currentDirection = Field.random(Field.MIN_DIRECTION,
      Field.MAX_DIRECTION);
  }

  int decideMove() {
    // look all around for rabbit
    canSeeRabbitNow = false;
    for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++)
    {
      if (getObjectInDirection(i) == Field.RABBIT) {
        canSeeRabbitNow = haveSeenRabbit = true;
        directionToRabbit = i;
        distanceToRabbit = distanceToObject(i);
      }
    }
    // if rabbit has been seen recently,
    // move toward its last known position
    if (haveSeenRabbit) {
      if (distanceToRabbit > 0) {
        distanceToRabbit--;
        return directionToRabbit;
      } else { // rabbit was here--where did it go?
        haveSeenRabbit = false;
        currentDirection = Field.random(Field.MIN_DIRECTION,
          Field.MAX_DIRECTION);
      }
    }
    // either haven't seen rabbit, or lost track of rabbit
    // continue with current direction, maybe dodging bushes
    if (canMove(currentDirection))
      return currentDirection;
    else if
    (canMove(Field.calculateNewDirection(currentDirection, 1)))
      return Field.calculateNewDirection(currentDirection, 1);
    else if
    (canMove(Field.calculateNewDirection(currentDirection, -1)))
      return Field.calculateNewDirection(currentDirection, -1);
    else {
      currentDirection = Field.random(Field.MIN_DIRECTION,
        Field.MAX_DIRECTION);
      for (int i = 0; i < 8; i++) {
        if (canMove(currentDirection))
          return currentDirection;
        else
          currentDirection = Field.calculateNewDirection(
            currentDirection, 1);
      }
    }
    // stuck!
    return Field.STAY;
  }

}

class Rabbit extends Animal {

  public Rabbit(Field field, int row, int column) {
    super(field, row, column);
  }

  public int decideMove() {
    return random(Field.MIN_DIRECTION, Field.MAX_DIRECTION);
  }

}

class Bush {

}



















