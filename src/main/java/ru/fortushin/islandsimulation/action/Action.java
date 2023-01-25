package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.datareader.DataReader;
import ru.fortushin.islandsimulation.entities.Herbivorous;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;
import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.models.Cell;

import java.nio.file.Path;
import java.util.*;

public class Action {
    private static final Path chanceToEatCsv = Path.of("src/main/resources/eat-chance.csv");
    private final Random r = new Random();
    private Map<Predator, Integer> newPredatorsMap = new HashMap<>();
    private Map<Herbivorous, Integer> newHerbivoresMap = new HashMap<>();
    private Map<Predator, Integer> newComersPredators = new HashMap<>();
    private Map<Herbivorous, Integer> newComersHerbivores = new HashMap<>();
    private final List<Animal> wishToMoveAnimals = new ArrayList<>();
    private List<Plant> newPlantsList = new ArrayList<>();
    private Cell cell;
    private String info;

    public String doTurn(Map<Predator, Integer> predators, Map<Herbivorous, Integer> herbivorous, List<Plant> plants, Cell cell) throws Exception {
        this.cell = cell;
        String preTurnData = "Pre turn data: " + (Counter.count(predators) + Counter.count(herbivorous))
                + " animals, " + plants.size() + " plants.\n";

        newPredatorsMap = predators;
        newHerbivoresMap = herbivorous;
        newPlantsList = plants;

        String cellInfo = "------- Cell: " + cell.getX() + ":" + cell.getY() + " --------\n";
        String movedAnimals = (filterWhoWishToMove(newPredatorsMap) + filterWhoWishToMove(newHerbivoresMap))
                + " have left this part of the island.\n";
        String newComers = cell.getNewComers().size() + " animals are new comers for this cell.\n";

        info = eat();
        String newBornAnimals = (multiply(newPredatorsMap) + multiply(newHerbivoresMap))
                + " animals were born on this cell.\n";

        info = cellInfo + newComers + preTurnData + movedAnimals + info + newBornAnimals + multiplyPlants();

        updateCell();
        return info;

    }

    public void settleNewComer(Animal animal, int newX, int newY){
        if(cell.getX() == newX && cell.getY() == newY){
            if(animal instanceof Predator){
                for(var predator : newPredatorsMap.entrySet()){
                    if (predator.getKey().getName().equals(animal.getName())){
                        newPredatorsMap.put((Predator) animal, predator.getValue() + 1);
                    }
                }
            }
            if(animal instanceof Herbivorous){
                for(var herbivorous : newHerbivoresMap.entrySet()){
                    if (herbivorous.getKey().getName().equals(animal.getName())){
                        newHerbivoresMap.put((Herbivorous) animal, herbivorous.getValue() + 1);
                    }
                }
            }
        }
    }

    public void updateCell() {
        cell.setPredators(newPredatorsMap);
        cell.setHerbivores(newHerbivoresMap);
        cell.setPlants(newPlantsList);
    }

