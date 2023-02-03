package ru.fortushin.islandsimulation.utils;

import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Predator;
import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;

import java.util.Map;

public class MapHandler {
    public static void settleOnCell(Animal animal, Cell currentCell) {
        if (animal instanceof Predator) {
            int quantity = countQuantityForPredatorSpecies(animal.getName(), currentCell);
            currentCell.getPredators().put((Predator) animal, quantity + 1);
        }else {
            int quantity = countQuantityForHerbivorousSpecies(animal.getName(), currentCell);
            currentCell.getHerbivores().put((Herbivorous) animal, quantity + 1);
        }
    }


    public static int countQuantityForHerbivorousSpecies(String name, Cell cell) {
        return cell.getHerbivores().entrySet().stream()
                .filter(herbivorous -> herbivorous.getKey().getName().equals(name))
                .mapToInt(Map.Entry::getValue).sum();
    }

    public static int countQuantityForPredatorSpecies(String name, Cell cell) {
        return cell.getPredators().entrySet().stream()
                .filter(predator -> predator.getKey().getName().equals(name))
                .mapToInt(Map.Entry::getValue).sum();
    }
    public static double getHerbivorousWeight(String name, Cell cell) {
        return cell.getHerbivores().keySet().stream()
                .filter(herbivorous -> herbivorous.getName().equals(name))
                .mapToDouble(Animal::getWeight).sum();
    }
    public static void removeHerbivorousByName(String name, Cell cell) {
        int currentSpeciesQuantity = countQuantityForHerbivorousSpecies(name, cell);
        Herbivorous herbivorousToRemove = new Herbivorous();
        herbivorousToRemove.setName(name);
        cell.getHerbivores().put(herbivorousToRemove, currentSpeciesQuantity - 1);
    }

}
