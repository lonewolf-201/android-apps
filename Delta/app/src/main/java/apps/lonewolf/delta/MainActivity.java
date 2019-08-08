package apps.lonewolf.delta;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    RelativeLayout r1,r2;
    Animation uptodown,downtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1 = findViewById(R.id.r1);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        r1.setAnimation(uptodown);
        r2 = findViewById(R.id.r2);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        r2.setAnimation(downtoup);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(MainActivity.this, home.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
