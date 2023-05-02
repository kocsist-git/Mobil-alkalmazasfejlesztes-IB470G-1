package hu.larzo.asvany;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.HashMap;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private Cart cart;
    private CartActivity cartActivity;

    public CartItemAdapter(List<CartItem> cartItems, Context context, Cart cart, CartActivity cartActivity) {
        this.cartItems = cartItems;
        this.context = context;
        this.cart = cart;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.cartItemName.setText(cartItem.getProduct().getName());
        holder.cartItemQuantity.setText("Mennyiség: " + cartItem.getQuantity());
        holder.cartItemPrice.setText("Ár: " + cartItem.getProduct().getPrice() + "Ft");

        Glide.with(context)
                .load(cartItem.getProduct().getImageUrl())
                .into(holder.cartItemImage);


        Animation scaleAnimation2 = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.zizeg_animation);
        holder.itemView.startAnimation(scaleAnimation2);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImage;
        TextView cartItemName;
        TextView cartItemQuantity;
        TextView cartItemPrice;
        Button cart_item_remove;
        CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.cart_item_image);
            cartItemName = itemView.findViewById(R.id.cart_item_name);
            cartItemQuantity = itemView.findViewById(R.id.cart_item_quantity);
            cartItemPrice = itemView.findViewById(R.id.cart_item_price);

            cart_item_remove = itemView.findViewById(R.id.cart_item_remove);

            cart_item_remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product product = cartItems.get(position).getProduct();
                    cart.removeItem(product);
                    cartActivity.loadCartItems();
                    Toast.makeText(context, "A termék eltávolítva a kosárból", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
