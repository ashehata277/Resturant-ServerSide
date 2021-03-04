package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.AsynchronousChannelGroup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
    private DatabaseReference reference;
    private DatabaseReference referenceInner;
    private volatile ArrayList<OrderItem> infoOrders= new ArrayList<>();
    private ArrayList<MenuItem> Order = new ArrayList<>();
    private ListView OrderList;
    private Adapter adapter;
    private ProgressDialog waiting;
    private SimpleDateFormat date;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrderList = (ListView)findViewById(R.id.OrderList);
        calendar= Calendar.getInstance();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        waiting = new ProgressDialog(this);
        waiting.setMessage("Wait Please");
        Init();
        ImageView Refresh = (ImageView) findViewById(R.id.refresh);
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadOrdersData("Orders");
            }
        });
        TextView Old = (TextView)findViewById(R.id.Old);
        Old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this,OldOrders.class));
            }
        });
    }
    public void ReadOrdersData(String name)
    {

        waiting.show();
        for (int i=0;i<infoOrders.size();i++)
        {
            infoOrders.remove(i);
        }
        infoOrders.clear();
         reference = FirebaseDatabase.getInstance().getReference().child(name);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot childChild : childDataSnapshot.child("The order").getChildren()) {
                        Order.add(new MenuItem((String) childChild.child("name").getValue(),
                                (String) childChild.child("note").getValue(),
                                (Long) childChild.child("number").getValue()));
                    }
                    infoOrders.add(new OrderItem(childDataSnapshot.getKey(),
                            (String) childDataSnapshot.child("OrderDate").getValue(),
                            (Long) childDataSnapshot.child("Total Price").getValue(),
                            (String) childDataSnapshot.child("phone").getValue(),
                            String.valueOf( childDataSnapshot.child("Accepted").getValue()),
                            (double) childDataSnapshot.child("Location").child("latitude").getValue(),
                            (double) childDataSnapshot.child("Location").child("longitude").getValue(),
                            Order));
                    Order.clear();
                }
                adapter =  new Adapter(infoOrders);
                OrderList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                waiting.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void Init()
    {
        waiting.show();
        reference = FirebaseDatabase.getInstance().getReference().child("Orders");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot childChild : childDataSnapshot.child("The order").getChildren()) {
                        Order.add(new MenuItem((String) childChild.child("name").getValue(),
                                (String) childChild.child("note").getValue(),
                                (Long) childChild.child("number").getValue()));
                    }
                    infoOrders.add(new OrderItem(childDataSnapshot.getKey(),
                            (String) childDataSnapshot.child("OrderDate").getValue(),
                            (Long) childDataSnapshot.child("Total Price").getValue(),
                            (String) childDataSnapshot.child("phone").getValue(),
                            String.valueOf(childDataSnapshot.child("Accepted").getValue()),
                            (double) childDataSnapshot.child("Location").child("latitude").getValue(),
                            (double) childDataSnapshot.child("Location").child("longitude").getValue(),
                            Order));
                    Order.clear();
                }
                adapter =  new Adapter(infoOrders);
                OrderList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                waiting.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void TransfertoLongDataBase(String Code,String where)
    {
        String TodayDate =date.format(calendar.getTimeInMillis());
        DatabaseReference Move = FirebaseDatabase.getInstance().getReference().child("Orders").child(Code);
        DatabaseReference to = FirebaseDatabase.getInstance().getReference().child(where).child(TodayDate).child(Code);
        Move.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                to.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Move.removeValue();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class Adapter extends BaseAdapter
    {
        private ArrayList<OrderItem> content = new ArrayList<>();
        public Adapter(ArrayList<OrderItem> content) {
            this.content = (ArrayList<OrderItem>) content.clone();
        }
        @Override
        public int getCount() {
            return content.size();
        }

        @Override
        public Object getItem(int position) {
            return content.get(position).getCode();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater lay = getLayoutInflater();
            View view= lay.inflate(R.layout.orderitem,parent,false);
            TextView Code = (TextView) view.findViewById(R.id.OrderCode);
            TextView Phone = (TextView) view.findViewById(R.id.Phone);
            TextView Accepted = (TextView) view.findViewById(R.id.Accepted);
            TextView Date = (TextView) view.findViewById(R.id.Date);
            TextView Price =(TextView)view.findViewById(R.id.Price);
            TextView Order = (TextView) view.findViewById(R.id.Order);
            TextView Location = (TextView) view.findViewById(R.id.location);
            Code.setText(content.get(position).getCode());
            Phone.setText(content.get(position).getPhone());
            Date.setText(content.get(position).getDate());
            Accepted.setText(content.get(position).getState());
            Price.append(content.get(position).getPrice().toString());
            Order.setText("");
           int size=content.get(position).getOrder().size();
           for (int i=0;i<size;i++)
           {
               Order.append(content.get(position).getOrder().get(i).getName() +":"+content.get(position).getOrder().get(i).getNote()+":"+content.get(position).getOrder().get(i).getNo()+"\n");
           }
            ImageView Accept = view.findViewById(R.id.accept);
            ImageView Refuse = view.findViewById(R.id.refuse);

            Accept.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    referenceInner = FirebaseDatabase.getInstance().getReference().child("Orders").child(content.get(position).getCode());
                    referenceInner.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String,Object> map =  new HashMap<>();
                            map.put("Accepted",true);
                            referenceInner.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(MainActivity.this,"The Order Accepted",Toast.LENGTH_SHORT).show();
                                    content.get(position).setState("true");
                                    TransfertoLongDataBase(content.get(position).getCode(),"Accepted Orders");
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            Refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    referenceInner = FirebaseDatabase.getInstance().getReference().child("Orders").child(content.get(position).getCode());
                    referenceInner.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String,Object> map =  new HashMap<>();
                            map.put("Accepted",false);
                            referenceInner.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(MainActivity.this,"The Order Refused",Toast.LENGTH_SHORT).show();
                                    content.get(position).setState("false");
                                    TransfertoLongDataBase(content.get(position).getCode(),"Refused Orders");
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
            Location.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent Location =  new Intent (MainActivity.this,Location.class);
                    Location.putExtra("Lat",content.get(position).getLat());
                    Location.putExtra("Log",content.get(position).getLog());
                    startActivity(Location);
                }
            });
            return view;
        }
    }
}