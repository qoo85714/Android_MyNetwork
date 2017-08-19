package tw.jason.app.helloworld.mynetwork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private  Bitmap bmp;
    private  UIHandeler handler;
    private File sdroot,savdPDF;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handeler = new UIHandeler();

        //img = (ImageView)findViewById(R.id.img);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }else{
            init();
        }


        //Bitmap bmp = BitmapFactory.decodeStream();
    }
    private void init(){
        handler = new UIHandeler();
        img = (ImageView)findViewById(R.id.img);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        sdroot = Environment.getExternalStorageDirectory();
    }


    public void test1(View view){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.iii.org.tw/");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = br.readLine())!=null){
                        Log.i("brad",line);
                    }
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("brad",e.toString());
                }

            }
        }.start();
    }
    private void parseJSON(String json){
        try {
            JSONArray root = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void test2(View view){
        new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://www.iii.org.tw/assets/images/information-news/image004.jpg");
                    HttpURLConnection conn =
                     (HttpURLConnection)url.openConnection();
                    conn.connect();
                    bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    handler.sendEmptyMessage(0);

                }catch (Exception e){
                    Log.i("brad",e.toString());
                }
            }
        }.start();


    }
    public void test3(View view){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                try {
                    savdPDF = new File(sdroot,"url.pdf");
                    BufferedOutputStream bout =
                            new BufferedOutputStream(new FileOutputStream(savdPDF));
                    URL url = new URL(
                            "http://pdfmyurl.com/?url=http://www.gamer.com.tw");
                    HttpURLConnection conn= (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
                    byte[] buf = new byte[4096]; int len=0;
                    while ((len = bin.read(buf)) !=-1){
                        bout.write(buf,0,len);
                    }
                    bin.close();
                    bout.flush();
                    bout.close();

                    handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    Log.i("brad", "e:"+e.toString());
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }
    public void test4(View view){
        new Thread(){
            @Override
            public void run() {

                try {
                    MultipartUtility mu =
                            new MultipartUtility(
                                    "http://10.0.2.2:7080/brad2101/Brad11", "UTF-8", "");
                    mu.addFilePart("upload", savdPDF);
                    List<String> ret = mu.finish();
                    for (String line: ret){
                        Log.i("brad", line);
                    }
                }catch(Exception e){
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }



    private  class  UIHandeler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0 :img.setImageBitmap(bmp); break;
                case 1 :
                    Toast.makeText(MainActivity.this, "Save OK", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    //showPDF();
                    break;

                case 2 :
                    Toast.makeText(MainActivity.this, "Save Fail", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;

            }

        }
    }

    private void showPDF(){
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(savdPDF),"application/pdf");
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(it);
    }
}
