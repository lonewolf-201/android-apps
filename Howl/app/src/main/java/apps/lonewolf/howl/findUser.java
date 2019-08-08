package apps.lonewolf.howl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class findUser extends AppCompatActivity {
    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLM;

    ArrayList<UserObject> contactList,userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        contactList = new ArrayList<>();
        userList  = new ArrayList<>();
        initializer();
        getContactList();
    }
    private void getContactList(){
        String iso = getCountrySIO();
        ArrayList<String> phoner = new ArrayList<>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");
            if(!String.valueOf(phone.charAt(0)).equals("+")){
                phone = iso+phone ;
            }
            if(!phoner.contains(phone)) {
                phoner.add(phone);
                UserObject mContacts = new UserObject(name, phone);
                contactList.add(mContacts);
                getUserDetails(mContacts);
            }
        }
    }

    private void getUserDetails(UserObject mContacts) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("phone").equalTo(mContacts.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i("FindUser","if-0");
                    String phone = "",
                            name="";
                    for(DataSnapshot c: dataSnapshot.getChildren()){
                            if(c.child("phone").getValue()!=null){
                                Log.i("FindUser","if-1");
                                phone = c.child("phone").getValue().toString();
                            }
                            if(c.child("name").getValue()!=null){
                                Log.i("FindUser","if-2");
                                name = c.child("name").getValue().toString();
                            }

                            UserObject mUser = new UserObject(name,phone);
                            userList.add(mUser);
                            mUserListAdapter.notifyDataSetChanged();
                            return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FindUser",databaseError.getMessage());
            }
        });
    }

    private String getCountrySIO(){
        String iso="";
        TelephonyManager TpM = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(TpM.getNetworkCountryIso()!=null){
            if(!TpM.getNetworkCountryIso().equals("")){
                iso = TpM.getNetworkCountryIso();
            }
        }
        return CountryToPhonePrefix.getPhone(iso);
    }
    private void initializer() {
        mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLM = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mUserList.setLayoutManager(mUserListLM);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}
