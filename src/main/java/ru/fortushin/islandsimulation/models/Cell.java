package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;

import java.util.*;

public class Cell {
    private final int x;
    private final int y;
    private Map<Predator, Integer> predators = new HashMap<>();
    private Map<Herbivorous, Integer> herbivores = new HashMap<>();
    private List<Animal> newComers = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Map<Predator, Integer> getPredators() {
        return predators;
    }
    public void setPredators(Map<Predator, Integer> predators) {
        this.predators = predators;
    }

    public Map<Herbivorous, Integer> getHerbivores() {
        return herbivores;
    }

    public void setHerbivores(Map<Herbivorous, Integer> herbivorous) {
        this.herbivores = herbivorous;
    }

    public List<Animal> getNewComers() {
        return newComers;
    }

    public void setNewComers(List<Animal> newComers) {
        this.newComers = newComers;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    @Override
    public String toString() {
        return "Cell{" + x + ":" + y +
                " predators=" + predators +
                ", herbivorous=" + herbivores +
                ", plants=" + plants.size() +
                '}';
    }



}
