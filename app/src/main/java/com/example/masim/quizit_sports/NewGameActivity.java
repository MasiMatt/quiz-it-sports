package com.example.masim.quizit_sports;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.R.attr.right;
import static android.R.attr.visible;
import static android.graphics.drawable.Drawable.createFromStream;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.masim.quizit_sports.R.drawable.roundedbutton;

public class NewGameActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView image;
    String path;
    Button buttons[] = new Button[4], hint;
    numberRandomizer randomizer = new numberRandomizer();
    int correctBtn, imageId = randomizer.randoImg();
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer, mTimer2;
    TextView timerText;
    int ctr=0, score = 0;
    int playerTaken[] = new int[305];
    GameType game = new GameType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        image = (ImageView) findViewById(R.id.imageView);
        buttons[0] = (Button)findViewById(R.id.button_answer1);
        buttons[0].setOnClickListener(this);
        buttons[1] = (Button)findViewById(R.id.button_answer2);
        buttons[1].setOnClickListener(this);
        buttons[2] = (Button)findViewById(R.id.button_answer3);
        buttons[2].setOnClickListener(this);
        buttons[3] = (Button)findViewById(R.id.button_answer4);
        buttons[3].setOnClickListener(this);
        hint = (Button)findViewById(R.id.buttonHint);
        hint.setOnClickListener(this);

        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);
        timerText=(TextView) findViewById(R.id.timerText);
        mProgressBar.setRotation(180);

        for(int i=0; i<305; i++)
            playerTaken[i] = 0;

        test();
    }

    @Override
    public void onClick(View v) {
        if(v == buttons[correctBtn])
        {
            score++;
            mCountDownTimer.cancel();
            v.setBackgroundColor(getResources().getColor(R.color.green));

            //mark.setImageResource(R.drawable.right);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    test();
                }
            }, 500);
        }

        else if(v == hint)
        {
            mCountDownTimer.cancel();
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            new AlertDialog.Builder(NewGameActivity.this)
                    .setMessage(databaseAccess.getHint(imageId))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //timerText.setText("0");
                            //ctr=0;
                            mProgressBar.setProgress(10);
                            mCountDownTimer=new CountDownTimer(1000*(11-(ctr/59)),(11-(ctr/59))) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    timerText.setText("" + millisUntilFinished / 1000);
                                    ctr++;
                                    mProgressBar.setProgress(ctr);

                                }

                                @Override
                                public void onFinish() {
                                    timerText.setText("0");
                                    ctr=0;

                                    buttons[correctBtn].setBackgroundColor(getResources().getColor(R.color.green));

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(NewGameActivity.this, DisplayScoreActivity.class);
                                            i.putExtra("SCORE_ID", score);
                                            score = 0;
                                            startActivity(i);
                                        }
                                    }, 1500);

                                }
                            };
                            mCountDownTimer.start();
                        }
                    }).show();
            databaseAccess.close();
        }

        else
        {
            mCountDownTimer.cancel();
            v.setBackgroundColor(getResources().getColor(R.color.red));
            buttons[correctBtn].setBackgroundColor(getResources().getColor(R.color.green));

            //mark.setImageResource(R.drawable.wrong);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(NewGameActivity.this, DisplayScoreActivity.class);
                    i.putExtra("SCORE_ID", score);
                    score = 0;
                    startActivity(i);
                }
            }, 1500);
        }
    }

    public void test()
    {
        buttons[0].setBackgroundColor(getResources().getColor(R.color.white));
        buttons[1].setBackgroundColor(getResources().getColor(R.color.white));
        buttons[2].setBackgroundColor(getResources().getColor(R.color.white));
        buttons[3].setBackgroundColor(getResources().getColor(R.color.white));

        while(playerTaken[imageId-1] == 1)
            imageId = randomizer.randoImg();

        playerTaken[imageId-1] = 1;

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        path = databaseAccess.getImageLink(imageId);
        databaseAccess.close();

        try {
            Drawable d = createFromStream(getAssets().open("images/" + path), null);
            image.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int j = imageId;
        int takenArr[] = new int[4];
        for(int i=0; i<4; i++)
            takenArr[i] = imageId;

        databaseAccess.open();
        correctBtn = randomizer.randoBtn();
        buttons[correctBtn].setText(databaseAccess.getName(imageId));

        for(int i=0; i<4; i++)
        {
            if(i == correctBtn)
                continue;

            while(j == takenArr[0] || j == takenArr[1] || j == takenArr[2] || j == takenArr[3])
                j = randomizer.randoNames();

            buttons[i].setText(databaseAccess.getName(j));
            takenArr[i] = j;
        }
        databaseAccess.close();

        //if(game.getGameType() == 1)
            progressBar();
    }

    public void progressBar()
    {
        timerText.setText("0");
        ctr=0;
        mProgressBar.setProgress(10);
        mCountDownTimer=new CountDownTimer(11000,11) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("" + millisUntilFinished / 1000);
                ctr++;
                System.out.println(Math.round(ctr/59));
                mProgressBar.setProgress(ctr);

            }

            @Override
            public void onFinish() {
                timerText.setText("0");
                ctr=0;

                buttons[correctBtn].setBackgroundColor(getResources().getColor(R.color.green));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(NewGameActivity.this, DisplayScoreActivity.class);
                        i.putExtra("SCORE_ID", score);
                        score = 0;
                        startActivity(i);
                    }
                }, 1500);

            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(NewGameActivity.this)
                .setMessage("Are you sure you want to exit the game??")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(NewGameActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .show();
    }
}