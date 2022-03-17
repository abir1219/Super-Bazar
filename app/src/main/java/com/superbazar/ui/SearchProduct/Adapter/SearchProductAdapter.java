package com.superbazar.ui.SearchProduct.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.superbazar.R;
import com.superbazar.ui.SearchProduct.Model.SearchProductModel;

import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchAdapterViewHolder>  {
    List<SearchProductModel> modelList;
    Context context;

    public SearchProductAdapter(List<SearchProductModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {

        SearchProductModel searchedModel = modelList.get(position);
        Picasso.get().load(Uri.parse(searchedModel.getImage())).into(holder.searchProdImage);
        holder.searchProdName.setText(searchedModel.getName());
        holder.tvDesc.setText(searchedModel.getProdDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id",modelList.get(position).getProdid());
                Navigation.findNavController(view).navigate(R.id.nav_search_product_to_product_details,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView searchProdImage;
        TextView searchProdName,tvDesc;

        public SearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            searchProdImage = itemView.findViewById(R.id.ivItem);
            searchProdName = itemView.findViewById(R.id.tvItem);
            tvDesc = itemView.findViewById(R.id.tvDesc);

            //itemView.setOnClickListener(this);
        }


    }
}