    public String eat() throws Exception {
        int diedPredators = 0;
        int diedHerbivores = 0;
        int eatenPlants = 0;

        if (newPlantsList.size() > newHerbivoresMap.size()) {
            int eatenPlantsAmount = newPlantsList.size() - newHerbivoresMap.size();
            for (int i = 0; i < eatenPlantsAmount; i++) {
                newPlantsList.remove(0);
                eatenPlants++;
            }
        } else if (newPlantsList.size() < newHerbivoresMap.size()) {
            int diedHerbivorousAmount = newHerbivoresMap.size() - newPlantsList.size();

            for (int i = 0; i < diedHerbivorousAmount; i++) {
                for (var victim : newHerbivoresMap.entrySet()) {
                    newHerbivoresMap.put(victim.getKey(), victim.getValue() - 1);
                    diedHerbivores++;
                    break;
                }
            }
        } else {
            eatenPlants = newPlantsList.size();
            newPlantsList.clear();
        }

        List<String[]> chancesToEat = DataReader.readAllLines(chanceToEatCsv);
        String[] victims = chancesToEat.get(0);

        for (var predator : newPredatorsMap.entrySet()) {
            for (int i = 0; i < predator.getValue(); i++) {
                int huntingAttempts = 0;
                double saturationLevel = 0.0;
                for (String[] chance : chancesToEat) {
                    if (chance[0].equals(predator.getKey().getName())) {
                        for (int j = 1; j < chance.length; j++) {
                            try {
                                int chanceToEat = Integer.parseInt(chance[j]);
                                if (chanceToEat == 0) {
                                    continue;
                                }
                                huntingAttempts++;
                                if (huntingAttempts > 3) {
                                    if (saturationLevel == 0) {
                                        newPredatorsMap.put(predator.getKey(), predator.getValue() - 1);
                                        diedPredators++;
                                    }
                                    break;
                                }
                                int randomChance = r.nextInt(100) + 1;

                                if (randomChance <= chanceToEat) {
                                    String eaten = victims[j];
                                    for (var victim : newHerbivoresMap.entrySet()) {
                                        if (victim.getKey().getName().equals(eaten)) {
                                            newHerbivoresMap.put(victim.getKey(), victim.getValue() - 1);
                                            diedHerbivores++;
                                            saturationLevel += victim.getKey().getWeight();
                                            break;
                                        }
                                    }
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }

                    }
                }
                if (saturationLevel < predator.getKey().getKgsForSaturation()) {
                    newPredatorsMap.put(predator.getKey(), predator.getValue() - 1);
                }
            }
        }
        return "Predators and Herbivorous have made their cycle of saturation.\n"
                + "Predators died from starvation: "
                + diedPredators + "\n"
                + "Herbivorous been eaten or died from starvation: "
                + diedHerbivores + "\n"
                + "Plants that were eaten: "
                + eatenPlants + "\n"
                + "Survived Predators and Herbivorous are fed and ready to multiply" + "\n";


    }

    public int multiply(Map<? extends Animal, Integer> animals) {
        int newBorn = 0;
        int females = 0;

        for (var animal : animals.entrySet()) {
            for (int i = 0; i < animal.getValue(); i++) {
                if (animal.getKey().getSex().equals("F")) {
                    females++;
                }
            }
            for (int i = 0; i < females; i++) {
                if (animal.getKey() instanceof Predator) {
                    switch (r.nextInt(2)) {
                        case 1 -> {
                            animal.getKey().setSex();
                            newPredatorsMap.put((Predator) animal.getKey(), animal.getValue() + 1);
                            newBorn++;
                        }
                    }
                }
                if (animal.getKey() instanceof Herbivorous) {
                    switch (r.nextInt(2)) {
                        case 1 -> {
                            animal.getKey().setSex();
                            newHerbivoresMap.put((Herbivorous) animal.getKey(), animal.getValue() + 1);
                            newBorn++;
                        }
                    }
                }
            }
        }
        return newBorn;
    }





    private String multiplyPlants() {
        int newPlantsQuantity = 0;
        for (int i = 0; i < r.nextInt(200 - newPlantsList.size() + 1); i++) {
            newPlantsList.add(new Plant());
            newPlantsQuantity++;
        }
        return "New plants: " + newPlantsQuantity + "\n";
    }

    private int filterWhoWishToMove(Map<? extends Animal, Integer> animals) {
        int counter = 0;
        for (var animal : animals.entrySet()) {

            for (int i = 0; i < animal.getValue(); i++) {
                if (r.nextBoolean()) {
                    counter++;
                    if (animal.getKey() instanceof Predator) {
                        newPredatorsMap.put((Predator) animal.getKey(), animal.getValue() - 1);
                        Move.replaceAnimalToAnotherCell(animal.getKey(), this);
                    }
                    if (animal.getKey() instanceof Herbivorous) {
                        newHerbivoresMap.put((Herbivorous) animal.getKey(), animal.getValue() - 1);
                        Move.replaceAnimalToAnotherCell(animal.getKey(), this);
                    }
                }
            }
        }
        return counter;
    }
}
