package com.example.ex11042;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Intent siCred, siAddExpense;
    private SQLiteDatabase db;
    private HelperDB hlp;
    private final String[] categories = {"All", "Restaurant", "Recreation", "Shopping", "Transferring money", "Buying online", "Other..."};
    private Spinner spCategoryFilter;
    private String strSelectedCategory;
    private EditText etMaxAmountFilter, etMinAmountFilter, etDescriptionSearch;
    private TextView tvInfoToUserSearch;
    private List<Expense> toSortExpenseList;
    private Cursor crsr;
    private ToggleButton tbSortType;
    private CustomAdapter customAdapter;
    private ListView lvSorted;
    private double maxAmount, minAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        siCred = new Intent(this, CreditsActivity.class);
        siAddExpense = new Intent(this, AddExpenseActivity.class);
        spCategoryFilter = findViewById(R.id.spCategoryFilter);
        etMaxAmountFilter = findViewById(R.id.etMaxAmountFilter);
        etMinAmountFilter = findViewById(R.id.etMinAmountFilter);
        tvInfoToUserSearch = findViewById(R.id.tvInfoToUserSearch);
        etDescriptionSearch = findViewById(R.id.etDescriptionSearch);
        tbSortType = findViewById(R.id.tbSortType);
        lvSorted = findViewById(R.id.lvSorted);

        toSortExpenseList = new ArrayList<>();

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        spCategoryFilter.setAdapter(adp);
        spCategoryFilter.setOnItemSelectedListener(this);
    }

    public void startToSortExpenseListByAmount()
    {
        int pos = 0;
        int size = toSortExpenseList.size();

        while (pos < size)
        {
            if (pos == 0 || toSortExpenseList.get(pos).getAmount() <= toSortExpenseList.get(pos -1).getAmount())
            {
                pos++;
            }
            else
            {
                Collections.swap(toSortExpenseList, pos, pos-1);
                pos--;
            }
        }
    }

    private void startToSortExpenseListByDate()
    {
        int pos = 0;
        int size = toSortExpenseList.size();

        while (pos < size)
        {
            if (pos == 0)
            {
                pos++;
            }
            String strDatePos = toSortExpenseList.get(pos).getDate();
            String strDateDecPos = toSortExpenseList.get(pos-1).getDate();

            if (Integer.parseInt(strDatePos.substring(0,4) + strDatePos.substring(5,7) + strDatePos.substring(8)) <= Integer.parseInt(strDateDecPos.substring(0,4) + strDateDecPos.substring(5,7) + strDateDecPos.substring(8)))
            {
                pos++;
            }
            else
            {
                Collections.swap(toSortExpenseList, pos, pos-1);
                pos--;
            }
        }
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * <p>
     * This method inflates the menu resource (R.menu.main) into the provided Menu
     * object and adds the items to the action bar.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * <p>
     * This implementation checks if the selected item is the "Credits" or "AddExpense" or "Main" menu item
     * and, if so, starts the activity defined by the Intent 'si'.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed,
     * true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuMain)
        {
            finish();
        }
        else if (id == R.id.menuCredits)
        {
            finish();
            startActivity(siCred);
        }
        else if (id == R.id.menuAddExpense)
        {
            finish();
            startActivity(siAddExpense);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        strSelectedCategory = categories[i];
        onSearch(new View(this));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i("Spinner","Nothing selected");
    }

    public void onSearch(View view) {
        if (etMaxAmountFilter.getText().toString().isEmpty() || etMinAmountFilter.getText().toString().isEmpty())
        {
            tvInfoToUserSearch.setText("You must fill the [Min] and [Max] values!");
        }
        else if (!AddExpenseActivity.validEditText(etMinAmountFilter.getText().toString()) || !AddExpenseActivity.validEditText(etMaxAmountFilter.getText().toString()))
        {
            tvInfoToUserSearch.setText("Invalid number (/numbers)!");
        }
        else if (Double.parseDouble(etMinAmountFilter.getText().toString()) > Double.parseDouble(etMaxAmountFilter.getText().toString()))
        {
            tvInfoToUserSearch.setText("The [Min] value can't be bigger than the [Max] value!");
        }
        else
        {
            maxAmount = Double.parseDouble(etMaxAmountFilter.getText().toString());
            minAmount = Double.parseDouble(etMinAmountFilter.getText().toString());

            tvInfoToUserSearch.setText("");

            toSortExpenseList.clear();

            db = hlp.getReadableDatabase();

            crsr = db.query(Expenses.TABLE_EXPENSES, null, null, null, null, null, null);

            int colId = crsr.getColumnIndex(Expenses.KEY_ID);
            int colAmount = crsr.getColumnIndex(Expenses.AMOUNT);
            int colDate = crsr.getColumnIndex(Expenses.DATE);
            int colCategory = crsr.getColumnIndex(Expenses.CATEGORY);
            int colDescription = crsr.getColumnIndex(Expenses.DESCRIPTION);

            crsr.moveToFirst();
            while (!crsr.isAfterLast())
            {
                int id = crsr.getInt(colId);
                double amount = crsr.getDouble(colAmount);
                String date = crsr.getString(colDate);
                String category = crsr.getString(colCategory);
                String description = crsr.getString(colDescription);

                Expense expense = new Expense(id, description, amount, category, date);

                toSortExpenseList.add(expense);

                crsr.moveToNext();
            }

            crsr.close();
            db.close();

            for (int i = 0 ; i < toSortExpenseList.size() ; i++)
            {
                if (toSortExpenseList.get(i).getAmount() < minAmount || toSortExpenseList.get(i).getAmount() > maxAmount)
                {
                    toSortExpenseList.remove(i);
                    i--;
                }
            }

            if (tbSortType.isChecked()) // Sort by date
            {
                startToSortExpenseListByDate();
            }
            else // Sort by amount
            {
                startToSortExpenseListByAmount();
            }

            for (int i = 0 ; i < toSortExpenseList.size() ; i++)
            {
                if (strSelectedCategory.equals("All"))
                {
                    break;
                }
                else if (!toSortExpenseList.get(i).getCategory().equals(strSelectedCategory))
                {
                    toSortExpenseList.remove(i);
                    i--;
                }
            }

            if (!etDescriptionSearch.getText().toString().isEmpty())
            {
                for (int i = 0 ; i < toSortExpenseList.size() ; i++)
                {

                    String description = etDescriptionSearch.getText().toString();

                    if (!toSortExpenseList.get(i).getDescription().contains(description))
                    {
                        toSortExpenseList.remove(i);
                        i--;
                    }
                }
            }


            customAdapter = new CustomAdapter(this, toSortExpenseList);
            lvSorted.setAdapter(customAdapter);
        }
    }
}