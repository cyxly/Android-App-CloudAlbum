package com.room2201.camerafileupload;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class WatchVideo extends Activity {
    /** Called when the activity is first created. */

    Button playButton ;
    VideoView videoView ;
    EditText rtspUrl ;
    RadioButton radioStream;
    RadioButton radioFile;
    String inform;
    private String userName = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_watch_video);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        InputStream inputStream = null;

        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
//        String urlStr = "http://i.cs.hku.hk/~yxchen/AndroidFileUpload/yxchen/VideoUploaded";
        String urlStr = Config.FILE_UPLOAD_DIR + userName + "/VideoUploaded";

        // 取得inputStream，并进行读取
        try {
            inputStream = getResultForRequest(urlStr);
            if(inputStream == null){
                Intent i = new Intent(WatchVideo.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        } catch (IOException e) {
            inform = "You haven't uploaded any videos!";
            Intent i = new Intent(WatchVideo.this, MainActivity.class);
            i.putExtra("inform", inform);
            i.putExtra("username", userName);
            startActivity(i);

            finish();
            e.printStackTrace();
        }
//        getNewsInfo();
//        getServerVersion();
//        Toast.makeText(WatchVideo.this, getServerVersion(), Toast.LENGTH_SHORT).show();

//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(60 * 1000);
//            conn.setReadTimeout(60 * 1000);
//            // 取得inputStream，并进行读取
//          inputStream = conn.getInputStream();
//           System.out.print(inputStream);
//        } catch (IOException e) {
//           System.out.print(inputStream);
//            Intent i = new Intent(WatchVideo.this, MainActivity.class);
//            startActivity(i);
//            finish();
//            e.printStackTrace();
//        }

     /*   catch (IOException e) {
            Intent i = new Intent(WatchVideo.this, MainActivity.class);
            startActivity(i);
            finish();
            e.printStackTrace();
        }
*/

        LinearLayout totallinearLayout=new LinearLayout(this);
        totallinearLayout.setOrientation(LinearLayout.VERTICAL);
        Button button=new Button(this);
        button.setBackgroundColor(getResources().getColor(R.color.btn_bg));
        button.setText("Back To Home Page");
        button.setTextColor(getResources().getColor(R.color.white));
        button.setWidth(50);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WatchVideo.this, MainActivity.class);
                i.putExtra("username", userName);
                startActivity(i);
                finish();
            }
        });
        totallinearLayout.addView(button);
        ScrollView scrollView=new ScrollView(this);
        LinearLayout linearLayout=new LinearLayout(this);

        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams videolp = new LinearLayout.LayoutParams(width,600 );
        LinearLayout.LayoutParams empty = new LinearLayout.LayoutParams(width,100 );
        // LinearLayout.LayoutParams scrollparams = new LinearLayout.LayoutParams(width,100 );
        lp.setMargins(100, 30, 0, 0);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ArrayList<String> url=getString(inputStream);

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ImageView> image=new ArrayList<ImageView>();
        for(int i=0;i<url.size();i++){
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//获取网络视频
            retriever.setDataSource(url.get(i), new HashMap<String, String>());
//获取本地视频
//retriever.setDataSource(url);
            //   Bitmap bitmap2= ThumbnailUtils.createVideoThumbnail(this,, MediaStore.Video.Thumbnails.MINI_KIND);

            //Bitmap bitmap2=createVideoThumbnail("http://i.cs.hku.hk/~yxchen/AndroidFileUpload/Upload/VID_20161127_203946.mp4", 50, 30) ;
            Bitmap bitmap = retriever.getFrameAtTime();
            // bitmap.createBitmap(bitmap,200,30,30,10);

         /*   Rect rect = new Rect((int) distance, (int) endHeight,
                    (int) (distance + viewWidth), (int) startHeight);
            canvas.drawBitmap(bar_bitmap, null, rect, null);
        */

            //Log.i("a",bitmap2+"");
            ImageView temp=new ImageView(this);
            temp.setBackgroundColor(Color.BLACK);
            temp.setImageBitmap(bitmap);
            final String j=url.get(i);

            temp.setOnClickListener(new View.OnClickListener() {
                //int j=i;

                @Override
                public void onClick(View arg0) {
                    Uri uri = Uri.parse(j);
                    // 调用系统自带的播放器来播放流媒体视频
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Log.v("URI:::::::::", uri.toString());
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
                    //videoViews.get(i).start();
                    //  TextView textview = (TextView)findViewById(R.id.textView1);
                    //textview.setText("你点击了Button");
                }
            });
            image.add(temp);

            retriever.release();
        }

      /*  String path  = Environment.getExternalStorageDirectory().getPath();
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path+"/123.rmvb");

        Bitmap bitmap = media.getFrameAtTime();

        image = (ImageView)this.findViewById(R.id.imageView1);
        image.setImageBitmap(bitmap);
*/


        for(int i=0;i<image.size();i++){

            linearLayout.addView(image.get(i),videolp);
            LinearLayout linearLayout1=new LinearLayout(this);
            linearLayout.addView(linearLayout1,empty);



        }
        scrollView.addView(linearLayout,lp);

        totallinearLayout.addView(scrollView);
        // setContentView(scrollView);
        setContentView(totallinearLayout);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static ArrayList<String> getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        ArrayList<String> sb = new ArrayList<String>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.add(line);
                //sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    //play rtsp stream
    private void PlayRtspStream(String rtspUrl){
        videoView.setVideoURI(Uri.parse(rtspUrl));
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(WatchVideo.this, MainActivity.class);
        i.putExtra("username", userName);
        startActivity(i);
        finish();
    }

    public static InputStream getResultForRequest(String urlString)
            throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        InputStream is = urlConnection.getInputStream();
        if (is == null)
            return null;
//        StringBuffer sb = new StringBuffer();
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String line = null;
//        try {
//            while ((line = br.readLine()) != null)
//                sb.append(line);
//        } finally {
//            br.close();
//            is.close();
//        }
//        is.close();
        return is;
//        return sb.toString();
    }

}
