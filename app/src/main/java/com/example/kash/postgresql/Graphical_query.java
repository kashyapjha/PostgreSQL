package com.example.kash.postgresql;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Graphical_query extends ActionBarActivity
{
    String url,user,DB,password;
    int port;
    String connurl;
    Connection conn;
    ListView lvTables;
    ArrayList<String> tables =new ArrayList<>();
    String addTable="+ Create new table +";
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
        Log.d("GQ/onCreate", "Connection Info: " + "\nUser: " + user + "\nPassword: " + password + "\nDatabase Name:  " + DB + "\nURL: " + url + "\nPort " + port);
        connect();
        lvTables=(ListView)findViewById(R.id.lvTables);
        final ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,tables);
        lvTables.setAdapter(adapter);
        lvTables.setClickable(true);
        lvTables.setLongClickable(true);
        lvTables.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String table=adapter.getItem(position);
                if (adapter.getItem(position)!=addTable)
                {
                    Intent intent=new Intent(Graphical_query.this,Table.class);
                    intent.putExtra("Table", table);
                    Log.d("GQ/onItemClick", table + " selected");
                    startActivity(intent);
                }
                else
                {
                    Intent intent =new Intent(Graphical_query.this,NewTable.class);
                    startActivity(intent);
                }
            }
        });
        lvTables.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                return true;
            }
        });
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
                String[] types={"TABLE"};
                ResultSet rs = md.getTables(null, null, "%",types);
                while (rs.next())
                {
                    Log.d("GQ/doInBackground",rs.getString(3));
                    tables.add(rs.getString(3));
                }
                tables.add(addTable);
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
        Connect a=new Connect();
        a.execute();
        Log.d("Connect","connect() ended");
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
