package com.gryco.walking;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

public class DataTable extends AppCompatActivity {

    private ResultsViewModel resultsViewModel;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
   // ArrayList<Results> results;
    AppDatabase db;
    List<Results> results;
    String count;
    String points;
    String CountDay;

    Button saveData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_ofdata);

        RecyclerView recyclerView = findViewById(R.id.recycler_vie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);


        resultsViewModel = ViewModelProviders.of(this).get(ResultsViewModel.class);
        resultsViewModel.getAllResults().observe(this, new Observer<List<Results>>() {
            @Override
            public void onChanged(@Nullable List<Results> results) {
                adapter.setResults(results);

            }
        });

        //recyclerView = findViewById(R.id.recycler_view);

       /* results = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            Results result = new Results("7851","7","2018-09-01" + i);
           results.add(result);
      }*/
       /* Runnable rr = new Runnable() {
            @Override
            public void run() {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production").build();
                results =  db.resultsDao().getAllResults();

            }
        };
        Thread Roomdata = new Thread(rr);
        Roomdata.start();*/

        /*saveData = findViewById(R.id.savdate);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                count = mIntent.getStringExtra("Steps");
                points = mIntent.getStringExtra("Points");
                CountDay = mIntent.getStringExtra("Date");
                //db.resultsDao().insertAll(new Results(count,points,CountDay));
               // adapter = new UserAdapter(results);

            }
        });*/

        /*Intent mIntent = getIntent();
        count = mIntent.getStringExtra("Steps");
        points = mIntent.getStringExtra("Points");
        CountDay = mIntent.getStringExtra("Date");*/

        /*class insertAsyncTask extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class,"production").build();
                db.resultsDao().insertAll(new Results(count,points,CountDay));
                return null;
            }
        }*/

        /*@Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        */

        //db.resultsDao().insertAll(new Results(count,points,CountDay));
       // results =  db.resultsDao().getAllResults();

        //results = DataBuilder.db.resultsDao().getAllResults();

       /* recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(results);
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.data_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                resultsViewModel.deleteAll();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
