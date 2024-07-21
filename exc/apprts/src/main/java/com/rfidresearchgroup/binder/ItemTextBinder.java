package com.rfidresearchgroup.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rfidresearchgroup.holder.ItemCommonViewHolder;
import com.rfidresearchgroup.javabean.ItemTextBean;
import com.rfidresearchgroup.rfidtools.R;

public class ItemTextBinder extends ItemCommonBinder<ItemTextBean, ItemTextBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_content_text, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ItemTextBean item) {
        super.onBindViewHolder(holder, item);
        holder.txtMessage.setText(item.getMessage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.onClick(v, holder.getAdapterPosition());
            }
        });
    }

    static class ViewHolder extends ItemCommonViewHolder {
        TextView txtMessage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}
