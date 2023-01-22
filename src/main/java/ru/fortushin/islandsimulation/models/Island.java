package ru.fortushin.islandsimulation.models;

import java.util.Scanner;

public class Island {
    private Cell[][] islandCells;

    public void initializeIsland() throws Exception {
        System.out.println("Please type the island dimension like: 'X Y'");
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        sc.close();

        islandCells = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                islandCells[i][j] = new Cell(i, j);
            }
        }

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                islandCells[i][j].makeTurn();
            }
        }

    }
    public Cell[][] getIsland(){
        return islandCells;
    }



}
