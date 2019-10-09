package apps.lonewolf.delta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dialogBuild extends AppCompatDialogFragment {
    private static final String title="Choose Action";
    private String url;
    private String filename;
    public dialogBuild(String filename,String url){
        this.filename=filename;
        this.url=url;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage("Do you want to Download or Stream "+filename+" ?");
        builder.setPositiveButton("Download", (dialog, which) ->{
            Intent serviceIntent = new Intent(getActivity(),downloadService.class);
            serviceIntent.putExtra("url",url);
            serviceIntent.putExtra("filename",filename);
            if(getActivity()!=null){
                getActivity().startService(serviceIntent);
            }
            Toast.makeText(getActivity(), "Download", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Play", (dialog, which) -> {
            Toast.makeText(getActivity(),"Play",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });
        return builder.create();
    }
}
