package apps.lonewolf.delta;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static apps.lonewolf.delta.notificationHelper.CHANNEL_ID;

public class downloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 1920;
    public static final  String dir = Environment.getExternalStorageDirectory() + "/" + "delta/Downloads";
    public downloadService() {
        super("Download Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = intent.getStringExtra("url");
        String filename = intent.getStringExtra("filename");
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Intent notificationIntent = new Intent(this,home.class);
        notificationIntent.putExtra("Dm","download");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Delta Downloader: ")
                .setContentText(filename)
                .setSmallIcon(R.drawable.delta)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        new CountDownTimer(5000,1000){
            @Override
            public void onFinish() {
                stopSelf();
            }
            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
        try{
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            connection.connect();
            int filesize = connection.getContentLength();
            InputStream input = new BufferedInputStream(u.openStream());
            OutputStream output = new FileOutputStream(dir+filename);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while((count=input.read(data))!=-1){
                total+=count;
                Bundle resultData = new Bundle();
                resultData.putInt("progress",(int)(total * 100/filesize));
                receiver.send(UPDATE_PROGRESS,resultData);
                output.write(data,0,count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
        receiver.send(UPDATE_PROGRESS, resultData);
        }
    public void dircheck(){
        String dir = Environment.getExternalStorageDirectory() + "/" + "delta/Downloads";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdir();
        }
    }

}
