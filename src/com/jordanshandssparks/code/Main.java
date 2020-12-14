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
      // System.out.println("x");
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
  private boolean haveSeenFox = false;
  private boolean canSeeFoxNow = false;
  private int distanceToFox;
  private int directionToFox;
  private int currentDirection;
  private int oppDirectionToFox;


  int rabbitRow;
  int foxRow;
  int rabbitCol;
  int foxCol;
  Location foxLocation;
  int rabbitRow1;
  int rabbitCol1;

  public Rabbit(Field field, int row, int column) {
    super(field, row, column);
    Location foxLocation = field.getFoxLocation();
    Location rabbitLocation = field.getRabbitLocation();
    int rabbitRow = rabbitLocation.getRow();
    int foxRow = foxLocation.getRow();
    int rabbitCol = rabbitLocation.getCol();
    int foxCol = foxLocation.getCol();
    this.rabbitRow = rabbitRow;
    this.foxRow = foxRow;
    this.rabbitCol = rabbitCol;
    this.foxCol = foxCol;
    this.foxLocation = foxLocation;
    rabbitRow1 = rabbitRow;
    rabbitCol1 = rabbitCol;
  }


  public int findDir(int direction, int distance) {
    int direction90; //90
    int direction_90; //-90
    int direction135; //135
    int direction_135; //-135
    int direction180; //180
    int direction45; //45
    int direction_45; //-45

    if (direction > 5)
      direction90 = direction - 6;
    else
      direction90 = direction + 2;

    if (direction < 1)
      direction_90 = direction + 6;
    else
      direction_90 = direction - 2;

    if (direction > 4)
      direction135 = direction - 5;
    else
      direction135 = direction + 3;

    if (direction < 2)
      direction_135 = direction + 5;
    else
      direction_135 = direction - 3;

    if (direction < 4)
      direction180 = direction + 4;
    else
      direction180 = direction - 4;


    if (direction > 6)
      direction45 = direction - 7;
    else
      direction45 = direction + 1;

    if (direction < 1)
      direction_45 = direction + 7;
    else
      direction_45 = direction - 1;

    // Decide which way to move
    if (canMove(direction135) ) {

      return direction135;
    }

    else if (canMove(direction_135) ) {

      return direction_135;
    }

    else if (canMove(direction90) ) {

      return direction90;
      //
    }

    else if (canMove(direction_90) ) {

      return direction_90;

    }

    if (distance>5) {
      if (canMove(direction45) ) {

        return direction45;
      }
      else if (canMove(direction_45) ) {

        return direction_45;
      }
    } else {
      if (canMove(direction180) ) {

        return direction180;
      }
    }

    return 0;
  }


  public boolean tester()
  {
    if(rabbitCol1 != 18 && rabbitCol1 != 1 && rabbitRow1 != 18 && rabbitRow1 != 1)
    {
      return true;
    }
    return false;
  }

  public void rowCol(int direction)
  {
    if (direction == 0 )
      rabbitRow1--;
    if(direction == 4)
      rabbitRow1++;
    if (direction == 2)
      rabbitCol1++;
    if(direction == 6)
      rabbitCol1--;
    if(direction == 1)
    {
      rabbitRow1--;
      rabbitCol1++;
    }
    if(direction == 3)
    {
      rabbitRow1++;
      rabbitCol1++;
    }
    if(direction == 5)
    {
      rabbitRow1++;
      rabbitCol1--;
    }
    if(direction == 7)
    {
      rabbitRow1--;
      rabbitCol1--;
    }
  }

  public int decideMove() {
    canSeeFoxNow = false;
    //find direction of fox if seen
    for (int i = Field.MIN_DIRECTION; i <= Field.MAX_DIRECTION; i++) {
      if (getObjectInDirection(i) == Field.FOX) {
        canSeeFoxNow = haveSeenFox = true;
        directionToFox = i;
        distanceToFox = distanceToObject(i);
      }
    }

    //rabbit used to get caught on the edges cause it would see the fox behind it and move towards the wall but now it moves towards the center



    if(canSeeFoxNow && directionToFox==0 && rabbitCol1 > 13)
    {
      if(canMove(5))
      {
        rowCol(5);
        return 5;
      }
    }
    if(canSeeFoxNow && directionToFox==4  && rabbitCol1 > 13)
    {
      if(canMove(7))
      {
        rowCol(7);
        return 7;
      }
    }
    if(canSeeFoxNow && directionToFox==2  && rabbitRow1 > 13)
    {
      if(canMove(7))
      {
        rowCol(7);
        return 7;
      }
    }
    if(canSeeFoxNow && directionToFox==6  && rabbitRow1 > 13)
    {
      if(canMove(1))
      {
        rowCol(1);
        return 1;
      }
    }
    if(canSeeFoxNow && directionToFox==4  && rabbitCol1 < 6)
    {
      if(canMove(1))
      {
        rowCol(1);
        return 1;
      }
    }
    if(canSeeFoxNow && directionToFox==0  && rabbitCol1 < 6)
    {
      if(canMove(3))
      {
        rowCol(3);
        return 3;
      }
    }
    if(canSeeFoxNow && directionToFox==6  && rabbitRow1 < 6)
    {
      if(canMove(3))
      {
        rowCol(3);
        return 3;
      }
    }
    if(canSeeFoxNow && directionToFox==2  && rabbitRow1 < 6)
    {
      if(canMove(5))
      {
        rowCol(5);
        return 5;
      }
    }




    //  System.out.println("Fox: "+foxRow +"x"+ foxCol);

//System.out.println(rabbitRow1 + "x" + rabbitCol1);
    // System.out.println("Rabbit: "+rabbitRow +"x"+ rabbitCol);

    if(canSeeFoxNow) {
      currentDirection = findDir(directionToFox, distanceToFox);
      distanceToFox--;
      rowCol(currentDirection);
      return (currentDirection);
    }


    if(rabbitRow1 == 19 && canMove(0))
    {
      rowCol(0);
      return 0;
    }
    if(rabbitRow1 == 0 && canMove(4))
    {
      rowCol(4);
      return 4;
    }
    if(rabbitCol1 == 19 && canMove(6))
    {
      rowCol(6);
      return 6;
    }
    if(rabbitCol1 == 0 && canMove(2))
    {
      rowCol(2);
      return 2;
    }



    return Field.STAY;
  }


  public int oppositeDir(int direction)
  {
    int oppositeDir;
    if (direction <= 3)
      oppositeDir = direction + 4;
    else
      oppositeDir = direction - 4;
    //  }
    return oppositeDir;
  }

}
class Bush {

}


//direction 1 = NE











//if rabbit is heading 4 and fox
