package apps.lonewolf.delta.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import apps.lonewolf.delta.R;

public class DownloadManager extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downloadmanager,container,false);
        String dir = Environment.getExternalStorageDirectory() + "/" + "delta/Downloads";
        File f = new File(dir);
        if(!f.exists()){
            f.mkdir();
        }

        return view;
    }
}
