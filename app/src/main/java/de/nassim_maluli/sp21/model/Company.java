package de.uni_marburg.sp21.model;

import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Model Class for companies, exactly like they are stored on firestore.
 *
 * @author chris
 * @see OpeningHours
 * @see Address
 * @see Location
 * @see Organization
 * @see ProductGroup
 * @see Message
 */

public class Company implements Serializable {
    public Integer id;
    public String name, description, mail, url, owner, openingHoursComments, productsDescription, geoHash;
    public Address address;
    public Location location;
    public OpeningHours openingHours;
    public List<String> types;
    public List<Organization> organizations;
    public List<Message> messages;
    public List<ProductGroup> productGroups;
    public List<String> imagePaths;
    public boolean deliveryService;


    /**
     * no arg constructor (for some reason needed by firestore)
     */
    public Company() {
        //no arg constructor
    }

    /**
     * constructor for a company
     * @param id id of the company
     * @param name name of the company
     * @param description description of the company
     * @param mail mail address to reach the company
     * @param url website of the company
     * @param owner owner of the company
     * @param openingHoursComments comments regarding the opening hours
     * @param productsDescription description of the products sold by the company
     * @param geoHash useful for geo queries at some point
     * @param address address of the company
     * @param location location (lon lat) of the company
     * @param openingHours opening hours of the company
     * @param types type of company (producer, restaurant, etc.)
     * @param organizations organizations to which the company is affiliated
     * @param messages messages from the company
     * @param productGroups product groups offered by the company
     * @param deliveryService true if the company offers a delivery service
     */

