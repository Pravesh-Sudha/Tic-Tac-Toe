package com.example.tictactoe;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameLogic {
    private int [][] gameBoard;
    private String[] playerNames = {"Player 1" , "Player 2"};
    private int player = 1;
    private Button playAgainBTN;
    private int[] winType = {-1,-1,-1};
    private Button homeBTN;
    private TextView playerTurn;
    private  Context context;
    int winSound,TieSound ;
    int score = 0;
    TextView player1Score ,player2Score  ;




    public int[] getWinType() {
        return winType;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public Button getPlayAgainBTN() {
        return playAgainBTN;
    }

    public void setPlayAgainBTN(Button playAgainBTN) {
        this.playAgainBTN = playAgainBTN;
    }

    public Button getHomeBTN() {
        return homeBTN;
    }

    public void setHomeBTN(Button homeBTN) {
        this.homeBTN = homeBTN;
    }

    public TextView getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(TextView playerTurn) {
        this.playerTurn = playerTurn;
    }

    public GameLogic(Context context) {
        this.context = context;
        gameBoard = new int[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                gameBoard[r][c] = 0;
            }
        }



    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public boolean updateGameBoard(int row , int col){
        if (gameBoard[row-1][col-1] == 0){
            gameBoard[row-1][col-1] = player;
            if (player == 1){
                playerTurn.setText(playerNames[1] + "'s Turn!");
            }else {
                playerTurn.setText(playerNames[0] + "'s Turn!");
            }
            return true;
        }else{
            return false;
        }
    }
    public boolean winnerCheck (){


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();
//
//
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();

        winSound = soundPool.load(context , R.raw.actualwin , 1);
        TieSound = soundPool.load(context , R.raw.drawsound , 1);


        boolean isWinner = false;
        int boardFilled = 0;

        //Horizontal Check
        for (int r = 0 ; r < 3 ; r++ ){
            if (gameBoard[r][0] == gameBoard[r][1] && gameBoard[r][0] == gameBoard[r][2] &&
                gameBoard[r][0] != 0){
                winType = new int[]{ r ,0 ,1};
                isWinner = true;
            }
        }
        //Vertical Check
        for (int c = 0 ; c < 3 ; c++ ){
            if (gameBoard[0][c] == gameBoard[1][c] && gameBoard[2][c] == gameBoard[0][c] &&
                    gameBoard[0][c] != 0){
                winType = new int[]{0,c,2};
                isWinner = true;
            }
        }
        //negative Diagonal
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[0][0] == gameBoard[2][2] &&
                gameBoard[0][0] != 0){
            winType = new int[]{0,2,3};
            isWinner = true;
        }
        //positive Diagonal
        if (gameBoard[2][0] == gameBoard[1][1] && gameBoard[2][0] == gameBoard[0][2] &&
                gameBoard[2][0] != 0){
            winType = new int[]{2,2,4};
            isWinner = true;
        }
        for (int r = 0 ; r < 3 ; r++){
            for (int c = 0 ; c < 3 ; c++){
                if (gameBoard[r][c] != 0){
                    boardFilled += 1;
                }
            }
        }
        if (isWinner){
            playAgainBTN.setVisibility(View.VISIBLE);
            homeBTN.setVisibility(View.VISIBLE);
            playerTurn.setText(playerNames[player-1] + "'s Won!!!!!!");

            //Wining Sound
            MediaPlayer mp = MediaPlayer.create(context, R.raw.actualwin);
            if (mp.isPlaying()){
                mp.stop();
            }else{
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            }

            return true;
        }
        else if (boardFilled == 9){
            playAgainBTN.setVisibility(View.VISIBLE);
            homeBTN.setVisibility(View.VISIBLE);
            //Match Tie Sound
            MediaPlayer mp = MediaPlayer.create(context , R.raw.drawsound);
            if (mp.isPlaying()){
                mp.stop();
            }else{
                mp.start();
                mp.setOnCompletionListener(MediaPlayer::release);
            }

            playerTurn.setText( "Match Tied");
            return false;
        }else{
            return false;
        }
    }
    public void resetGame(){
        gameBoard = new int[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                gameBoard[r][c] = 0;
            }
        }
        player = 1;
        playAgainBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        playerTurn.setText(playerNames[0] + "'s Turn!");

    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }


}

