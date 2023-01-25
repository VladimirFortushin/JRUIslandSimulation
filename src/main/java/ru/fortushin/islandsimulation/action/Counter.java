package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.models.Animal;

import java.util.Map;

public class Counter {
    public static int count(Map<? extends Animal, Integer> mapToCount){
        int counter = 0;
        for(var animal : mapToCount.entrySet()){
            counter += animal.getValue();
        }
        return counter;
    }
}
