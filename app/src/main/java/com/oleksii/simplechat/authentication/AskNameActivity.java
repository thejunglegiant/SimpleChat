package com.oleksii.simplechat.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;

public class AskNameActivity extends AppCompatActivity {
    private EditText nameText, surnameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_name);

        // Setting up StatusBar color to the primary one
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        nameText = findViewById(R.id.name_text);
        surnameText = findViewById(R.id.surname_text);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (isNameAndSurnameCorrect()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isNameAndSurnameCorrect()) {
            // TODO Save to Room
        } else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.delete();
            }
        }
    }

    private boolean isNameAndSurnameCorrect() {
        String pattern = "([а-яА-ЯёЁ]|[a-zA-Z])[^#&<>\"_~:;$^%{}.?\\s]{1,20}$";
        return nameText.getText().toString().trim().matches(pattern)
                && surnameText.getText().toString().trim().matches(pattern);
    }
}