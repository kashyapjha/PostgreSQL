package com.example.kash.postgresql;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Table extends ActionBarActivity
{
    String connurl;
    String table;
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        connurl=getIntent().getStringExtra("Connection URL");
        table=getIntent().getStringExtra("Table");
        Log.d("Table","Connection URL: "+connurl+"\n Table: "+table);
        Connect con=new Connect();
        con.execute();

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
                Statement st=conn.createStatement();
                st.setFetchSize(100);
                ResultSet rs=st.executeQuery("select * from "+table);
                while(rs.next())
                {
                    StringBuilder sb = new StringBuilder();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int numberOfColumns = rsmd.getColumnCount();
                    for (int i = 1; i <= numberOfColumns; i++)
                    {
                        sb.append(rs.getString(i));
                        if (i < numberOfColumns)
                        {
                            sb.append(", ");
                        }
                    }
                    String data = sb.toString();
                    Log.d("Table",data);
                }
                Log.d("Table","End of ResultSet");
            }
            catch (SQLException e)
            {
                Log.e("",e.toString());
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
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
