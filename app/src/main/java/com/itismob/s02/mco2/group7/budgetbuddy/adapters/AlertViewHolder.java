package com.itismob.s02.mco2.group7.budgetbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.itismob.s02.mco2.group7.budgetbuddy.R;
import com.itismob.s02.mco2.group7.budgetbuddy.models.AlertItem;

public class AlertViewHolder extends RecyclerView.ViewHolder {

    private final MaterialCardView alertCard;
    private final TextView iconTv, titleTv, messageTv;

    public AlertViewHolder(@NonNull View itemView) {
        super(itemView);
        alertCard = itemView.findViewById(R.id.alertCard);
        iconTv = itemView.findViewById(R.id.iconTv);
        titleTv = itemView.findViewById(R.id.titleTv);
        messageTv = itemView.findViewById(R.id.messageTv);
    }

    public void bindData(AlertItem alert) {
        titleTv.setText(alert.getTitle());
        messageTv.setText(alert.getMessage());

        int bg;
        String icon;
        switch (alert.getType()) {
            case AlertItem.TYPE_EXCEEDED:
                bg = R.color.red_light;
                icon = "🚨";
                break;
            case AlertItem.TYPE_WARNING:
                bg = R.color.orange_light;
                icon = "⚠️";
                break;
            default:
                bg = R.color.green_light;
                icon = "📅";
        }
        iconTv.setText(icon);
        alertCard.setCardBackgroundColor(itemView.getContext().getColor(bg));
    }
}