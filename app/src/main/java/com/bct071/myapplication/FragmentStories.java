package com.bct071.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStories extends Fragment {


    private static final String SQLITE_TABLE_STORIES="Story";
    private static  int cstory_number;
    public MasterDbAdapter dbHelper;
    public SimpleCursorAdapter dataAdapter;
    ProgressDialog mProgressDialog;
    ListView listView;
    private  int test;

    Cursor cursor;
    int[] to = new int[] {
            R.id.text_code,
            R.id.text_title,
            R.id.text_description,

    };

    String[] columns=new String[] {
            MasterDbAdapter.KEY_CODE,
            MasterDbAdapter.KEY_TITLE,
            MasterDbAdapter.KEY_DESCRIPTION,

    };

    public FragmentStories() {
        test=0;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_poems, container, false);


        TextView textView=(TextView)view.findViewById(R.id.text_category);
        textView.setText("Stories");

        dbHelper = new MasterDbAdapter(getContext());
        dbHelper.open();



        //for button
        Button bt=(Button) view.findViewById(R.id.refresh_content);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JsoupListViewA().execute();
            }
        });


        cursor=dbHelper.fetchAllData(SQLITE_TABLE_STORIES);


        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                getContext(), R.layout.content_info,
                cursor,
                columns,
                to,
                0);

        listView = (ListView) view.findViewById(R.id.my_list_view);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // to get cursor ,row ko lagi

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String link =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));

                Intent i=new Intent(getContext(),ContentView.class)
                        .putExtra("url",link)
                        .putExtra("category",SQLITE_TABLE_STORIES)
                        .putExtra("description",cursor.getString(cursor.getColumnIndexOrThrow("description")));
                startActivity(i);

            }
        });

        EditText myFilter = (EditText) view.findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchDataByName(SQLITE_TABLE_STORIES,constraint.toString());
            }
        });



        return view;
    }

    private class JsoupListViewA extends AsyncTask<Void, Void, Void> {

        Connection con = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            con = Jsoup.connect("http://www.writenepal.com/archives/category/stories")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .timeout(180000)
                    .ignoreHttpErrors(true)
                    .followRedirects(true);

            Log.i("TAG", "CONNECTED!!!");
            //Document doc = Jsoup.connect("http://www.writenepal.com/archives/category/poems").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(60000).ignoreHttpErrors(true).followRedirects(true).get();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getContext());
            // Set progressdialog title
            mProgressDialog.setTitle("Write Nepal");
            // Set progressdialog message
            mProgressDialog.setMessage("Downloading contents...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCanceledOnTouchOutside(getRetainInstance());
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // arraylist = new ArrayList<HashMap<String, String>>();
           // Document doc = null;

            try {
                // Connect to the Website URL

              Document doc = con.get();

                Elements titles = doc.select("div.span8 h2");
                Elements descris = doc.select(".span8 p");

                Elements link=doc.select(".entry.archive-box h2>a");


                int l = titles.size();

                //Clean all data
                //dbHelper.deleteAllCountries();
                if(titles!=null &&descris!=null) {
                    dbHelper.deleteAllData(SQLITE_TABLE_STORIES);
                    test=1;
                    if(titles.size()==descris.size())
                    {
                        cstory_number=titles.size();
                    }
                    else{
                        cstory_number=descris.size();

                    }
                    for(int i=0;i<cstory_number;i++){
                        Log.i("data got",trimme(descris.get(i).text(),10));
                        //this should add fetched data in database
                        String a=titles.get(i).text();
                        String b=trimme(descris.get(i).text(),100);
                        String c= link.get(i).attr("href");

                        dbHelper.insertData(SQLITE_TABLE_STORIES,c,a,b);
                    }
                }




                Log.i("Tag","Database do back working");


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if(test==0){
                Snackbar.make(getView(), "Internet and I are not connected. Please Try Again", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

            cursor=dbHelper.fetchAllData(SQLITE_TABLE_STORIES);
            dataAdapter.notifyDataSetChanged();
            dataAdapter = new SimpleCursorAdapter(
                    getContext(), R.layout.content_info,
                    cursor,
                    columns,
                    to,
                    0);
            listView.setAdapter(dataAdapter);

            mProgressDialog.dismiss();


            //  dbHelper.close();

        }

    }
    public static String trimme(String s,int n){
        if(s.length()<n){

            return s;
        }
        else{
            return(s.subSequence(0, n)+"...");
        }

    }
}
