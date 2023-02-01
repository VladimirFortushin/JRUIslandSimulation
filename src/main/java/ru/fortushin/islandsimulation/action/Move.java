package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Move  {
    private static int currentX;
    private static int currentY;
    private static Random r = new Random();
    private static Cell[][] cells;
    public static void setCurrentCoordinates(int x, int y, Cell[][] cells){
        Move.currentX = x;
        Move.currentY = y;
        Move.cells = cells;
    }
    public static void replaceAnimalToAnotherCell(Animal animal){
        switch (r.nextInt(4)){
            case 0 -> moveUp(animal);
            case 1 -> moveDown(animal);
            case 2 -> moveLeft(animal);
            case 3 -> moveRight(animal);
        }
    }

    private static void moveRight(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells());
        int locX = currentX + movePoints;
        placeOnCell(animal, locX, currentY);
    }

    private static void moveLeft(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells());
        int locX = currentX - movePoints;
        placeOnCell(animal, locX, currentY);
    }

    private static void moveUp(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells());
        int locY = currentY + movePoints;
        placeOnCell(animal, currentX, locY);
    }

    private static void moveDown(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells());
        int locY = currentY - movePoints;
        placeOnCell(animal, currentX, locY);
    }

    private static void placeOnCell(Animal animal, int newX, int newY) {
        try {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    if(cells[i][j].getX() == newX && cells[i][j].getY() == newY){
                        cells[i][j].getNewComers().add(animal);
                    }
                }
            }


        } catch (IndexOutOfBoundsException e) {
            replaceAnimalToAnotherCell(animal);
        }
    }
}
