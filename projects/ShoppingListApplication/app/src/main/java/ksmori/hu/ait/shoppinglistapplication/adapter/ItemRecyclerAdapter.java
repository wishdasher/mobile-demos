package ksmori.hu.ait.shoppinglistapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ksmori.hu.ait.shoppinglistapplication.MainActivity;
import ksmori.hu.ait.shoppinglistapplication.R;
import ksmori.hu.ait.shoppinglistapplication.data.Item;

public class ItemRecyclerAdapter
        extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {
    private List<Item> shoppingList;
    private Context context;
    private Realm realmItem;

    public ItemRecyclerAdapter(Context context, Realm realmItem) {
        this.context = context;
        this.realmItem = realmItem;

        shoppingList = new ArrayList<Item>();
        showPurchased();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(shoppingList.get(position).getName());
        holder.cbPurchased.setChecked(shoppingList.get(position).isPurchased());
        holder.tvPrice.setText("$ " + shoppingList.get(position).getPrice());

        Item.Category category = Item.Category.getCategoryFromIndex(
                shoppingList.get(position).getCategory()
        );
        holder.ivIcon.setImageResource(findImageSource(category));

        final Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.delete_anim);

        holder.cbPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItem.beginTransaction();
                shoppingList.get(holder.getAdapterPosition()).setPurchased(holder.cbPurchased.isChecked());
                realmItem.commitTransaction();
                if (holder.cbPurchased.isChecked() && !((MainActivity) context).isToggleOn()) {
                    removeItem(holder.getAdapterPosition());
                }
            }
        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).viewItemInformation(shoppingList.get(holder.getAdapterPosition()));
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
                        shoppingList.get(holder.getAdapterPosition()).getItemID()
                );
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.startAnimation(animation);
                deleteItem(holder.getAdapterPosition());

            }
        });
    }

    private int findImageSource(Item.Category category) {
        switch (category) {
            case BEVERAGE:
                return R.drawable.beverage;
            case BREAD:
                return R.drawable.bread;
            case CANNED_GOOD:
                return R.drawable.canned;
            case DAIRY:
                return R.drawable.dairy;
            case DRIED_GOOD:
                return R.drawable.dried;
            case MEAT:
                return R.drawable.meat;
            case PRODUCE:
                return R.drawable.produce;
            case SNACK:
                return R.drawable.snack;
            case BOOK:
                return R.drawable.book;
            case CLOTHING:
                return R.drawable.clothing;
            case ELECTRONIC:
                return R.drawable.electronic;
            case HOUSEHOLD:
                return R.drawable.household;
            case TOILETRY:
                return R.drawable.toiletry;
            case OTHER:
            default:
                return R.drawable.other;
        }
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public void addItem(String itemID) {
        Item item = realmItem.where(Item.class).equalTo("itemID", itemID).findFirst();

        shoppingList.add(0, item);
        notifyItemInserted(0);
    }

    public void updateItem(String itemID, int positionToEdit) {
        Item item = realmItem.where(Item.class).equalTo("itemID", itemID).findFirst();

        shoppingList.set(positionToEdit, item);
        notifyItemChanged(positionToEdit);
    }

    private void removeItem(int position) {
        shoppingList.remove(position);
        notifyItemRemoved(position);
    }

    private void deleteItem(int position) {
        realmItem.beginTransaction();
        shoppingList.get(position).deleteFromRealm();
        realmItem.commitTransaction();

        shoppingList.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteAllItems() {
        realmItem.beginTransaction();
        for (Item item : shoppingList) {
            item.deleteFromRealm();
        }
        realmItem.commitTransaction();
        shoppingList.clear();
        notifyDataSetChanged();
    }

    public void showPurchased() {
        shoppingList.clear();
        RealmResults<Item> itemResult = realmItem.where(Item.class).findAll();

        for (int i = itemResult.size() - 1; i >= 0; i--) {
            shoppingList.add(itemResult.get(i));
        }
        notifyDataSetChanged();
    }

    public void hidePurchased() {
        shoppingList.clear();
        RealmResults<Item> itemResult = realmItem.where(Item.class)
                .equalTo("purchased", false).findAll();

        for (int i = itemResult.size() - 1; i >= 0; i--) {
            shoppingList.add(itemResult.get(i));
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvPrice;
        private CheckBox cbPurchased;

        private Button btnView;
        private Button btnEdit;
        private Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            ivIcon = (ImageView) itemView.findViewById(R.id.icon);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            cbPurchased = (CheckBox) itemView.findViewById(R.id.cbPurchased);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            btnView = (Button) itemView.findViewById(R.id.btnView);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
