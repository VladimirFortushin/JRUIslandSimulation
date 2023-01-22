package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;
import ru.fortushin.islandsimulation.models.Island;

import java.util.Random;

public class Move extends Action {
    private int x;
    private int y;
    private Random r = new Random();
    private final Island island = new Island();
    private final Cell[][] cells = island.getIsland();

    public void setCurrentCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void replaceAnimalToAnotherCell(Animal animal){
        switch (r.nextInt(4)){
            case 0 -> moveUp(animal);
            case 1 -> moveDown(animal);
            case 2 -> moveLeft(animal);
            case 3 -> moveRight(animal);
        }
    }

    private void moveRight(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        animal.setLocationX(x + movePoints);
        placeOnCell(animal);

    }

    private void moveLeft(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        animal.setLocationX(x - movePoints);
        placeOnCell(animal);
    }

    private void moveUp(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        animal.setLocationX(y - movePoints);
        placeOnCell(animal);
    }

    private void moveDown(Animal animal) {
        int movePoints = r.nextInt(animal.getMaxMoveCells() + 1);
        animal.setLocationX(y + movePoints);
        placeOnCell(animal);
    }

    private void placeOnCell(Animal animal) {
        try {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    if (animal.getLocationX() == i && animal.getLocationY() == j) {
                        cells[i][j].addNewComer(animal);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            switch (r.nextInt(4)) {
                case 0 -> moveUp(animal);
                case 1 -> moveDown(animal);
                case 2 -> moveLeft(animal);
                case 3 -> moveRight(animal);
            }
        }

    }


}
