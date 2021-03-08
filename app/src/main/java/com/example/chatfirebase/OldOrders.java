package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OldOrders extends AppCompatActivity
{
    private DatabaseReference reference;
    private volatile ArrayList<OrderItem> infoOrders= new ArrayList<>();
    private ArrayList<MenuItem> Order = new ArrayList<>();
    private ListView OrderList;
    private Adapter2 adapter;
    private ProgressDialog waiting;
    private Dialog  DateDialog;
    private TextView refused ;
    private TextView accepted ;
    private Calendar calendar;
    private TextView ChangeDate;
    private SimpleDateFormat date;
    private String FinalDate;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_orders);
        Init();
        ChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateFromUser();
            }
        });
        refused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadOrdersData("Refused Orders");
            }
        });
        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadOrdersData("Accepted Orders");
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
        reference = FirebaseDatabase.getInstance().getReference().child(name).child(FinalDate);
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
                adapter =  new Adapter2(infoOrders);
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
        ChangeDate = (TextView)findViewById(R.id.changedate);
        OrderList = (ListView)findViewById(R.id.dynamic);
        waiting = new ProgressDialog(this);
        waiting.setMessage("Wait Please");
        DateDialog =  new Dialog(this);
        DateDialog.setContentView(R.layout.datepicker);
        refused = (TextView) findViewById(R.id.OrdersRefused);
        accepted = (TextView) findViewById(R.id.OrdersAccepted);
        calendar = Calendar.getInstance();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        FinalDate = date.format(Calendar.getInstance().getTimeInMillis());
        ReadOrdersData("Accepted Orders");
    }
    public void getDateFromUser()
    {
        final DatePicker datePicker = (DatePicker)DateDialog.findViewById(R.id.Datepicker);
        TextView Cancel= (TextView) DateDialog.findViewById(R.id.cancel);
        TextView OK = (TextView) DateDialog.findViewById(R.id.ok);
        long Day = 1000*60*60*24;
        datePicker.setMinDate(calendar.getTimeInMillis()-12*30*Day);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        datePicker.setMaxDate(calendar.getTimeInMillis());
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                FinalDate = date.format(calendar.getTime());
                DateDialog.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog.dismiss();
            }
        });
        DateDialog.show();
    }
    public class Adapter2 extends BaseAdapter
    {
        private ArrayList<OrderItem> content = new ArrayList<>();
        public Adapter2(ArrayList<OrderItem> content) {
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
            View view= lay.inflate(R.layout.orderitem2,parent,false);
            TextView Code = (TextView) view.findViewById(R.id.OrderCode);
            TextView Phone = (TextView) view.findViewById(R.id.Phone);
            TextView Accepted = (TextView) view.findViewById(R.id.Accepted);
            TextView Date = (TextView) view.findViewById(R.id.Date);
            TextView Price =(TextView)view.findViewById(R.id.Price);
            TextView Order = (TextView) view.findViewById(R.id.Order);
            TextView location =(TextView) view.findViewById(R.id.locationorder);
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
            location.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent Location =  new Intent (OldOrders.this,Location.class);
                    Location.putExtra("Lat",content.get(position).getLat());
                    Location.putExtra("Log",content.get(position).getLog());
                    startActivity(Location);
                }
            });
            return view;
        }
    }
}
