package com.example.comfelix_nath_anprojetx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListeDestinationsAdapter extends ArrayAdapter<Voyage> {

    private Context context;
    private int layoutResource;
    //private List<Voyage> voyages;

    public ListeDestinationsAdapter(@NonNull Context context, int resource, @NonNull List<Voyage> voyages) {
        super(context, resource, voyages);
        this.context = context;
        this.layoutResource = resource;
        //this.voyages = voyages;
    }

    @Override
    public int getCount() {
        //return voyages.size();
        return super.getCount();
    }

    @Nullable
    @Override
    public Voyage getItem(int position) {
        //return voyages.get(position);
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResource, parent, false);
        }


        Voyage voyage = getItem(position);;

        TextView tvDestination = convertView.findViewById(R.id.tvDestinationListe);
        TextView tvDescription = convertView.findViewById(R.id.tvDescriptionListe);
        TextView tvPrix = convertView.findViewById(R.id.tvPrixListe);
        ImageView imgDestination = convertView.findViewById(R.id.imgDestinationListe);

        tvDestination.setText(voyage.getDestination());
        tvDescription.setText(voyage.getDescription());
        tvPrix.setText(voyage.getPrix() + " $");

        // Chargement de l’image depuis l’URL
        new LoadImageAsync(imgDestination).execute(voyage.getImage_url());

        return convertView;
    }

    private static class LoadImageAsync extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageAsync(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                InputStream input = new URL(urls[0]).openStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
        }
    }

    public void updateData(List<Voyage> nouvellesDestinations) {

        //Ce clear cause problème et empêche l'affichage des voyages, malheureusement sans lui les voyages se dupliquent plus on ajoute de filtres
        //clear();
        addAll(nouvellesDestinations);
        notifyDataSetChanged();
    }


}
