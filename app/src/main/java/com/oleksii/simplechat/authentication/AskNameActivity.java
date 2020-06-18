package com.oleksii.simplechat.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;

public class AskNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_name);

        EditText nameText = findViewById(R.id.name_text);
        EditText surnameText = findViewById(R.id.surname_text);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            String pattern = "([а-яА-ЯёЁ]|[a-zA-Z])[^#&<>\"_~:;$^%{}.?\\s]{1,20}$";
            if (nameText.getText().toString().trim().matches(pattern)
                && surnameText.getText().toString().trim().matches(pattern)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("name", nameText.getText().toString().trim());
                intent.putExtra("surname", surnameText.getText().toString().trim());
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.wrong_name_or_surname, Toast.LENGTH_SHORT).show();
            }
        });
    }
}