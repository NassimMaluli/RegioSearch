package de.uni_marburg.sp21.controller;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.uni_marburg.sp21.R;
import de.uni_marburg.sp21.SearchActivity;
import de.uni_marburg.sp21.model.Company;
import de.uni_marburg.sp21.model.Organization;

/**
 * Class to handle the database connection and backend for the SearchActivity.
 *
 * @author chris
 *
 * @see SearchActivity
 */

public class SearchController {

    Activity parent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "SearchController";
    public ArrayList<Company> companies = new ArrayList<>();
    public ArrayList<Organization> organizations = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> filters = new ArrayList<>();
    ArrayList<Organization> selectedOrganizations = new ArrayList<>();

    /**
     * constructor for SearchController
     * @param parent the activity which created the instance, should be the SearchActivity.
     */
    public SearchController(Activity parent) {
        this.parent = parent;
    }

    /**
     * gets the companies and organizations from the database.
     * @see  <a href=SearchController#getCompanies()>getCompanies()</a>
     * @see  <a href=SearchController#getOrganizations()>getCompanies()</a>
     * @return the task of initialising (to add an onCompleteListener)
     */
    public Task<QuerySnapshot> init() {
         return getCompanies().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //populate companiesList
                int i = 0;
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Company company = document.toObject(Company.class);
                    companies.add(company);
                    i++;
                    // uncomment next line to see results in the terminal
                    //Log.d(TAG, i + " : \n" + company.toString());
                }
                //Log.d("number of companies", String.valueOf(companies.size()));
                organizations = getOrganizations(companies);

                FavoritesController fC = new FavoritesController(parent);
                if(!fC.favorites.isEmpty()){
                    if(fC.update(companies)){
                        String toastText = parent.getString(R.string.searchcontroller_toast_new_messages);
                        Toast toast = Toast.makeText(parent.getApplicationContext(),toastText, Toast.LENGTH_SHORT);
                        toast.show();
                    } /* else is only for debugging
                    else{
                        String toastText = "Your favorites don't have new messages";
                        Toast toast = Toast.makeText(parent.getApplicationContext(),toastText, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    */
                }



            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    /**
     * gets all the companies from the database.
     * @return the task of getting the companies.
     */
    private Task<QuerySnapshot> getCompanies() {
        return db.collection("companies").get();
    }

    /**
     * gets all the organizations in a given list of companies.
     * @param companies the companies from which to get the organization names
     * @return an ArrayList of Organizations
     */

    private ArrayList<Organization> getOrganizations(ArrayList<Company> companies) {
        ArrayList<Organization> res = new ArrayList<>();
        for (Company c: companies) {
            res.addAll(c.getOrganizations());
        }
        return res;
    }

    //FILTERCONTROLLER
    /**
     * sets the filters with which the companies should later be filtered.
     * @param filters an ArrayList of Strings representing the filters.
     */

    public void setFilters(ArrayList<String> filters) {
        this.filters = filters;
    }

    /**
     * sets the companies which should later be filtered
     * @param companies an ArrayList of companies.
     */
    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void setSelectedOrganizations(ArrayList<Organization> organizations) {
        this.selectedOrganizations = organizations;
    }

    //work in progress
    public ArrayList<Company> performSearch(ArrayList<Company> comps, String query){
        String[] categoryTokens = {"Company name", "Owner name", "Types of companies", "Address of company", "Description of company",
                "Description of product", "Product group tags", "Opening hours comments", "Name of organization", "Messages of company"};
        ArrayList<String> chosenCategories = new ArrayList<>();
        if (categories.isEmpty()){
            categories.add("Company name");
        }
        for (String s : categoryTokens) {
            if (categories.contains(s)){
                chosenCategories.add(s);
                //Log.d("chosenCategories: ", chosenCategories.toString());
            }
        }
        ArrayList<Company> results = new ArrayList<>();
        int i = 0;
        for(Company c : comps){
            if(c.textSearch(query, chosenCategories)){
                i++;
                //Log.d("textSearch true", i + " times.");
                results.add(c);
            }
        }

        /*
        for debugging purposes
            Log.d("number of searchResults:", String.valueOf(results.size()));
            for(Company c : results){
                Log.d("searchResult", c.toString());
            }


        */

        return results;
    }

