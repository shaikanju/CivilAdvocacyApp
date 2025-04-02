package com.anju.civiladvocacyapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<MainItem> dataList;

    public CustomAdapter(List<MainItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        MainItem data = dataList.get(position);
        holder.bind(data);
        holder.itemView.setOnClickListener(v -> {
            // Handle the item click event here
            // You can start a new activity or perform other actions
            Intent intent = new Intent(v.getContext(), OfficialActivity.class);

            intent.putExtra("mainItem", data); // Make sure MainItem is Serializable or Parcelable
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView2;
        private TextView textView3;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }

        public void bind(MainItem data) {
            textView2.setText(data.getOffice());
            String nameWithParty = data.getName() + " (" + data.getParty() + ")";
            textView3.setText(nameWithParty);

            if (data.getPhotoUrl() != null && !data.getPhotoUrl().isEmpty()) {
                Picasso.get()
                        .load(data.getPhotoUrl())
                        .error(R.drawable.brokenimage) // Set the placeholder drawable resource here
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image);
            }
        }
    }
}
