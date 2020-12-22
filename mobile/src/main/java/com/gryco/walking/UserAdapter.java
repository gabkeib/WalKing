package com.gryco.walking;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<Results> results = new ArrayList<>();

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int i) {
        holder.username.setText(results.get(i).getStepscount());
        holder.pointscount.setText(results.get(i).getPointscount());
        holder.date.setText(results.get(i).getSpecificdate());
        holder.distance.setText(results.get(i).getDistance());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Results> results){
        this.results = results;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView pointscount;
        public TextView date;
        public TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.Username);
            pointscount = itemView.findViewById(R.id.pointscoun);
            date = itemView.findViewById(R.id.date);
            distance = itemView.findViewById(R.id.distance);
        }
    }
}
