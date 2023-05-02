package hu.larzo.asvany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("products")
                .orderBy("price", Query.Direction.DESCENDING)
                .limit(5);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Product> asvanyList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product asvany = document.toObject(Product.class);
                    asvanyList.add(asvany);
                }

                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(asvanyList);
                favoritesRecyclerView.setAdapter(favoriteAdapter);
            } else {
                Log.w("Firestore", "Error getting documents.", task.getException());
            }
        });
    }



    @Override
    protected int getCurrentMenuItemId() {
        return R.id.menu_search;
    }

}
