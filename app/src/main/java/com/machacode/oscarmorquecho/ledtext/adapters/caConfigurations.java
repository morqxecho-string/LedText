package com.machacode.oscarmorquecho.ledtext.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.machacode.oscarmorquecho.ledtext.R;
import com.machacode.oscarmorquecho.ledtext.interfaces.onClickActions;
import com.machacode.oscarmorquecho.ledtext.models.LedTextRealm;
import com.machacode.oscarmorquecho.ledtext.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class caConfigurations extends RecyclerView.Adapter<caConfigurations.ViewHolderConfiguration>{
    private Context context;
    private List<LedTextRealm> lsLedTextRealm = new ArrayList<>();
    private onClickActions onClickListener;

    class ViewHolderConfiguration extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvCreatedAt;
        private CardView cvConfiguration;

        ViewHolderConfiguration(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
            cvConfiguration = view.findViewById(R.id.cvConfiguration);
        }
    }

    public caConfigurations(Context context, onClickActions mOnClickDetail) {
        this.context = context;
        this.onClickListener = mOnClickDetail;
    }

    public void setNewList(List<LedTextRealm> lsLedTextRealm) {
        this.lsLedTextRealm.clear();
        this.lsLedTextRealm.addAll(lsLedTextRealm);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderConfiguration onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_configuration, parent, false);

        return new ViewHolderConfiguration(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConfiguration holder, int position){
        final LedTextRealm ledTextRealm = lsLedTextRealm.get(position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = format.format(ledTextRealm.getCreated_at());

        holder.tvName.setText(ledTextRealm.getText());
        holder.tvCreatedAt.setText(dateString);

        holder.tvName.setTextColor(ledTextRealm.getColorText());
        holder.tvCreatedAt.setTextColor(ledTextRealm.getColorText());
/*
        holder.cvConfiguration.setBackgroundColor(Integer.valueOf(Utilities.getColorFromInt(ledTextRealm.getColorBackgroundColor())));
*/

        String hexColor = String.format("#%06X", (0xFFFFFF & ledTextRealm.getColorBackgroundColor()));
        String hex = Integer.toHexString(ledTextRealm.getColorBackgroundColor());
        int intc = ledTextRealm.getColorBackgroundColor();
        holder.cvConfiguration.setBackgroundColor(ledTextRealm.getColorBackgroundColor());
        holder.cvConfiguration.setOnClickListener(v -> onClickListener.onClickConfiguration(ledTextRealm));
    }

    @Override
    public int getItemCount() {
        return lsLedTextRealm.size();
    }
}