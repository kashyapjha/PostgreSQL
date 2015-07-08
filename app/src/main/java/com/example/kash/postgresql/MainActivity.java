package com.example.kash.postgresql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public void populateList()
    {
        File prefsdir=new File(getApplicationInfo().dataDir,"shared_prefs");
        if(prefsdir.exists() && prefsdir.isDirectory())
        {
            final String[] profiles=prefsdir.list();
            final ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<profiles.length;i++)
            {
                list.add(profiles[i].replaceFirst(".xml",""));
            }
            list.remove("com.example.kash.postgresql_preferences");
            for (String s : list)
            {
                if(s.endsWith(".bak"))
                    list.remove(s);
            }
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
                    Log.d("onItemClick", "Profile " + profile + " selected");
                    //openTextual(profile);
                    openGraphical(profile);
                }
            });
            lvProfiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                {
                    final String profile = adapter.getItem(position);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Delete profile " + profile+" ?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            File file = new File(getFilesDir().getPath() + "/../shared_prefs/" + profile + ".xml");
                            Log.d("", file.toString());
                            if (file.delete()) {
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                populateList();
                            } else {
                                Toast.makeText(getApplicationContext(), "Not deleted", Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                        }
                    });
                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return true;
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
        Log.d("openTextual","Profile Name: "+profile+"\n"+"URL: "+url+"\n"+"Username: "+user+"\n"+"Password: "+password+"\n"+"Database Name: "+DB+"\n"+"Port Name: "+port);
        Intent intent=new Intent(this,TextQuery.class);
        intent.putExtra("User",user);
        intent.putExtra("URL", url);
        intent.putExtra("DB",DB);
        intent.putExtra("Password",password);
        intent.putExtra("Port",port);
        startActivity(intent);
    }

    private void openGraphical(String profile)
    {
        SharedPreferences prefs= this.getSharedPreferences(profile, Context.MODE_PRIVATE);
        user=prefs.getString("User", "").toString();
        url=prefs.getString("URL", "");
        password=prefs.getString("Password", "");
        DB=prefs.getString("DB", "");
        port = prefs.getInt("Port", 5432);
        Log.d("openGraphical", "Profile Name: " + profile + "\n" + "URL: " + url + "\n" + "Username: " + user + "\n" + "Password: " + password + "\n" + "Database Name: " + DB + "\n" + "Port Name: " + port);
        Intent intent=new Intent(this,Graphical_query.class);
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
