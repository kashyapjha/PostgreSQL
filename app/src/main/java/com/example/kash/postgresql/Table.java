package com.example.kash.postgresql;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Table extends ActionBarActivity
{
    String connurl;
    String tablename;
    Connection conn;
    ListView lvRows;
    ArrayList<String[]> list;
    ArrayAdapter<String> adapter;
    public String table[][];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        connurl=getIntent().getStringExtra("Connection URL");
        tablename=getIntent().getStringExtra("Table");
        Log.d("Table", "Connection URL: " + connurl + "\n Table: " + tablename);
        lvRows=(ListView) findViewById(R.id.lvRows);
        connect();
    }
    private class Connect extends AsyncTask<Void, Void, String>
    {
        protected void onPostExecute()
        {
            Log.d("","onPostExecute started");
            list = new ArrayList<String[]>(table.length);
            adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list);
            lvRows.setAdapter(adapter);
            for(String[] row: table)
            {
                list.add(row);
                Log.d("","Row added");
            }
        }
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
                Statement st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                st.setFetchSize(100);
                ResultSet rs=st.executeQuery("select * from " + tablename);
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();
                rs.last();
                int numberOfRows=rs.getRow();
                rs.first();
                Log.d("Table Dimensions: ", "Rows:" + numberOfRows + ", Columns:" + numberOfColumns);
                table= new String[numberOfRows][numberOfColumns];
                int i=1;
                int j;
                do
                {
                    for(j=1;j<=numberOfColumns;j++)
                    {
                        table[i-1][j-1]=rs.getString(j);
                        Log.d("Data",rs.getString(j));
                    }
                    i++;
                }
                while(rs.next());
                Log.d("Table","End of ResultSet");
                /*runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        adapter.notifyDataSetChanged();
                    }
                });*/
            }
            catch (SQLException e)
            {
                Log.e("SQLException",e.toString());
            }
            return null;
        }
    }

    public void connect()
    {
        Connect con=new Connect();
        con.execute();
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
