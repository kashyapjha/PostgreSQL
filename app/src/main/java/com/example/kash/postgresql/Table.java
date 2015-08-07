package com.example.kash.postgresql;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Table extends ActionBarActivity
{
    String connurl;
    String tablename;
    Connection conn;
    public String table[][];
    public int numberOfRows;
    public int numberOfColumns;
    TableLayout tableLayout;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        connurl=getIntent().getStringExtra("Connection URL");
        tablename=getIntent().getStringExtra("Table");
        Log.d("Table", "Connection URL: " + connurl + "\n Table: " + tablename);
        frameLayout= (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.draw(new Canvas());
        tableLayout= (TableLayout) findViewById(R.id.tableLayout);
        connect();
    }
    private void fillTable(final int x, final int y, final String[][] matrix, TableLayout table)
    {
        Log.d("","fillTable started");
        table.removeAllViews();
        for (int i = 0; i < x; i++)
        {
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < y; j++)
            {
                EditText edit = new EditText(getApplicationContext());
                edit.setInputType(EditorInfo.TYPE_NULL);
                edit.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                edit.setText((matrix[i][j]));
                edit.setTextColor(Color.BLACK);
                edit.setKeyListener(null);
                row.addView(edit);
            }
            table.addView(row);
        }
        Log.d("", "fillTable stopped");
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
                Statement st=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                st.setFetchSize(100);
                ResultSet rs=st.executeQuery("select * from " + tablename);
                ResultSetMetaData rsmd = rs.getMetaData();
                numberOfColumns = rsmd.getColumnCount();
                rs.last();
                numberOfRows=rs.getRow();
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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.d("","Number of rows: "+numberOfRows+", Number of columns: "+numberOfColumns);
                        fillTable(numberOfRows, numberOfColumns, table, tableLayout);
                    }
                });
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

