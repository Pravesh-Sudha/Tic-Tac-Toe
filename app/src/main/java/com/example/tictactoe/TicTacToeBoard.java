package com.example.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TicTacToeBoard extends View {
    private final int boardColor;
    private final int XColor;
    private final int OColor;
    private boolean winningLine = false;
    private final int winningLineColor;
    private int cellSize = getWidth()/3;
    private final GameLogic game;
    private final Paint paint = new Paint();

    public TicTacToeBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        game = new GameLogic(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TicTacToeBoard
                , 0, 0);
        try {
                boardColor = a.getInteger(R.styleable.TicTacToeBoard_boardColor , 0);
                XColor = a.getInteger(R.styleable.TicTacToeBoard_XColor , 0);
                OColor = a.getInteger(R.styleable.TicTacToeBoard_OColor , 0);
                winningLineColor = a.getInteger(R.styleable.TicTacToeBoard_winningLineColor , 0);
        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int dimension = Math.min(getMeasuredWidth() , getMeasuredHeight());
        cellSize = dimension/3;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        
        drawGameBoard(canvas);
        drawMarkers(canvas);
        if (winningLine){
            paint.setColor(winningLineColor);
            drawWinningLine(canvas);
        }
    }

    private void drawMarkers(Canvas canvas) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.getGameBoard()[r][c] != 0) {
                    if (game.getGameBoard()[r][c] == 1) {
                        drawX(canvas, r, c);
                    } else {
                        drawO(canvas, r, c);
                    }
                }
            }
        }
        if(game.getPlayer() == 1){
            playXSound();
        }else playOSound();
    }


    private void drawGameBoard(Canvas canvas) {
        paint.setColor(boardColor);
        paint.setStrokeWidth(16);
        for (int c= 1; c < 3;c++){
            canvas.drawLine(cellSize * c , 0,cellSize * c ,canvas.getWidth() , paint);
        }
        for (int r= 1; r < 3 ; r++){
            canvas.drawLine(0 ,cellSize * r , canvas.getWidth() ,cellSize * r , paint);
        }
    }
    private void drawX(Canvas canvas , int row, int col){
        paint.setColor(XColor);
        canvas.drawLine((float) ((col+1) * cellSize - cellSize * 0.2) ,
                          (float)(row*cellSize + cellSize * 0.2) ,
                             (float)(col *cellSize + cellSize * 0.2) ,
                              (float)((row + 1) * cellSize - cellSize * 0.2), paint);

        canvas.drawLine((float)((col) * cellSize +  cellSize * 0.2) ,
                (float)(row*cellSize +  cellSize * 0.2) ,
                (float)((col+1) *cellSize - cellSize * 0.2) ,
                (float)((row + 1) * cellSize - cellSize * 0.2) , paint);

    }
    private void drawO(Canvas canvas , int row, int col){
        paint.setColor(OColor);
        canvas.drawOval((float) (col * cellSize +  cellSize * 0.2),
                (float) (row * cellSize +  cellSize * 0.2) ,
                (float) ((col*cellSize + cellSize) -  cellSize * 0.2) ,
                (float) ((row*cellSize + cellSize) -  cellSize * 0.2)
                , paint);
    }
    private void drawHorizontalLine(Canvas canvas , int row , int col){
        canvas.drawLine(col , row *cellSize + (float)cellSize/2 ,
                cellSize * 3 , row*cellSize +(float) cellSize/2 ,
                paint);
    }
    private void drawVerticalLine(Canvas canvas , int row , int col){
        canvas.drawLine(col *cellSize +(float) cellSize/2 , row ,
                col * cellSize + (float)cellSize/2 , cellSize * 3 ,
                paint);
    }
    private void drawDiagonalPositiveLine(Canvas canvas){
        canvas.drawLine(0, cellSize*3 , cellSize*3 , 0 , paint);
    }
    private void drawDiagonalNegativeLine(Canvas canvas){
        canvas.drawLine(0, 0 , cellSize*3 , cellSize * 3 , paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            int row = (int) Math.ceil(y/cellSize);
            int col = (int) Math.ceil(x/cellSize);

            if(!winningLine){
                if (game.updateGameBoard(row , col)){
                    invalidate();
                    if (game.winnerCheck()){
                        winningLine = true;
                    }
                    if (game.getPlayer() % 2 == 0){
                        game.setPlayer(game.getPlayer()-1);
                    }else{
                        game.setPlayer(game.getPlayer()+1);
                    }
                }
            }
            invalidate();
            return true;
        }
        return false;
    }
    public void setUpGame(Button playAgain , Button home, TextView playerDisplay , String[] name){
        game.setHomeBTN(home);
        game.setPlayAgainBTN(playAgain);
        game.setPlayerTurn(playerDisplay);
        game.setPlayerNames(name);
    }
    private void drawWinningLine (Canvas canvas){
        int row = game.getWinType()[0];
        int col = game.getWinType()[1];

        switch (game.getWinType()[2]){
            case 1:
                drawHorizontalLine(canvas , row , col);
                break;
            case 2:
                drawVerticalLine(canvas , row , col);
                break;
            case 3:
                drawDiagonalNegativeLine(canvas);
                break;
            case 4:
                drawDiagonalPositiveLine(canvas);
                break;
        }
    }
    public void resetGame(){
        game.resetGame();
        winningLine = false;
    }
    public void playXSound(){
        MediaPlayer mp = MediaPlayer.create(getContext() , R.raw.xsound);
        if (mp.isPlaying()){
            mp.stop();
        }else{
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }
    }
    public void playOSound(){
        MediaPlayer mp = MediaPlayer.create(getContext() , R.raw.osound);
        if (mp.isPlaying()){
            mp.stop();
        }else{
            mp.start();
            mp.setOnCompletionListener(MediaPlayer::release);
        }
    }
}
