package com.example.mines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Score extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreLabel = findViewById(R.id.scoreLabel);
        TextView highscoreLabel = findViewById(R.id.highscoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score+"");
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        int highscore = settings.getInt("HIGH_SCORE", 56);
        if(score>highscore){
            highscoreLabel.setText("HIGH SCORE:" + score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else {
            highscoreLabel.setText("HIGH SCORE:" + highscore);
        }
    }
    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}