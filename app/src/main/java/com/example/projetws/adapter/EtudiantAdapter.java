package com.example.projetws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetws.beans.Etudiant;

import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.ViewHolder> {

    private List<Etudiant> etudiants;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Etudiant etudiant);
    }

    public EtudiantAdapter(List<Etudiant> etudiants, Context context, OnItemClickListener listener) {
        this.etudiants = etudiants;
        this.context = context;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomPrenom;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNomPrenom = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(v -> listener.onItemClick(etudiants.get(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Etudiant e = etudiants.get(position);
        holder.tvNomPrenom.setText(e.getNom() + " " + e.getPrenom());
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    public void updateList(List<Etudiant> newList) {
        etudiants = newList;
        notifyDataSetChanged();
    }
}
