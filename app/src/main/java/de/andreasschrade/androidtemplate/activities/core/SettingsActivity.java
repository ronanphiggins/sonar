package de.andreasschrade.androidtemplate.activities.core;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;
import de.andreasschrade.androidtemplate.activities.peripheral.HomeActivity;
import de.andreasschrade.androidtemplate.utilities.CustomDialogClass;
import de.andreasschrade.androidtemplate.utilities.bitmapUtil;

/**
 * This Activity provides several settings. Activity contains {@link PreferenceFragment} as inner class.
 *
 * Created by Andreas Schrade on 14.12.2015.
 */
public class SettingsActivity extends BaseActivity {

    private final int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();

        Button button = (Button) findViewById(R.id.changepicture);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("info", "change pic clicked");

                Crop.pickImage(SettingsActivity.this);

                /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);*/
            }
        });
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_settings;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_prefs);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(imageReturnedIntent.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, imageReturnedIntent);
        }

        /*switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Log.i("info", "image select success");



                        //Crop.of(selectedImage, output).asSquare().start(SettingsActivity.this);



                        /*int h = 200; // height in pixels
                        int w = 200; // width in pixels
                        Bitmap scaled = Bitmap.createScaledBitmap(selectedImage, h, w, true);

                        String currentId = Backendless.UserService.CurrentUser().getObjectId();
                        String[] parts = currentId.split("-");
                        final String part = parts[4];
                        String imageLoad = part + ".png";

                        Boolean overwrite = true;



                        Bitmap scaled = bitmapUtil.BITMAP_RESIZER(selectedImage, 640, 960);


                        Backendless.Files.Android.upload(scaled,
                                Bitmap.CompressFormat.PNG,
                                100,
                                imageLoad,
                                "media",
                                overwrite,
                                new AsyncCallback<BackendlessFile>() {
                                    @Override
                                    public void handleResponse(final BackendlessFile backendlessFile) {

                                        Log.i("info", "upload success");

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Glide.get(SettingsActivity.this).clearDiskCache();

                                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                                //finish();


                                            }
                                        }).start();


                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                        Log.i("info", "upload failed");

                                    }
                                });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }*/
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            final CustomDialogClass cdd = new CustomDialogClass(SettingsActivity.this);
            cdd.progressDialog("Changing profile picture..");

            Log.i("info", "crop worked");

            final Uri imageUri = Crop.getOutput(result);
            final InputStream imageStream;
            try {

                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Log.i("info", "image select success");

                String currentId = UserIdStorageFactory.instance().getStorage().get();
                String[] parts = currentId.split("-");
                final String part = parts[4];
                String imageLoad = part + ".png";

                Boolean overwrite = true;



                Bitmap scaled = bitmapUtil.BITMAP_RESIZER(selectedImage, 900, 900);


                Backendless.Files.Android.upload(scaled,
                        Bitmap.CompressFormat.PNG,
                        100,
                        imageLoad,
                        "media",
                        overwrite,
                        new AsyncCallback<BackendlessFile>() {
                            @Override
                            public void handleResponse(final BackendlessFile backendlessFile) {

                                Log.i("info", "upload success");

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Glide.get(SettingsActivity.this).clearDiskCache();

                                        cdd.checkDialog();

                                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                        //finish();


                                    }
                                }).start();


                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {

                                Log.i("info", "upload failed");

                            }
                        });




            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }




            //resultView.setImageURI(Crop.getOutput(result));


        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
