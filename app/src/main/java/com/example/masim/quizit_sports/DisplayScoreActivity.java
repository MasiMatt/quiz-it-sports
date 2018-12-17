package com.example.masim.quizit_sports;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayScoreActivity extends AppCompatActivity {
    TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_score);

        scoreText=(TextView) findViewById(R.id.displayScore);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("SCORE_ID", 0);
            scoreText.setText(String.valueOf(String.valueOf(intValue)));

    }

    public void onBackPressed() {
        startActivity(new Intent(DisplayScoreActivity.this, MainActivity.class));
    }
}
