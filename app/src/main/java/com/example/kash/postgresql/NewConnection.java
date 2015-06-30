package com.example.kash.postgresql;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewConnection extends ActionBarActivity implements View.OnClickListener
{
    EditText etProf,etURL,etUser,etPass,etDB,etPort;
    Button btnAdd;
    String prof,url,user,pass,DB;
    int port;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection);
        etProf=(EditText) findViewById(R.id.etProf);
        etURL=(EditText) findViewById(R.id.etURL);
        etUser=(EditText) findViewById(R.id.etUser);
        etPass=(EditText) findViewById(R.id.etPass);
        etDB=(EditText) findViewById(R.id.etDB);
        etPort=(EditText) findViewById(R.id.etPort);
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_connection, menu);
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

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnAdd:
            {
                prof=etProf.getText().toString();
                url=etURL.getText().toString();
                user=etUser.getText().toString();
                pass=etPass.getText().toString();
                DB=etDB.getText().toString();
                port= Integer.parseInt(etPort.getText().toString());
                Toast.makeText(this,"Profile Name: "+prof+"\n"+"URL: "+url+"\n"+"Username: "+user+"\n"+"Password: "+pass+"\n"+"Database Name: "+DB+"\n"+"Port Name: "+port,Toast.LENGTH_LONG).show();
                SharedPreferences prefs=this.getSharedPreferences(prof,Context.MODE_PRIVATE);
                prefs.edit().putString("Profile",prof).apply();
                prefs.edit().putString("URL",url).apply();
                prefs.edit().putString("User",user).apply();
                prefs.edit().putString("Password",pass).apply();
                prefs.edit().putString("DB",DB).apply();
                prefs.edit().putInt("Port", port).apply();
                finish();
            }
            break;
        }
    }
}
