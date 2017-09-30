package com.innazis.continents.continents.adapters;

/**
 * Created by inna on 27/09/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innazis.continents.continents.MainActivity;
import com.innazis.continents.continents.R;
import com.squareup.picasso.Picasso;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesAdapterViewHolder> {

    private final Context _context;

    final private CountriesAdapterOnClickHandler _clickHandler;

    private Cursor cursor;

    public interface CountriesAdapterOnClickHandler {
        void onClick(String name);
    }

    public CountriesAdapter(@NonNull Context context, CountriesAdapterOnClickHandler clickHandler) {
        _context = context;
        _clickHandler = clickHandler;
    }

    @Override
    public CountriesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.countries_list_item, parent, false);
        view.setFocusable(true);
        return new CountriesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountriesAdapterViewHolder holder, int position) {

        cursor.moveToPosition(position);

        // COUNTRY NAME
        String name = cursor.getString(MainActivity.INDEX_COLUMN_NAME);
        holder.nameView.setText(name);

        // REGION AND SUBREGION
        String region = cursor.getString(MainActivity.INDEX_COLUMN_REGION)
                + ", " + cursor.getString(MainActivity.INDEX_COLUMN_SUBREGION);
        holder.regionView.setText(region);

        // SUBREGION
        //String subregion = cursor.getString(MainActivity.INDEX_COLUMN_SUBREGION);

        // COUNTRY AREA
        String area = (cursor.getInt(MainActivity.INDEX_COLUMN_AREA) == 0) ? "--" : String.valueOf(cursor.getInt(MainActivity.INDEX_COLUMN_AREA)) + "km";
        holder.areaView.setText(area);

        // COUNTRY FLAG
        String svgString = cursor.getString(MainActivity.INDEX_COLUMN_FLAG);

        //because data comes with svg format and I didn't want to implement glide or another svg converter, I'll use a default image
        String flag = "https://upload.wikimedia.org/wikipedia/commons/5/5c/Israel_flag_300.png";
        Picasso.with(_context).load(flag).fit().into(holder.flagView);
        //holder.flagView.setImageURI();
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    class CountriesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView nameView;
        final TextView areaView;
        final TextView regionView;
        final ImageView flagView;

        CountriesAdapterViewHolder(View view) {
            super(view);

            nameView = (TextView) view.findViewById(R.id.country_name_tv);
            regionView = (TextView) view.findViewById(R.id.region_tv);
            areaView = (TextView) view.findViewById(R.id.area_size_tv);
            flagView = (ImageView) view.findViewById(R.id.conuntry_flag_iv);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            String countryId = cursor.getString(MainActivity.INDEX_COLUMN_COUNTRY_ID);
            _clickHandler.onClick(countryId);
        }
    }
}