    public Company(int id,
                   String name,
                   String description,
                   String mail,
                   String url,
                   String owner,
                   String openingHoursComments,
                   String productsDescription,
                   String geoHash,
                   Address address,
                   Location location,
                   OpeningHours openingHours,
                   List<String> types,
                   List<Organization> organizations,
                   List<Message> messages,
                   List<ProductGroup> productGroups,
                   boolean deliveryService) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mail = mail;
        this.url = url;
        this.owner = owner;
        this.openingHoursComments = openingHoursComments;
        this.productsDescription = productsDescription;
        this.geoHash = geoHash;
        this.address = address;
        this.location = location;
        this.openingHours = openingHours;
        this.types = types;
        this.organizations = organizations;
        this.messages = messages;
        this.productGroups = productGroups;
        this.deliveryService = deliveryService;
    }


    /**
     * constructs a string to represent company data
     * @return a string holding all information about a company.
     */
    @Override
    public String toString() {
        return "Company{" + ",\n" +
                "id=" + id + ",\n" +
                "name='" + name + '\'' + ",\n" +
                "description='" + description + '\'' + ",\n" +
                "mail='" + mail + '\'' + ",\n" +
                "url='" + url + '\'' + ",\n" +
                "owner='" + owner + '\'' + ",\n" +
                "openingHoursComments='" + openingHoursComments + '\'' + ",\n" +
                "productsDescription='" + productsDescription + '\'' + ",\n" +
                "geoHash='" + geoHash + '\'' + ",\n" +
                "address=" + address + ",\n" +
                "location=" + location + ",\n" +
                "openingHours=" + openingHours + ",\n" +
                "types=" + types + ",\n" +
                "organizations=" + organizations + ",\n" +
                "messages=" + messages + ",\n" +
                "productGroups=" + productGroups + ",\n" +
                "deliveryService=" + deliveryService + ",\n" +
                "imagePaths=" + imagePaths + ", \n" +
                '}';
    }


    /**
     * id getter
     * @return id of the company
     */
    public Integer getId() {
        return id;
    }

    /**
     * name getter
     * @return name of the company
     */
    public String getName() {
        return name;
    }

    /**
     * organization getter
     * @return List of organizations of the company
     */
    public List<Organization> getOrganizations() {
        return organizations;
    }

    /**
     * determines if the company matches the product category filters.
     * @param categoryFilters the selected filters for the categories.
     * @return true if company matches filters
     */
    public boolean filterProductCat(ArrayList<String> categoryFilters) {
        if(categoryFilters.isEmpty()){
            //Log.d("productCat", "is empty");
            return true;
        }

        if(productGroups == null || productGroups.isEmpty() ){
            return false;
        }
        List<ProductGroup> matched = this.productGroups.stream()
                .filter(productGroup -> categoryFilters.contains(productGroup.getCategory()))
                .collect(Collectors.toList());

        if (!matched.isEmpty()) {
            return true;
        } else {return false;}
    }

    /**
     * determines if company matches the company type filters.
     * @param companyTypeFilters the filters for the company types.
     * @return true if company matches filters.
     */
    public boolean filterCompanyType(ArrayList<String> companyTypeFilters) {
        if (companyTypeFilters.isEmpty()){
            //Log.d("companyTypeFilters", "is empty");
            return true;
        }
        if(types == null || types.isEmpty()){
            return false;
        }

        List<String> matched = this.types.stream().filter(type -> companyTypeFilters.contains(type)).collect(Collectors.toList());

        if (!matched.isEmpty()) {
            return true;
        } else {return false;}
    }

    /**
     * determines if the company is currently open (only if the currentlyOpen filter is selected)
     * @param selected is the currentlyOpen filter selected?
     * @return true if company is currently open or currentlyOpen-filter isn't selected.
     */
    public boolean currentlyOpen(Boolean selected){
        String TAG = "currentlyOpenFilter";
        if(!selected) {
            return true;
        }

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY) + 2;
        int minute = cal.get(Calendar.MINUTE);
        //Log.d(TAG, "day: " + day + "hour: " + hour + "minute: " + minute);

        ArrayList<HashMap<String, String>> openingHoursToday;

        switch (day) {
            case Calendar.MONDAY:
                openingHoursToday = this.openingHours.getMonday();
                break;
            case Calendar.TUESDAY:
                openingHoursToday = this.openingHours.getTuesday();
                break;
            case Calendar.WEDNESDAY:
                openingHoursToday = this.openingHours.getWednesday();
                break;
            case Calendar.THURSDAY:
                openingHoursToday = this.openingHours.getThursday();
                break;
            case Calendar.FRIDAY:
                openingHoursToday = this.openingHours.getFriday();
                break;
            case Calendar.SATURDAY:
                openingHoursToday = this.openingHours.getSaturday();
                break;
            case Calendar.SUNDAY:
                openingHoursToday = this.openingHours.getSunday();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + day);
        }


        if(openingHoursToday == null) {
            return false;
        }
        String startToday = openingHoursToday.get(0).get("start");
        int startHour = Integer.parseInt(startToday.substring(0, 2));
        int startMinute = Integer.parseInt(startToday.substring(3, 5));
        String endToday = openingHoursToday.get(0).get("end");
        int endHour = Integer.parseInt(endToday.substring(0,2));
        int endMinute = Integer.parseInt(endToday.substring(3,5));

        //Log.d(TAG, "openingHours.toString() " + startToday);

        if(hour < startHour || hour > endHour) {
            return false;
        } else if(hour > startHour && hour < endHour){
            return true;
        } else if (hour == startHour) {
            return minute < startMinute;
        } else if (hour == endHour) {
            return minute < endMinute;
        }
        else {return false;}
    }

    /**
     * determines if the company offers delivery (only if the delivery filter is selected)
     * @param selected is the delivery filter selected?
     * @return true if company offers delivery, or if deliveryFilter isn't selected.
     */
    public boolean filterDelivery(boolean selected) {
        if(!selected) {
            return true;
        } else {
            return deliveryService;
        }
    }

    /**
     * Filters companies regarding specific opening hours.
     * @param selectedOpeningHours the selected opening hours
     * @param relevant checks if filtering should be executed
     * @return true if company is open at specified hours
     */
    public boolean filterOpeningHours(HashMap<String, String> selectedOpeningHours, boolean relevant){
        if(!relevant){
            return true;
        }

        String selectedDay = selectedOpeningHours.get("day");

        String selectedStart = selectedOpeningHours.get("start");
        int selectedStartHour = Integer.parseInt(selectedStart.substring(0,2));
        int selectedStartMinute = Integer.parseInt(selectedStart.substring(3,5));

        String selectedEnd = selectedOpeningHours.get("end");
        int selectedEndHour = Integer.parseInt(selectedEnd.substring(0,2));
        int selectedEndMinute = Integer.parseInt(selectedEnd.substring(3,5));

        String start;

        String end;

        switch(selectedDay) {
            case "monday":
                if(this.openingHours.monday == null){
                    return false;
                }
                start = this.openingHours.monday.get(0).get("start");
                end = this.openingHours.monday.get(0).get("end");
                break;
            case "tuesday":
                if(this.openingHours.tuesday == null){
                    return false;
                }
                start = this.openingHours.tuesday.get(0).get("start");
                end = this.openingHours.tuesday.get(0).get("end");
                break;
            case "wednesday":
                if(this.openingHours.wednesday == null){
                    return false;
                }
                start = this.openingHours.wednesday.get(0).get("start");
                end = this.openingHours.wednesday.get(0).get("end");
                break;
            case "thursday":
                if(this.openingHours.thursday == null){
                    return false;
                }
                start = this.openingHours.thursday.get(0).get("start");
                end = this.openingHours.thursday.get(0).get("end");
                break;
            case "friday":
                if(this.openingHours.friday == null){
                    return false;
                }
                start = this.openingHours.friday.get(0).get("start");
                end = this.openingHours.friday.get(0).get("end");
                break;
            case "saturday":
                if(this.openingHours.saturday == null){
                    return false;
                }
                start = this.openingHours.saturday.get(0).get("start");
                end = this.openingHours.saturday.get(0).get("end");
                break;
            case "sunday":
                if(this.openingHours.sunday == null){
                    return false;
                }
                start = this.openingHours.sunday.get(0).get("start");
                end = this.openingHours.sunday.get(0).get("end");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedDay);
        }

        int startHour = Integer.parseInt(start.substring(0,2));
        int startMinute = Integer.parseInt(start.substring(3,5));
        int endHour = Integer.parseInt(end.substring(0,2));
        int endMinute = Integer.parseInt(end.substring(3,5));


        if(selectedStartHour > startHour && selectedStartHour < endHour){
            return true;
        } else if (selectedEndHour < endHour && selectedEndHour > startHour){
            return true;
        } else if (selectedStartHour == startHour && selectedStartMinute > startMinute){
            return true;
        } else if (selectedEndHour == endHour && selectedEndMinute < endMinute) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Filters companies regarding specific organizations
     * @param selectedOrganizations list of organizations to check
     * @return true if one or more organizations match
     */
    public boolean filterOrganizations(ArrayList<Organization> selectedOrganizations) {
        if(selectedOrganizations.isEmpty()){return true;}
        if(this.organizations.isEmpty()){return false;}

        for (Organization o : selectedOrganizations) {
            if (this.organizations.contains(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Performs the textsearch on the specified fields.
     * @param query the search query
     * @param categories the selected categories
     * @return true if company matches the search
     */
    public boolean textSearch(String query, ArrayList<String> categories) {

        for (String category : categories) {
            switch (category) {
                case "Company name":
                    if(this.searchForName(query)){return true;}
                    break;
                case "Owner name":
                    if(this.searchForOwner(query)){return true;}
                    break;
                case "Types of companies":
                    if(this.searchForTypes(query)){return true;}
                    break;
                case "Address of company":
                    if(this.searchForAddress(query)){return true;}
                    break;
                case "Description of company":
                    if(this.searchForCompanyDescription(query)){return true;}
                    break;
                case "Description of product":
                    if(this.searchForProductDescription(query)){return true;}
                    break;
                case "Product group tags":
                    if(this.searchForProductTags(query)){return true;}
                    break;
                case "Opening hours comments":
                    if(this.searchForOpeningHourComments(query)){return true;}
                    break;
                case "Name of organization":
                    if(this.searchForOrganizationName(query)){return true;}
                    break;
                case "Messages of company":
                    if(this.searchForMessages(query)){return true;}
                    break;
            }

        }

        return false;
    }


    //helper methods for textSearch
    private boolean searchForName(String query){
        if(name == null) {
            return false;
        }
         if(name.contains(query)){
             //Log.d("searchForName", "is true");
             return true;
         }
         return false;
    }

    private boolean searchForOwner(String query){
        if(owner == null){
            return false;
        }
        if(owner.contains(query)){return true;}
        else return false;
    }

    private boolean searchForTypes(String query){
        if(types == null) {
            return false;
        }
        for(String type : types){
            if(type.contains(query)){return true;}
        }
        return false;
    }

    private boolean searchForAddress(String query){
        if(address == null) {
            return false;
        }
        if (address.getCity() != null){
            if(address.getCity().contains(query)){return true;}
        }
        if (address.getStreet() != null){
            if(address.getStreet().contains(query)){return true;}
        }
        if (address.getZip() != null){
            if(address.getZip().contains(query)){return true;}
        }
        return false;
    }

    private boolean searchForCompanyDescription(String query){
        if(description == null) {
            return false;
        }
        if(description.contains(query)){return true;}
        return false;
    }

    private boolean searchForProductDescription(String query){
        if(productsDescription == null) {
            return false;
        }
        if(productsDescription.contains(query)){return true;}
        return false;
    }

    private boolean searchForProductTags(String query){
        if(productGroups == null) {
            return false;
        }
        for(ProductGroup p : productGroups) {
            for(String tag : p.getProductTags()) {
                if (tag.contains(query)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean searchForOpeningHourComments(String query){
        if(openingHoursComments == null) {
            return false;
        }
        if(openingHoursComments.contains(query)){return true;}
        return false;
    }

    private boolean searchForOrganizationName(String query){
        if(organizations == null) {
            return false;
        }
        for(Organization o : organizations) {
            if(o.getName().contains(query)){
                return true;
            }
        }
        return false;
    }

    private boolean searchForMessages(String query){
        if(messages == null) {
            return false;
        }
        for(Message m : messages) {
            if(m.getContent().contains(query)){
                return true;
            }
        }
        return false;
    }

    /**
     * Id setter.
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Name setter
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Description setter
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Mail address setter
     * @param mail the mail address
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * URL setter
     * @param url the URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Owner setter
     * @param owner name of the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Opening hours comments setter
     * @param openingHoursComments the comments regarding the opening hours
     */
    public void setOpeningHoursComments(String openingHoursComments) {
        this.openingHoursComments = openingHoursComments;
    }

    /**
     * Product description setter
     * @param productsDescription the product description
     */
    public void setProductsDescription(String productsDescription) {
        this.productsDescription = productsDescription;
    }

    /**
     * Geo hash setter
     * @param geoHash the geoHash
     */
    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

    /**
     * Address setter
     * @param address the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Location setter
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Opening hours setter
     * @param openingHours the opening hours
     */
    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    /**
     * Type setter
     * @param types list of types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * Organizations setter
     * @param organizations list of organizations
     */
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    /**
     * Messages setter
     * @param messages list of messages
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Product group setter
     * @param productGroups list of product groups
     */
    public void setProductGroups(List<ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    /**
     * Delivery service setter
     * @param deliveryService boolean regarding deliveryService (true if company delivers)
     */
    public void setDeliveryService(boolean deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * imagePaths setter
     * @param imagePaths the image paths of the company
     */
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    /**
     * Description getter
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mail address getter
     * @return the mail address
     */
    public String getMail() {
        return mail;
    }

    /**
     * URL getter
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Owner getter
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Opening hours comments getter
     * @return opening hours comments
     */
    public String getOpeningHoursComments() {
        return openingHoursComments;
    }

    /**
     * Product description getter
     * @return product description
     */
    public String getProductsDescription() {
        return productsDescription;
    }

    /**
     * Geo hash getter
     * @return the geo hash
     */
    public String getGeoHash() {
        return geoHash;
    }

    /**
     * Address getter
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Location getter
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Opening hours getter
     * @return the openingHours
     */
    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    /**
     * Types getter
     * @return list of types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Message getter
     * @return List of messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Product groups getter
     * @return List of product groups
     */
    public List<ProductGroup> getProductGroups() {
        return productGroups;
    }

    /**
     * isDeliveryService getter
     * @return boolean regarding delivery services
     */
    public boolean isDeliveryService() {
        return deliveryService;
    }

    /**
     * image path getter
     * @return the image paths of the company
     */
    public List<String> getImagePaths() {
        return imagePaths;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id.equals(company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
