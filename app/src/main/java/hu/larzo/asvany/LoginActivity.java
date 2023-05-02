package hu.larzo.asvany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Keressük meg a "Új ügyfél vagyok" gombot
        Button registerButton = findViewById(R.id.register_button);

        // Állítsuk be a gombra a kattintás eseménykezelőt
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hozzunk létre egy Intent-et, amely elindítja a RegisterActivity-t
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);

                // Indítsuk el a RegisterActivity-t
                startActivity(registerIntent);
                overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
            }
        });

    }

    @Override
    protected int getCurrentMenuItemId() { // Beállítja, hogy melyik menü icon lenegy kiválasztva
        return R.id.menu_account;
    }



}