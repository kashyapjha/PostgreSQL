package com.example.kash.postgresql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{
    String user,url,DB,password;
    int port;
    ListView lvProfiles;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvProfiles= (ListView)findViewById(R.id.lvProfiles);
        populateList();
    }

    public void populateList() {
        File prefsdir=new File(getApplicationInfo().dataDir,"shared_prefs");
        if(prefsdir.exists() && prefsdir.isDirectory())
        {
            String[] profiles=prefsdir.list();
            final ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<profiles.length;i++)
            {
                list.add(profiles[i].replaceFirst(".xml",""));
            }
            list.remove("com.example.kash.postgresql_preferences");
            final ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
            lvProfiles.setAdapter(adapter);
            lvProfiles.setClickable(true);
            lvProfiles.setLongClickable(true);
            lvProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    String profile = adapter.getItem(position);
                    profile = profile.replaceFirst(".xml", "");
                    Toast.makeText(getApplicationContext(), "Profile " + profile + " selected", Toast.LENGTH_SHORT).show();
                    openTextual(profile);
                }
            });
            lvProfiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    return false;
                }
            });
        }
    }

    private void openTextual(String profile)
    {
        SharedPreferences prefs= this.getSharedPreferences(profile, Context.MODE_PRIVATE);
        user=prefs.getString("User", "").toString();
        url=prefs.getString("URL", "");
        password=prefs.getString("Password", "");
        DB=prefs.getString("DB", "");
        port = prefs.getInt("Port", 5432);
        Toast.makeText(this,"Profile Name: "+profile+"\n"+"URL: "+url+"\n"+"Username: "+user+"\n"+"Password: "+password+"\n"+"Database Name: "+DB+"\n"+"Port Name: "+port,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,TextQuery.class);
        intent.putExtra("User",user);
        intent.putExtra("URL", url);
        intent.putExtra("DB",DB);
        intent.putExtra("Password",password);
        intent.putExtra("Port",port);
        startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        populateList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        switch (id)
        {
            case R.id.NewConn:
            {
                Intent intent =new Intent(this,NewConnection.class);
                startActivity(intent);
            }
            break;
            case R.id.action_settings:
            {
                Intent intent =new Intent(this,SettingsActivity.class);
                startActivity(intent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
