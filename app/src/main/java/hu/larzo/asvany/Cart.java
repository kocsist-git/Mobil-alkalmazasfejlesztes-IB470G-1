package hu.larzo.asvany;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private HashMap<Product, Integer> cartItems;

    public Cart()
    {
        cartItems = new HashMap<>();
    }

    public void addItem(Product product) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, cartItems.get(product) + 1);
        } else {
            cartItems.put(product, 1);
        }
        updateDatabase();

    }

    private void updateDatabase() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Konvertáld a cartItems-t olyan formátumra, amelyet a Firestore könnyen kezelni tud
            Map<String, Object> cartData = new HashMap<>();
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                cartData.put(entry.getKey().getName(), entry.getValue());
            }

            // Frissítsd a Firestore adatbázist
            db.collection("user").document(userId).update("cart", cartData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Sikeres frissítés
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hiba történt a frissítés során
                        }
                    });
    }



    public HashMap<Product, Integer> getCartItems() {
        return cartItems;
    }

    public void setCartItems(HashMap<Product, Integer> cartItems) {
        this.cartItems = cartItems;
    }


    public void removeItem(Product product) {
        if (cartItems.containsKey(product)) {
            int currentQuantity = cartItems.get(product);
            if (currentQuantity > 1) {
                cartItems.put(product, currentQuantity - 1);
            } else {
                cartItems.remove(product);
            }
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> cartData = new HashMap<>();
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            cartData.put(entry.getKey().getName(), entry.getValue());
        }
        return cartData;
    }

    public void fromMap(Map<String, Object> cartData, List<Product> products) {
        cartItems.clear();
        for (Map.Entry<String, Object> entry : cartData.entrySet()) {
            String productName = entry.getKey();
            int quantity = ((Long) entry.getValue()).intValue();
            Product product = findProductByName(productName, products);
            if (product != null) {
                cartItems.put(product, quantity);
            }
        }
    }

    private Product findProductByName(String productName, List<Product> products) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    public void clear(){
        cartItems.clear();
    }


}

