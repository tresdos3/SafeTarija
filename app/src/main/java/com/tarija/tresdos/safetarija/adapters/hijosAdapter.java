package com.tarija.tresdos.safetarija.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tarija.tresdos.safetarija.R;
import com.tarija.tresdos.safetarija.RastreoActivity;
import com.tarija.tresdos.safetarija.other.ItemMenu;
import com.tarija.tresdos.safetarija.other.itemHijo;

import java.util.List;

public class hijosAdapter extends RecyclerView.Adapter<hijosAdapter.MyViewHolder> {

    private Context mContext;
    private List<itemHijo> Lista_Hijos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_c, state_c, key_c;
        public ImageView image_c, overflow_c;

        public MyViewHolder(View view) {
            super(view);
            name_c = (TextView) view.findViewById(R.id.title_h);
            state_c = (TextView) view.findViewById(R.id.sub_title_h);
            key_c = (TextView) view.findViewById(R.id.uid_key);
            image_c = (ImageView) view.findViewById(R.id.image_h);
            overflow_c = (ImageView) view.findViewById(R.id.overflow_h);
        }
    }
    public hijosAdapter(Context mContext, List<itemHijo> lista_hijos) {
        this.mContext = mContext;
        this.Lista_Hijos = lista_hijos;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hijos_card, parent, false);
        return new hijosAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.key_c.setVisibility(View.GONE);
        itemHijo hijo = Lista_Hijos.get(position);
        holder.name_c.setText(hijo.getName());
        holder.state_c.setText(hijo.getActivo());
        holder.key_c.setText(hijo.getKey());
        Glide.with(mContext).load(hijo.getImage()).into(holder.image_c);
        holder.image_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RastreoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Key_Hijo", holder.key_c.getText().toString());
                mContext.startActivity(intent);
            }
        });
        holder.name_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RastreoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Key_Hijo", holder.key_c.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Lista_Hijos.size();
    }
}
