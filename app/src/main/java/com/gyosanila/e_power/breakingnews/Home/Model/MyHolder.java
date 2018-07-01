package com.gyosanila.e_power.breakingnews.Home.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyosanila.e_power.breakingnews.R;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView item_img;
    public TextView item_title, item_author, item_source, item_publish;
    ItemClickListener itemClickListener;


    public MyHolder(View itemView) {
        super(itemView);

        item_img = itemView.findViewById(R.id.imgNews);
        item_title = itemView.findViewById(R.id.tvTitle);
        item_author =  itemView.findViewById(R.id.tvAuthor);
        item_source =  itemView.findViewById(R.id.tvSource);
        item_publish =  itemView.findViewById(R.id.tvPublish);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}
