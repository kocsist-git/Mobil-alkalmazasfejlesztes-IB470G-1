package hu.larzo.asvany;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getCurrentMenuItemId(); // Absztrakt a nenü kiválasztásához

    private CartNotificationService cartNotificationService;
    private static final String CHANNEL_ID = "shop_notification_channel";
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
            bottomNavigationView.setSelectedItemId(getCurrentMenuItemId()); // Beállítjuk a kívát menü elemet
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartNotificationService = new CartNotificationService(this);
    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupBottomNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private boolean isChangingActivity = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (!isChangingActivity && MyApplication.getInstance().getCartItems().size() > 0) {
            cartNotificationService.send("Termékek maradtek a kosárban");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChangingActivity = false;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            item -> {
                isChangingActivity = true;
                int itemId = item.getItemId();
                if (itemId != getCurrentMenuItemId()) { // Csak akkor hívja meg, ha nem ő az aktív jeleneleg.
                    if (itemId == R.id.menu_home) {
                        Intent mainIntent = new Intent(BaseActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                    } else if (itemId == R.id.menu_cart) {
                        Intent cartIntent = new Intent(BaseActivity.this, CartActivity.class);
                        startActivity(cartIntent);
                        overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                    } else if (itemId == R.id.menu_search) {
                        Intent favIntent = new Intent(BaseActivity.this, FavoriteActivity.class);
                        startActivity(favIntent);
                        overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                    } else if (itemId == R.id.menu_account) {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser != null) {
                            // A felhasználó be van jelentkezve
                            Intent profilIntent = new Intent(BaseActivity.this, ProfilActivity.class);
                            startActivity(profilIntent);
                            overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                        } else {
                            // Nincs bejelentkezett felhasználó
                            Intent loginIntent = new Intent(BaseActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            overridePendingTransition(0, 0); // Letiltja az animációt az activity váltás során
                        }
                    }
                }
                  return true;
              };



}
