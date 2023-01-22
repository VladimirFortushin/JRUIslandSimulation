package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.action.Action;
import ru.fortushin.islandsimulation.entities.Herbivores;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;

import java.util.*;

public class Cell {
    private int turnNumber;
    private final int x;
    private final int y;
    public Cell(int x, int y) throws Exception {
        this.x = x;
        this.y = y;
        settleEntitiesOnCell();
    }
    private Map<Predator, Integer> predators = new HashMap<>();
    private Map<Herbivores, Integer> herbivorous = new HashMap<>();
    private List<Plant> plants = new ArrayList<>();

    public void addNewComer(Animal animal) {
        List<Predator> newComersPredators = new ArrayList<>();
        List<Herbivores> newComersHerbivorous = new ArrayList<>();

        if(animal instanceof Predator){
            newComersPredators.add((Predator) animal);
        }else{
            newComersHerbivorous.add((Herbivores) animal);
        }
    }


    public void setPredators(Map<Predator, Integer> predators) {
        this.predators = predators;
    }

    public void setHerbivorous(Map<Herbivores, Integer> herbivorous) {
        this.herbivorous = herbivorous;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    private final Random r = new Random();

    public void settleEntitiesOnCell() throws Exception {
        AnimalFabric fabric = new AnimalFabric();
        Map<Animal, Integer> allAnimalsForCell = fabric.getAnimalsForCell();
        for(Map.Entry<Animal, Integer> entry : allAnimalsForCell.entrySet()){
            entry.getKey().setLocationX(x);
            entry.getKey().setLocationY(y);
            if(entry.getKey() instanceof Predator){predators.put((Predator) entry.getKey(), entry.getValue());}
            if(entry.getKey() instanceof Herbivores){herbivorous.put((Herbivores) entry.getKey(), entry.getValue());}
        }
        for (int i = 0; i < r.nextInt(200); i++) {
            plants.add(new Plant());
        }
    }

    public int makeTurn() throws Exception {
        List<Animal> allAnimalsOnCell = new ArrayList<>();
        for(Map.Entry<Predator, Integer> entry : predators.entrySet()){
            for (int i = 0; i < entry.getValue(); i++) {
                allAnimalsOnCell.add(entry.getKey());
            }
        }
        for(Map.Entry<Herbivores, Integer> entry : herbivorous.entrySet()){
            for (int i = 0; i < entry.getValue(); i++) {
                allAnimalsOnCell.add(entry.getKey());
            }
        }
        Action action = new Action();
        action.doTurn(allAnimalsOnCell, plants);
        action.transferCoordinates(x, y);
        action.getEndTurnData();
        turnNumber++;
        return turnNumber;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", predators=" + predators +
                ", herbivorous=" + herbivorous +
                ", plants=" + plants.size() +
                '}';
    }


}
