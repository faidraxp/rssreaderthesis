package com.example.rssnewsreader.rss.Adapter;



import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.RSSObject;
import com.example.rssnewsreader.R;
import com.example.rssnewsreader.sqlite.FirebaseConfig;


class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{


    public TextView txtTitle,txtPubDate,txtContent;
    ItemClickListener itemClickListener;

    ///ItemClickListener itemClickListener;
    public FeedViewHolder(View itemView,ItemClickListener itemClickListener) {
        super(itemView);

        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtPubDate = itemView.findViewById(R.id.txtPubDate);
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


public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    public static String TAG="RSS FEED ADAPTER";
    private RSSObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;
    ItemClickListener itemClickListener;

    public FeedAdapter(RSSObject rssObject, Context mContext,ItemClickListener itemClickListener) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        this.itemClickListener=itemClickListener;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row,parent,false);
        return new FeedViewHolder(itemView,itemClickListener);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());
        holder.txtContent.setText(rssObject.getItems().get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}