package hu.larzo.asvany;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItems;
    private Cart cart;
    private TextView cartInfo;
    private Button buttonCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        MyApplication app = (MyApplication) getApplicationContext();
        cart = app.getInstance();

        recyclerView = findViewById(R.id.rv_cart_items);

        cartInfo = findViewById(R.id.cart_info);
        buttonCheckout = findViewById(R.id.button_checkout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(cartItems, this, cart, CartActivity.this);
        recyclerView.setAdapter(cartItemAdapter);


        Button orderButton = findViewById(R.id.button_checkout);
        orderButton.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(checkoutIntent);
        });

        loadCartItems();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadCartItems() {
        HashMap<Product, Integer> cartItemsMap = cart.getCartItems();
        cartItems.clear();

        for (Map.Entry<Product, Integer> entry : cartItemsMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            cartItems.add(new CartItem(product, quantity));
        }
        int a = cart.getCartItems().size() ;
        if (a == 0){
            cartInfo.setText("A kosár üres");
            buttonCheckout.setVisibility(View.INVISIBLE);
        }else {
            buttonCheckout.setActivated(true);
            cartInfo.setText("");
            buttonCheckout.setVisibility(View.VISIBLE);
        }

        cartItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getCurrentMenuItemId() {
        return R.id.menu_cart;
    }
}
