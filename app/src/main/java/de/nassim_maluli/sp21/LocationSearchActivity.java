package de.uni_marburg.sp21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.sp21.controller.LocationSearchController;
import de.uni_marburg.sp21.controller.SearchController;
import de.uni_marburg.sp21.model.Company;

/**
 * Main View for the search of companies.
 *
 * @author chris
 */

public class LocationSearchActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    // declaring the request codes for the Permission requests
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;


    ArrayList<Company> companies = new ArrayList<>();



    private Activity getActivity() {
        return this;
    }



    //initiate all switches
    Switch UseCurrentLocationSwitch;
    Switch EnterLocationSwitch;


    //textViews that changes
    TextView LocationTextView;


    //the Map widget
    //the Maps
    MapView mapView;
    GoogleMap map;
    //LatLng coordinates to be displayed
    LatLng currentLatLng;
    //Default location
    Location defaultLocation;

    //global radius variable
    private double radius = 0.0;

    //Geocoder Components for location search
    private Geocoder geocoder;
    private String geocoderCity;
    private String geocoderAddress;

    //Map UI Components
    ImageButton zoomOutButton;
    ImageButton zoomInButton;
    ImageButton centerMapButton;
    //circles drown on the map
    ArrayList<Circle> circles;

    //searchView UI Widget components
    //searchViews to get the searched location as a text
    SearchView CityOrPostalCodeSearchView;
    SearchView addressSearchView;
    //checkButtons
    ImageView checkCityImageView;
    ImageView checkAddressImageView;
    //buttons
    Button findLocationButton;
    Button showFilteredCompaniesOnMapButton;
    Button showInListButton;


    //Location Tracker
    LocationManager locationManager;

    //Radius setting Options
    SeekBar radiusSeekBar;
    TextView radiusTextView;

    //companies filtered by location search
    private ArrayList<Company> companiesFilteredByLocation;

    //all Layouts
    //linearLayout containing components of location setting
    LinearLayout locationSettingOptions;

    //linearLayout containing indication of current location
    LinearLayout currentLocationInfo;

    //linearLayout containing components to enter desired Location
    LinearLayout enterLocationLayout;

    //the layout showing radius setting components
    LinearLayout radiusLayout;

    //the layout containing the mapView
    LinearLayout mapLayout;

    //layout with a button to show the companies within a radius
    LinearLayout showFilteredCompaniesOnMapLayout;

    //linear layout with the original buttons
    LinearLayout originalButtonsLayout;

    //linear layout with the show in list button
    LinearLayout showInListLayout;




    LocationSearchController locationSearchController = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        getSupportActionBar().setTitle(getString(R.string.locationsearch));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SearchController searchController = new SearchController(this);



        locationSearchController = LocationSearchController.getInstance(this);


        //initiate Switches
        UseCurrentLocationSwitch = findViewById(R.id.UseCurrentLocation_switch);
        EnterLocationSwitch = findViewById(R.id.EnterLocation_switch);

        //google Map
        mapView = findViewById(R.id.mapView);
        //location manager to get the location of the user
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        //default display location
        defaultLocation = new Location("initial Provider");
        defaultLocation.setLatitude(50.807869);
        defaultLocation.setLongitude(8.766196);
        geocoder = new Geocoder(this);

        //initializing the zoomIn and zoomOut buttons
        zoomInButton = findViewById(R.id.zoomIn_button);
        zoomOutButton = findViewById(R.id.zoomOut_button);
        centerMapButton = findViewById(R.id.centerMap_button);


        //initializing the searchViews to collect data as a text
        CityOrPostalCodeSearchView = findViewById(R.id.CityOrPostalCode_searchView);
        addressSearchView = findViewById(R.id.address_searchView);
        //initializing their checkSigns
        checkCityImageView = findViewById(R.id.checkCity_imageView);
        checkCityImageView.setVisibility(View.GONE);
        checkAddressImageView = findViewById(R.id.checkAddess_imageView);
        checkAddressImageView.setVisibility(View.GONE);
        //init Buttons
        findLocationButton = findViewById(R.id.findLocation_button);
        findLocationButton.setVisibility(View.GONE);
        showFilteredCompaniesOnMapButton = findViewById(R.id.showFilteredCopmaniesOnMap_button);
        showInListButton = findViewById(R.id.showInList_button);


        //initialize textViews that change
        LocationTextView = findViewById(R.id.Location_textView);

        //initializing the seekBar radius components
        radiusSeekBar = findViewById(R.id.Radius_seekBar);
        radiusSeekBar.setProgress(0);
        radiusSeekBar.setMax(100);

        //initializing the textView responsible for displaying the progress on the seekBar
        radiusTextView = findViewById(R.id.radius_textView);
        radiusTextView.setText(String.valueOf(radiusSeekBar.getProgress()));


        //Radius Circles : are all the circles drawn on the map
        circles = new ArrayList<Circle>();


        //companies filtered by location
        companiesFilteredByLocation = new ArrayList<Company>();


        //initializing all containerComponents
        locationSettingOptions = findViewById(R.id.locationSettingOptions);
        currentLocationInfo = findViewById(R.id.currentLocationInfo);
        enterLocationLayout = findViewById(R.id.enterLocation);
        radiusLayout = findViewById(R.id.radius_layout);
        mapLayout = findViewById(R.id.map_layout);
        showInListLayout = findViewById(R.id.showInList_linearLayout);
        showFilteredCompaniesOnMapLayout = findViewById(R.id.showFilteredCompaniesOnMap_layout);

        //Hide interactive menus
        currentLocationInfo.setVisibility(View.GONE);
        enterLocationLayout.setVisibility(View.GONE);
        radiusLayout.setVisibility(View.GONE);
        mapLayout.setVisibility(View.GONE);
        showFilteredCompaniesOnMapLayout.setVisibility(View.GONE);
        showInListLayout.setVisibility(View.GONE);

        //initializing the map view with a google map from google map services
        mapView.getMapAsync(this::onMapReady);
        mapView.onCreate(savedInstanceState);


        searchController.init().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        companies = searchController.companies;



                        // get the User's actual Location and use it in the search
                        UseCurrentLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {

                                    EnterLocationSwitch.setChecked(false);
                                    LocationTextView.setText("current location :");
                                    radiusLayout.setVisibility(View.VISIBLE);
                                    showFilteredCompaniesOnMapLayout.setVisibility(View.VISIBLE);
                                    mapLayout.setVisibility(View.VISIBLE);
                                    currentLocationInfo.setVisibility(View.VISIBLE);


                                    //our default location now is the fetched actual location
                                    locationSearchController.fetchCurrentLocation(locationManager);


                                    //a feature to show all companies in a 100 meter Radius upon initialization
                                    companiesFilteredByLocation.clear();
                                    companiesFilteredByLocation = locationSearchController.filterCompaniesByLocation(100, companies, defaultLocation);
                                    displayFilteredCompaniesOnMap(companiesFilteredByLocation);

                                } else {

                                    enterLocationLayout.setVisibility(View.GONE);
                                    radiusLayout.setVisibility(View.GONE);
                                    showFilteredCompaniesOnMapLayout.setVisibility(View.GONE);
                                    showInListLayout.setVisibility(View.GONE);
                                    currentLocationInfo.setVisibility(View.GONE);
                                    mapLayout.setVisibility(View.GONE);
                                    map.clear();
                                    radiusSeekBar.setProgress(0);
                                }
                            }
                        });

                        //Enter the  Location manually and search it
                        EnterLocationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    UseCurrentLocationSwitch.setChecked(false);
                                    enterLocationLayout.setVisibility(View.VISIBLE);
                                    findLocationButton.setVisibility(View.VISIBLE);
                                    mapLayout.setVisibility(View.VISIBLE);
                                    currentLocationInfo.setVisibility(View.VISIBLE);
                                    LocationTextView.setText("location");
                                    map.clear();
                                } else {
                                    enterLocationLayout.setVisibility(View.GONE);
                                    radiusLayout.setVisibility(View.GONE);
                                    findLocationButton.setVisibility(View.GONE);
                                    mapLayout.setVisibility(View.GONE);
                                    showFilteredCompaniesOnMapLayout.setVisibility(View.GONE);
                                    showInListLayout.setVisibility(View.GONE);
                                    currentLocationInfo.setVisibility(View.GONE);
                                    map.clear();
                                    radiusSeekBar.setProgress(0);
                                }
                            }
                        });



                        //Map UI onClick methods

                        zoomInButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                map.animateCamera(CameraUpdateFactory.zoomBy(1));
                            }
                        });

                        zoomOutButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                map.animateCamera(CameraUpdateFactory.zoomBy(-1));
                            }
                        });
                        centerMapButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LatLng currentCoordinates = new LatLng(defaultLocation.getLatitude(), defaultLocation.getLongitude());
                                //center the camera and refresh the focus
                                refreshMap();
                            }
                        });


                        //interactive Elements when typing in a text (city and postal code)
                        CityOrPostalCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (CityOrPostalCodeSearchView.getQuery() != "") {
                                    checkCityImageView.setVisibility(View.VISIBLE);
                                    geocoderCity = CityOrPostalCodeSearchView.getQuery().toString();
                                }
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                checkCityImageView.setVisibility(View.GONE);
                                return false;
                            }
                        });

                        //interactive Elements when typing in a text (address)
                        addressSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (addressSearchView.getQuery() != "") {
                                    checkAddressImageView.setVisibility(View.VISIBLE);
                                    geocoderAddress = addressSearchView.getQuery().toString();
                                }
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                checkAddressImageView.setVisibility(View.GONE);
                                return false;
                            }
                        });


                        //adding the findLocationButton to initiate the Geocoder search
                        findLocationButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (checkCityImageView.getVisibility() == View.VISIBLE) {

                                    //the search String contains the city name and the address with out the spaces on the sides
                                    String searchString = geocoderCity + "," + geocoderAddress;
                                    searchString.trim();

                                    try {
                                        //init the List that contains all the addresses that the geocoder returns
                                        List<Address> addresses = geocoder.getFromLocationName(searchString, 1);


                                        if (addresses.size() > 0) {

                                            //the only result we need
                                            Address foundLocation = addresses.get(0);

                                            // new latitude and longitudes
                                            LatLng foundLocationCoordinates = new LatLng(foundLocation.getLatitude(), foundLocation.getLongitude());

                                            //updating the default location
                                            defaultLocation.setLatitude(foundLocation.getLatitude());
                                            defaultLocation.setLongitude(foundLocation.getLongitude());


                                            //refreshing the map to show new location
                                            refreshMap();

                                            //new coordinated new names to show
                                            String locationName = null;
                                            locationName = locationSearchController.decodeNameFromLocation(foundLocationCoordinates,geocoder);
                                            LocationTextView.setText("Location : " + locationName);

                                            //activating the radius search widget to enter the radius circumference
                                            if (LocationTextView.getText() != "Unknown Location") {


                                                    //toggling the radius search widget
                                                    radiusLayout.setVisibility(View.VISIBLE);
                                                    showFilteredCompaniesOnMapLayout.setVisibility(View.VISIBLE);


                                                    //a feature to show all companies in a 100 meter Radius upon initialization
                                                    companiesFilteredByLocation.clear();
                                                    companiesFilteredByLocation = locationSearchController.filterCompaniesByLocation(100, companies, defaultLocation);
                                                    displayFilteredCompaniesOnMap(companiesFilteredByLocation);

                                            }

                                        } else {

                                            //if the List of addresses is empty i.e. no such place was found then return this message
                                            Toast.makeText(getActivity(), "Invalid Location", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } else {

                                    //user did not submit the query in the searchViews
                                    Toast.makeText(getActivity(), "Please submit City and postal code", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                        //the showFilteredCompaniesOnMap button on click listener : responsible for showing markers on the map representing the filtered companies
                        showFilteredCompaniesOnMapButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //filtering the companies coordinates within a radius
                                companiesFilteredByLocation.clear();
                                companiesFilteredByLocation = locationSearchController.filterCompaniesByLocation(radius, companies, defaultLocation);
                                displayFilteredCompaniesOnMap(companiesFilteredByLocation);
                                showInListLayout.setVisibility(View.VISIBLE);
                            }
                        });


                        showInListButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if (companiesFilteredByLocation.size() > 0) {


                                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                                    intent.putExtra("companiesFilteredByLocation", companiesFilteredByLocation);
                                    ArrayList<Location> locations = new ArrayList<>();
                                    locations.add(defaultLocation);
                                    intent.putExtra("userLocation",locations);
                                    startActivity(intent);

                                }

                            }
                        });


                        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                //setting the seekBar options to increment Kilometers
                                progress = progress / 5;
                                progress = progress * 5;

                                //setting the radius
                                radius = progress * 1000;

                                //showing the radius in KM on the textView
                                radiusTextView.setText(String.valueOf("" + progress));

                                //drawing the Radius Circle on the map
                                //our coordinates for the center of the circle are the coordinates of the current location (whether specified by GPS or address input)
                                LatLng coordinates = new LatLng(defaultLocation.getLatitude(), defaultLocation.getLongitude());

                                //remove every Circle before adding a new one
                                removeCircles();

                                //drawCircle(coordinates, progress);
                                drawCircle(coordinates, radius);

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationSearchController.fetchCurrentLocation(locationManager);
            }else{
                Toast.makeText(getActivity(),"Permission Denied : Cannot determine Location ...",Toast.LENGTH_SHORT).show();
            }
        }
    }




    /**
     * draws a specific radius specified Circle that represents a given radius
     * @param coordinates center of the drawn circle
     * @param radius the radius of the drawn circle
     */
    public void drawCircle(LatLng coordinates, double radius){


        Circle circle = map.addCircle(new CircleOptions()
                .center(coordinates)
                .radius(radius)
                .strokeColor(Color.BLUE)
                .fillColor(Color.TRANSPARENT));

        circles.add(circle);
        //Log.d("we have >> ",circles.size()+"");
    }


    // a method to clear all circles drawn on the map to give the illusion of continuity
    public void removeCircles(){
        // for every drawn circle delete it
        for(int i = 0; i < circles.size(); i++){
            circles.get(i).remove();
            circles.remove(circles.get(i));
        }
    }



    // refreshing the map to show new location
    public void refreshMap(){

        //clearing the map before any
        map.clear();
        LatLng newCoordiantes = new LatLng(defaultLocation.getLatitude(), defaultLocation.getLongitude());
        //Log.d("Refresh map Coo : ",newCoordiantes.toString());

        //setting the marker to show location and zooming the camera to the target location
        map.addMarker(new MarkerOptions().position(newCoordiantes).title(LocationTextView.getText().toString()).snippet("This is the center of the search"));
        map.moveCamera(CameraUpdateFactory.newLatLng(newCoordiantes));
        map.animateCamera(CameraUpdateFactory.zoomTo(8));
    }


    /**
     * this method displays all filtered companies locations on the map
     * @param companiesFilteredByLocation gets an ArrayList of LatLng coordinates
     */
    public void displayFilteredCompaniesOnMap(ArrayList<Company> companiesFilteredByLocation){

        //clear of all markers and circles
        map.clear();
        removeCircles();
        //refresh for current location
        refreshMap();
        //draws the radius circle after clearing the map
        drawCircle(new LatLng(defaultLocation.getLatitude(),defaultLocation.getLongitude()),radius);

        if(companiesFilteredByLocation.size() > 0) {

            //for every LatLng coordinate we display a marker
            for (Company c : companiesFilteredByLocation) {

                LatLng companyCoordinates = new LatLng(c.getLocation().getLat(),c.getLocation().getLon());


                map.addMarker(new MarkerOptions().position(companyCoordinates).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(c.name).snippet(c.description));

            }

        }else{

            Toast.makeText(getActivity(),"No Companies found in this Radius ..",Toast.LENGTH_SHORT).show();

        }
    }



    //Location-Change Listeners
    @Override
    public void onLocationChanged(@NonNull Location location) {


        //setting the defaultLocation on every update
        defaultLocation.setLatitude(location.getLatitude());
        defaultLocation.setLongitude(location.getLongitude());


        //refresh the map with the new location
        refreshMap();


        //finally we decode or reverse geocode the Location name (current Location name)

        //getting the current locationName in the locationTextView
        String locationName = "failed to fetch Name";

        //has to be implemented in a try - catch Bloch because of the Null-Pointer Exception

        try {

            locationName = locationSearchController.decodeNameFromLocation(new LatLng(defaultLocation.getLatitude(),defaultLocation.getLongitude()),geocoder);



        } catch (IOException e) {
            e.printStackTrace();
        }


        LocationTextView.setText("Location : "+locationName);

    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        //initializing the map
        map = googleMap;

        // the location to be displayed on the map
        currentLatLng = new LatLng(defaultLocation.getLatitude(), defaultLocation.getLongitude());

        //adding a marker to the location that is desired
        map.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(8));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState( Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

    }

}