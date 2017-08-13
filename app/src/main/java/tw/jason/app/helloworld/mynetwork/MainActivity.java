package tw.jason.app.helloworld.mynetwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private  Bitmap bmp;
    private  UIHandeler handeler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handeler = new UIHandeler();

        img = (ImageView)findViewById(R.id.img);
        //Bitmap bmp = BitmapFactory.decodeStream();
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
                    handeler.sendEmptyMessage(0);

                }catch (Exception e){
                    Log.i("brad",e.toString());
                }



            }
        }.start();


    }

    private  class  UIHandeler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(bmp);
        }
    }
}
