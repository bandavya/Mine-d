package com.example.mines.utility;

import java.util.Random;
public class Generator {

    public static int[][] generate( int BombNo , final int width , final int height){
        Random r = new Random();

        int [][] grid = new int[width][height];
        for( int x = 0 ; x< width ;x++ ){
            grid[x] = new int[height];
        }

        while( BombNo > 0 ){
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            // -1 is the bomb
            if( grid[x][y] != -1 ){
                grid[x][y] = -1;
                BombNo--;
            }
        }
        grid = calculateNeigbours(grid,width,height);

        return grid;
    }

    private static int[][] calculateNeigbours( int[][] grid , final int width , final int height){
        for( int x = 0 ; x < width ; x++){
            for( int y = 0 ; y < height ; y++){
                grid[x][y] = getNeighbourNumber(grid,x,y,width,height);
            }
        }

        return grid;
    }

    private static int getNeighbourNumber( final int grid[][] , final int x , final int y , final int width , final int height){
        if( grid[x][y] == -1 ){
            return -1;
        }

        int count = 0;

        if( MineLoc(grid,x - 1 ,y + 1,width,height)) count++; // top-left
        if( MineLoc(grid,x,y + 1,width,height)) count++; // top
        if( MineLoc(grid,x + 1 ,y + 1,width,height)) count++; // top-right
        if( MineLoc(grid,x - 1 ,y,width,height)) count++; // left
        if( MineLoc(grid,x + 1 ,y,width,height)) count++; // right
        if( MineLoc(grid,x - 1 ,y - 1,width,height)) count++; // bottom-left
        if( MineLoc(grid,x,y - 1,width,height)) count++; // bottom
        if( MineLoc(grid,x + 1 ,y - 1,width,height)) count++; // bottom-right

        return count;
    }

    private static boolean MineLoc( final int [][] grid, final int x , final int y , final int width , final int height){
        if( x >= 0 && y >= 0 && x < width && y < height ){
            if( grid[x][y] == -1 ){
                return true;
            }
        }
        return false;
    }

}