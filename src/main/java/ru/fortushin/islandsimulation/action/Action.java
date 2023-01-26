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
    private List<Plant> newPlantsList = new ArrayList<>();
    public static List<Cell> cells = new ArrayList<>();
    private Cell cell;

    public String doTurn(Map<Predator, Integer> predators, Map<Herbivorous, Integer> herbivorous, List<Plant> plants, Cell cell) throws Exception {
        this.cell = cell;
        if(cell.getNewComers().size() != 0){Settler.settleNewComersOn(cells, cell);}

        String preTurnData = "Pre turn data: " + (Counter.count(predators) + Counter.count(herbivorous))
                + " animals, " + plants.size() + " plants.\n";

        newPredatorsMap = predators;
        newHerbivoresMap = herbivorous;
        newPlantsList = plants;

        String cellInfo = "------- Cell: " + cell.getX() + ":" + cell.getY() + " --------\n";
        String movedAnimals = (filterWhoWishToMove(newPredatorsMap) + filterWhoWishToMove(newHerbivoresMap))
                + " have left this part of the island.\n";
        String newComers = cell.getNewComers().size() + " animals are new comers for this cell.\n";

        String info = eat();
        String newBornAnimals = "New born animals on this cell: " + (multiply(newPredatorsMap) + multiply(newHerbivoresMap)) + "\n";

        info = cellInfo + newComers + preTurnData + movedAnimals + info + newBornAnimals + multiplyPlants();

        updateCell();
        return info;

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
        double herbivorousSaturationLevel = 0.0;


        for(var herbivorous : newHerbivoresMap.entrySet()){
            for (int i = 0; i < herbivorous.getValue(); i++) {
                if(newPlantsList.size() != 0){
                    for (int j = 0; j < newPlantsList.size(); j++) {
                        double plantWeight = 1.0;
                        if(herbivorousSaturationLevel >= herbivorous.getKey().getKgsForSaturation()){
                            break;
                        }
                        if(newPlantsList.size() > 0){
                            plantWeight -= herbivorous.getKey().getKgsForSaturation();
                            if(plantWeight <= 0.0){
                                newPlantsList.remove(0);
                                eatenPlants++;
                            }
                            herbivorousSaturationLevel++;
                        }
                    }
                }else{
                    newHerbivoresMap.put(herbivorous.getKey(), herbivorous.getValue() - 1);
                    diedHerbivores++;
                }
            }
        }


        List<String[]> chancesToEat = DataReader.readAllLines(chanceToEatCsv);
        String[] victims = chancesToEat.get(0);

        for (var predator : newPredatorsMap.entrySet()) {
            for (int i = 0; i < predator.getValue(); i++) {
                int huntingAttempts = 0;
                double saturationLevel = 0.0;
                for (String[] chance : chancesToEat) {
                    if (huntingAttempts > 1){break;}
                    if (chance[0].equals(predator.getKey().getName())) {
                        for (int j = 1; j < chance.length; j++) {
                            if (huntingAttempts > 1){break;}
                            try {
                                int chanceToEat = Integer.parseInt(chance[j]);
                                if (chanceToEat == 0) {
                                    continue;
                                }
                                huntingAttempts++;
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
                    diedPredators++;
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


        for (var animal : animals.entrySet()) {

            int females = 0;
            int males = 0;
            int actsOfMultiply = 0;
            for (int i = 0; i < animal.getValue(); i++) {
                if(r.nextBoolean()){
                    males++;
                }else {
                    females++;
                }
            }

            if(males > females){
                actsOfMultiply = females;
            }
            if(males < females){
                actsOfMultiply = males;
            }
            if(males == females){
                actsOfMultiply = males;
            }

            for (int i = 0; i < actsOfMultiply; i++) {
                if (animal.getKey() instanceof Predator) {
                    switch (r.nextInt(2)) {
                        case 1 -> {
                            newPredatorsMap.put((Predator) animal.getKey(), animal.getValue() + 1);
                            newBorn++;
                        }
                    }
                }
                if (animal.getKey() instanceof Herbivorous) {
                    switch (r.nextInt(2)) {
                        case 1 -> {
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
                        Move.replaceAnimalToAnotherCell(animal.getKey());
                    }
                    if (animal.getKey() instanceof Herbivorous) {
                        newHerbivoresMap.put((Herbivorous) animal.getKey(), animal.getValue() - 1);
                        Move.replaceAnimalToAnotherCell(animal.getKey());
                    }
                }
            }
        }
        return counter;
    }
}
