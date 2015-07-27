package com.example.kash.postgresql;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class NewTable extends ActionBarActivity
{
    EditText etTableName;
    RelativeLayout main;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_table);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("onClick","btnAdd Clicked");
                EditText et=new EditText(getApplicationContext());
                Spinner sp=new Spinner(getApplicationContext());
                etTableName=(EditText)findViewById(R.id.etTableName);
                etTableName.setId(1);
                et.setId(2);
                sp.setId(3);
                RelativeLayout.LayoutParams etParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams spParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                etParams.addRule(RelativeLayout.BELOW,1);
                spParams.addRule(RelativeLayout.BELOW,2);
                et.setLayoutParams(etParams);
                sp.setLayoutParams(spParams);
                main.addView(et);
                main.addView(sp);
            }
        });
        main=(RelativeLayout)findViewById(R.id.rlmain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_table, menu);
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
