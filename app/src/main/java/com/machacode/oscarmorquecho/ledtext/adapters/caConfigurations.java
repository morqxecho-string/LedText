package com.machacode.oscarmorquecho.ledtext.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.machacode.oscarmorquecho.ledtext.R;
import com.machacode.oscarmorquecho.ledtext.interfaces.onClickActions;
import com.machacode.oscarmorquecho.ledtext.models.LedTextRealm;
import com.machacode.oscarmorquecho.ledtext.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class caConfigurations extends RecyclerView.Adapter<caConfigurations.ViewHolderConfiguration> {
    //region Variables
    private Context context;
    private List<LedTextRealm> lsLedTextRealm = new ArrayList<>();
    private onClickActions onClickListener;
    //endregion

    //region ViewHolder
    class ViewHolderConfiguration extends RecyclerView.ViewHolder {
        private MaterialTextView tvName;
        private MaterialTextView tvCreatedAt;
        private MaterialCardView cvConfiguration;

        ViewHolderConfiguration(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvCreatedAt = view.findViewById(R.id.tvCreatedAt);
            cvConfiguration = view.findViewById(R.id.cvConfiguration);
        }

        void setData(LedTextRealm ledTextRealm) {
            if (ledTextRealm != null) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = format.format(ledTextRealm.getCreated_at());

                tvName.setText(ledTextRealm.getText());
                tvCreatedAt.setText(dateString);

                tvName.setTextColor(ledTextRealm.getColorText());
                tvCreatedAt.setTextColor(ledTextRealm.getColorText());

                /*
                holder.cvConfiguration.setBackgroundColor(Integer.valueOf(Utilities.getColorFromInt(ledTextRealm.getColorBackgroundColor())));
                */
                String hexColor = String.format("#%06X", (0xFFFFFF & ledTextRealm.getColorBackgroundColor()));
                String hex = Integer.toHexString(ledTextRealm.getColorBackgroundColor());
                int intc = ledTextRealm.getColorBackgroundColor();

                cvConfiguration.setBackgroundColor(ledTextRealm.getColorBackgroundColor());
                cvConfiguration.setOnClickListener(v -> onClickListener.onClickConfiguration(ledTextRealm));
                cvConfiguration.setOnLongClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(context, cvConfiguration);
                    ((Activity) context).getMenuInflater().inflate(R.menu.menu_delete, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(item -> {
                        Realm realm = Utilities.createRealmInstance(context);
                        LedTextRealm ledTextRealmToDelete = realm.where(LedTextRealm.class).equalTo("id", ledTextRealm.getId()).findFirst();
                        if (ledTextRealmToDelete != null) {
                            realm.executeTransaction(realm1 -> ledTextRealmToDelete.deleteFromRealm());
                            if (lsLedTextRealm.get(getAdapterPosition()) != null)
                                lsLedTextRealm.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                        return false;
                    });
                    popupMenu.show();
                    return true;
                });
            }
        }
    }
    //endregion

    //region Constructor
    public caConfigurations(Context context, onClickActions mOnClickDetail) {
        this.context = context;
        this.onClickListener = mOnClickDetail;
    }
    //endregion

    //region Methods
    public void setNewList(List<LedTextRealm> lsLedTextRealm) {
        this.lsLedTextRealm.clear();
        this.lsLedTextRealm.addAll(lsLedTextRealm);
        notifyDataSetChanged();
    }
    //endregion

    //region Events
    @NonNull
    @Override
    public ViewHolderConfiguration onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_configuration, parent, false);
        return new ViewHolderConfiguration(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConfiguration holder, int position){
        final LedTextRealm ledTextRealm = lsLedTextRealm.get(position);
        holder.setData(ledTextRealm);
    }

    @Override
    public int getItemCount() {
        return lsLedTextRealm.size();
    }
    //endregion
}