package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.models.Animal;

import java.util.List;
import java.util.Map;

public class Counter {
    public static int countFromMap(Map<? extends Animal, Integer> mapToCount){
        int counter = 0;
        for(var animal : mapToCount.entrySet()){
            counter += animal.getValue();
        }
        return counter;
    }

    public static String countFromList(List<Animal> listToCount){
        int predatorsCounter = 0;
        int herbivoresCounter = 0;

        for(Animal animal : listToCount){
            if(animal.getSpecies().equals("predator")){predatorsCounter++;}
            else if(animal.getSpecies().equals("herbivorous")){herbivoresCounter++;}
        }
        return predatorsCounter + " predators, " + herbivoresCounter + " herbivores";
    }
}
