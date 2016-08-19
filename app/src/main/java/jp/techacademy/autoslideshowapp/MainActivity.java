package jp.techacademy.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Timer;
import java.util.TimerTask;

import static android.provider.MediaStore.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    public static int countImageNum = 0;

    Button nextButton;
    Button backButton;
    Button startButton;
    Handler handler;
    Timer timer;
    Cursor cursor;

    public void onClick(View v) {

        if (v != null) {
            if (v == this.nextButton) {
                getContentsInfo();
                this.cursor.moveToNext();

                setButtonState(true,true,false);

                if (this.cursor.moveToNext()) {


                    if (countImageNum == 0) {
                        Log.d("ee", "aa");
                        int fieldIndex = cursor.getColumnIndex(Images.Media._ID);
                        Long id = cursor.getLong(fieldIndex);
                        id++;
                        countImageNum++;

                        Log.d("id", "id:" + id);

                        Uri imageUri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id);

                        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);

                        imageVIew.setImageURI(imageUri);



                    } else if (v == startButton){


                    }
                }


            } else if (v == backButton) {

                int fieldIndex = cursor.getColumnIndex(Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                countImageNum--;
                id += countImageNum;

                Uri imageUri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                imageVIew.setImageURI(imageUri);

                setButtonState(true, false, true);

            }
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.nextButton = (Button) findViewById(R.id.nextButton);
        this.nextButton.setOnClickListener(this);

        this.backButton = (Button) findViewById(R.id.backButton);
        this.backButton.setOnClickListener(this);

        this.startButton = (Button) findViewById(R.id.startButton);
        this.startButton.setOnClickListener(this);

        setButtonState(true, true, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo();

            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }


        } else


            getContentsInfo();


    }


    public void setButtonState(boolean next, boolean start, boolean back) {
        nextButton.setEnabled(next);
        startButton.setEnabled(start);
        backButton.setEnabled(back);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                }
                break;
            default:
                break;
        }
    }

    private void getContentsInfo() {

        ContentResolver resolver = this.getContentResolver();
        this.cursor = resolver.query(
                Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );


        if (cursor.moveToFirst()) {

            int fieldIndex = cursor.getColumnIndex(Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);

            Uri imageUri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id);
            Log.d("ANDROID", "URI : " + imageUri.toString());

            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
        }
            cursor.close();

    }
}




