package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.datareader.DataReader;
import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Predator;

import java.nio.file.Path;
import java.util.*;

public class AnimalFactory extends Animal {
    private final Random r = new Random();
    private final Path animalsParamsCsv = Path.of("src/main/resources/animals-params.csv");
    private final List<String[]> animalStatsList = DataReader.readAllLines(animalsParamsCsv);

    public AnimalFactory() throws Exception {
    }

    public Map<Animal, Integer> getAnimalsForCell() throws Exception {
        Map<Animal, Integer> animalsPerCell = new HashMap<>();
        List<Animal> animals = enrichAnimalSpecies(new AnimalFactory());
        animals.forEach(a -> animalsPerCell.put(a, r.nextInt(a.getMaxQuantityPerCell())));
        return animalsPerCell;
    }

    private List<Animal> enrichAnimalSpecies(Animal animal) {
        List<Animal> listOfAnimalsPerCell = new ArrayList<>();
        for (String[] row : animalStatsList) {
            if(row[0].equals("Entity")){continue;}
            if(row[5].equals("predator")){animal = new Predator();}
            if(row[5].equals("herbivorous")){animal = new Herbivorous();}
            if(row[5].equals("plant")){continue;}
            animal.setName(row[0]);
            animal.setSpecies(row[5]);
            animal.setWeight(Double.parseDouble(row[1]));
            animal.setMaxQuantityPerCell(Integer.parseInt(row[2]));
            animal.setMaxMoveCells(Integer.parseInt(row[3]));
            animal.setKgsForSaturation(Double.parseDouble(row[4]));
            listOfAnimalsPerCell.add(animal);
        }
        return listOfAnimalsPerCell;
    }

}
