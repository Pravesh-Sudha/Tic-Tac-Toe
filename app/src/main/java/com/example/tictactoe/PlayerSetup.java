package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PlayerSetup extends AppCompatActivity {
    public static final String PLAYER_NAMES ="player1" ;
    EditText player1;
    EditText player2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_setup);
        player1 = findViewById(R.id.player1Edit);
        player2 = findViewById(R.id.player2Edit);

    }

    public void submitNames(View view){
        if (TextUtils.isEmpty(player1.getText().toString()) || TextUtils.isEmpty(player2.getText().toString())){
            Toast.makeText(this, "Enter the names of Both Players", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(PlayerSetup.this ,GameDisplay.class);
            intent.putExtra(PLAYER_NAMES ,new String[]{player1.getText().toString() ,player2.getText().toString()} );;
            startActivity(intent);

        }


    }
}