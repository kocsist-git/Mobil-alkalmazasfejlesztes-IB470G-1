package hu.larzo.asvany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfilActivity extends BaseActivity {

    private TextView userName, userEmail, userCountry, userZip, userCounty, userAddress, registrationDate;
    private Button logoutButton, addAddress;
    private FirebaseAuth mAuth;

    @Override
    protected int getCurrentMenuItemId() {
        return R.id.menu_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userCountry = findViewById(R.id.user_country);
        userZip = findViewById(R.id.user_zip);
        userCounty = findViewById(R.id.user_county);
        userAddress = findViewById(R.id.user_address);
        registrationDate = findViewById(R.id.registration_date);
        logoutButton = findViewById(R.id.logout_button);
        addAddress = findViewById(R.id.add_address);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userEmail.setText(currentUser.getEmail());

            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("user").document(userId);

            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        addAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent AddressIntent = new Intent(ProfilActivity.this, AddressActivity.class);
                                startActivity(AddressIntent);
                            }
                        });
                        userName.setText(document.getString("name"));

                        Date registrationDateValue = document.getDate("registrationDate");
                        if (registrationDateValue != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String formattedRegistrationDate = sdf.format(registrationDateValue);
                            registrationDate.setText(formattedRegistrationDate);
                        }
                    }
                }
            });

            // Címek lekérdezése
            userDocRef.collection("addresses").limit(1).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot addressDoc = querySnapshot.getDocuments().get(0);
                        userCountry.setText(addressDoc.getString("country"));
                        userZip.setText(addressDoc.getString("zipCode"));
                        userCounty.setText(addressDoc.getString("county"));
                        userAddress.setText(addressDoc.getString("address"));
                    }
                }
            });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent loginIntent = new Intent(ProfilActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

}