    /**
     * filters the companies based on the selected filters.
     * At the moment only the productCategory, companyType, deliveryService and currentlyOpen can be filtered.
     * The function to filter for specific opening hours and for specific organizations is still missing.
     * @return an ArrayList of companies with the results of the filters.
     */
    public ArrayList<Company> filterCompanies() {
        if (filters.isEmpty()){
            return companies;
        }

        String tag = "filterCompanies";
        String[] productCatTokens = {"vegetables", "fruits", "meat", "meatproducts",
                "cereals", "milk", "eggs", "honey", "beverages", "bakedgoods", "pasta",
                "MilkProducts"};
        String[] companyTypeTokens = {"producer", "shop", "hotel", "restaurant", "mart"};


        Boolean currentlyOpenChosen = false;
        boolean deliveryChosen = false;
        boolean openingHoursRelevant = false;

        //Log.d("FILTERS", filters.toString());

        // init results
        ArrayList<Company> results = new ArrayList<>();

        // populate productCatFilters
        ArrayList<String> productCatFilters = new ArrayList<>();

        for (String s: productCatTokens
        ) {
            if(filters.contains(s.toLowerCase())){
                productCatFilters.add(s.toLowerCase());
            }
        }
        //Log.d(tag, productCatFilters.toString());

        // populate companyTypeFilter
        ArrayList<String> companyTypeFilters = new ArrayList<>();
        for (String s: companyTypeTokens
        ) {
            if(filters.contains(s.toLowerCase())){
                companyTypeFilters.add(s.toLowerCase());
            }
        }

        //check service tokens
        if(filters.contains("delivery")){
            deliveryChosen = true;
        }

        HashMap<String, String> openingHours = new HashMap<>();

        if(filters.contains("currentlyOpen")){
            currentlyOpenChosen = true;
        } else {
            // filter for specific opening Hours
            String[] weekTokens = {"monday","tuesday","wednesday","thursday","friday","saturday", "sunday"};
            String weekDay = "";

            for(String s : weekTokens){
                if(filters.contains(s)){
                    weekDay = s;
                    openingHoursRelevant = true;
                }
            }
            if (openingHoursRelevant) {
                if(weekDay.length() == 0){
                    Log.e("filterCompanies()", "there is no day selected");
                    return null;
                }


                List<String> times = filters.stream()
                        .filter(s -> {return s.matches("[0-1][0-9]:[0-5][0-9]");})
                        .collect(Collectors.toList());
                if(times.size() != 2){
                    Log.e("filterCompanies()", "there was a problem while filtering openingHours");
                    return null;
                }

                String start = times.get(0);
                String end = times.get(1);
                openingHours.put("start", start);
                openingHours.put("end", end);
                openingHours.put("day", weekDay);
            }



        }


        // execute filter

        for (Company c : companies) {
            //Log.d("companies before filtering", c.toString());
            if(c.filterProductCat(productCatFilters) &&
                    c.filterCompanyType(companyTypeFilters) &&
                    c.currentlyOpen(currentlyOpenChosen) &&
                    c.filterDelivery(deliveryChosen) &&
                    c.filterOpeningHours(openingHours, openingHoursRelevant) &&
                    c.filterOrganizations(selectedOrganizations)) {
                results.add(c);
            }
        }
        Log.d("number of results:", String.valueOf(results.size()));
        return results;

    }


    //maybe another solution than the delays, write the companies to a json file to store locally.
    /*
    private void writeToCache(ArrayList<Company> content){

        try {
            String filename = "companies.json";
            Gson gson = new Gson();

            File cacheDir = parent.getApplicationContext().getCacheDir();
            FileOutputStream fos = new FileOutputStream(cacheDir.getPath() + "/" + filename);
            String json = gson.toJson(content);
            Log.d(TAG, "writing in cache");
            Log.d(TAG, json);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

     */
}
