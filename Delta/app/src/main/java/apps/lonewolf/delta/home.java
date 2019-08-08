package apps.lonewolf.delta;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import apps.lonewolf.delta.fragment.ListFragment;


public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    File sources = new File(Environment.getExternalStorageDirectory()+"/delta/sources.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getPermission();
        String folder = Environment.getExternalStorageDirectory() + "/" + "delta/";
        File f = new File(folder);
        if (!f.exists()) {
            f.mkdir();
        }
        copyFile(R.raw.sources, "sources.txt");
        copyFile(R.raw.desc, "desc.txt");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,
                    new ListFragment()).commit();
        }
        lister lister = new lister(loader());
        lister.execute();
    }


    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void copyFile(int sources, String s) {
        String folder = Environment.getExternalStorageDirectory() + "/" + "delta/";
        File file = new File(folder + s);
        if (!file.exists()) {
            try {
                InputStream in = getResources().openRawResource(sources);
                FileOutputStream out = null;
                out = new FileOutputStream(folder + s);
                byte[] buff = new byte[1024];
                int read = 0;
                try {
                    while ((read = in.read(buff)) > 0) {
                        out.write(buff, 0, read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont,
                        new ListFragment()).commit();
                break;
            case R.id.add:
                final Dialog dialog = new Dialog(home.this);
                dialog.setContentView(R.layout.dialog);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = 850;
                final EditText edt = dialog.findViewById(R.id.http);
                final EditText et = dialog.findViewById(R.id.dd);
                Button add = dialog.findViewById(R.id.ok);
                Button cancel = dialog.findViewById(R.id.cancel);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String htp="\n";
                        htp+=edt.getText().toString();
                        String dd="\n";
                        dd+=et.getText().toString();
                        String folder = Environment.getExternalStorageDirectory() + "/" + "delta/";
                        String rub = "sources.txt";
                        String ber = "desc.txt";
                        try {
                            FileOutputStream fis = new FileOutputStream(new File(folder + rub),true);
                            FileOutputStream ser = new FileOutputStream(new File(folder + ber),true);
                            byte[] b = htp.getBytes();
                            fis.write(b);
                            b=dd.getBytes();
                            ser.write(b);
                            fis.close();
                            ser.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public ArrayList<String> loader(){
        ArrayList<String> src = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(sources));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String url = bufferedReader.readLine();
            while(url!=null){
                int i = url.indexOf('h');
                url = url.substring(i);
                src.add(url);
                url = bufferedReader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return src;
    }
}
