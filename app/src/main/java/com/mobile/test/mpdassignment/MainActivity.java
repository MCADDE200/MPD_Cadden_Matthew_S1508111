package com.mobile.test.mpdassignment;


//
// Name                 Matthew Cadden
// Student ID           S1508111
// Programme of Study   Computer Games (Software Development)
//


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;



import java.io.InputStream;


import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    //private Button startButton;
    private String result;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    private ArrayList<EarthquakeDataClass> earthquakeList;

    public ArrayList<String> titles;
    public ArrayList<String> descriptions;
    public ArrayList<String> displayList;


    private ListView scrollList;
    private Spinner spinner1;
    private Button sortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Evaluation", "Sets The layout to be portrait");
            setContentView(R.layout.activity_main);
        } else {
            Log.d("Evaluation", "Sets The layout to be landscape");
            setContentView(R.layout.activity_landscape);
        }

        earthquakeList = new ArrayList<EarthquakeDataClass>();
        titles = new ArrayList<String>();
        descriptions = new ArrayList<String>();

        scrollList = (ListView) findViewById(R.id.scrollList);
        sortButton = (Button) findViewById(R.id.btnSubmit);
        sortButton.setOnClickListener(this);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();



        new GetEarthquakeData().execute();





        /*setContentView(R.layout.activity_main);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);

        setContentView(R.layout.recyclerview_activity);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterClass(earthquakeList);
        recyclerView.setAdapter(adapter);*/

    }

    public void onClick(View aview) {

    }


    public class GetEarthquakeData extends AsyncTask<String, Void, String> {


        private String url = urlSource;
        private XmlPullParserFactory xmlFactoryObject;
        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Get Earthquake Information from XML");
            pDialog.setMessage("Loading...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(17500);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream stream = connection.getInputStream();

                xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser pullParser = xmlFactoryObject.newPullParser();

                pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                pullParser.setInput(stream, null);
                String result = parseXML(pullParser);
                stream.close();

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("AsyncTask", "exception");
                return null;
            }
        }

        public String parseXML(XmlPullParser pullParser) {

            int event;
            String text = null;
            String result = "";
            boolean itemCheck = false;


            try {
                event = pullParser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name = pullParser.getName();

                    switch (event) {
                        case XmlPullParser.START_TAG:
                            if (name.equals("item")) {
                                itemCheck = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = pullParser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            //get country name
                            if (itemCheck) {
                                if (name.equals("title")) {
                                    result = text;
                                    titles.add(text);
                                }
                                if (name.equals("description")) {
                                    result = text;
                                    descriptions.add(text);
                                    SplitEarthquakeDescription(text);
                                }
                            }
                            if (name.equals("item")) {
                                itemCheck = false;
                            }
                            break;
                    }
                    event = pullParser.next();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            DisplayData();
        }

        public void callBackData(String result) {


        }
    }

    public void SplitEarthquakeDescription(String descriptionsText) {

        float latitude;
        float longitude;
        float magnitude;
        int depth;
        String date;
        String time;
        String location;

        String[] s = descriptionsText.split(";");


        latitude = Float.parseFloat(s[2].substring(11, 17).trim());
        longitude = Float.parseFloat(s[2].substring(18, 24).trim());
        magnitude = Float.parseFloat(s[4].substring(12, 16).trim());
        depth = Integer.parseInt(s[3].substring(7, 10).trim());
        date = s[0].substring(18, 34);
        time = s[0].substring(35, 43);
        location = s[1].split(":")[1];
        location = location.trim();


        EarthquakeDataClass earthquake = new EarthquakeDataClass(latitude, longitude, magnitude, depth, date, time, location);
        earthquakeList.add(earthquake);
    }

    public void DisplayData() {
        final ArrayList<String> displayList = new ArrayList<String>();
        for (int i = 0; i < earthquakeList.size(); i++) {
            displayList.add(earthquakeList.get(i).location + " Magnitude of " + earthquakeList.get(i).magnitude);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, displayList);
        scrollList.setAdapter(adapter);

        scrollList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this,
                        "Location : " + String.valueOf(earthquakeList.get(i).location)
                        + "\nDate : " + earthquakeList.get(i).date
                        + "\nTime : " + earthquakeList.get(i).time
                        +"\nLatitude : " + String.valueOf(earthquakeList.get(i).latitude)
                        +"\nLongitude : " + String.valueOf((earthquakeList.get(i).longitude))
                        +"\nDepth : " + String.valueOf(earthquakeList.get(i).depth)
                        +"\nMagnitude : " + String.valueOf((earthquakeList.get(i).magnitude)),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        sortButton = (Button) findViewById(R.id.btnSubmit);

        sortButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<EarthquakeDataClass> temp = new ArrayList<EarthquakeDataClass>();

                switch (String.valueOf((spinner1.getSelectedItem()))) {

                    case "Magnitude":
                        temp = MagnitudeSort();
                        break;
                    case "North":
                        temp = NorthSort();
                        break;
                    case "South":
                        temp = NorthSort();
                        Collections.reverse(temp);
                        break;
                    case "East":
                        temp = WestSort();
                        Collections.reverse(temp);
                        break;
                    case "West":
                        temp = WestSort();
                        break;
                    case "Deepest":
                        temp = DeepSort();
                        break;
                }
                displayList = new ArrayList<String>();
                for (int i = 0; i < temp.size(); i++) {
                    displayList.add(temp.get(i).location + " Magnitude of " + temp.get(i).magnitude);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, displayList);
                scrollList.setAdapter(adapter);
            }
        });
    }



    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public ArrayList<EarthquakeDataClass> MagnitudeSort()
    {
        ArrayList<EarthquakeDataClass> temp = earthquakeList;
        for (int i = 0; i < temp.size(); i++)
        {
            for (int j = 1; j < (temp.size() - i); j++)
            {
                if (temp.get(j-1).magnitude > temp.get(j).magnitude)
                {
                    EarthquakeDataClass tempF = temp.get(j);
                    temp.set((j), temp.get(j-1));
                    temp.set((j-1), tempF);
                }
            }
        }
        return temp;
    }

    public ArrayList<EarthquakeDataClass> NorthSort()
    {
        ArrayList<EarthquakeDataClass> temp = earthquakeList;
        for (int i = 0; i < temp.size(); i++)
        {
            for (int j = 1; j < (temp.size() - i); j++)
            {
                if (temp.get(j-1).latitude < temp.get(j).latitude)
                {
                    EarthquakeDataClass tempF = temp.get(j-1);
                    temp.set((j-1), temp.get(j));
                    temp.set((j), tempF);
                }
            }
        }
        return temp;
    }

    public ArrayList<EarthquakeDataClass> WestSort()
    {
        ArrayList<EarthquakeDataClass> temp = earthquakeList;
        for (int i = 0; i < temp.size(); i++)
        {
            for (int j = 1; j < (temp.size() - i); j++)
            {
                if (temp.get(j-1).longitude > temp.get(j).longitude)
                {
                    EarthquakeDataClass tempF = temp.get(j-1);
                    temp.set((j-1), temp.get(j));
                    temp.set((j), tempF);
                }
            }
        }
        return temp;
    }

    public ArrayList<EarthquakeDataClass> DeepSort()
    {
        ArrayList<EarthquakeDataClass> temp = earthquakeList;
        for (int i = 0; i < temp.size(); i++)
        {
            for (int j = 1; j < (temp.size() - i); j++)
            {
                if (temp.get(j-1).depth < temp.get(j).depth)
                {
                    EarthquakeDataClass tempF = temp.get(j-1);
                    temp.set((j-1), temp.get(j));
                    temp.set((j), tempF);
                }
            }
        }
        return temp;
    }
}