package com.example.rssnewsreader.rss.Adapter;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.rssnewsreader.R;
import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.DataInitialize;

import java.util.ArrayList;
import java.util.List;

class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtTitle,txtContent;
    ItemClickListener itemClickListener;
    public ResourceViewHolder(View itemView,ItemClickListener itemClickListener) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtContent = itemView.findViewById(R.id.txtContent);
        this.itemClickListener=itemClickListener;

        //Set Event
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition());

    }
}

public class RecourcesAdapter extends RecyclerView.Adapter<ResourceViewHolder>{


    List<DataInitialize> resources;
    private List<DataInitialize> resourcesFullList;
    private Context mContext;
    private LayoutInflater inflater;
    ItemClickListener itemClickListener;

    public RecourcesAdapter(List<DataInitialize> resources, Context mContext, ItemClickListener itemClickListener) {
        this.resources = resources;
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(mContext);
        resourcesFullList = new ArrayList<>(resources);
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.resources_row,viewGroup,false);
        return new ResourceViewHolder(itemView,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder resourceViewHolder, int i) {

        resourceViewHolder.txtTitle.setText(resources.get(i).getSearchName());
        resourceViewHolder.txtContent.setText(resources.get(i).getLink());
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }
    
    //@Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DataInitialize> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(resourcesFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DataInitialize item : resourcesFullList) {
                    if (item.getSearchName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            resources.clear();
            resources.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public DataInitialize getClickedItem(int position){
         return resources.get(position);
    }
}
