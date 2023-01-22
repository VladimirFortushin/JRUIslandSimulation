package ru.fortushin.islandsimulation.action;

import ru.fortushin.islandsimulation.datareader.DataReader;
import ru.fortushin.islandsimulation.models.Animal;
import ru.fortushin.islandsimulation.entities.Herbivores;
import ru.fortushin.islandsimulation.entities.Plant;
import ru.fortushin.islandsimulation.entities.Predator;
import ru.fortushin.islandsimulation.models.Cell;
import ru.fortushin.islandsimulation.models.Island;

import java.nio.file.Path;
import java.util.*;

public class Action extends Thread {
    private int x;
    private int y;
    private final Path chanceToEatCsv = Path.of("src/main/resources/eat-chance.csv");
    private final Random r = new Random();
    private final List<Animal> newAnimalsList = new ArrayList<>();
    private final List<Plant> newPlantsList = new ArrayList<>();

    public void doTurn(List<Animal> allAnimalsOnCell, List<Plant> plants) throws Exception {
        List<Animal> animalsWishToStay = new ArrayList<>();
        List<Animal> animalsWishToMove = new ArrayList<>();

        for (Animal animal : allAnimalsOnCell){
                switch (r.nextInt(2)){
                    case 0 -> animalsWishToStay.add(animal);
                    case 1 -> animalsWishToMove.add(animal);
                }
        }

        List<Animal> survivors = eat(animalsWishToStay, plants);
        List<Animal> newBorn = multiply(survivors);
        move(animalsWishToMove);

        newAnimalsList.addAll(survivors);
        newAnimalsList.addAll(newBorn);


    }

    public List<Animal> eat(List<Animal> animalsWishToEat, List<Plant> plants) throws Exception {
        List<Animal> survivors = new ArrayList<>();
        List<String[]> chancesToEat = DataReader.readAllLines(chanceToEatCsv);
        String[] topRow = chancesToEat.get(0);
        List<Predator> hungryPredators = new ArrayList<>();
        List<Herbivores> hungryHerbivorous = new ArrayList<>();
        for(Animal animal : animalsWishToEat){
            if(animal instanceof Predator){hungryPredators.add((Predator) animal);}
            if(animal instanceof Herbivores){hungryHerbivorous.add((Herbivores) animal);}
        }
        for(Predator predator : hungryPredators){
            for(String[] chance : chancesToEat){
                if(chance[0].equals(predator.getName())){
                    for (int i = 1; i < chance.length; i++) {
                        try{
                            int chanceToEat = Integer.parseInt(chance[i]);
                            if(chanceToEat == 0){continue;}
                            int result = r.nextInt(100) + 1;
                            if(result <= chanceToEat){
                                Herbivores victim = new Herbivores();
                                for(Herbivores herbivores : hungryHerbivorous){
                                    if(herbivores.getName().equals(topRow[i])){
                                        victim = herbivores;
                                        break;
                                    }
                                }
                                survivors.add(predator);
                                hungryHerbivorous.remove(victim);
                            }
                        }catch (NumberFormatException e){
                            continue;
                        }
                    }
                }
            }
        }

        for (Herbivores herbivorous : hungryHerbivorous) {
            if (plants.size() == 0) {
                break;
            }

            plants.remove(0);
            survivors.add(herbivorous);

        }
        newPlantsList.addAll(plants);
        return survivors;
    }

    public List<Animal> multiply(List<Animal> animalsToMultiply) {
        List<Animal> newBorn = new ArrayList<>();
        for(Animal animal1 : animalsToMultiply){
            for (Animal animal2 : animalsToMultiply){
                if(animal1.getSpecies().equals(animal2.getSpecies())
                        && animal1.getSex().equals("M") && animal2.getSex().equals("F")){
                    for (int i = 0; i < r.nextInt(3); i++) {
                        newBorn.add(r.nextBoolean() ? animal1 : animal2);
                    }
                    animal2.setSpecies("alreadyMultiplied");
                    break;
                }

            }
        }
        if(newPlantsList.size() < 200){
            for (int i = 0; i < newPlantsList.size(); i++) {
                newPlantsList.add(new Plant());
                if(newAnimalsList.size() == 200){break;}
            }
        }
        return newBorn;
    }
    public void move(List<Animal> animalsWishToMove) {
        Move move = new Move();
        animalsWishToMove.forEach(move::replaceAnimalToAnotherCell);
    }


    public void transferCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
        Move move = new Move();
        move.setCurrentCoordinates(x, y);
    }


    public void getEndTurnData() throws Exception {
        List<String[]> chancesToEat = DataReader.readAllLines(chanceToEatCsv);
        String[] topRow = chancesToEat.get(0);
        Map<Predator, Integer> predators = new HashMap<>();
        Map<Herbivores, Integer> herbivorous = new HashMap<>();
        int counter = 0;

        for(Animal animal : newAnimalsList){
            for (int i = 1; i < topRow.length; i++) {
                if(animal.getName().equals(topRow[i])){
                    if(animal instanceof Predator){
                        counter++;
                    }
                }

            }

        }
        Island island = new Island();
        Cell[][] cells = island.getIsland();
        cells[x][y].setPredators();

    }
}
