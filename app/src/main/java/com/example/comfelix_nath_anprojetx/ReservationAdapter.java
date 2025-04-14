package com.example.comfelix_nath_anprojetx;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ReservationAdapter extends BaseAdapter {

    private Context context;
    private List<Reservation> reservations;
    private DatabaseHelper dbHelper;


    public ReservationAdapter(Context context, List<Reservation> reservations, DatabaseHelper dbHelper) {
        this.context = context;
        this.reservations = reservations;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.historique_layout, parent, false);
        }

        Reservation reservation = reservations.get(position);

        TextView tvDestination = convertView.findViewById(R.id.tvDestinationHistorique);
        TextView tvDateVoyage = convertView.findViewById(R.id.tvDateHistorique);
        TextView tvMontant = convertView.findViewById(R.id.tvPrixHistorique);
        final TextView tvStatut = convertView.findViewById(R.id.tvStatutHistorique);
        ImageButton btnAnnuler = convertView.findViewById(R.id.btnSupprimer);

        tvDestination.setText(reservation.getDestination());
        tvDateVoyage.setText(reservation.getDateVoyage());
        tvMontant.setText(String.valueOf(reservation.getMontant()));
        tvStatut.setText(reservation.getStatut());

        if (reservation.getStatut().equals("annulée")) {
            tvStatut.setBackgroundColor(Color.RED);
            tvStatut.setText("Annulé");
        } else {
            tvStatut.setText("Confirmé");
        }

        btnAnnuler.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment annuler cette réservation ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        dbHelper.updateReservationStatus(reservation.getId(), "annulée");
                        reservation.setStatut("annulée");
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        return convertView;
    }

}

