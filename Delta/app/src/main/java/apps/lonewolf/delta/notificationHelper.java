package apps.lonewolf.delta;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class notificationHelper extends Application {
    public static final String CHANNEL_ID = "NotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        creatNotificationChannel();
    }

    private void creatNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Delta Downloader: ",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}