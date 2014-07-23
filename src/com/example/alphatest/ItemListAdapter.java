package com.example.alphatest;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter<Item>{

	Context context;
	
    public ItemListAdapter(Context context, int textViewResourceId, List<Item> items) {
        super(context, textViewResourceId, items);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_list_row, null);
        }

        Item item = getItem(position);
    	
        
        TextView title = (TextView) row.findViewById(R.id.item_list_row_title);
        TextView pubDate = (TextView) row.findViewById(R.id.item_list_row_pub_date);
        title.setText(item.getTitle());
        pubDate.setText(item.getPubDate());

        return row;
    }

}
