package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;

import java.util.Random;

public class Move  {
    private static int x;
    private static int y;
    private static Random r = new Random();
    private static Cell[][] cells;
    public static void setIslandData(Cell[][] cells, int x, int y){
        Move.cells = cells;
        Move.x = x;
        Move.y = y;
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
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        int locX = x + movePoints;
        placeOnCell(animal, locX, y);
    }

    private static void moveLeft(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        int locX = x - movePoints;
        placeOnCell(animal, locX, y);
    }

    private static void moveUp(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        int locY = y + movePoints;
        placeOnCell(animal, x, locY);
    }

    private static void moveDown(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        int locY = y - movePoints;
        placeOnCell(animal, x, locY);
    }

    private static void placeOnCell(Animal animal, int newX, int newY) {
        try {

            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    if(cells[i][j].getX() == newX && cells[i][j].getY() == newY){
                        cells[i][j].getNewComers().add(animal);
                    }
                }

            }
            cells[newX][newY].getNewComers().add(animal);

        } catch (IndexOutOfBoundsException e) {
            replaceAnimalToAnotherCell(animal);
        }
    }
}
