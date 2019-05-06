package com.medisensehealth.fdccontributor.activities.mypatientOphthalmology;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.medisensehealth.fdccontributor.R;
import com.medisensehealth.fdccontributor.activities.settings.ChangePasswordActivity;
import com.medisensehealth.fdccontributor.utils.HCConstants;
import com.medisensehealth.fdccontributor.utils.ShareadPreferenceClass;
import com.medisensehealth.fdccontributor.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SALMA on 24-12-2018.
 */
public class EditFundusImageActivity extends AppCompatActivity {
    ShareadPreferenceClass shareadPreferenceClass;
    SharedPreferences sharedPreferences;
    int USER_ID;
    String USER_NAME, USER_LOGIN_TYPE, SELECTED_IMAGE_URL;

    ImageView imageView;
    Bitmap myBitmap;
    private RelativeLayout rl_Main;
    Button updateButton;
    boolean    isSaved = false;

    private List<PointerCircles> pointerList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundus_image_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if( bundle != null) {
            String title = bundle.getString("title");
            SELECTED_IMAGE_URL  = bundle.getString("IMAGE_URL");
            setTitle(title);

            Log.d(Utils.TAG, " ************ EditFundusImageActivity *********** ");
        }

        shareadPreferenceClass = new ShareadPreferenceClass(EditFundusImageActivity.this);
        sharedPreferences = shareadPreferenceClass.getSharedPreferences(EditFundusImageActivity.this);

        if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("1"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_DOC_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_DOC_REFID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("2"))) {
            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_PART_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_PART_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }
        else  if((sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0").equals("3"))) {

            USER_NAME =  sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_MARKET_NAME, "NAME");
            USER_ID = sharedPreferences.getInt(HCConstants.PREF_LOGINACTIVITY_MARKET_ID, 0);
            USER_LOGIN_TYPE = sharedPreferences.getString(HCConstants.PREF_LOGINACTIVITY_LOGINTYPE,"0");

            Utils.USER_LOGIN_TYPE = USER_LOGIN_TYPE;
            Utils.USER_LOGIN_ID = USER_ID;
            Utils.USER_LOGIN_NAME = USER_NAME;
        }

        Log.d(Utils.TAG+ "USER_ID: ", String.valueOf(USER_ID));
        Log.d(Utils.TAG+ "LOG_TYPE: ", USER_LOGIN_TYPE);
        Log.d(Utils.TAG+ "IMAGE_URL: ", SELECTED_IMAGE_URL);

        initializationViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    private void initializationViews() {
        pointerList = new ArrayList<>();
       imageView = (ImageView) findViewById(R.id.fundus_image_view);

        /* File imgFile = new File(SELECTED_IMAGE_URL);

        if(imgFile.exists()){

            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);

        }*/

        rl_Main = (RelativeLayout) findViewById(R.id.rl_main);
        rl_Main.addView(new MyView(this));

        updateButton = (Button) findViewById(R.id.fundus_image_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveImageTask().execute(null, null, null);
            }
        });
    }

    public class SaveImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) { //Running in background
            View content = rl_Main;
            content.setDrawingCacheEnabled(true);
            content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmap = content.getDrawingCache();
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "CameraDemo");

            if (!mediaStorageDir.exists()){
                if (!mediaStorageDir.mkdirs()){
                    Log.d("CameraDemo", "failed to create directory");
                    return null;
                }
            }
            File file = new File(SELECTED_IMAGE_URL);
            FileOutputStream ostream;
            try {
                file.createNewFile();
                ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                ostream.flush();
                ostream.close();
                isSaved = true;
            } catch (Exception e) {
                e.printStackTrace();
                isSaved = false;
            }
            return null;
        }

        @Override
        protected  void onPreExecute() { //Activity is on progress
            //displayToast("Your image is saving...");
        }

        @Override
        protected void onPostExecute(Void v) { //Activity is done...
            if (isSaved == true) {
                Log.d(Utils.TAG ,"Image was saved.");
                finish();
            }
            if (isSaved == false) {
                Log.d(Utils.TAG ,"Unable to save image. Try again later.");
            }
        }
    }


    class MyView extends View{


        Paint paint = new Paint();
        Point point = new Point();
        public MyView(Context context) {
            super(context);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(20);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
           /* Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.images);
            canvas.drawBitmap(b, 0, 0, paint);
            canvas.drawCircle(point.x, point.y, 100, paint);*/

            File imgFile = new File(SELECTED_IMAGE_URL);

            if(imgFile.exists()){

                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               // canvas.drawBitmap(myBitmap, 0, 0, paint);

                // canvas.drawCircle(point.x, point.y, 10, paint);  old

                int padding = 50;
                Rect rectangle = new Rect(padding,padding, canvas.getWidth()- padding ,canvas.getHeight()- padding);
                canvas.drawBitmap(myBitmap, null, rectangle, null);

                for(int i=0;i<pointerList.size(); i++) {
                    canvas.drawCircle(pointerList.get(i).pointX, pointerList.get(i).pointY, 5, paint);
                }

            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    point.x = event.getX();
                    point.y = event.getY();

                    pointerList.add(new PointerCircles(point.x, point.y));

            }
            invalidate();
            return true;

        }

    }
    class Point {
        float x, y;
    }

    private class PointerCircles {
        private float pointX, pointY;

        public PointerCircles(float x, float y) {
            this.pointX = x;
            this.pointY = y;
        }

        public float getPointX() { return pointX; }
        public void setPointX(float pointX) {  this.pointX = pointX; }

        public float getPointY() { return pointY; }
        public void setPointY(float pointY) {  this.pointY = pointY; }
    }
}
