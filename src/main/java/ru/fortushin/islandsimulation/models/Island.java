package ru.fortushin.islandsimulation.models;

import ru.fortushin.islandsimulation.action.Action;
import ru.fortushin.islandsimulation.action.Move;

import java.util.Scanner;

public class Island {
    private int x;
    private int y;
    private Cell[][] island;
    private int turnNo = 0;

    public void initializeIsland() throws Exception {
        System.out.println("Please type the island dimension like: 'X Y'");
        Scanner sc = new Scanner(System.in);
        x = sc.nextInt();
        y = sc.nextInt();


        island = new Cell[x][y];


        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                island[i][j] = new Cell(i, j);
                CellFabric cellFabric = new CellFabric(i, j);
                cellFabric.settleEntitiesOnCell(island[i][j]);
                Move.setIslandData(island, i, j);
            }
        }

        while (true){
            sc.nextLine();
            makeTurn();
        }

    }

    public void makeTurn() throws Exception {
        System.out.println("\nTurn# " + ++turnNo + "\n");
        Action action = new Action();


        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                System.out.println(action.doTurn(island[i][j].getPredators(),
                        island[i][j].getHerbivores(),
                        island[i][j].getPlants(),
                        island[i][j]));
            }
        }



    }
}
