package apps.lonewolf.omega;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResource;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.DriveRequestInitializer;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private DriveServiceHelper mDriveServiceHelper;
    private GoogleSignInAccount googleAccount;
    private final int REQUEST_CODE_OPEN_DOCUMENT =10;
    Context mContext;
    View view;
    Button btn;
    EditText edt;
    private Drive driveService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home,container,false);
        btn = view.findViewById(R.id.btn);
        edt = view.findViewById(R.id.edt);
        googleAccount = GoogleSignIn.getLastSignedInAccount(mContext);
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        mContext, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(googleAccount.getAccount());
        driveService =
                new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName("Omega")
                        .build();
        mDriveServiceHelper = new DriveServiceHelper(driveService);
        btn.setOnClickListener(v -> {
            if(mDriveServiceHelper!=null){
                Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();
                startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
            }
            query();
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_OPEN_DOCUMENT){
            if (data != null){
                Uri uri = data.getData();
                if(uri!=null){
                    openFileFromFilePicker(uri);
                }
            }
        }
    }

    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d("HomeFrag", "Opening " + uri.getPath());
            mDriveServiceHelper.openFileUsingStorageAccessFramework(mContext.getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception ->
                            Log.e("HomeFrag", "Unable to open file from picker.", exception));
        }
    }
    private void createFile(boolean flag) {
        if(flag) {
            if (mDriveServiceHelper != null) {
                Log.d("HomeFrag", "Creating a file.");
                mDriveServiceHelper.createFolder("OmegaMusic");
            }
        }
    }
    private void query() {
        if (mDriveServiceHelper != null) {
            Log.d("HomeFrag", "Querying for files.");

            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        if(fileList.getFiles().get(0).getName().equals("OmegaMusic")){
                            Toast.makeText(mContext, "OmegaMusic is there", Toast.LENGTH_SHORT).show();
                            String s = fileList.getFiles().get(0).getId();
                        }
                        else{
                            createFile(true);
                        }
                    })
                    .addOnFailureListener(exception -> Log.e("HomeFrag", "Unable to query files.", exception));
        }
    }
}
