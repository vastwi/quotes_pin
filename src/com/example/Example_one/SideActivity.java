package com.example.Example_one;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.example.Resources.QuotesServiceAdapters;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.quotes_pin.R;


/**
 * Created with IntelliJ IDEA.
 * User: jayachk
 * Date: 4/25/14
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */

public class SideActivity extends Activity implements View.OnClickListener {

        Button bt1;
        List<String> listList = new ArrayList<String>();
    private final QuotesServiceAdapters quotesServiceAdapters = new QuotesServiceAdapters();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.side);
            bt1 = (Button) findViewById(R.id.submit);
            bt1.setOnClickListener(this);

            String projects = quotesServiceAdapters.getProjects();
            try {
                JSONArray jsonArray = new JSONArray(projects);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    listList.add(jsonObject.getString("project"));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Get a reference to the AutoCompleteTextView in the layout
            AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.project);
// Get the string array
//            String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
//            ArrayAdapter<String> adapter =
//                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
            ArrayAdapter<String>adapter = new ArrayAdapter<String> (this,
                    android.R.layout.simple_list_item_1,listList);
            textView.setAdapter(adapter);
        }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
        public void onClick(View v)
        {

            EditText quote = (EditText) findViewById(R.id.quote);
            EditText name = (EditText) findViewById(R.id.name);
            EditText project = (EditText) findViewById(R.id.project);

            if(quote.getText().toString().length() < 1  || name.getText().toString().length() < 1 || project.getText().toString().length() < 1){
                Toast.makeText(getApplicationContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            } else {

            postData(quote.getText().toString(), name.getText().toString(), project.getText().toString());

            Toast.makeText(getApplicationContext(), "Quote saved", Toast.LENGTH_SHORT).show();

            quote.setText("");
            name.setText("");
            project.setText("");

            Intent i = new Intent(getApplicationContext(),MyActivity.class);
            startActivity(i);
            setContentView(R.layout.main);
            }
        }

        public void postData(String quote, String name, String project) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.16.4.16:8765/quote");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("project", project));
                nameValuePairs.add(new BasicNameValuePair("by_name", name));
                nameValuePairs.add(new BasicNameValuePair("quote",quote));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem item = menu.findItem(R.id.add_quote);
            item.setVisible(false);
            this.invalidateOptionsMenu();
            return true;
        }
        @Override
        public void onBackPressed() {
            Toast.makeText(getApplicationContext(), "Back disabled in App", Toast.LENGTH_SHORT).show();
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.list_quote:
                    Intent i = new Intent(getApplicationContext(),MyActivity.class);
                    startActivity(i);
                    setContentView(R.layout.main);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }


}
