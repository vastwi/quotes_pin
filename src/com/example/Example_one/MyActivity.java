package com.example.Example_one;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import com.example.Grid.StaggeredGridView;
import com.example.Resources.Data;
import com.example.Resources.DataAdapter;
import com.example.Resources.NothingSelectedSpinnerAdapter;
import com.example.Resources.QuotesServiceAdapters;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//public class MyActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
public class MyActivity extends ActionBarActivity {
    private final QuotesServiceAdapters quotesServiceAdapters = new QuotesServiceAdapters();
    /**
     * Called when the activity is first created.
     */

    private Spinner spinner;
    List<String> listList = new ArrayList<String>();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
//    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String projects = quotesServiceAdapters.getProjects();
        try {
            JSONArray jsonArray = new JSONArray(projects);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listList.add(jsonObject.getString("project"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//        mTitle = "Project List";

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, listList));
// Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.filter_2, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Select Project");
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        spinner = (Spinner) findViewById(R.id.spinner);
//        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.spinner_layout, listList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setPrompt("Select Project");
//
//        spinner.setAdapter(
//                new NothingSelectedSpinnerAdapter(
//                        adapter,
//                        R.layout.spinner_layout,
//                        this)
//        );
//        spinner.setOnItemSelectedListener(this);

        final EditText edittext = (EditText) findViewById(R.id.search);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    LinearLayout mainView = (LinearLayout) findViewById(R.id.right);
//                    mainView.removeAllViews();

//                    spinner.setAdapter(
//                            new NothingSelectedSpinnerAdapter(
//                                    adapter,
//                                    R.layout.spinner_layout,
//                                    MyActivity.this)
//                    );

                    String response = null;
                    try {
                        response = quotesServiceAdapters.searchFor(edittext.getText().toString().trim());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    DisplayResponse(response);

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        String response = quotesServiceAdapters.recentQuotes();
        DisplayResponse(response);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.list_quote);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_quote:
                Intent i = new Intent(getApplicationContext(),SideActivity.class);
                startActivity(i);
                setContentView(R.layout.side);
                return true;
            default:
                if (mDrawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }

//    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//
//        if (position > 0) {
//            EditText edittext = (EditText) findViewById(R.id.search);
//            edittext.setText("");
//            LinearLayout mainView = (LinearLayout) findViewById(R.id.right);
////        mainView.removeAllViewsInLayout();
//            String projectName = (String) spinner.getSelectedItem();
//            String response = quotesServiceAdapters.quotesByProject(projectName);
//            DisplayResponse(response);
//        }
//    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back disabled in App", Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    public void DisplayResponse(String response) {
//        LinearLayout listView = (LinearLayout) findViewById(R.id.right);
        StaggeredGridView mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        final ArrayList<Data> datas = new ArrayList<Data>();
        try {
            JSONArray responseArray = new JSONArray(response);
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject jsonObject = responseArray.getJSONObject(i);
                Data data = new Data();
                data.imageUrl = "https://jiresal-test.s3.amazonaws.com/deal3.png";
                data.quote = jsonObject.getString("quote");
                data.name = jsonObject.getString("by_name");
                data.project_name = jsonObject.getString("project") ;
                datas.add(data);
            }
        }

            catch(Exception e){
                e.printStackTrace();
            }
        DataAdapter adapter= new DataAdapter(this, R.layout.quote_row, datas);

        mGridView.setAdapter(adapter);
        }

    private void selectItem(int position) {
//        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

// Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position,true);
        setTitle(R.string.app_name);
        mDrawerLayout.closeDrawer(mDrawerList);
        EditText edittext = (EditText) findViewById(R.id.search);
        edittext.setText("");
        String response = quotesServiceAdapters.quotesByProject(listList.get(position));
        DisplayResponse(response);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}


