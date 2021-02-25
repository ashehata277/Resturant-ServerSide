package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference reference ;
    private DatabaseReference reference3 ;
    private EditText message;
    private ImageButton imageButton;
    private ListView listView;
    private String TempKey;
    private ArrayList<ListItem> ChatList= new ArrayList<ListItem>();
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private String Name,ChatMessage,ChatUserName;
    private long type;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Name =  getIntent().getExtras().getString("Name");
        Log.i("TAG",Name);
        reference = FirebaseDatabase.getInstance().getReference().child("main");
       message = (EditText)findViewById(R.id.Message);
       imageButton = (ImageButton)findViewById(R.id.imageButton);
       recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new RecycleViewAdapter(ChatList, this);
        recyclerView.setAdapter(adapter);
       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Map<String,Object> map = new HashMap<String,Object>();
               TempKey= reference.push().getKey();
               reference.updateChildren(map);                       //I Think this line is not useful
               DatabaseReference reference2 = reference.child(TempKey);
               Map<String,Object> map2 =  new HashMap<String,Object>();
               map2.put("Name",Name);
               map2.put("Message",message.getText().toString());
               map2.put("type",1);
               reference2.updateChildren(map2);
               Log.i("TAg", String.valueOf(ChatList.size()));
               ChatList.add(new ListItem(1,message.getText().toString()));
               Log.i("TAg", String.valueOf(ChatList.size()));
               adapter.notifyDataSetChanged();
               message.setText("");
           }
       });
       reference3 = FirebaseDatabase.getInstance().getReference().child("MainChatRoom");
       reference3.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               ChatAdd(snapshot);
           }
           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatAdd(snapshot);
           }
           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {
           }
           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1,menu);
        MenuItem search_item =menu.findItem(R.id.searchview);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(search_item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return  true;
    }
    public void ChatAdd(DataSnapshot snapshot)
    {
        Iterator i  = snapshot.getChildren().iterator();
        while(i.hasNext())
        {
            ChatMessage = (String)((DataSnapshot)i.next()).getValue();
            ChatUserName= (String)((DataSnapshot)i.next()).getValue();
            type = (Long) ((DataSnapshot)i.next()).getValue();
            ChatList.add(new ListItem(2,ChatMessage));
            adapter.notifyDataSetChanged();
            adapter.notifyItemRangeChanged(ChatList.size(),ChatList.size());
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChatActivity.this,MainActivity.class));
        super.onBackPressed();
    }
}