package de.andreasschrade.androidtemplate.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.andreasschrade.androidtemplate.R;

/**
 * Just dummy content. Nothing special.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class BidderContent {

    /**
     * An array of sample items.
     */
    public static final List<BidderItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, BidderItem> ITEM_MAP = new HashMap<>(5);

    static {
        //addItem(new BidderItem("1", R.drawable.dummypic, "Sarah Conway", "Let's go for a drink?", "Let's go for a drink?"));

    }



    public static void addItem(BidderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void removeItem(int index) {

        String str = Integer.toString(index);

        ITEMS.remove(index);
        ITEM_MAP.remove(str);
    }

    public static int count() {

        return ITEMS.size();

    }

    public static void clear() {
        ITEMS.clear();
        ITEM_MAP.clear();


    }

    public static class BidderItem {
        public final String id;
        public final String photoId;
        public final String name;
        public final String offer;
        public final String bio;
        public final String objectId;
        public final String deviceId;

        public BidderItem(String id, String photoId, String name, String offer, String bio, String objectId, String deviceId) {
            this.id = id;
            this.photoId = photoId;
            this.name = name;
            this.offer = offer;
            this.bio = bio;
            this.objectId = objectId;
            this.deviceId = deviceId;
        }
    }
}
