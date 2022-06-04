package de.uni_marburg.sp21;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.uni_marburg.sp21.model.Address;
import de.uni_marburg.sp21.model.Company;
import de.uni_marburg.sp21.model.Message;
import de.uni_marburg.sp21.model.OpeningHours;
import de.uni_marburg.sp21.model.Organization;
import de.uni_marburg.sp21.model.ProductGroup;

public class FilterTests {

    String[] productCatTokens = {"vegetables", "fruits", "meat", "meatproducts",
            "cereals", "milk", "eggs", "honey", "beverages", "bakedgoods", "pasta",
            "MilkProducts"};
    String[] companyTypeTokens = {"producer", "shop", "hotel", "restaurant", "mart"};



    Company testCompany;

    @Test
    public void filterProductCatTest(){

        for (String s: productCatTokens){

            ArrayList<String> productCategories = new ArrayList<>();
            productCategories.add(s);


            // set up company that should match
            initTestCompany();
            ArrayList<ProductGroup> productGroupList = new ArrayList<>();
            ProductGroup productGroup = new ProductGroup();
            productGroupList.add(productGroup);
            productGroup.setCategory(s);

            //company shouldn't match
            assertFalse(testCompany.filterProductCat(productCategories));

            testCompany.setProductGroups(productGroupList);
            // company should match
            assertTrue(testCompany.filterProductCat(productCategories));
        }

    }

    @Test
    public void filterCompanyTypesTest(){
        for (String s : companyTypeTokens){
            ArrayList<String> companyTypes = new ArrayList<>();
            companyTypes.add(s);
            // setup test company
            initTestCompany();

            assertFalse(testCompany.filterCompanyType(companyTypes));

            testCompany.setTypes(companyTypes);

            assertTrue(testCompany.filterCompanyType(companyTypes));

        }
    }

    @Test
    public void filterDeliveryTest(){
        initTestCompany();
        testCompany.setDeliveryService(true);
        assertTrue(testCompany.filterDelivery(true));

        testCompany.setDeliveryService(false);
        assertFalse(testCompany.filterDelivery(true));
    }

    @Test
    public void filterOrganizationsTest(){
        initTestCompany();

        ArrayList<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Organization orga = new Organization();
            orga.setId(i);
            organizations.add(orga);
        }
        testCompany.setOrganizations(organizations);

        ArrayList<Organization> selected = new ArrayList<>();
        // 1-5 matching
        for (Organization org : organizations){
           selected.add(org);
           assertTrue(testCompany.filterOrganizations(selected));
        }

        //try with no organizations in company
        organizations = new ArrayList<>();
        testCompany.setOrganizations(organizations);
        assertFalse(testCompany.filterOrganizations(selected));

        //try with no matching org in company
        Organization orgInCompany = new Organization();
        orgInCompany.setId(1);
        organizations.add(orgInCompany);
        testCompany.setOrganizations(organizations);
        Organization selectedOrg = new Organization();
        selectedOrg.setId(2);
        selected = new ArrayList<>();
        selected.add(selectedOrg);

        assertFalse(testCompany.filterOrganizations(selected));

    }

    @Test
    public void currentlyOpenTest(){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY) + 2; //German TimeZone

        String startHour;

        if(hour - 1 < 10) {
            startHour = "0" + Integer.toString(hour-1);
        } else {
            startHour = Integer.toString(hour-1);
        }

        String endHour;
        if(hour + 1 < 10){
            endHour = "0" + Integer.toString(hour+1);
        } else {
            endHour = Integer.toString(hour+1);
        }

        HashMap<String, String> testHours = new HashMap<>();
        testHours.put("start", startHour+":00");
        testHours.put("end", endHour+":00");

        ArrayList<HashMap<String, String>> hours = new ArrayList<>();
        hours.add(testHours);




        initTestCompany();
        OpeningHours oH = new OpeningHours();
        switch(day) {
            case Calendar.MONDAY:
                oH.setMonday(hours);
            case Calendar.TUESDAY:
                oH.setTuesday(hours);
            case Calendar.WEDNESDAY:
                oH.setWednesday(hours);
            case Calendar.THURSDAY:
                oH.setThursday(hours);
            case Calendar.FRIDAY:
                oH.setFriday(hours);
            case Calendar.SATURDAY:
                oH.setSaturday(hours);
            case Calendar.SUNDAY:
                oH.setSunday(hours);

        }

        testCompany.setOpeningHours(oH);

        assertTrue(testCompany.currentlyOpen(true));

    }

    @Test
    public void filterOpeningHoursTest(){
        HashMap<String, String> selected = new HashMap<>();
        selected.put("day", "monday");
        selected.put("start", "12:00");
        selected.put("end", "14:00");

        OpeningHours oH = new OpeningHours();
        HashMap<String, String> compOpening = new HashMap<>();
        compOpening.put("start", "11:00");
        compOpening.put("end", "13:00");
        ArrayList<HashMap<String, String>> opening = new ArrayList<>();
        opening.add(compOpening);

        oH.setMonday(opening);

        initTestCompany();
        testCompany.setOpeningHours(oH);
        //matching
        assertTrue(testCompany.filterOpeningHours(selected, true));

        selected = new HashMap<>();
        selected.put("day", "tuesday");
        selected.put("start", "12:00");
        selected.put("end", "14:00");
        // wrong day
        assertFalse(testCompany.filterOpeningHours(selected, true));

        selected = new HashMap<>();
        selected.put("day", "monday");
        selected.put("start", "14:00");
        selected.put("end", "16:00");
        // wrong hours
        assertFalse(testCompany.filterOpeningHours(selected, true));

    }

    private void initTestCompany(){
        testCompany = new Company();
    }



}
