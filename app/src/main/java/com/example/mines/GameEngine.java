package com.example.mines;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mines.grid.Cell;
import com.example.mines.utility.Generator;
import com.example.mines.utility.PrintGrid;

public class GameEngine {
    private static GameEngine instance;

    public static final int BOMB_NUMBER = 8;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private Context context;
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


    private Cell[][] GameGrid = new Cell[WIDTH][HEIGHT];

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    private GameEngine() {
    }

    public void createGrid(Context context) {
        Log.e("GameEngine", "createGrid is working");
        this.context = context;

        // create the grid and store it
        int[][] GeneratedGrid = Generator.generate(BOMB_NUMBER, WIDTH, HEIGHT);
        PrintGrid.print(GeneratedGrid, WIDTH, HEIGHT);
        setGrid(context, GeneratedGrid);

    }

    private void setGrid(final Context context, final int[][] grid) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (GameGrid[x][y] == null) {
                    GameGrid[x][y] = new Cell(context, x, y);
                }
                GameGrid[x][y].setValue(grid[x][y]);
                GameGrid[x][y].invalidate();
            }
        }
    }

    public Cell getCellAt(int position) {
        int x = position % WIDTH;
        int y = position / HEIGHT;
        return GameGrid[x][y];
    }

    public Cell getCellAt(int x, int y) {
        return GameGrid[x][y];
    }

    public void click(int x, int y) {
        if (x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !getCellAt(x, y).isClicked()) {
            getCellAt(x, y).setClicked();
            checkEnd();
/*
            if (getCellAt(x, y).getValue() == 0) {
                for (int xt = -1; xt <= 1; xt++) {

                    for (int yt = -1; yt <= 1; yt++) {
                        if (xt != yt) {
                            click(x + xt, y + yt);
                        }
                    }

                }


            }

 */

            if (getCellAt(x, y).isBomb()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }
                onGameLost();
            }
        }


    }

    private boolean checkEnd() {
        int BombNotfound = BOMB_NUMBER;
        int NotRevealed = WIDTH*HEIGHT;
        for ( int x = 0 ; x < WIDTH ; x++ ) {
            for (int y = 0; y < HEIGHT; y++) {
                if( getCellAt(x,y).isRevealed()){
                    NotRevealed--;
                }

                if( getCellAt(x,y).isBomb() && !getCellAt(x,y).isRevealed() ){
                    BombNotfound--;
                }
            }
        }
        if( BombNotfound == 0 && NotRevealed == BOMB_NUMBER ){
            Toast.makeText(context,"YOU'VE WON THE GAME!!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context.getApplicationContext(), Score.class);
            intent.putExtra("SCORE",56);
            context.startActivity(intent);
        }
        return false;
    }

    private void onGameLost() {
        Toast.makeText(context, "GAME OVER", Toast.LENGTH_LONG).show();
        int Score = -1;
        for ( int x = 0 ; x < WIDTH ; x++ ) {
            for (int y = 0; y < HEIGHT; y++) {
                if (getCellAt(x, y).isRevealed()) {
                    Score++;
                }
            }
        }
        Toast.makeText(context, "YOUR SCORE:" + Score, Toast.LENGTH_LONG).show();


        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                getCellAt(x, y).setRevealed();
            }
        }

        Intent intent = new Intent(context.getApplicationContext(), Score.class);
        intent.putExtra("SCORE",Score);
        context.startActivity(intent);

    }
}