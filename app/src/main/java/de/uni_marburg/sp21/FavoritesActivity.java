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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.sp21.controller.Favorite;
import de.uni_marburg.sp21.controller.FavoritesController;
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


public class FavoritesActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<Company> companies;
    FavoritesController favoritesController;
    //identifier used to distinguish a Favorite List from a "normal" Result List
    int identifier = 1;
    Location defaultLocation;


    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorties_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        defaultLocation = new Location("Initial Provider");

        setTitle(getString(R.string.favorites));
        favoritesController = new FavoritesController(this);


        try {

            companies = favoritesController.getFavoriteCompanies();
            recyclerView = findViewById(R.id.recyclerviewfav);
            //Companies are sent to the MyAdapter Class
            MyAdapter myAdapter = new MyAdapter(this, companies, intent, identifier, defaultLocation);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            companies = favoritesController.getFavoriteCompanies();
            recyclerView = findViewById(R.id.recyclerviewfav);


            Intent intent = getIntent();
            MyAdapter myAdapter = new MyAdapter(this, companies, intent, identifier, defaultLocation);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}