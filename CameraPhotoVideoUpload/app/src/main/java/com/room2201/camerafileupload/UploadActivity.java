package com.room2201.camerafileupload;

import com.room2201.camerafileupload.AndroidMultiPartEntity.ProgressListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UploadActivity extends Activity {
	// LogCat tag
	private static final String TAG = MainActivity.class.getSimpleName();

	private ProgressBar progressBar;
	private String filePath = null;
    private String userName = null;
	private TextView txtPercentage;
//	private ImageView imgPreview;
	private VideoView vidPreview;
//	private Button btnUpload;
	long totalSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		txtPercentage = (TextView) findViewById(R.id.txtPercentage);
//		btnUpload = (Button) findViewById(R.id.btnUpload);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
//		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		vidPreview = (VideoView) findViewById(R.id.videoPreview);


		ActionBar actionBar = getActionBar();

		// Receiving the data from previous activity
		Intent i = getIntent();

		// video path that is captured in previous activity
		filePath = i.getStringExtra("filePath");

        // doc path recording the URL
//        docPath =

		// boolean flag to identify the media type, image or video
		boolean isImage = i.getBooleanExtra("isImage", true);
        userName = i.getStringExtra("username");
		if (filePath != null) {
			// Displaying the image or video on the screen
			previewMedia(isImage);
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
		}
        new UploadFileToServer().execute();

	}

	/**
	 * Displaying captured image/video on the screen
	 * */
	private void previewMedia(boolean isImage) {
		// Checking whether captured media is image or video
//        imgPreview.setVisibility(View.GONE);
        vidPreview.setVisibility(View.VISIBLE);
        vidPreview.setVideoPath(filePath);
        // start playing
        vidPreview.start();

	}

	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			progressBar.setProgress(0);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Making progress bar visible
			progressBar.setVisibility(View.VISIBLE);

			// updating progress bar value
			progressBar.setProgress(progress[0]);

			// updating percentage value
			txtPercentage.setText(String.valueOf(progress[0]) + "%");
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {

							@Override
							public void transferred(long num) {
								publishProgress((int) ((num / (float) totalSize) * 100));
							}
						});

				File sourceFile = new File(filePath);
				// Adding file data to http body
				entity.addPart("image", new FileBody(sourceFile));

				// Adding doc data to http body
                entity.addPart("doc", new FileBody(sourceFile));
				// Extra parameters if you want to pass to server
				entity.addPart("website",
						new StringBody("www.test.com"));
				entity.addPart("email", new StringBody("somebody@gmail.com"));

                entity.addPart("userName", new StringBody(userName+"/"));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);

			// showing the server response in an alert dialog
			showAlert(result);

			super.onPostExecute(result);
		}

	}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String respond=null;
        try {
            respond = parseUpload(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setMessage(respond).setTitle("Response from Servers")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
//                        try {
//                            String filePathInServer = parseMessage(message);
////                            writeFileSdcardFile(filePathInServer+"\n");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        getDocFromServer();
                        Intent i = new Intent(UploadActivity.this, WatchVideo.class);
						i.putExtra("username", userName);
                        startActivity(i);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}


    public void writeFileSdcardFile(String string) throws IOException{
        String uploadfile = string;
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
                File videoUploaded = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+Config.IMAGE_DIRECTORY_NAME,
                        Config.DOC_NAME);

                // Create the storage directory if it does not exist
                if (!videoUploaded.exists()) {
                    if (!videoUploaded.createNewFile()) {
                        Log.d(TAG, "Oops! Failed create "
                                + Config.DOC_NAME + " file");
                    }
                }

                FileOutputStream outStream = new FileOutputStream(videoUploaded, true);
                outStream.write(uploadfile.getBytes());
                outStream.close();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

	public String parseMessage(String message) throws JSONException {
        JSONObject jsonObj = new JSONObject(message);
        String filePathInServer = jsonObj.getString("file_path");
        return filePathInServer;
    }

    public String parseUpload(String message) throws JSONException {
        JSONObject jsonObj = new JSONObject(message);
        String filePathInServer = jsonObj.getString("message");
        return filePathInServer;
    }
}