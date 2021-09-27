package com.gamestockz.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gamestockz.R;
import com.gamestockz.data.modals.GameSectionResult;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<com.gamestockz.Adapters.ResultAdapter.ResultViewHolder>{

    Context context;
    ArrayList<GameSectionResult> gameSectionResultArrayList;

    public ResultAdapter(Context context, ArrayList<GameSectionResult> gameSectionResultArrayList) {
        this.context = context;
        this.gameSectionResultArrayList = gameSectionResultArrayList;
    }


    @NonNull
    @Override
    public ResultAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game_section_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        String result = gameSectionResultArrayList.get(position).getResult();
        GameSectionResult gameSectionResult = gameSectionResultArrayList.get(position);

        holder.gameId.setText(gameSectionResult.getGameId());

        if (result.equalsIgnoreCase("1")) {
            holder.result.setCardBackgroundColor(Color.RED);
        } else if (result.equalsIgnoreCase("2")){
            holder.result.setCardBackgroundColor(Color.GREEN);
        }


    }

    @Override
    public int getItemCount() {
        return gameSectionResultArrayList.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView gameId;
        CardView result;


        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            gameId = itemView.findViewById(R.id.gameId);
            result = itemView.findViewById(R.id.result);

        }
    }

}
