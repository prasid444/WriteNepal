package com.bct071.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOffline extends Fragment {


    private static final String SQLITE_TABLE_OFFLINE = "Offline";
    public MasterDbAdapter dbHelper;
    public SimpleCursorAdapter dataAdapter;
    ListView listView;
    Cursor cursor;

    Button button;


    int[] to = new int[]{
            R.id.text_title,
            R.id.text_description,

    };

    String[] columns = new String[]{
            MasterDbAdapter.KEY_TITLE,
            MasterDbAdapter.KEY_DESCRIPTION,

    };
    //private MenuInflater menuInflater;

    public FragmentOffline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poems, container, false);



        TextView textView = (TextView) view.findViewById(R.id.text_category);
        textView.setText("Offline Mode");



        dbHelper = new MasterDbAdapter(getContext());
        dbHelper.open();

        cursor = dbHelper.fetchAllSavedData(SQLITE_TABLE_OFFLINE);
        dataAdapter = new SimpleCursorAdapter(
                getContext(), R.layout.content_info,
                cursor,
                columns,
                to,
                0);

        listView = (ListView) view.findViewById(R.id.my_list_view);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // to get cursor ,row ko lagi

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                Intent i = new Intent(getContext(), OfflineView.class)
                        .putExtra("_title", cursor.getString(cursor.getColumnIndexOrThrow("titles")))
                        .putExtra("_source", cursor.getString(cursor.getColumnIndexOrThrow("source")));
                startActivity(i);

            }
        });


        EditText myFilter = (EditText) view.findViewById(R.id.myFilter);
        myFilter.setVisibility(View.GONE);

        button = (Button) view.findViewById(R.id.refresh_content);
        button.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_one:
                dbHelper.deleteOffline(info.id);
                Toast.makeText(getContext(),"Data deleted successfully",Toast.LENGTH_SHORT).show();
                cursor=dbHelper.fetchAllSavedData(SQLITE_TABLE_OFFLINE);
                dataAdapter.notifyDataSetChanged();
                dataAdapter = new SimpleCursorAdapter(
                        getContext(), R.layout.content_info,
                        cursor,
                        columns,
                        to,
                        0);
                listView.setAdapter(dataAdapter);
                return true;
            case R.id.delete_all:

                new AlertDialog.Builder(getContext())
                        .setTitle("Write Nepal")
                        .setMessage("Are you sure you want to delete all saved data?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                dbHelper.deleteAllData(SQLITE_TABLE_OFFLINE);
                                Toast.makeText(getContext(),"All data deleted successfully",Toast.LENGTH_SHORT).show();
                                cursor=dbHelper.fetchAllSavedData(SQLITE_TABLE_OFFLINE);
                                dataAdapter.notifyDataSetChanged();
                                dataAdapter = new SimpleCursorAdapter(
                                        getContext(), R.layout.content_info,
                                        cursor,
                                        columns,
                                        to,
                                        0);
                                listView.setAdapter(dataAdapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show();




                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}


