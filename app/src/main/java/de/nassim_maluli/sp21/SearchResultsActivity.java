package de.uni_marburg.sp21;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.location.Location;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.sp21.controller.FavoritesController;
import de.uni_marburg.sp21.controller.LocationSearchController;
import de.uni_marburg.sp21.model.Company;
import android.location.Location;
import de.uni_marburg.sp21.model.Message;

/**
 * Gets an Intent from the Search Activity and makes an Array
 * of Companies. This Array of Comapnies is sent with a RecyclerView
 * and an Identifier to the MyAdapter Class
 *
 * @author Luke
 */

public class SearchResultsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Company> companies;
    //List from  SearchActivity Location search
    ArrayList<Company> companiesFilteredByLocation;
    Integer identifier = 0;
    public FavoritesController fc;
    //O is for the Results List, 1 is for the Favorites List. With
    //an Identifier only one MyAdapter Class is needed reducing Redundancy
    ArrayList<Location> locations;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fc = new FavoritesController(this);
        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        setTitle(getString(R.string.results));


        locations = (ArrayList<Location>) getIntent().getSerializableExtra("userLocation");
        Location distanceLocation = new Location("Initial Provider");
        if(locations != null) {
            distanceLocation = locations.get(0);
        }

        companies = (ArrayList<Company>) intent.getSerializableExtra("results");
        // Array of Companies is assigned

        companiesFilteredByLocation = (ArrayList<Company>) intent.getSerializableExtra("companiesFilteredByLocation");


        recyclerView = findViewById(R.id.recyclerview);

        if (companies != null){
            MyAdapter myAdapter = new MyAdapter(this, companies, intent, identifier,distanceLocation);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if(companiesFilteredByLocation != null) {
            // added a new adapter to be displayed by the rec.View
            MyAdapter LocationSearchResultsAdapter = new MyAdapter(this, companiesFilteredByLocation, intent, identifier,distanceLocation);
            recyclerView.setAdapter(LocationSearchResultsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

}