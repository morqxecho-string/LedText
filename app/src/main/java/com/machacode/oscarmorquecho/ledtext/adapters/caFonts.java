package com.machacode.oscarmorquecho.ledtext.adapters;

import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.machacode.oscarmorquecho.ledtext.R;
import com.machacode.oscarmorquecho.ledtext.interfaces.onClickActions;
import com.machacode.oscarmorquecho.ledtext.models.Font;

import java.util.ArrayList;
import java.util.List;

public class caFonts extends RecyclerView.Adapter<caFonts.ViewHolderPasos>{
    private List<Font> lsFonts = new ArrayList<>();
    private onClickActions onClickListener;

    class ViewHolderPasos extends RecyclerView.ViewHolder {
        private TextView tvInitialsFont;
        private TextView tvFullNameFont;
        private LinearLayout llFont;
        private ImageView ivFont;

        ViewHolderPasos(View view) {
            super(view);
            tvInitialsFont = view.findViewById(R.id.tvInitialsFont);
            tvFullNameFont = view.findViewById(R.id.tvFullNameFont);
            llFont = view.findViewById(R.id.llFont);
            ivFont = view.findViewById(R.id.ivFont);
        }
    }

    public caFonts(onClickActions mOnClickDetail) {
        this.onClickListener = mOnClickDetail;
    }

    public void setNewList(List<Font> lsFonts) {
        this.lsFonts.clear();
        this.lsFonts.addAll(lsFonts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPasos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fonts, parent, false);

        return new ViewHolderPasos(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPasos holder, int position){
        final Font font = lsFonts.get(position);

        holder.ivFont.setImageDrawable(TextDrawable.builder().buildRect(font.getFontName().substring(0, 1), Color.RED));
        //holder.tvInitialsFont.setText(font.substring(0,1));
        holder.tvFullNameFont.setText(font.getFontName());
        holder.llFont.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onClickListener.onClick(font.getFontPath(), font.getTYPE_FONT());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lsFonts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemCount();
    }
}