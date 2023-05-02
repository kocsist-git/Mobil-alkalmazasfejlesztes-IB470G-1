package hu.larzo.asvany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication app = (MyApplication) getApplicationContext();
        Cart cart = app.getInstance();

        recyclerView = findViewById(R.id.rv_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this, cart);
        recyclerView.setAdapter(productAdapter);

        loadProducts();
    }
    private void loadProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String imageUrl = document.getString("imageUrl");
                                String price = document.getString("price");
                                String title = document.getString("title");
                                productList.add(new Product(name, imageUrl, price, title));
                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Hiba történt: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Product getProductByName(String productName) {
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    @Override
    protected int getCurrentMenuItemId() {
        return R.id.menu_home;
    }
}