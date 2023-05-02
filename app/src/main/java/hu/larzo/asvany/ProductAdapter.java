package hu.larzo.asvany;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private Cart cart;
    private FirebaseAuth mAuth;

    public ProductAdapter(List<Product> productList, Context context, Cart cart) {
        this.productList = productList;
        this.context = context;
        this.cart = cart;
        mAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice() + " Ft");
        holder.productTitle.setText(product.getTitle());

        // Betöltjük a képet a Firebase Storage-ból a Glide segítségével
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);

        Animation scaleAnimation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.scale_animation);
        holder.itemView.startAnimation(scaleAnimation);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productTitle;

        Button addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productTitle = itemView.findViewById(R.id.product_title);

            addToCart = itemView.findViewById(R.id.add_to_cart);

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser != null) {



                        int position = getAdapterPosition();
                        Product product = productList.get(position);
                        cart.addItem(product);
                        HashMap<Product, Integer> cartItems = cart.getCartItems();
                        Toast.makeText(context, "A termék hozzáadva a kosárhoz", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        context.startActivity(loginIntent);
                    }
                }
            });

        }
    }
}

