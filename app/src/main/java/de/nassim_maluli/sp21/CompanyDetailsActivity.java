package de.uni_marburg.sp21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.List;

import de.uni_marburg.sp21.model.Message;


/**
 * This Class takes the Company Information it recieved from the MyAdapter
 * Class and packs it into an Activity for the User to see
 *
 * @author Luke
 */

public class CompanyDetailsActivity extends AppCompatActivity {

    TextView company_name, description, url, email, location, address, owner, ohc, pd, openhour,mess ;
    String CompanyName, Description, URL, Email, lat,log, Address, Owner, ID, OHC, PD, Geohash, Openhour;

    ArrayList<String> uris;
    ImageView picsImageView;
    int position = 0;


    String Messages;
    StorageReference storageReference;
    Picasso picasso;

    Location userCurrentLocation;
    Location CompanyLocation;
    double meters;

    Activity getActivity(){ return this;}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.companydetails));

        //dataBase ref
        FirebaseStorage db = FirebaseStorage.getInstance();
        storageReference = db.getReference();

        //getting picasso
        picasso = Picasso.get();
        picsImageView = findViewById(R.id.pics_imageView);
        //uris
        uris = (ArrayList<String>) getIntent().getSerializableExtra("ImagePath");

        displayImageAtPosition(position);

        //photo Gallery components
        ImageButton leftImageButton = findViewById(R.id.left_imageButton);
        ImageButton rightImageButton = findViewById(R.id.right_imageButton);


        CompanyLocation = new Location("Initial Provider1");
        userCurrentLocation = new Location("Initial Provider2");


        company_name = findViewById(R.id.company_name);
        mess = findViewById(R.id.messages);
        description = findViewById(R.id.description);
        url = findViewById(R.id.url);
        email = findViewById(R.id.email);
        location = findViewById(R.id.location);
        address = findViewById(R.id.address);
        owner = findViewById(R.id.owner);
        ohc = findViewById(R.id.opencomm);
        pd = findViewById(R.id.pd);
        openhour = findViewById(R.id.openhour);
        //TextViews are assigned to later be set to have the appropiate Information

        getData();
        //"gets" or unpacks the data sent to the Class via an Intent
        setData();
        //sets the data to the responding TextView in the corresponding Activity


        //imageGallery movements
        leftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0){
                    Log.d("these are the Uris >>>>>>>>>>>>",uris.toString());
                    position--;
                }else{
                    position = uris.size()-1;
                }
                displayImageAtPosition(position);
            }
        });


        rightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < uris.size()-1){
                    position++;
                }else{
                    position = 0;
                }
                displayImageAtPosition(position);
            }
        });


    }


    private void getData(){
        if(getIntent().hasExtra("CompanyName") && getIntent().hasExtra("Description")) {

            CompanyName = getIntent().getStringExtra("CompanyName");
            Description = getIntent().getStringExtra("Description");
            URL = getIntent().getStringExtra("URL");
            Email = getIntent().getStringExtra("Email");
            Address = getIntent().getStringExtra("Address");
            Owner = getIntent().getStringExtra("Owner");
            lat = getIntent().getStringExtra("CompanyLatitude");
            log = getIntent().getStringExtra("CompanyLongitude");
            ID = getIntent().getStringExtra("ID");
            OHC = getIntent().getStringExtra("Opening Hours Comments");
            PD = getIntent().getStringExtra("Products Description");
            Geohash = getIntent().getStringExtra("Geo Hash");
            Openhour = getIntent().getStringExtra("Opening Hours");
            Messages = getIntent().getStringExtra("Messages");


            // All the data is unpacked and assigned to a String Value.


            //nassim changes
            ArrayList<Location> locations = (ArrayList<android.location.Location>) getIntent().getSerializableExtra("locations");
            if(locations.get(0).getLatitude() != 0.0) {
                userCurrentLocation = locations.get(0);


                CompanyLocation.setLatitude(Double.valueOf(lat));
                CompanyLocation.setLongitude(Double.valueOf(log));

                Log.d("Here goes the Coordinates for user...", userCurrentLocation.getLatitude() + "," + userCurrentLocation.getLongitude());
                Log.d("Here goes the Coordinates for the company...", CompanyLocation.getLatitude() + "," + CompanyLocation.getLongitude());
                meters = (int) userCurrentLocation.distanceTo(CompanyLocation);

            }else{
                meters = 0.0;
            }

        }else{
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        //If something goes wrong and no data is received an Error message is shown to the User

    }

    private void setData(){

        //The String values, particularly Open Hour and Message have been modified to look better and be less confusing
        //Additionally, not all values are avaiable for all Companies, thus this issue has been addressed (see Distance and Message)
        company_name.setText(CompanyName);
        description.setText(Description);
        if(URL == null){
            url.setText(getString(R.string.urlstring));
        }else {
            url.setText("URL: " + URL);
        }
        email.setText(getString(R.string.emailadress) + " " + Email);
        if(meters != 0.0){
            location.setText(getString(R.string.distancestring) + " " +meters / 1000 + " km");
        }else{
            String coordinates = getString(R.string.latstring) + " " + lat + "\n" + getString(R.string.longstring) + " " + log;
            location.setText(coordinates);
        }
        address.setText(Address.replaceAll("Address", getString(R.string.address) + " "));
        owner.setText(getString(R.string.ownerstring) + " " + Owner);
        ohc.setText(OHC);
        pd.setText(PD);
        final String re = Pattern.quote("[{");
        final String re2 = Pattern.quote("}]");
        final String re3 = Pattern.quote("{");
        final String re4 = Pattern.quote("}");

        openhour.setText(Openhour.replaceAll("null", getString(R.string.statusstring)).replaceAll(re,"").replaceAll(re2,"")
                .replaceAll("start=",getString(R.string.openstring) + " ").replaceAll("end=",getString(R.string.closestring) + " ")
                .replaceAll("Monday",getString(R.string.monday)).replaceAll("Tuesday",getString(R.string.tuesday))
                .replaceAll("Wednesday",getString(R.string.wednesday)).replaceAll("Thursday", getString(R.string.thursday))
                .replaceAll("Friday",getString(R.string.friday)).replaceAll("Saturday", getString(R.string.saturday))
                .replaceAll("Sunday",getString(R.string.sunday)).replaceAll("OpeningHours",getString(R.string.openinghours))
                .replaceAll(re3,"").replaceAll(re4,""))
        ;
        if(Messages.length() < 4){
            mess.setText(getString(R.string.messagestring));
        }else {
            mess.setText(Messages.substring(1,Messages.length() -1).replaceAll(", ","").replaceAll("Message of the Day", getString(R.string.messageday))
                    .replaceAll("Date of posting", getString(R.string.postingdate)));
        }


        //Every text view in the UI-Activity is assigned its corresponding String Value


    }

    public void displayImageAtPosition(int position){
        StorageReference imagePathRef;
        String ImagePath = uris.get(position);
        List<String> urls = new ArrayList<>();

            imagePathRef = storageReference.child(ImagePath);
            imagePathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    picasso.load(uri.toString()).into(picsImageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

    }
}