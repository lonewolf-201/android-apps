package apps.lonewolf.delta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import apps.lonewolf.delta.Items.urlitem;

public class shows extends AppCompatActivity {

    ListView show;
    TextView text;
    ProgressBar progressBar;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        url = getIntent().getExtras().getString("url");
        show = findViewById(R.id.show_list);
        text = findViewById(R.id.url_name);
        progressBar = findViewById(R.id.prog);
        if(url!=null){
            text.setText(url);
        }else{
            text.setText("NULL");
        }
        String s= url;
        pagerLoader pagerLoader = new pagerLoader(s,this);
        pagerLoader.execute();
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String r =s;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suffix = show.getItemAtPosition(position).toString();
                if(!suffix.equals("../")) {
                    r = r + suffix;
                    pagerLoader n = new pagerLoader(r, shows.this);
                    n.execute();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        String res = text.getText().toString();
        if(url.equals(res)) {
            super.onBackPressed();
        }else{
            int n = res.lastIndexOf('/');
            res = res.substring(0,n);
            n = res.lastIndexOf('/');
            res = res.substring(0,n+1);
            pagerLoader pg = new pagerLoader(res,this);
            pg.execute();
        }
    }

    private class pagerLoader extends AsyncTask<String,Boolean, ArrayList<String>>{
        String urls;
        Context context;
        ArrayList<String> names = new ArrayList<>();
        public pagerLoader(String urls,Context context){
            this.urls = urls;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            names.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> item = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(urls).get();
                Elements links = doc.getElementsByTag("a");
                for(Element link:links){
                    String s= link.attr("href");
                    String r = link.text();
                    item.add(s);
                    names.add(r);
                }
            }catch (IOException io){
                if(io.toString().contains("404")){
                    finish();
                }
                Log.e("Not Ok",io.toString());
            }
            return item;
        }

        @Override
        protected void onPostExecute(ArrayList<String> urlitems) {
            super.onPostExecute(urlitems);
            progressBar.setVisibility(View.INVISIBLE);
            if(urls==url){
                text.setText(url);
            }else{
                text.setText(urls);
            }
            ArrayAdapter<String> ar = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,names);
            //ArrayAdapter<String> br = new ArrayAdapter<>(context,android.R.layout.simple_list_item_2,urlitems);
            show.setAdapter(ar);
            //show.setAdapter(br);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
        }
    }
}
