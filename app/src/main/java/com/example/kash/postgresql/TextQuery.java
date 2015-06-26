package com.example.kash.postgresql;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TextQuery extends ActionBarActivity
{
    String url,user,db,pass;
    int port;

    private class FetchSQL extends AsyncTask<Void,Void,String>
    {
        protected String doInBackground(Void... params) {
            String retval = "";
            try
            {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
                retval = e.toString();
            }
            String connurl = "jdbc:postgresql://"+url+"/"+db+"?user="+user+"&password="+pass;
            Log.d("Connection URL: ",connurl);
            Connection conn;
            try
            {
                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql;
                sql = "select * from students";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next())
                {
                    retval = rs.getString(1);
                }
                rs.close();
                st.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
                retval = e.toString();
            }
            return retval;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_query);
        FetchSQL a=new FetchSQL();
        a.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_query, menu);
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
