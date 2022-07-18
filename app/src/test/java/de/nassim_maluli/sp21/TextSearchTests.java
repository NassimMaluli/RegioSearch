package de.uni_marburg.sp21;

import org.junit.Test;

import java.util.ArrayList;

import de.uni_marburg.sp21.model.Address;
import de.uni_marburg.sp21.model.Company;
import de.uni_marburg.sp21.model.Message;
import de.uni_marburg.sp21.model.Organization;
import de.uni_marburg.sp21.model.ProductGroup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextSearchTests {
    Company testCompany;
    ArrayList<String> selected= new ArrayList<>();

    @Test
    public void companyNameTest(){
        selected = new ArrayList<>();
        selected.add("Company name");
        initTestCompany();
        testCompany.setName("test");
        assertTrue(testCompany.textSearch("te", selected));
        assertFalse(testCompany.textSearch("et", selected));
    }

    @Test
    public void ownerNameTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Owner name");
        testCompany.setOwner("test owner");
        assertTrue(testCompany.textSearch("st", selected));
        assertFalse(testCompany.textSearch("won", selected));
    }

    @Test
    public void typeOfCompTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Types of companies");
        ArrayList<String> types = new ArrayList<>();
        types.add("producer");
        testCompany.setTypes(types);
        assertTrue(testCompany.textSearch("produce", selected));
        assertFalse(testCompany.textSearch("proud", selected));
    }

    @Test
    public void addressTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Address of company");
        Address address = new Address();
        address.setStreet("Wilhelmstr.");
        testCompany.setAddress(address);
        assertTrue(testCompany.textSearch("Wilhelm", selected));
        assertFalse(testCompany.textSearch("Gutenberg", selected));
    }

    @Test
    public void descriptionTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Description of company");
        testCompany.setDescription("bla bli blub");
        assertTrue(testCompany.textSearch("bla", selected));
        assertFalse(testCompany.textSearch("blob", selected));
    }

    @Test
    public void productDescriptionTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Description of product");
        testCompany.setProductsDescription("bla bli blub");
        assertTrue(testCompany.textSearch("bla", selected));
        assertFalse(testCompany.textSearch("blob", selected));
    }

    @Test
    public void productTagsTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Product group tags");
        ProductGroup pG = new ProductGroup();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("thisisatag");
        pG.setProductTags(tags);
        ArrayList<ProductGroup> pGList = new ArrayList<>();
        pGList.add(pG);
        testCompany.setProductGroups(pGList);
        assertTrue(testCompany.textSearch("thisis", selected));
        assertFalse(testCompany.textSearch("thisisnot", selected));
    }

    @Test
    public void openingHoursCommentTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Opening hours comments");
        testCompany.setOpeningHoursComments("thisisacomment");
        assertTrue(testCompany.textSearch("thisis", selected));
        assertFalse(testCompany.textSearch("thisisnot", selected));
    }

    @Test
    public void organizationTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Name of organization");
        Organization org = new Organization();
        org.setName("thisisorg");
        ArrayList<Organization> orgList = new ArrayList<>();
        orgList.add(org);
        testCompany.setOrganizations(orgList);
        assertTrue(testCompany.textSearch("thisis", selected));
        assertFalse(testCompany.textSearch("thisisnot", selected));
    }

    @Test
    public void messagesTest(){
        selected = new ArrayList<>();
        initTestCompany();
        selected.add("Messages of company");
        Message message = new Message();
        message.setContent("thisisamessage");
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        testCompany.setMessages(messages);
        assertTrue(testCompany.textSearch("thisis", selected));
        assertFalse(testCompany.textSearch("thisisnot", selected));
    }

    private void initTestCompany() {
        testCompany = new Company();
    }
}
