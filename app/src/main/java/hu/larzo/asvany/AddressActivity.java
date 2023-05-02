package hu.larzo.asvany;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends BaseActivity {

    private Button saveButton;
    private EditText countryEditText;
    private EditText zipCodeEditText;
    private EditText countyEditText;
    private EditText addressEditText;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        saveButton = findViewById(R.id.saveButton);
        countryEditText = findViewById(R.id.countryEditText);
        zipCodeEditText = findViewById(R.id.zipCodeEditText);
        countyEditText = findViewById(R.id.countyEditText);
        addressEditText = findViewById(R.id.addressEditText);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        saveButton.setOnClickListener(v -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                String country = countryEditText.getText().toString();
                String zipCode = zipCodeEditText.getText().toString();
                String county = countyEditText.getText().toString();
                String address = addressEditText.getText().toString();

                Map<String, Object> addressData = new HashMap<>();
                addressData.put("country", country);
                addressData.put("zipCode", zipCode);
                addressData.put("county", county);
                addressData.put("address", address);

                db.collection("user").document(userId).collection("addresses").add(addressData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(AddressActivity.this, "Cím sikeresen mentve", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddressActivity.this, "Hiba a cím mentése közben: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(AddressActivity.this, "Nincs bejelentkezett felhasználó", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected int getCurrentMenuItemId() { // Beállítja, hogy melyik menü icon lenegy kiválasztva
        return R.id.menu_account;
    }
}
