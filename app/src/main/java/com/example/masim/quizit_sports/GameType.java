package com.example.masim.quizit_sports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameType extends AppCompatActivity {
    private int gameType = 0;
    private Button b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        b2 = (Button)findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameType = 0;
                startActivity(new Intent(GameType.this, NewGameActivity.class));
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameType = 1;
                startActivity(new Intent(GameType.this, NewGameActivity.class));
            }
        });
    }

    public int getGameType(){
        return gameType;
    }
}
