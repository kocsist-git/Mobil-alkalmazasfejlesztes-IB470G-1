package hu.larzo.asvany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private EditText registerEmail, registerPassword, registerConfirmPassword, registerName;
    private Button registerButton;

    @Override
    protected int getCurrentMenuItemId() { // Beállítja, hogy melyik menü icon lenegy kiválasztva
        return R.id.menu_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        registerName = findViewById(R.id.register_name);
        registerButton = findViewById(R.id.register_button);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        String passwordAgain = registerConfirmPassword.getText().toString();
        String name = registerName.getText().toString();

        //TODO:jelszó ellenőrzés

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Sikeres regisztráció",
                                    Toast.LENGTH_LONG).show();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            String userId = mAuth.getCurrentUser().getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", name);
                            user.put("registrationDate", new Date());

                            db.collection("user")
                                    .document(userId)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Log.d("Firestore", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                          //  Log.w("Firestore", "Error writing document", e);
                                        }
                                    });

                            // Hozzunk létre egy Intent-et, amely elindítja a RegisterActivity-t
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);

                            // Indítsuk el a RegisterActivity-t
                            startActivity(mainIntent);
                            overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                        }else{
                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Sikertelen regisztráció: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}