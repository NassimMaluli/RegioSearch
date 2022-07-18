package de.uni_marburg.sp21.controller;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.uni_marburg.sp21.model.Company;

/**
 * Class to handle favorites of the user
 *
 * @author chris
 */
public class FavoritesController {

    Gson gson = new Gson();

    Activity activity;

    ArrayList<Favorite> favorites;
    Boolean hasChanged;

    //initialise a cache file to store favorites
    String filename = "favorites";

    /**
     * Constructor for FavoritesController
     * @param activity activity which creates the FavoritesController
     */
    public FavoritesController(Activity activity) {
        this.activity = activity;
        hasChanged = false;
        init();
    }

    /**
     * adds a given company to the favorites
     * @param company the company to add to the favorites
     */
    public void addFavorite(Company company) {
        if(!isFavorite(company)) {
            favorites.add(new Favorite(company));
            saveToFile();
            hasChanged = true;
        }
    }

    /**
     * removes a given company from the favorites
     * @param company the comapny to remove from the favorites
     */
    public void removeFavorite(Company company) {
        Favorite favorite = new Favorite(company);
        if(favorites.contains(favorite)){
            favorites.remove(favorite);
            hasChanged = true;
        }
    }

    /**
     * gets the list of favorite companies
     * @return the list of favorite companies
     * @throws IOException
     */
    public ArrayList<Company> getFavoriteCompanies() throws IOException {
        favorites = getFavorites();
        ArrayList<Company> result = new ArrayList<>();
        for (Favorite f : favorites){
            result.add(f.company);
        }
        return result;
    }

    /**
     * gets the list of favorites from a JSON File
     * @return the list of favorites
     * @throws IOException
     */
    public ArrayList<Favorite> getFavorites() throws IOException {
        File file = new File(activity.getFilesDir(), filename);
        this.favorites = new ArrayList<>();
        if (file.exists()) {
            JsonReader reader = new JsonReader(new FileReader(file));
            Type type = new TypeToken<ArrayList<Favorite>>() {
            }.getType();
            this.favorites = gson.fromJson(reader, type);
            reader.close();
        }

        return favorites;
    }

    /**
     * empties the list of favorites
     */
    public void deleteFavorites() {
        favorites = new ArrayList<>();
        hasChanged = true;
    }

    /**
     * closes the favoriteController (if changes were made, these are save din the favorites json file.)
     */
    public void close(){
        if(hasChanged){
            saveToFile();
            init();
        }
    }

    private void init(){
        File file = new File(activity.getFilesDir(), filename);
        if (!file.exists()){
            favorites = new ArrayList<>();
        }
        else {
            try {
                favorites = getFavorites();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        File file = new File(activity.getFilesDir(), filename);
        String json = gson.toJson(favorites);
        try {
            FileOutputStream fos = activity.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isFavorite(Company company) {
        int companyID = company.getId();
        for(Favorite f: favorites){
            if(f.company.getId() == companyID){
                return true;
            }
        }
        return false;
    }

    public boolean update(ArrayList<Company> newCompanies) {
        ArrayList<Favorite> newFavorites = new ArrayList<>();
        boolean res = false;
        for (Favorite f : favorites){
            for(Company c : newCompanies){
                if (c.equals(f.company)){
                    Favorite newFav;
                    if (f.checkForNewMessages(c)){
                        newFav = new Favorite(c, true);
                        res = true;
                    } else {
                        newFav = f;
                    }
                    newFavorites.add(newFav);
                }
            }
        }
        this.favorites = newFavorites;
        saveToFile();
        return res;
    }

    public ArrayList<Company> companiesWithNewMessages() {
        ArrayList<Company> res = new ArrayList<>();
        for (Favorite f : favorites){
            if(f.newMessages){
                res.add(f.company);
            }
        }
        return res;
    }

    public void resetMessages(Company c){
        for (Favorite f: favorites){
            if(f.company.equals(c)){
                f.newMessages = false;
                saveToFile();
                return;
            }
        }
    }
    //getter, setter
}

