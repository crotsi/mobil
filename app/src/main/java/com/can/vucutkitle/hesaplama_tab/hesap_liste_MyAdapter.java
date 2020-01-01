package com.can.vucutkitle.hesaplama_tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.can.vucutkitle.R;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hp on 3/18/2016.
 */
public class hesap_liste_MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private final Context mContext;
    private final List<Object> mRecyclerViewItems;

    ItemClickListener listener;

    public hesap_liste_MyAdapter(Context context, List<Object> recyclerViewItems, ItemClickListener listener) {
        this.mContext = context;
        this.mRecyclerViewItems = recyclerViewItems;
        this.listener = listener;

    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView kilo,boy,ideal_kilo,tarih;



        ItemClickListener itemClickListener;


        MenuItemViewHolder(View view) {
            super(view);

            tarih = (TextView) view.findViewById(R.id.tarih);
            kilo= (TextView) view.findViewById(R.id.kilo);
            boy = (TextView) view.findViewById(R.id.boy);
            ideal_kilo = (TextView) view.findViewById(R.id.ideal_kilo);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }


        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;

        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        return MENU_ITEM_VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.hesaplama_kayit_item_container, viewGroup, false);

        return new MenuItemViewHolder(menuItemLayoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        hesap_liste_MenuItemRv menuItem;
        MenuItemViewHolder menuItemHolder;
        menuItemHolder = (MenuItemViewHolder) holder;
        menuItem = (hesap_liste_MenuItemRv) mRecyclerViewItems.get(position);
        menuItemHolder.kilo.setText(menuItem.get_kilo());
        menuItemHolder.boy.setText(menuItem.get_boy());
        menuItemHolder.ideal_kilo.setText(menuItem.get_ideal_kilo());
        menuItemHolder.tarih.setText(menuItem.get_tarih());
        menuItemHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                //Snackbar.make(v,String.valueOf(players.get(pos).getId()), Snackbar.LENGTH_SHORT).show();
                try {
                    //listener.onItemClick(v, idsi);
                } catch (Exception e) {
                }
                //Toast.makeText(this.getApplicationContext(), "bak", Toast.LENGTH_LONG).show();
            }


        });
    }

    public interface ItemClickListener {
        void onItemClick(View v, int pos);
    }
}
