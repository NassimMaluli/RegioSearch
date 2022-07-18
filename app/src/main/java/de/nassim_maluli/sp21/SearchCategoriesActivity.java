package de.uni_marburg.sp21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**This activity is designed to give the user the opportunity
 * of setting which information of the companies, especially which data categories
 * will be taken into account during a search.
 * @author Melisa
 */

public class SearchCategoriesActivity extends AppCompatActivity {

    ArrayList<CheckBox> allCheckBoxes = new ArrayList<>();

    String[] categoriesToken = {"Company name", "Owner name", "Types of companies", "Address of company", "Description of company",
            "Description of product", "Product group tags", "Opening hours comments", "Name of organization", "Messages of company"};

    Button submit;


    /**
     * This method tends to manage all the check boxes that
     * are needed for our implementation just by adding them in our
     * ArrayList of type CheckBox.
     *
     * @param allCheckBoxes
     */

    public void setCheckBoxes(ArrayList<CheckBox> allCheckBoxes) {

        allCheckBoxes.add(findViewById(R.id.checkBox));
        allCheckBoxes.add(findViewById(R.id.checkBox2));
        allCheckBoxes.add(findViewById(R.id.checkBox3));
        allCheckBoxes.add(findViewById(R.id.checkBox4));
        allCheckBoxes.add(findViewById(R.id.checkBox5));
        allCheckBoxes.add(findViewById(R.id.checkBox6));
        allCheckBoxes.add(findViewById(R.id.checkBox7));
        allCheckBoxes.add(findViewById(R.id.checkBox8));
        allCheckBoxes.add(findViewById(R.id.checkBox9));
        allCheckBoxes.add(findViewById(R.id.checkBox10));

    }

    /**
     * onCreate method manages to handle all the checkBoxes by proving which checkBoxes
     * has been checked, adding the text of categoriesTokens to our result and lastly
     * adding the checked box to a list of CheckBoxes in order to prove if this list
     * empty is. If this is the case than a toast will appear by informing the user,
     * that at least one data categorie should be choosed.
     * At the end our result will be send to the main search activity and an intent will be thrown.
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_categories);

        Intent intent = getIntent();

        TextView textView = findViewById(R.id.button4);
        submit = findViewById(R.id.submit);

        getSupportActionBar().setTitle(getString(R.string.categories));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setCheckBoxes(allCheckBoxes);

        submit.setOnClickListener(v -> {

            ArrayList<CheckBox> box = new ArrayList<>();
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < allCheckBoxes.size(); i++) {
                if (allCheckBoxes.get(i).isChecked()) {
                    allCheckBoxes.get(i).setChecked(true);
                    result.add(categoriesToken[i]);
                    box.add(allCheckBoxes.get(i));
                }
            }

            if (box.isEmpty())
                Toast.makeText(SearchCategoriesActivity.this.getApplicationContext(), getResources().getString(R.string.categories_toast), Toast.LENGTH_SHORT).show();
            else {
                Intent intent1 = new Intent(this, SearchActivity.class);
                intent1.putExtra("categories", result);
                startActivity(intent1);
            }

        });
    }

    public void submit(View view) {


    }
}


