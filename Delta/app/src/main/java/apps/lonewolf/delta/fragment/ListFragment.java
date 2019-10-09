package apps.lonewolf.delta.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import apps.lonewolf.delta.R;
import apps.lonewolf.delta.shows;

public class ListFragment extends Fragment {
    ArrayList<String> urls = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list = view.findViewById(R.id.list);
        File sources = new File(Environment.getExternalStorageDirectory()+"/delta/sources.txt");
        File des = new File(Environment.getExternalStorageDirectory()+"/delta/desc.txt");
        BufferedReader bufferedReader = null;
        BufferedReader buffered = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(sources));
            buffered = new BufferedReader(new FileReader(des));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String desc = buffered.readLine();
            String url = bufferedReader.readLine();
            while(url!=null && desc!=null){
                int i = url.indexOf('h');
                int j = desc.indexOf(' ');
                url = url.substring(i);
                desc = desc.substring(j+1);
                urls.add(url);
                url = bufferedReader.readLine();
                desc = buffered.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,urls);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s= list.getItemAtPosition(position).toString();
                Intent i = new Intent(getActivity(), shows.class);
                i.putExtra("url",s);
                startActivity(i);
            }
        });
        return view;
    }
}