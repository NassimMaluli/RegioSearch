package de.uni_marburg.sp21;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import de.uni_marburg.sp21.controller.Favorite;
import de.uni_marburg.sp21.controller.FavoritesController;
import de.uni_marburg.sp21.model.Company;

import android.location.Location;
import de.uni_marburg.sp21.model.Message;
import de.uni_marburg.sp21.controller.LocationSearchController;


/**
 * MyAdapter implements the ViewHolder for the Search Results Activity or the
 * Favorites Activity. It then sends all the Company Information (Company Name, Location, Opening Hours, ect..)
 * to the Company Details Activity
 *
 * @author Luke
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Intent intent1;
    ArrayList<Company> companies1;
    Context context;
    Integer identifier;
    // identifier is used to distinguish between a Favorite List and a Results List
    Location distanceLocation;

    public MyAdapter(Context ct, ArrayList<Company> companies, Intent intent, Integer identiy, Location userLocation) {

        intent1 = intent;
        companies1 = companies;
        context = ct;
        identifier = identiy;
        distanceLocation = userLocation;


    }


    @NonNull
    @Override
    //This Method populates the Viewholder, either with my_row (Results List)
    // or with my_row_favorites for the Favorite List
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (identifier == 1) {
            View view = inflater.inflate(R.layout.my_row_favorites, parent, false);
            // Favorites list is filled with a Row with a "Remove Favorite" button
            return new MyViewHolder(view);
        } else if (identifier == 0) {
            View view = inflater.inflate(R.layout.my_row, parent, false);
            // Results List get a "Add to Favorites" Button
            return new MyViewHolder(view);
        }else
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mytext.setText(companies1.get(position).getName());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyDetailsActivity.class);
                intent.putExtra("CompanyName", companies1.get(position).getName());
                intent.putExtra("Description", companies1.get(position).getDescription());
                intent.putExtra("Owner", companies1.get(position).getOwner());
                intent.putExtra("URL", companies1.get(position).getUrl());
                intent.putExtra("Email", companies1.get(position).getMail());
                intent.putExtra("Address", companies1.get(position).getAddress().toString());
                intent.putExtra("Messages", companies1.get(position).getMessages().toString());

                //ImagePath
                ArrayList<String> uris = new ArrayList<String>();
                uris = (ArrayList<String>) companies1.get(position).getImagePaths();
                intent.putExtra("ImagePath", uris);


                //should be separated from the other list because of compatibility problems
                intent.putExtra("CompanyLatitude", companies1.get(position).getLocation().getLat().toString());
                intent.putExtra("CompanyLongitude", companies1.get(position).getLocation().getLon().toString());


                intent.putExtra("Opening Hours Comments", companies1.get(position).getOpeningHoursComments());
                intent.putExtra("Products Description", companies1.get(position).getProductsDescription());
                intent.putExtra("Opening Hours", companies1.get(position).getOpeningHours().toString());

                //intent.putExtra("Image", companies1.get(position).imagePaths);

                //When the User clicks on a Company, all the Company information will be put in an intent
                //and sent to the CompanyDetailsActivity to be shown to the User



                ArrayList<Location> locations = new ArrayList<>();
                locations.add(distanceLocation);
                intent.putExtra("locations",locations);


                //if the activity is the FavoritesList, and the clicked company has new messages,
                //the favorite has to be updated to no longer show the notification
                if(identifier == 1){
                    FavoritesController fc = ((FavoritesActivity)getActivity()).favoritesController;
                    if(fc.companiesWithNewMessages().contains(companies1.get(position))){
                        fc.resetMessages(companies1.get(position));
                    }
                }

                context.startActivity(intent);
            }
        });

        if (identifier == 0){
            FavoritesController fc = ((SearchResultsActivity)getActivity()).fc;
            if (fc.isFavorite(companies1.get(position))){
                holder.favoritesButton.setVisibility(View.GONE);
            } else {
                holder.favoritesButton.setOnClickListener(v -> {
                    fc.addFavorite(companies1.get(position));
                    fc.close();
                    holder.favoritesButton.setVisibility(View.GONE);
                });
            }
        } else if (identifier == 1){
            FavoritesActivity activity =(FavoritesActivity) getActivity();
            FavoritesController fc = activity.favoritesController;

            //notification logic
            ImageView notification = holder.itemView.findViewById(R.id.notification_icon);
            ArrayList<Company> companiesWithNewMessages = fc.companiesWithNewMessages();

            if(!companiesWithNewMessages.contains(companies1.get(position))){
                notification.setVisibility(View.GONE);
            }


            holder.favoritesButton.setOnClickListener(v -> {
                fc.removeFavorite(companies1.get(position));
                fc.close();
                activity.reload();
            });
        }

    }

    @Override
    public int getItemCount() {
        if (companies1 == null){
            return 0;
        }
        return companies1.size();
        //returns the amount of companies to be presented
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button favoritesButton;
        TextView mytext;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mytext = itemView.findViewById(R.id.company_name);
            // Every child of the View Holder is assinged its unique Company Name
            mainLayout = itemView.findViewById(R.id.mainLayout);
            favoritesButton = itemView.findViewById(R.id.favorites);
        }
    }

    private Activity getActivity() {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

}



