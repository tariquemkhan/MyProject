package com.example.dropboximageuploader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.dropbox.client2.DropboxAPI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.icu.lang.UCharacter.LineBreak.LINE_FEED;

/**
 * Created by root on 1/2/17.
 */

public class DropboxFileAsync extends AsyncTask<ArrayList<JSONObject>, Void, Void> {

    private Context mContext;

    private DropboxAPI dropbox;

    private String filePath;

    private String fileName;

    private String uniqueId;

    private String http_url = "http://192.168.0.177:8081/file_upload";

    private InputStream in = null;
    ;

    private HttpResponse response = null;

    private String charset = "UTF-8";
    ;

    private String boundary;

    private PrintWriter writer;

    private OutputStream outputStream;

    private String TAG = "Alpha";

    private static final String LINE_FEED = "\r\n";

    private final String ACCESS_TOKEN = "775-u5959qAAAAAAAAAAIxMkmXD_2_4uqH7xXv9bghQF5DItfwbHiefcbuXNIReD";

    public DropboxFileAsync(Context context, DropboxAPI dropboxAPI) {
        mContext = context;
        dropbox = dropboxAPI;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(ArrayList<JSONObject>... params) {
        try {
            ArrayList<JSONObject> list = params[0];
            for (JSONObject jsonObject : list) {
                Log.d(TAG, "doInBackground: Path : " + jsonObject.getString("path"));
                File imagefile = new File(jsonObject.getString("path"));
                Log.d(TAG, "doInBackground: File : " + imagefile);
                Log.d(TAG, "doInBackground: Existence : " + imagefile.exists());
                if (imagefile.exists()) {
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
                    File file = new File(jsonObject.getString("path"));
                    //entityBuilder.addBinaryBody("file", file);
                    entityBuilder.addTextBody("image_base64", encodedImage);
                    //ContentBody contentBody = new FileBody(file,mimeType);
                    //entityBuilder.addPart("part_value",contentBody);
                    //entityBuilder.addBinaryBody("bytes_data",b);
                    entityBuilder.addTextBody("name", jsonObject.getString("name"));
                    entityBuilder.addTextBody("access_key", ACCESS_TOKEN);
                    entityBuilder.addTextBody("id", jsonObject.getString("id"));
                    HttpEntity entity = entityBuilder.build();
                    httppost.setEntity(entity);

                    //hitting the api and getting response
                    response = httpclient.execute(httppost);
                    JSONObject responseJson = new JSONObject(EntityUtils.toString(response.getEntity()));
                    Log.d(TAG, "doInBackground: Response : " + responseJson);
                    Log.d(TAG, "doInBackground: status : " + responseJson.getInt("status"));
                    String id = null;
                    if (responseJson.has("id")) {
                        id = responseJson.getString("id");
                    }
                    sendBroadcast(responseJson.getInt("status"), id);
                }

            }

            //Log.d(TAG, "doInBackground: Response : "+response.toString());
        } catch (Exception e) {
            Log.d(TAG, "Exception inside doInBackground of DropBoxFileAsync : " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public void sendBroadcast(int status, String id) {
        Intent intent = new Intent(UploaderActivity.STATUS_UPDATE_RECEIVER);
        intent.putExtra("STATUS", status);
        intent.putExtra("ID", id);
        mContext.sendBroadcast(intent);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    private String getResponse(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
