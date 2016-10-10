package de.andreasschrade.androidtemplate.activities.peripheral;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.backendless.Bid;
import de.andreasschrade.androidtemplate.backendless.Tender;
import de.andreasschrade.androidtemplate.utilities.SwipeDismissListViewTouchListener;
import de.andreasschrade.androidtemplate.utilities.Wrapper;
import de.andreasschrade.androidtemplate.wrapper.BidderContent;

/**
 * Shows a list of all available quotes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class BidListFragment extends ListFragment {

    private Callback callback = dummyCallback;

    private MyListAdapter myList;

    //private View myV;

    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myList = new MyListAdapter();
        setListAdapter(myList);
        setHasOptionsMenu(true);





    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView listView = getListView(); //EX:
        listView.setTextFilterEnabled(true);
        registerForContextMenu(listView);
        super.onActivityCreated(savedInstanceState);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    Log.i("info", "swiped");

                                    BidderContent.BidderItem objIdobject = BidderContent.ITEMS.get(position);

                                    String objId = objIdobject.objectId;

                                    Log.i("info", objId);



                                    Bid bid = new Bid();
                                    bid.setObjectId(objId);

                                    Backendless.Persistence.of(Bid.class).remove(bid,
                                            new AsyncCallback<Long>() {
                                                public void handleResponse(Long response) {
                                                    Log.i("info", "delete success");

                                                }

                                                public void handleFault(BackendlessFault fault) {

                                                    Log.i("info", "delete failed" + fault);

                                                }
                                            });

                                    BidderContent.removeItem(position);
                                }
                                myList.notifyDataSetChanged();

                            }
                        });

        listView.setOnTouchListener(touchListener);



    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // notify callback about the selected list item
        callback.onItemSelected(BidderContent.ITEMS.get(position).id);

        //BidderContent.removeItem(position);

        //BidderContent.addItem(new BidderContent.BidderItem("3", "1234", "JAck", "Jack", "Jack"));

        //myList.notifyDataSetChanged();

    }








    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /**
     * Deprecated on API 23 but still necessary for pre API 23 devices.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }

        callback = (Callback) context;
    }

    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return BidderContent.ITEMS.size();
        }

        @Override
        public Object getItem(int position) {
            return BidderContent.ITEMS.get(position);
        }

        @Override
        public long getItemId(int position) {
            return BidderContent.ITEMS.get(position).id.hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article, container, false);





                //myV = convertView;


            }



            final BidderContent.BidderItem item = (BidderContent.BidderItem) getItem(position);
            ((TextView) convertView.findViewById(R.id.article_title)).setText(item.name);
            ((TextView) convertView.findViewById(R.id.article_subtitle)).setText(item.offer);
            final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
            String url = "https://api.backendless.com/A0819152-C875-C222-FF18-0516AB9ACC00/v1/files/media/" + item.photoId + ".png";
            Glide.with(getActivity()).load(url).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });

            return convertView;
        }
    }

    public BidListFragment() {
    }
}
