package ksmori.hu.ait.shoppinglistapplication.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import ksmori.hu.ait.shoppinglistapplication.MainApplication;
import ksmori.hu.ait.shoppinglistapplication.R;

public class Item extends RealmObject {

    public enum Category {
        BEVERAGE(MainApplication.getContext().getResources().getString(R.string.beverage), 0),
        BREAD(MainApplication.getContext().getResources().getString(R.string.bread), 1),
        CANNED_GOOD(MainApplication.getContext().getResources().getString(R.string.canned_good), 2),
        DAIRY(MainApplication.getContext().getResources().getString(R.string.dairy), 3),
        DRIED_GOOD(MainApplication.getContext().getResources().getString(R.string.dried_good), 4),
        MEAT(MainApplication.getContext().getResources().getString(R.string.meat), 5),
        PRODUCE(MainApplication.getContext().getResources().getString(R.string.produce), 6),
        SNACK(MainApplication.getContext().getResources().getString(R.string.snack), 7),
        BOOK(MainApplication.getContext().getResources().getString(R.string.book), 8),
        CLOTHING(MainApplication.getContext().getResources().getString(R.string.clothing), 9),
        ELECTRONIC(MainApplication.getContext().getResources().getString(R.string.electronic), 10),
        HOUSEHOLD(MainApplication.getContext().getResources().getString(R.string.household), 11),
        TOILETRY(MainApplication.getContext().getResources().getString(R.string.toiletry), 12),
        OTHER(MainApplication.getContext().getResources().getString(R.string.other), 13);

        private String category;
        private int index;

        Category(String aCategory, int anIndex) {
            category = aCategory;
            index = anIndex;
        }

        @Override
        public String toString() {
            return category;
        }

        public int getIndex() {
            return index;
        }

        public static Category getCategoryFromIndex(int index) {
            for (Category cat : Category.values()) {
                if (index == cat.getIndex()) return cat;
            }
            return Category.OTHER;
        }

        public static int getIndexFromCategory(Category cat) {
            return cat.getIndex();
        }
    }

    @PrimaryKey
    private String itemID;

    private int category;
    private String name;
    private String description;
    private double price;
    private boolean purchased;

    public Item() {};

    public Item(int category, String name, String description, int price, boolean purchased) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.purchased = purchased;

    }

    public String getItemID() {
        return itemID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}

