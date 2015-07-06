package com.example.kash.postgresql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Graphical_query extends ActionBarActivity
{
    String url,user,DB,password;
    int port;
    String connurl;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphical_query);
        user=getIntent().getStringExtra("User");
        password=getIntent().getStringExtra("Password");
        DB=getIntent().getStringExtra("User");
        url=getIntent().getStringExtra("URL");
        port=getIntent().getIntExtra("Port", 5432);
        connurl = "jdbc:postgresql://" + url + "/" + DB + "?user=" + user + "&password=" + password;
        Log.d("Connection Info","\nUser: "+user + "\nPassword: " + password + "\nDatabase Name:  " + DB + "\nURL: " + url + "\nPort " + port);
        connect();
    }

    private class Connect extends AsyncTask<Void, Void, String>
    {
        protected String doInBackground(Void... params)
        {
            try
            {
                Class.forName("org.postgresql.Driver");
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                Log.e("", e.toString());
            }
            try
            {
                conn = DriverManager.getConnection(connurl);
                DatabaseMetaData md = conn.getMetaData();
                ResultSet rs = md.getTables(null, null, "%", null);
                while (rs.next())
                {
                    Log.d("",rs.getString(3));
                }
            }
            catch (SQLException e)
            {
                Log.e("",e.toString());
            }
            return null;
        }
    }

    public void connect()
    {
        Log.d("","Connect");
        Connect a=new Connect();
        a.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graphical_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
