package hu.larzo.asvany;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CheckoutActivity extends BaseActivity {

    private Button checkoutButton;
    private FirebaseAuth mAuth;
    private Cart cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        MyApplication app = (MyApplication) getApplicationContext();
        cart = app.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").document(userId).collection("addresses").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            clearCart(db, userId);
                            cart.clear();
                        } else {
                            Intent addressIntent = new Intent(CheckoutActivity.this, AddressActivity.class);
                            startActivity(addressIntent);
                        }
                    }
                });
            }

    }
    private void clearCart(FirebaseFirestore db, String userId) {
        db.collection("user").document(userId).collection("cart").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot cartItems = task.getResult();
                if (cartItems != null) {
                    for (DocumentSnapshot item : cartItems.getDocuments()) {
                        db.collection("user").document(userId).collection("cart").document(item.getId()).delete();
                    }
                }
            }
        });
    }
    @Override
    protected int getCurrentMenuItemId() {
        return R.id.menu_cart;
    }
}
