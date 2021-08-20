package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameDisplay extends AppCompatActivity {
    TicTacToeBoard gameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);
        gameLogic = findViewById(R.id.ticTacToeBoard);
        Button playAgainBTN = findViewById(R.id.playAgainButton);
        Button homeBTN = findViewById(R.id.homeButton);
        TextView playerDisplay = findViewById(R.id.PlayerDisplayTextview);
        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);

        String[] names = getIntent().getStringArrayExtra("player1");
        if (names != null){
            playerDisplay.setText(names[0] + "'s Turn!");
        }

        gameLogic.setUpGame(playAgainBTN , homeBTN , playerDisplay , names);
    }

    public void backToHomeScreen(View view){
        Intent intent = new Intent(GameDisplay.this , MainActivity.class);
        startActivity(intent);
    }
    public void playAgainScreen(View view){
        gameLogic.resetGame();
        gameLogic.invalidate();
    }
}