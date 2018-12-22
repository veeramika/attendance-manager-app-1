package com.example.roshni.attendance;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    DataBaseHelper myDb;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    Button b1,b2;
    ListView listView;
    SubjectAdapter subjects;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb=new DataBaseHelper(this);
        b1=(Button)findViewById(R.id.add);
        b2=(Button)findViewById(R.id.view);


        dl=(DrawerLayout)findViewById(R.id.drawer_layout);
        abdt=new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav=(NavigationView)findViewById(R.id.nav_view);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id=item.getItemId();
                if(id==R.id.nav_addsubject) {
                    Toast.makeText(MainActivity.this, "Clicked on ADD", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,AddSubject.class);
                    startActivity(i);
                }
                if(id==R.id.nav_removesubject) {
                    Toast.makeText(MainActivity.this, "Clicked on DELETE", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,DeleteSubject.class);
                    startActivity(i);
                }
                if(id==R.id.nav_profile)
                    Toast.makeText(MainActivity.this,"Clicked on PROFILE",Toast.LENGTH_SHORT).show();
                if(id==R.id.nav_loogut)
                    Toast.makeText(MainActivity.this,"Clicked on LOGOUT",Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        ArrayList<subject> names=new ArrayList<>();
        Cursor res=myDb.getAllData();
        while (res.moveToNext())
        {
            String s0=res.getString(0);
            String s1=res.getString(1);
            String s2=res.getString(5);
            String s3=res.getString(2);
            String s4=res.getString(3);
            int a=Integer.parseInt(s3);
            int b=Integer.parseInt(s4);
            int c;
            if(a+b==0)
                c=0;
            else
                c=(a/(a+b))*100;
            String s5=String.valueOf(c);
            names.add(new subject(s0,s1,s2,s3,s4,s5));
        }
        subjects=new SubjectAdapter(this,names);
        listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(subjects);
    }

    public void Refresh(View view)
    {
        Intent i=new Intent(MainActivity.this,MainActivity.class);
        startActivity(i);
    }

    public void ViewData(View view)
    {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0)
        {
            //show error message;
            showmessgae("ERROR","Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext())
        {
            buffer.append("ID : "+res.getString(0)+"\n");
            buffer.append("Name : "+res.getString(1)+"\n");
            buffer.append("Minimum : "+res.getString(5)+"\n\n");
            //buffer.append("Marks : "+res.getString(3)+"\n\n");
        }
        //show all data
        showmessgae("DATA",buffer.toString());
    }
    public void showmessgae(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}