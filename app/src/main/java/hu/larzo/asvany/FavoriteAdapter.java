package hu.larzo.asvany;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Product> asvanyList;

    public FavoriteAdapter(List<Product> asvanyList) {
        this.asvanyList = asvanyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product asvany = asvanyList.get(position);
        holder.asvanyName.setText(asvany.getName());
        holder.asvanyPrice.setText(asvany.getPrice() + " Ft");
    }

    @Override
    public int getItemCount() {
        return asvanyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView asvanyName;
        TextView asvanyPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            asvanyName = itemView.findViewById(R.id.asvany_name);
            asvanyPrice = itemView.findViewById(R.id.asvany_price);
        }
    }
}
