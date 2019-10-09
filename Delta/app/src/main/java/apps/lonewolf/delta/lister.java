package apps.lonewolf.delta;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class lister extends AsyncTask<String, Integer, ArrayList<String>> {
    String folder = Environment.getExternalStorageDirectory() + "/" + "delta/";
    //File file = new File(folder +"series.txt");
    //File name = new File(folder+"name.txt");
    ArrayList<String> src;
    ArrayList<String> urls = new ArrayList<>();
    public lister(ArrayList<String> src){
        this.src = src;
    }
    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        for(String url:src){
            try {
                Document doc = Jsoup.connect(url).get();
                Elements links = doc.getElementsByTag("a");
                for(Element link : links){
                    urls.add(link.attr("href"));
                    Log.i("ok dood",link.text());
                }
            }catch (IOException io){
                Log.e("ok dood",io.getMessage());
            }
        }
        return urls;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(ArrayList<String> showItems) {
        try {
            File name = new File(folder+"name.txt");
            for (String htp : showItems) {
                htp = "\n" + htp;
                FileOutputStream fis = new FileOutputStream(name, false);
                byte[] b = htp.getBytes();
                fis.write(b);
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(showItems);
    }
}
