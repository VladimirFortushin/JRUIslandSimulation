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
    private final List<String[]> chancesToEat = DataReader.readAllLines(chanceToEatCsv);
    private final Random r = new Random();
    private Cell cell;

    public Action() throws Exception {
    }

    public String doTurnAndReturnData(Cell cell) {
        this.cell = cell;

        String cellInfo = "------- Cell: " + cell.getX() + ":" + cell.getY() + " --------\n";

        String newComers = "";

        String preTurnData = "Pre turn data: " + Counter.countFromMap(cell.getPredators())
                + " predators, " + Counter.countFromMap(cell.getHerbivores()) + " herbivores, "
                + cell.getPlants().size() + " plants.\n";

        String eatInfo = getSaturationInfo();
        String newBorns = "New born animals on this cell: " + multiply(cell.getPredators()) + " predators, "
                + multiply(cell.getHerbivores()) + " herbivores. " + multiplyPlants();

        String postTurnData = "Post turn data: " + Counter.countFromMap(cell.getPredators())
                + " predators, " + Counter.countFromMap(cell.getHerbivores()) + " herbivores, "
                + cell.getPlants().size() + " plants.\n";

        String movedAnimals = howManyWishToMove(cell.getPredators()) + " predators, " +
        howManyWishToMove(cell.getHerbivores()) + " herbivores have left this cell.\n";

        if (cell.getNewComers().size() != 0) {
            newComers = Counter.countFromList(cell.getNewComers()) + " are new comers for this cell.\n";
            cell.getNewComers().forEach(animal -> MapHandler.settleOnCell(animal, cell));
        }

        return cellInfo + preTurnData
                + eatInfo + newBorns + postTurnData + newComers + movedAnimals;
    }

    public String getSaturationInfo() {

        int diedPredators = 0;
        int diedHerbivores = 0;
        int eatenPlants = 0;
        double plantWeight = 1.0;

        for (var herbivorous : cell.getHerbivores().entrySet()) {
            for (int i = 0; i < herbivorous.getValue(); i++) {
                double herbivorousSaturationLevel = 0.0;
                if (cell.getPlants().size() != 0) {
                    for (int j = 0; j < cell.getPlants().size(); j++) {

                        if (herbivorousSaturationLevel >= herbivorous.getKey().getKgsForSaturation()) {
                            break;
                        }

                        plantWeight -= herbivorous.getKey().getKgsForSaturation();
                        if (plantWeight <= 0.0) {
                            cell.getPlants().remove(0);
                            eatenPlants++;
                            plantWeight = 1.0;
                        }
                        herbivorousSaturationLevel++;

                    }
                } else {
                        cell.getHerbivores().put(herbivorous.getKey(), herbivorous.getValue() - 1);
                        diedHerbivores++;
                }
            }
        }

        String[] victims = chancesToEat.get(0);

        for (var predator : cell.getPredators().entrySet()) {
            for (int i = 0; i < predator.getValue(); i++) {
                int huntingAttempts = 0;
                double predatorSaturationLevel = 0.0;
                for (String[] chance : chancesToEat) {

                    if (chance[0].equals(predator.getKey().getName())) {
                        for (int j = 1; j < chance.length; j++) {
                            if (huntingAttempts > 6 || predatorSaturationLevel >= predator.getKey().getKgsForSaturation()) {
                                break;
                            }
                            int chanceToEat;
                            try {
                                chanceToEat = Integer.parseInt(chance[j]);
                            } catch (NumberFormatException e){continue;}

                                if (chanceToEat == 0) {continue;}

                                String targetedHerbivorous = victims[j];

                                int currentSpeciesQuantity = MapHandler.countQuantityForHerbivorousSpecies(targetedHerbivorous, cell);

                                if(currentSpeciesQuantity == 0){continue;}

                                huntingAttempts++;
                                int randomChance = r.nextInt(100) + 1;

                                if (randomChance <= chanceToEat) {
                                    MapHandler.removeHerbivorousByName(targetedHerbivorous, cell);
                                    diedHerbivores++;
                                    predatorSaturationLevel += MapHandler.getHerbivorousWeight(targetedHerbivorous, cell);
                                    break;
                                }
                              }
                        }
                    }
                    if (predatorSaturationLevel < predator.getKey().getKgsForSaturation()) {
                        if(predator.getValue() == 0){continue;}
                        diedPredators++;
                        cell.getPredators().put(predator.getKey(), predator.getValue() - 1);
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
                if (r.nextBoolean()) {
                    males++;
                } else {
                    females++;
                }
            }

            if (males > females) {
                actsOfMultiply = females;
            }
            if (males <= females) {
                actsOfMultiply = males;
            }

            for (int i = 0; i < actsOfMultiply; i++) {
                if (r.nextBoolean()) {
                    MapHandler.settleOnCell(animal.getKey(), cell);
                    newBorn++;
                }
            }
        }
        return newBorn;
    }


    private String multiplyPlants() {
        int newPlantsQuantity = r.nextInt(200 - cell.getPlants().size() + 1);
        for (int i = 0; i < newPlantsQuantity; i++) {
            cell.getPlants().add(new Plant());
        }
        return "New plants: " + newPlantsQuantity + "\n";
    }

    private int howManyWishToMove(Map<? extends Animal, Integer> animals) {
        int counter = 0;
        for (var animal : animals.entrySet()) {
            if(animal.getKey().getName().equals("caterpillar")){continue;}
            for (int i = 0; i < animal.getValue(); i++) {
                if (r.nextBoolean()) {
                    counter++;
                    if (animal.getKey() instanceof Predator) {
                        cell.getPredators().put((Predator) animal.getKey(), animal.getValue() - 1);
                        Move.replaceAnimalToAnotherCell(animal.getKey());
                    }
                    if (animal.getKey() instanceof Herbivorous) {
                        cell.getHerbivores().put((Herbivorous) animal.getKey(), animal.getValue() - 1);
                        Move.replaceAnimalToAnotherCell(animal.getKey());
                    }
                }
            }
        }
        return counter;
    }
}
