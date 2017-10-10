package com.tarija.tresdos.safetarija.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarija.tresdos.safetarija.ContactListActivity;
import com.tarija.tresdos.safetarija.ContactosMainActivity;
import com.tarija.tresdos.safetarija.FiltroActivity;
import com.tarija.tresdos.safetarija.ProgramerActivity;
import com.tarija.tresdos.safetarija.R;
import com.tarija.tresdos.safetarija.RegistroActivity;
import com.tarija.tresdos.safetarija.TarijaCapitalActivity;
import com.tarija.tresdos.safetarija.UbicarActivity;
import com.tarija.tresdos.safetarija.other.ItemMenu;
import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemMenu> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
    public ItemAdapter(Context mContext, List<ItemMenu> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ItemMenu album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs());
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccionMenu(position);
//                Toast.makeText(mContext, "Posicion "+position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccionMenu(position);
//                Toast.makeText(mContext, "Posicion "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_context, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
    public void AccionMenu(int index){
        switch (index){
            case 0:
                Intent i = new Intent(mContext, RegistroActivity.class);
                mContext.startActivity(i);
                break;
            case 1:
                Intent a = new Intent(mContext, UbicarActivity.class);
                mContext.startActivity(a);
                break;
            case 2:
                Intent b = new Intent(mContext, FiltroActivity.class);
                mContext.startActivity(b);
                break;
            case 3:
                Intent c = new Intent(mContext, ContactListActivity.class);
                mContext.startActivity(c);
                break;
            case 4:
                Intent d = new Intent(mContext, TarijaCapitalActivity.class);
                mContext.startActivity(d);
                break;
            case 5:
                Intent e = new Intent(mContext, ProgramerActivity.class);
                mContext.startActivity(e);
                break;
        }
    }
}
