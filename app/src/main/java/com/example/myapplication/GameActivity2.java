package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_game_screen);

        // populating name and difficulty
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        TextView textViewHealth = findViewById(R.id.textViewHealth);
        ImageView imageViewChar = findViewById(R.id.imageViewCharacter);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        String savedName = preferences.getString("name", "");
        textViewName.setText(savedName);

        String savedDiff = preferences.getString("diff", "");
        textViewDiff.setText("Difficulty: " + savedDiff);

        // populating HP
        if (savedDiff.equals("easy")) {
            textViewHealth.setText("150");
        } else if (savedDiff.equals("medium")) {
            textViewHealth.setText("100");
        } else {
            textViewHealth.setText("50");
        }

        // populating the character icon
        String savedChar = preferences.getString("char", "");
        int imageResource;
        if (savedChar.equals("knight")) {
            imageResource = R.drawable.knight;
        } else if (savedChar.equals("elf")) {
            imageResource = R.drawable.elf;
        } else {
            imageResource = R.drawable.lizard;
        }

        imageViewChar.setImageResource(imageResource);

        // ending the game
        Button nextBtn = findViewById(R.id.secondNextButton);
        nextBtn.setOnClickListener(v -> {
            Intent end = new Intent(GameActivity2.this, GameActivity3.class);
            startActivity(end);
            finish();
        });
    }
}
