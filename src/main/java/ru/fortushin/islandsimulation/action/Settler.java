package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Predator;
import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;

import java.util.List;

public class Settler {
    public static void settleNewComersOn(List<Cell> cells, Cell currentCell){

        for(Cell newCell : cells) {
            if(newCell.getX() == currentCell.getX() && newCell.getY() == currentCell.getY()){
                for (Animal animal : newCell.getNewComers()) {
                    if (animal instanceof Predator) {
                        int quantity = 0;
                        for (var predator : newCell.getPredators().entrySet()) {
                            if (predator.getKey().getName().equals(animal.getName())) {
                                quantity =  predator.getValue();
                            }
                        }
                        newCell.getPredators().put((Predator) animal, quantity + 1);
                    }
                    if (animal instanceof Herbivorous) {
                        int quantity = 0;
                        for (var herbivorous : newCell.getHerbivores().entrySet()) {
                            if (herbivorous.getKey().getName().equals(animal.getName())) {
                                quantity = herbivorous.getValue();
                            }
                        }
                        newCell.getHerbivores().put((Herbivorous) animal, quantity + 1);
                    }
                }
            }

        }
    }
}
