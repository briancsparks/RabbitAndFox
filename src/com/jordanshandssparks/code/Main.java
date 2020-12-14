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
    for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
      if (getObjectInDirection(i) == Field.RABBIT) {
        canSeeRabbitNow = haveSeenRabbit = true;
        directionToRabbit = i;
        distanceToRabbit = distanceToObject(i);
//        System.out.printf("See You! %d\n", distanceToRabbit);
      }
    }
//    System.out.println("");

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

class Bush {

}

class Rabbit extends Animal {

  static char dirSymbols[] = {'^', '7', '>', 'J', 'v', 'L', '<', '\\', '*'};

  public Rabbit(Field model, int row, int column) {
    super(model, row, column);
  }

  public int decideMove() {
    for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
      if (getObjectInDirection(i) == Field.FOX) {
        int distance = distanceToObject(i);

        // Run directly away
        int willSettleFor = (i + 4) % 8;

        for (int j = Field.MIN_DIRECTION; j <= Field.MAX_DIRECTION; j++) {
          int directionFromFox = j + 3;
          int directionOfBlindSpot = (i + directionFromFox) % 8;

          // That's nice and all, but is that way unblocked?
          int blockDistance = distanceToObject(directionOfBlindSpot);
          if (blockDistance > 1) {
            int safety = howSafe(directionFromFox, distance);
            if (safety == 2) {
//              System.out.printf("   %2d %2d %d %c %c  ", blockDistance, distance, safety, dirSymbols[(i+4) % 8], dirSymbols[directionOfBlindSpot]);

              return directionOfBlindSpot;
            }

            if (safety > 0) {
              willSettleFor = directionFromFox;
            }
          }

//          System.out.printf("XX %2d\n", blockDistance);
        }

        return willSettleFor;
      }
    }

    return Field.STAY;
//    return random(Field.MIN_DIRECTION, Field.MAX_DIRECTION);
  }

  private int next(int i) {
    return (i+1) % 8;
  }

  // Returns how safe the direction is -- 0 is not safe at all, 1 is un-eaten, but you will be spotted, you want 2.
  private int howSafe(int turnN, int distance) {
    // 3 and 5 are always safe
    if (turnN == 3 || turnN == 5) {
      return 2;
    }

    // 4 is always spotted, but safe
    if (turnN == 4) {
      return 1;
    }

    if (distance <= 1) {
      return 0;
    } else if (distance == 2) {
      // 2, 3, 5, 6 are safe; 4 isn't good, but you'll last at least one more turn -- 3, 4, 5 have already been answered
      if (turnN == 1 || turnN == 7 || turnN == 8) {
        return 0;
      }

      // Only 2 and 6 are still possible
      return 2;
    }

    // They are 3 away or more, only 4, and 8 let us be seen
    if (turnN == 8) {
      return 1;
    }

    return 2;
  }

}

class RabbitX extends Animal {

  static char dirSymbols[] = {'^', '7', '>', 'J', 'v', 'L', '<', '\\', '*'};

  static private int middleRowNumber = Field.NUMBER_OF_ROWS / 2;
  static private int middleColNumber = Field.NUMBER_OF_COLUMNS / 2;
  static private int maxRowNumber = Field.NUMBER_OF_ROWS - 1;
  static private int maxColNumber = Field.NUMBER_OF_COLUMNS - 1;

  private Location bestSouth = new Location(maxRowNumber, middleColNumber);
  private Location bestWest = new Location(middleRowNumber, 0);
  private Location bestNorth = new Location(0, middleColNumber);
  private Location bestEast = new Location(middleRowNumber, maxColNumber);

  private int myDirection = Field.STAY;
  private Location goalLocation = null;
  private int countdown = -1;
  private char lastShownSymbol = ' ';

  public RabbitX(Field field, int row, int column) {
    super(field, row, column);

  }

  public int decideMove() {

    char fox = '?';
    char rabbit = dirSymbols[myDirection];

    // First, see if the fox can see me
    int foxDirection = -1;
    int foxDistance  = -1;
    for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
      if (getObjectInDirection(i) == Field.FOX) {
        foxDirection = i;
        foxDistance  = distanceToObject(i);
        break;
      }
    }

    // Have I been spotted?
    if (foxDirection != -1) {
      fox = dirSymbols[foxDirection];

      // Yes, evasive action! Go perpendicular
      myDirection = ccwFrom(foxDirection);
      rabbit = dirSymbols[myDirection];

      // Go this direction for a while
      countdown = foxDistance;

      // Now, seek out one of the best locations...
      Location locationNextTurn = locationWhenMovingFrom(row, column, myDirection);

      // Maybe North is best...
      goalLocation = bestNorth;
      int bestDistance = distanceBetween(locationNextTurn, bestNorth);

      // East?
      int distance = distanceBetween(locationNextTurn, bestEast);
      if (distance < bestDistance) {
        distance = bestDistance;
        goalLocation = bestEast;
      }

      // South?
      distance = distanceBetween(locationNextTurn, bestSouth);
      if (distance < bestDistance) {
        distance = bestDistance;
        goalLocation = bestSouth;
      }

      // West?
      distance = distanceBetween(locationNextTurn, bestWest);
      if (distance < bestDistance) {
        distance = bestDistance;
        goalLocation = bestWest;
      }



//      int direction = 0;
//      for (direction = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
//        Location maybeLocation = locationWhenMovingFrom(locationNextTurn.getRow(), locationNextTurn.getCol(), direction);
//        int distance_ = distanceBetween(maybeLocation, locationNextTurn);
//        if (distance_ < distance) {
//          distance = distance_;       /* 'direction' is already pointing us in that direction */
//        }
//      }

      show("!!!!!!!!!!!!!!!!!!!", countdown, fox, rabbit);
      return myDirection;
    }

