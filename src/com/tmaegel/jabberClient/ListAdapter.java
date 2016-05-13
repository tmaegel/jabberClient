package com.tmaegel.jabberClient;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Contact> {

        private final Context context;
        private final ArrayList<Contact> itemsArrayList;

        public ListAdapter(Context context, ArrayList<Contact> itemsArrayList) {
            super(context, R.layout.list_layout, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // create inflater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // get rowView from inflater
            View rowView = inflater.inflate(R.layout.list_layout, parent, false);

            // get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.label);
            TextView valueView = (TextView) rowView.findViewById(R.id.value);

            // set the text for textView
            labelView.setText(itemsArrayList.get(position).getName());
            valueView.setText(itemsArrayList.get(position).getJid());

            return rowView;
        }
}
