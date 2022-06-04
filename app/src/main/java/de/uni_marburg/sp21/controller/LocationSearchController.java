package de.uni_marburg.sp21.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.sp21.LocationSearchActivity;
import de.uni_marburg.sp21.model.Company;

public class LocationSearchController {




    private int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;

    Activity parent;


    public static LocationSearchController locationSearchController = null;



    //singleton
    private LocationSearchController(LocationSearchActivity parent){
            this.parent = parent;
    }



    public static LocationSearchController getInstance(LocationSearchActivity parent){
        if(locationSearchController == null){
            locationSearchController = new LocationSearchController(parent);
        }
        return locationSearchController;
    }





    public ArrayList<Company> filterCompaniesByLocation(double radius, ArrayList<Company> companies, Location defaultLocation){

        ArrayList<Company> companiesFilteredByLocation = new ArrayList<>();

        //end result of filtered coordinates
        ArrayList<LatLng> filteredCompaniesCoordinates = new ArrayList<LatLng>();


        //our current location coordinates to be compared with the companies coordinates and thereby filtered
        LatLng currentCoordinates = new LatLng(defaultLocation.getLatitude(), defaultLocation.getLongitude());

        if(companies.size() > 0) {


            for (Company c : companies) {

                //coordinates of each company
                LatLng companyCoordinates = new LatLng(c.getLocation().getLat(), c.getLocation().getLon());


                //the float array where the result of the next method of measuring the distance will be stored
                float[] distance = new float[1];

                //measuring the distance
                Location.distanceBetween(currentCoordinates.latitude, currentCoordinates.longitude, companyCoordinates.latitude, companyCoordinates.longitude, distance);

                //the radius we get is in meters and the calculated distance is also in meters
                if (distance[0] <= radius) {

                    companiesFilteredByLocation.add(c);
                }
            }

        }else {
            Toast.makeText(parent,"ERROR Connecting to Database cannot retrieve Results",Toast.LENGTH_SHORT).show();
        }

        return companiesFilteredByLocation;
    }




    public void fetchCurrentLocation(LocationManager locationManager) {

        Location newLocation = new Location("initial provider");
        //we check if we already have a permission to access the User's location data: Coarse and Fine
        if (ActivityCompat.checkSelfPermission(parent,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){



            //getting the current location
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) parent);


        } else {

            requestLocationPermission();

            // return defaultLocation;

        }

    }


    private void requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(parent,Manifest.permission.ACCESS_FINE_LOCATION)){

            new AlertDialog.Builder(parent)
                    .setTitle("Location Permission needed")
                    .setMessage("Location Permission is needed to enable the Location search functionality without it we can't determine your location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(parent, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_ACCESS_FINE_LOCATION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();


        }else{
            //requesting permission in case we don't need to show the permission rationale
            ActivityCompat.requestPermissions(parent, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_ACCESS_FINE_LOCATION);
        }

    }


    public String decodeNameFromLocation(LatLng coordinates, Geocoder geocoder) throws IOException {
        //the location name to be shown
        String locationName;

        //found addresses that correspond to the latLng
        List<Address> addresses = geocoder.getFromLocation(coordinates.latitude,coordinates.longitude,1);
        Log.d("found addresses : >>> ",addresses.get(0).toString());

        if(addresses.size() > 0){
            //acquiring the location name from the returned list of addresses
            locationName = addresses.get(0).getLocality()+","+addresses.get(0).getCountryName();
        }else{
            locationName = "Unknown Location";
        }
        return locationName;
    }





}