    // Are we still fleeing?
    if (countdown > 0) {
      countdown -= 1;
      show("!!!!!!", countdown, fox, dirSymbols[myDirection]);
      return myDirection;
    }

    // We are not just blindly fleeing anymore
    myDirection = Field.STAY;

    // If not, move toward goal location -- if we have one
    if (goalLocation != null) {
      int bestDistance = 1000000;
      int direction = 0;
      for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
        Location maybeLocation = locationWhenMovingFrom(row, column, i);
        int distance = distanceBetween(maybeLocation, goalLocation);
        if (distance < bestDistance) {
          bestDistance = distance;
          direction = i;
        }
      }

      myDirection = direction;
      show(":):):)", countdown, fox, dirSymbols[myDirection]);
      return direction;
    }

    /* else -- no goal, still not seen recently */
    myDirection = Field.STAY;
    show(":):):)", countdown, fox, dirSymbols[myDirection]);
    return myDirection;
    //return random(Field.MIN_DIRECTION, Field.MAX_DIRECTION);
  }

  private void show(String msg, int n, char fox, char rabbit) {
    if (rabbit == '*' && lastShownSymbol == '*') {
      return;
    }

    lastShownSymbol = rabbit;
    return;

//    System.out.println(msg);
//    if (n > 0) {
//      System.out.printf("%d\n", n);
//    }
//    System.out.printf("%c\n", fox);
//    System.out.printf("%c\n", rabbit);

  }

  static int ccwFrom(int direction) {
    switch (direction) {
      case Field.N:   return Field.W;
      case Field.NE:  return Field.NW;
      case Field.E:   return Field.N;
      case Field.SE:  return Field.NE;
      case Field.S:   return Field.E;
      case Field.SW:  return Field.SE;
      case Field.W:   return Field.S;
      case Field.NW:  return Field.SW;
    }

    return Field.STAY;
  }

  static private Location locationWhenMovingFrom(int row, int column, int direction) {
    return new Location(row + Field.rowChange(direction), column + Field.columnChange(direction));
  }

  static private int distanceBetween(Location a, Location b) {
    int rowDelta = a.getRow() - b.getRow();
    int colDelta = a.getCol() - b.getCol();

    return (int)Math.sqrt(rowDelta*rowDelta + colDelta*colDelta);
  }

  static private int quadrant(Location location) {

    // Which quadrant am I in?
    if (location.getRow() >= middleRowNumber) {
      if (location.getCol() >= middleColNumber) {
        return 4;
      } else {
        return 3;
      }
    } else {
      if (location.getCol() >= middleColNumber) {
        return 1;
      } else {
        return 2;
      }
    }
  }

}


// https://www.chegg.com/homework-help/questions-and-answers/java-code-assignment-di-erent-others-ve-given-full-working-program-need-improve-get-grade--q30745217

//public class Rabbit extends Animal {
//
//  // instance variables
//  private boolean haveSeenFox = false;
//  private boolean canSeeFoxNow = false;
//  private int distanceToFox;
//  private int directionToFox;
//  private int currentDirection;
//
//  public Rabbit(Model model, int row, int column) {
//    super(model, row, column);
//  }
//
//  //if rabbit sees fox it remembers the direction and distance
////moves away from where it was last seen
////if it gets away and doesnt see fox again it just continues in same direction
//  int decideMove() {
////method variables
//    int edgeCount = 0;
//    int freeSpace=0;
//    int possibleDirection = -1;
//
//// look all around for fox
//    canSeeFoxNow = false;
//    for (int i = Model.MIN_DIRECTION; i <= Model.MAX_DIRECTION; i++) {
//      if (look(i) == Model.FOX) {
//        canSeeFoxNow = haveSeenFox = true;
//        directionToFox = i;
//        distanceToFox = distance(i);
//      } // looks to see if the rabbit is close to the edges
//      else if (look(i) == Model.EDGE) {
//        edgeCount++;
//      }
//    }
//
//// if the rabbit has not seen fox and is close to the edges,
//// move away from the edges
//    if (!haveSeenFox) {
//      if (edgeCount > 0) {
//        for (int i = Model.MIN_DIRECTION; i < Model.MAX_DIRECTION; i++) {
//          if (canMove(i) && distance(i) > freeSpace) {
//            freeSpace = distance(i);
//            possibleDirection = i;
//          }
//        }
//      }
//      if (freeSpace == 0) return Model.STAY;
//      else return possibleDirection;
//    }
//
//    if (canMove(Model.turn(directionToFox, 4))) {
//      return Model.turn(directionToFox, 4);
//    } else if (canMove(Model.turn(directionToFox, 5))) {
//      return Model.turn(directionToFox, 5);
//    } else if (canMove(Model.turn(directionToFox, 3))) {
//      return Model.turn(directionToFox, 3);
//    }
//
//    if (canSeeFoxNow == false) {
//      return Model.STAY;
//    }
//
//    return random(Model.MIN_DIRECTION, Model.MAX_DIRECTION);
//  }
//}













