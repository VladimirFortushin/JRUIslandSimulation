package ru.fortushin.islandsimulation.models;

import java.util.Random;

public abstract class Animal {
    private String name;
    private String species;
    private int maxQuantityPerCell;
    private double weight;
    private int maxMoveCells;
    private double kgsForSaturation;
    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setMaxQuantityPerCell(int maxQuantityPerCell) {
        this.maxQuantityPerCell = maxQuantityPerCell;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setMaxMoveCells(int maxMoveCells) {
        this.maxMoveCells = maxMoveCells;
    }

    public void setKgsForSaturation(double kgsForSaturation) {
        this.kgsForSaturation = kgsForSaturation;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public int getMaxQuantityPerCell() {
        return maxQuantityPerCell;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxMoveCells() {
        return maxMoveCells;
    }

    public double getKgsForSaturation() {
        return kgsForSaturation;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", species='" + species + '\'' +
                '}';
    }
}
