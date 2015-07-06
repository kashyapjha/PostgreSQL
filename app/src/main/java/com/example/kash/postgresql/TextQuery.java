package com.example.kash.postgresql;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class TextQuery extends ActionBarActivity
{
    String url,user,DB,password;
    int port;
    EditText etQuery;
    Button btnExec;
    TextView tvResult;
    String connurl;
    Connection conn;
    ResultSet rs;
    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_query);
        user=getIntent().getStringExtra("User");
        password=getIntent().getStringExtra("Password");
        DB=getIntent().getStringExtra("User");
        url=getIntent().getStringExtra("URL");
        port=getIntent().getIntExtra("Port", 5432);
        Log.d("Connection Info",user+" "+password+" "+DB+" "+url+" "+port);
        etQuery=(EditText)findViewById(R.id.etQuery);
        tvResult=(TextView)findViewById(R.id.tvResult);
        btnExec=(Button)findViewById(R.id.btnExec);
        connurl = "jdbc:postgresql://" + url + "/" + DB + "?user=" + user + "&password=" + password;
        Log.d("",connurl);
        btnExec.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("", "Execute");
                connect();
            }
        });
    }

    private void connect()
    {
        Connect a=new Connect();
        Log.d("","Connect");
        a.execute();
    }

    public void exec(String connurl,String query)
    {
        Log.d("", "Exec");
        try
        {
            conn = DriverManager.getConnection(connurl);
            Statement st = null;
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                res=rs.getString(1);
            }
        }
        catch(SQLException e)
        {
            Log.e("exec",e.toString());
        }
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
            exec(connurl,etQuery.getText().toString());
            return null;
        }
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
