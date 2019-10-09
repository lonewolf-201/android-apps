package apps.lonewolf.delta;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class shows extends AppCompatActivity{
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
                Toast.makeText(shows.this, suffix, Toast.LENGTH_SHORT).show();
                if(suffix.endsWith(".mp4") || (suffix.endsWith(".mkv"))){
                    r=r+suffix;
                    opendialog(suffix,r);
                }
                else if(!suffix.equals("../") && (suffix.endsWith("/"))) {
                    r = r + suffix;
                    pagerLoader n = new pagerLoader(r, shows.this);
                    n.execute();
                }
            }
        });
    }
    public void opendialog(String filename,String u){
        dialogBuild build = new dialogBuild(filename,u);
        build.show(getSupportFragmentManager(),"action dialog");
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
            text.setText(res);
            pg.execute();
        }
    }

    private class pagerLoader extends AsyncTask<String,Boolean, ArrayList<String>>{
        String urls;
        Context context;
        public pagerLoader(String urls,Context context){
            this.urls = urls;
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    item.add(s);
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
            if(urls.equals(url)){
                text.setText(url);
            }else{
                text.setText(urls);
            }
            ArrayAdapter<String> ar = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,urlitems);
            show.setAdapter(ar);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
        }
    }
}
