package hu.larzo.asvany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    private TextView Email;
    private TextView Password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

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

        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login(){
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                    // Indítsuk el a RegisterActivity-t
                    startActivity(mainIntent);
                    overridePendingTransition(0, 0);
                }else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Sikertelen bejelentkezés: " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected int getCurrentMenuItemId() { // Beállítja, hogy melyik menü icon lenegy kiválasztva
        return R.id.menu_account;
    }



}