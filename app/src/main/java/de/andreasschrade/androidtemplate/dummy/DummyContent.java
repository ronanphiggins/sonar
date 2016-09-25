package de.andreasschrade.androidtemplate.dummy;

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
public class DummyContent {

    /**
     * An array of sample items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample items. Key: sample ID; Value: Item.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>(5);

    static {
        addItem(new DummyItem("1", R.drawable.p1, "Quote #1", "Steve Jobs", "Focusing is about saying No."));
        addItem(new DummyItem("2", R.drawable.p2, "Quote #2", "Napoleon Hill","A quitter never wins and a winner never quits."));
        addItem(new DummyItem("3", R.drawable.p3, "Quote #3", "Pablo Picaso", "Action is the foundational key to all success."));
        addItem(new DummyItem("4", R.drawable.p4, "Quote #4", "Napoleon Hill","Our only limitations are those we set up in our own minds."));
        addItem(new DummyItem("5", R.drawable.p5, "Quote #5", "Steve Jobs","Deciding what not do do is as important as deciding what to do."));
        addItem(new DummyItem("6", R.drawable.p1, "Quote #1", "Steve Jobs", "Focusing is about saying No."));
        addItem(new DummyItem("7", R.drawable.p2, "Quote #2", "Napoleon Hill","A quitter never wins and a winner never quits."));
        addItem(new DummyItem("8", R.drawable.p3, "Quote #3", "Pablo Picaso", "Action is the foundational key to all success."));
        addItem(new DummyItem("9", R.drawable.p4, "Quote #4", "Napoleon Hill","Our only limitations are those we set up in our own minds."));
        addItem(new DummyItem("10", R.drawable.p5, "Quote #5", "Steve Jobs","Deciding what not do do is as important as deciding what to do."));
        addItem(new DummyItem("11", R.drawable.p1, "Quote #1", "Steve Jobs", "Focusing is about saying No."));
        addItem(new DummyItem("12", R.drawable.p2, "Quote #2", "Napoleon Hill","A quitter never wins and a winner never quits."));
        addItem(new DummyItem("13", R.drawable.p3, "Quote #3", "Pablo Picaso", "Action is the foundational key to all success."));
        addItem(new DummyItem("14", R.drawable.p4, "Quote #4", "Napoleon Hill","Our only limitations are those we set up in our own minds."));
        addItem(new DummyItem("15", R.drawable.p5, "Quote #5", "Steve Jobs","Deciding what not do do is as important as deciding what to do."));
        addItem(new DummyItem("16", R.drawable.p1, "Quote #1", "Steve Jobs", "Focusing is about saying No."));
        addItem(new DummyItem("17", R.drawable.p2, "Quote #2", "Napoleon Hill","A quitter never wins and a winner never quits."));
        addItem(new DummyItem("18", R.drawable.p3, "Quote #3", "Pablo Picaso", "Action is the foundational key to all success."));
        addItem(new DummyItem("19", R.drawable.p4, "Quote #4", "Napoleon Hill","Our only limitations are those we set up in our own minds."));
        addItem(new DummyItem("20", R.drawable.p5, "Quote #5", "Steve Jobs","Deciding what not do do is as important as deciding what to do."));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class DummyItem {
        public final String id;
        public final int photoId;
        public final String title;
        public final String author;
        public final String content;

        public DummyItem(String id, int photoId, String title, String author, String content) {
            this.id = id;
            this.photoId = photoId;
            this.title = title;
            this.author = author;
            this.content = content;
        }
    }
}
