package com.hoka.webparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Elements mContent;
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView1);
        new NewThread().execute();
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.pro_item, mTitleList);
    }

    public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            Document doc;
            try {
                // определяем откуда будем воровать данные
                doc = Jsoup.connect("https://news.mail.ru/currency.html").get();
                // задаем с какого места, я выбрал заголовке статей
                mContent = doc.select(".s-rate-large__name");
                // чистим наш аррей лист для того что бы заполнить
                mTitleList.clear();
                // и в цикле захватываем все данные какие есть на странице
                for (Element titles : mContent) {
                    // записываем в аррей лист
                    mTitleList.add(titles.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ничего не возвращаем потому что я так захотел)
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            // после запроса обновляем листвью
            mListView.setAdapter(mAdapter);
        }
    }
}
