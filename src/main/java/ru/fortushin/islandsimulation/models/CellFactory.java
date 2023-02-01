package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;

import java.util.*;

public class CellFactory extends Cell {
    private final Random r = new Random();
    private final AnimalFactory fabric = new AnimalFactory();
    private final Map<Animal, Integer> allAnimalsForCell = fabric.getAnimalsForCell();
    private final Map<Predator, Integer> predatorsForCell = new HashMap<>();
    private final Map<Herbivorous, Integer> herbivorousForCell = new HashMap<>();
    private final List<Plant> plantsForCell = new ArrayList<>();



    public CellFactory(int x, int y) throws Exception {
        super(x, y);
    }

    public void settleEntitiesOnCell(Cell cell) {
        for(Map.Entry<Animal, Integer> entry : allAnimalsForCell.entrySet()){
            if(entry.getKey() instanceof Predator){predatorsForCell.put((Predator) entry.getKey(), entry.getValue());}
            if(entry.getKey() instanceof Herbivorous){herbivorousForCell.put((Herbivorous) entry.getKey(), entry.getValue());}
        }
        cell.setPredators(predatorsForCell);
        cell.setHerbivores(herbivorousForCell);
        int plantsCountForCell = r.nextInt(200) + 1;
        for (int i = 0; i < plantsCountForCell; i++) {
            plantsForCell.add(new Plant());
        }
        cell.setPlants(plantsForCell);
    }
}
