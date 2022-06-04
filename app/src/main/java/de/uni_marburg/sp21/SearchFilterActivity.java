package de.uni_marburg.sp21;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import de.uni_marburg.sp21.model.Organization;


/**
 * This activity is designed to widen the user's Selection of Company-related Information.
 * SearchFilterActivity uses a range of components to obtain the user's Preferences regarding
 * special features of a Company or specific information about the nature of its operations
 * or products
 * @author Nassim
 *
 */

public class SearchFilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * @return the current context or activity
     */
    private Activity getActivity(){
        return this;
    }

    // these Tokens are representatives of the data selected by the User

    String[] productCatTokens = {"vegetables", "fruits", "meat", "meatproducts",
            "cereals", "milk", "eggs", "honey", "beverages", "bakedgoods", "pasta",
            "MilkProducts"};
    String[] companyTypeTokens = {"producer", "shop", "hotel", "restaurant", "mart"};

    String[] serviceTokens = {"currentlyOpen", "delivery"};

    String[] weekTokens = {"monday","tuesday","wednesday","thursday","friday","saturday"};


    /**
     * Organisations From previous Activity(search activity)
     */
    ArrayList<Organization> organizations;




    /**
     * Component of the search Widget
     */
    ListView listViewOrg;
    ListView productListView;
    ListView companyListView;
    SearchView searchView;
    ArrayAdapter<String> filterAdapter;
    ArrayAdapter<String> productsArrayAdapter;
    ArrayAdapter<String> companyArrayAdapter;


    /**
     * The three Spinners responsible for the date-Input
     */
    Spinner spinnerDays;
    Spinner spinnerFrom;
    Spinner spinnerTo;


    /**
     * checkBoxes responsible for activating and deactivating the specificOpeningHours - spinners
     */
    CheckBox currentlyOpen;
    CheckBox delivery;
    CheckBox specificOpeningHoursCheckBox;


    /**
     * Gets called upon clicking the submit Button and serves multiple functionalities.
     *
     * First: goes through all the CheckBoxes from the allCheckBoxes List and gets the Text
     * previously assigned to the CheckBox and adds it to filterRes.
     *
     *  Second:goes through the ListView-Adapter Items and checks what items are selected and adds them
     * to filterRes arrayList
     *
     *  Third: checks whether the CurrentlyOpen_checkBox is checked or not, gets the data from the three
     * spinners (Day, From, To) accordingly and then adds it to The filterRes list .
     *
     * finally the filterRes are sent to the Parent activity and the new activity starts.
     *
     * @param view
     */
    public void submit(View view){

        //Items Selected From CheckBoxes
        ArrayList<String> filterRes = new ArrayList<>();

        //Organizations to be sent to the parent activity i.e. SearchActivity
        ArrayList<Organization> selectedOrganizations = new ArrayList<Organization>();

        for(int i = 0; productsArrayAdapter.getCount() > i; i++) {

            if (productListView.isItemChecked(i)) {
                //every checked Item will get added to the List
                String selectedItem = productListView.getItemAtPosition(i).toString();
                filterRes.add(selectedItem);
            }
        }


        for(int i = 0; companyArrayAdapter.getCount() > i; i++) {

            if (companyListView.isItemChecked(i)) {
                //every checked Item will get added to the List
                String selectedItem = companyListView.getItemAtPosition(i).toString();
                filterRes.add(selectedItem);
            }
        }

        //Second: check what Organizations are Selected
        for(int i = 0; filterAdapter.getCount() > i; i++) {

            if (listViewOrg.isItemChecked(i)) {
                //every checked Item will get added to the List
                String selectedItem = listViewOrg.getItemAtPosition(i).toString();
                selectedOrganizations.add(getOrganizationObject(selectedItem));
            }
        }

        if(delivery.isChecked()){

            filterRes.add(serviceTokens[1]);
        }

        //Finally we Check  what Data has the Spinner For us

        if(currentlyOpen.isChecked()) {

            filterRes.add(serviceTokens[0]);
            //Toast.makeText(getActivity(),"isNotActivated",Toast.LENGTH_SHORT).show();


        }else{


            if(specificOpeningHoursCheckBox.isChecked()) {
                //getting the Values of selections from the three Spinners
                filterRes.add(weekTokens[spinnerDays.getSelectedItemPosition()]);
                filterRes.add(spinnerFrom.getSelectedItem().toString());
                filterRes.add(spinnerTo.getSelectedItem().toString());
            }

        }

        Log.d("Here goes the filters >>> ",filterRes.toString());
        Intent intent = new Intent(getActivity(),SearchActivity.class);
        intent.putExtra("filters",filterRes);
        intent.putExtra("selectedOrganizations",selectedOrganizations);
        startActivity(intent);


    }

    /**
     * This onClick method gets called when the CurrentlyOpen_checkBox is checked.
     * disables the three spinners : spinner_week, spinner_From, spinner_To when the CurrentlyOpen_checkBox is checked
     * @param view
     */
    public void disableSpecificOpeningTimes(View view){


        //hide the Specific Opening Hours if Currently Open checkBox is Ticked-On
        if(currentlyOpen.isChecked()) {

            disableSpinners();
            specificOpeningHoursCheckBox.setChecked(false);
            specificOpeningHoursCheckBox.setEnabled(false);

        }else {
            specificOpeningHoursCheckBox.setEnabled(true);
        }
    }



    /**
     * disables the three Spinners
     */
    void disableSpinners(){

        findViewById(R.id.spinner_week).setEnabled(false);
        findViewById(R.id.spinner_from).setEnabled(false);
        findViewById(R.id.spinner_to).setEnabled(false);
    }

    /**
     * enables the three Spinners
     */
    void enableSpinners(){

        findViewById(R.id.spinner_week).setEnabled(true);
        findViewById(R.id.spinner_from).setEnabled(true);
        findViewById(R.id.spinner_to).setEnabled(true);
    }



    /**
     * Gets the names of the Organisations from a given ArrayList<Organizations>.
     * @param Orgs an ArrayList of Organizations
     * @return ArrayList<String> with the organisations names
     */
    public ArrayList<String> getOrganizationsNames(ArrayList<Organization> Orgs){
        ArrayList<String> namesOfOrganisations = new ArrayList<String>();
        for(Organization o : Orgs){
            namesOfOrganisations.add(o.getName());
        }
        return namesOfOrganisations;
    }



    /**
     * This method returns an Object of Organization.
     * @param OrganisationName the Name of the organization that we are searching for.
     * @return the Organization with the according Name
     */
    public Organization getOrganizationObject(String OrganisationName){

        LinkedHashSet<Organization> OrgSet = new LinkedHashSet<Organization>(organizations);

        for(Organization o : OrgSet){
            if(o.getName().equals(OrganisationName)){
                return o;
            }
        }
        return null;
    }



    /**
     * The main method in the activity gets called upon creation of the activity.
     * Initializes certain elements of the activity like buttons, spinners, listView and sets their parameters.
     * Invokes certain methods to manage the different components.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        Button submit = findViewById(R.id.submit_Button);


        //titel des Actionbars
        getSupportActionBar().setTitle("Filter");

        //Back-Button Enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //disable the spinners and allow using them after Checking the specific opening times CheckBox
        disableSpinners();


        //initializing the CheckBoxes responsible for the specificOpeningHours - functionality
        currentlyOpen = findViewById(R.id.CurrentlyOpen_checkBox);
        delivery = findViewById(R.id.Delivery_checkBox);
        specificOpeningHoursCheckBox = findViewById(R.id.specificOpeningHours_checkBox);


        //Getting the Organisation List from the Previous Activity
        Intent intent = getIntent();
        organizations =  (ArrayList<Organization>) intent.getSerializableExtra("organizations");
        //Turning the ArrayList into a String Array and eliminating duplicates to put it in the ListView ArrayAdapter.



        ArrayList<String> temp = getOrganizationsNames(organizations);
        LinkedHashSet<String> OrgSet = new LinkedHashSet<>(temp);
        ArrayList<String> namesOfOrgs = new ArrayList<>(OrgSet);
        String [] adapterArray = new String[namesOfOrgs.size()];

        //adding the final List of unique Organizations names to the ListView Adapter
        adapterArray = namesOfOrgs.toArray(adapterArray);


        //experiment
        productListView = findViewById(R.id.product_listView);
        productsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,productCatTokens);
        productListView.setAdapter(productsArrayAdapter);
        //experiment
        companyListView = findViewById(R.id.companyTypes_listView);
        companyArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,companyTypeTokens);
        companyListView.setAdapter(companyArrayAdapter);
        //
        TextView availableProductsTextView = findViewById(R.id.availableProducts_textView);
        availableProductsTextView.setVisibility(View.GONE);
        TextView CompanyTypeTextView = findViewById(R.id.companyType_textView);
        CompanyTypeTextView.setVisibility(View.GONE);
        //
        LinearLayout availableProductsLinearLayout = findViewById(R.id.availableProducts_linearLayout);
        LinearLayout companyTypeLinearLayout = findViewById(R.id.companyType_linearLayout);
        LinearLayout organizationsLayout = findViewById(R.id.organizations_layout);
        LinearLayout specificOpeningTimesLayout = findViewById(R.id.specificOpeningTimes_layout);
        //
        availableProductsLinearLayout.setVisibility(View.GONE);
        companyTypeLinearLayout.setVisibility(View.GONE);
        organizationsLayout.setVisibility(View.GONE);
        specificOpeningTimesLayout.setVisibility(View.GONE);
        //ImageButtons on Top of the Screen
        ImageButton productCategoryButton = findViewById(R.id.productCategory_button);
        ImageButton companyTypeButton = findViewById(R.id.companyType_button);
        ImageButton organizationButton = findViewById(R.id.organization_button);
        ImageButton timePreferenceButton = findViewById(R.id.timePrefrence_button);


        ArrayList<ImageButton> ImageButtons = new ArrayList<>();
        ImageButtons.add(productCategoryButton);
        ImageButtons.add(companyTypeButton);
        ImageButtons.add(organizationButton);
        ImageButtons.add(timePreferenceButton);





        productCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(availableProductsLinearLayout.getVisibility() == View.VISIBLE){
                    productCategoryButton.setColorFilter(Color.DKGRAY);
                    availableProductsLinearLayout.setVisibility(View.GONE);
                }else {
                    activateImageButton(ImageButtons,productCategoryButton);
                    availableProductsLinearLayout.setVisibility(View.VISIBLE);
                    companyTypeLinearLayout.setVisibility(View.GONE);
                    organizationsLayout.setVisibility(View.GONE);
                    specificOpeningTimesLayout.setVisibility(View.GONE);
                }
            }
        });


        companyTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (companyTypeLinearLayout.getVisibility() == View.VISIBLE) {
                    companyTypeButton.setColorFilter(Color.DKGRAY);
                    companyTypeLinearLayout.setVisibility(View.GONE);
                } else {
                    activateImageButton(ImageButtons,companyTypeButton);
                    availableProductsLinearLayout.setVisibility(View.GONE);
                    companyTypeLinearLayout.setVisibility(View.VISIBLE);
                    organizationsLayout.setVisibility(View.GONE);
                    specificOpeningTimesLayout.setVisibility(View.GONE);
                }
            }
        });


        organizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (organizationsLayout.getVisibility() == View.VISIBLE) {
                    organizationButton.setColorFilter(Color.DKGRAY);
                    organizationsLayout.setVisibility(View.GONE);
                } else {
                    activateImageButton(ImageButtons,organizationButton);
                    availableProductsLinearLayout.setVisibility(View.GONE);
                    companyTypeLinearLayout.setVisibility(View.GONE);
                    organizationsLayout.setVisibility(View.VISIBLE);
                    specificOpeningTimesLayout.setVisibility(View.GONE);
                }
            }
        });



        timePreferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (specificOpeningTimesLayout.getVisibility() == View.VISIBLE) {
                    timePreferenceButton.setColorFilter(Color.DKGRAY);
                    specificOpeningTimesLayout.setVisibility(View.GONE);
                } else {
                    activateImageButton(ImageButtons,timePreferenceButton);
                    availableProductsLinearLayout.setVisibility(View.GONE);
                    companyTypeLinearLayout.setVisibility(View.GONE);
                    organizationsLayout.setVisibility(View.GONE);
                    specificOpeningTimesLayout.setVisibility(View.VISIBLE);

                }
            }
        });


        //Organisation SearchFilter Variables

        //init of the different SearchFilterVar
        listViewOrg = findViewById(R.id.OrganizationList_View);
        searchView = findViewById(R.id.Search_View);
        filterAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,adapterArray);
        listViewOrg.setAdapter(filterAdapter);
        listViewOrg.setVisibility(View.GONE);

        //Some SearchView Logic
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listViewOrg.setVisibility(View.VISIBLE);
                filterAdapter.getFilter().filter(newText);

                return false;
            }
        });

        //ListView of The Organisation Results are hidden when the searchView is Unfocused
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    listViewOrg.setVisibility(View.VISIBLE);
                }else{
                    listViewOrg.setVisibility(View.GONE);
                }
            }
        });


        // a listener to apply the specificOpeningHours Logic for user input
        specificOpeningHoursCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if(currentlyOpen.isEnabled()){

                        currentlyOpen.setEnabled(false);
                        currentlyOpen.setChecked(false);

                    }
                    enableSpinners();

                }else{
                    currentlyOpen.setEnabled(true);
                    disableSpinners();

                }
            }
        });

        // a listener to apply the specificOpeningHours Logic for user input
        currentlyOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    if(specificOpeningHoursCheckBox.isEnabled()){

                        specificOpeningHoursCheckBox.setChecked(false);
                        specificOpeningHoursCheckBox.setEnabled(false);
                    }
                    disableSpinners();
                }else{

                    specificOpeningHoursCheckBox.setEnabled(true);

                }

            }
        });


//**************************************************************************************************

        //Spinner Operations


        //Spinner for days
        spinnerDays = findViewById(R.id.spinner_week);
        ArrayAdapter<CharSequence> adapterDays = ArrayAdapter.createFromResource(this,R.array.daysOfTheWeek, android.R.layout.simple_spinner_item);
        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapterDays);
        spinnerDays.setOnItemSelectedListener(this);

        //Spinner form (Time)
        spinnerFrom = findViewById(R.id.spinner_from);
        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,R.array.times, android.R.layout.simple_spinner_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapterFrom);
        spinnerFrom.setOnItemSelectedListener(this);

        //Spinner form (Time)
        spinnerTo = findViewById(R.id.spinner_to);
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this,R.array.times, android.R.layout.simple_spinner_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapterTo);
        spinnerTo.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void activateImageButton(ArrayList<ImageButton> imageButtons, ImageButton imageButton){

        for(ImageButton i : imageButtons){
            if(i == imageButton){
                i.setColorFilter(Color.GREEN);
            }else{
                i.setColorFilter(Color.DKGRAY);
            }
        }
    }




}