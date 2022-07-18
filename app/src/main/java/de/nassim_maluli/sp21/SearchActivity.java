package de.uni_marburg.sp21;


import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import java.util.ArrayList;
import de.uni_marburg.sp21.controller.SearchController;
import de.uni_marburg.sp21.model.Company;
import de.uni_marburg.sp21.model.Organization;

/**
 * Main View for the search of companies.
 *
 * @author chris
 */

public class SearchActivity extends AppCompatActivity {

    // declaring the request codes for the Permission requests
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;
   // private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 2;


    private static String TAG = "SearchActivity";
    ArrayList<Company> companies = new ArrayList<>();
    ArrayList<Company> results = new ArrayList<>();
    ArrayList<String> filters = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<Organization> selectedOrganizations = new ArrayList<>();





    SearchController searchController = new SearchController(this);



    private Activity getActivity() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle(getString(R.string.search));

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_activity));


        ImageView categoriesButton = findViewById(R.id.imageView1);
        ImageView filterButton = findViewById(R.id.imageView2);
        ImageView favoritesButton = findViewById(R.id.imageView3);
        ImageView LocationSearchButton = findViewById(R.id.imageView4);



        searchController.init().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                companies = searchController.companies;
                if (filters == null) {
                    results = companies;
                }


                categoriesButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), SearchCategoriesActivity.class);
                    startActivity(intent);
                });

                filterButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), SearchFilterActivity.class);
                    intent.putExtra("organizations", searchController.organizations);
                    startActivity(intent);
                });

                favoritesButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                    startActivity(intent);
                });

                LocationSearchButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), LocationSearchActivity.class);
                    startActivity(intent);
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //filter
                        searchController.setFilters(filters);
                        searchController.setSelectedOrganizations(selectedOrganizations);
                        ArrayList<Company> filterResults = searchController.filterCompanies();

                        //search

                        searchController.setCategories(categories);
                        ArrayList<Company> searchResults = searchController.performSearch(filterResults, query);
                        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                        //sends the filtered results to the Search Results Activity
                        intent.putExtra("results", searchResults);
                        startActivity(intent);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }



                });
            } else {
                Log.e(TAG, "error initialising searchController", task.getException());
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();


        if (intent.getSerializableExtra("filters") != null){
            filters = (ArrayList<String>)intent.getSerializableExtra("filters");
        }

        if(intent.getSerializableExtra("categories") != null) {
            categories = (ArrayList<String>) intent.getSerializableExtra("categories");
        }

        if(intent.getSerializableExtra("selectedOrganizations") != null) {
            selectedOrganizations = (ArrayList<Organization>) intent.getSerializableExtra("selectedOrganizations");
        }


        /*
        searchController.init().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                companies = searchController.companies;
                searchController.setCompanies(companies);
                if(!(filters == null)) {
                    Log.d("FILTERS", filters.toString());

                    searchController.setFilters(filters);

                    //doesn't work
                    //Log.d("!!COMPANIES", companies.toString());
                    results = searchController.filterCompanies();
                    //Log.d("**FILTERRESULTS**", results.toString());
                    Log.d("NUMBER OF RESULTS: ", String.valueOf(results.size()));

                }
            } else {
                Log.e(TAG, "error initialising searchController", task.getException());
            }
        });
         */
    }

}