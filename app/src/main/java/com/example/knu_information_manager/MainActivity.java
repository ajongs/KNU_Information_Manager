package com.example.knu_information_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView1);
        final Bundle bundle = new Bundle();
        new Thread(){
            @Override
            public void run() {
                String URL ="https://www.kongju.ac.kr/kor/article/student_news/?mno=&pageIndex=1&categoryCnt=1&searchCategory=&searchCategory0=&searchCondition=1&searchKeyword=%EB%B3%B5%EC%88%98%EC%A0%84%EA%B3%B5#article";

                try {
                    Document doc = Jsoup.connect(URL).get();
                    Elements temele = doc.select(".subject");
                    Boolean isEmpty = temele.isEmpty();
                    if(isEmpty==false){
                        bundle.putString("article", temele.text());
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("document 불러오기 실패");

                }
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            textView.setText(bundle.getString("article"));
        }
    };
}