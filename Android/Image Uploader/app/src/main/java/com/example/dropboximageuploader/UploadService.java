package com.example.dropboximageuploader;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by root on 10/2/17.
 */

public class UploadService extends IntentService {

    private String http_url = "http://192.168.0.177:8081/file_upload";

    private InputStream in = null;;

    private HttpResponse response = null;

    private String charset = "UTF-8";;

    private String boundary;

    private PrintWriter writer;

    private OutputStream outputStream;

    private String TAG = "Alpha";

    private static final String LINE_FEED = "\r\n";

    private final String ACCESS_TOKEN = "775-u5959qAAAAAAAAAAIxMkmXD_2_4uqH7xXv9bghQF5DItfwbHiefcbuXNIReD";
    
    public UploadService() {
        super(UploadService.class.getName());
        databaseHelper = new DatabaseHelper(this);
    }


    DatabaseHelper databaseHelper;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: inside UploadService : ");

        Bundle bundle = intent.getExtras();
        ArrayList<Map> list = new ArrayList<>();
        if (bundle.containsKey("IMAGE_LIST")) {
            Log.d(TAG, "onHandleIntent: inside if when contains IMAGE_LIST");
            list = (ArrayList<Map>) intent.getSerializableExtra("IMAGE_LIST");
            Log.d(TAG, "onHandleIntent: Count in service : "+list.size());
        }
            if (list != null) {
                for (Map<String,String> map : list){
                    try {
                        Log.d(TAG, "onHandleIntent: Path : " + map.get("path"));
                        File imagefile = new File(map.get("path"));
                        //if (imagefile.exists()) {
                            FileInputStream fis = null;
                            fis = new FileInputStream(imagefile);
                            Bitmap bm = BitmapFactory.decodeStream(fis);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpPost httppost = new HttpPost(http_url);
                            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                            File file = new File(map.get("path"));
                            //entityBuilder.addBinaryBody("file", file);
                            entityBuilder.addTextBody("image_base64", encodedImage);
                            //ContentBody contentBody = new FileBody(file,mimeType);
                            //entityBuilder.addPart("part_value",contentBody);
                            //entityBuilder.addBinaryBody("bytes_data",b);
                            entityBuilder.addTextBody("name", map.get("name"));
                            entityBuilder.addTextBody("access_key", ACCESS_TOKEN);
                            entityBuilder.addTextBody("id", map.get("id"));
                            HttpEntity entity = entityBuilder.build();
                            httppost.setEntity(entity);

                            //hitting the api and getting response
                            response = httpclient.execute(httppost);
                            JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
                            Log.d(TAG, "onHandleIntent: Response : " + responseJson);
                            Log.d(TAG, "onHandleIntent: status : " + responseJson.getInt("status"));
                            String id = null;
                            if (responseJson.getInt("status") == 1) {
                                Log.d(TAG, "onHandleIntent: Uploaded count : " + databaseHelper.getUpladedImageCount());
                                int updateStatus = databaseHelper.updateStatus(responseJson.getString("id"));
                            }
                            if (responseJson.has("id")) {
                                id = responseJson.getString("id");
                            }
                        //}
                    }catch (Exception e) {
                        Log.d(TAG, "onHandleIntent: Exception inside service : " + e.toString());
                        //sendBroadcast(responseJson.getInt("status"),id);
                    }
                }
            }

    }

    public void sendBroadcast(int status, String id) {
        Intent intent = new Intent(UploaderActivity.STATUS_UPDATE_RECEIVER);
        intent.putExtra("STATUS",status);
        intent.putExtra("ID",id);
        this.sendBroadcast(intent);
    }
}
