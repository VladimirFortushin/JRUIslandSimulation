package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.action.Action;
import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;

import java.util.*;

public class CellFabric extends Cell {
    private final Random r = new Random();
    private final AnimalFabric fabric = new AnimalFabric();
    private final Map<Animal, Integer> allAnimalsForCell = fabric.getAnimalsForCell();
    private final Map<Predator, Integer> predatorsForCell = new HashMap<>();
    private final Map<Herbivorous, Integer> herbivorousForCell = new HashMap<>();
    private final Action action = new Action();
    private final List<Plant> plantForCell = new ArrayList<>();
    private List<Cell> cells = new ArrayList<>();

    public CellFabric(int x, int y) throws Exception {
        super(x, y);
    }

    public void settleEntitiesOnCell(Cell cell) {
        for(Map.Entry<Animal, Integer> entry : allAnimalsForCell.entrySet()){
            if(entry.getKey() instanceof Predator){predatorsForCell.put((Predator) entry.getKey(), entry.getValue());}
            if(entry.getKey() instanceof Herbivorous){herbivorousForCell.put((Herbivorous) entry.getKey(), entry.getValue());}
        }
        cell.setPredators(predatorsForCell);
        cell.setHerbivores(herbivorousForCell);
        for (int i = 0; i < r.nextInt(200); i++) {
            plantForCell.add(new Plant());
        }
        cell.setPlants(plantForCell);
        cells.add(cell);
    }
}
