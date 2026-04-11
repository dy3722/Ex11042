package com.example.ex11042;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Intent siCred, siSearch;
    private String strDate, strSelectedCategory;
    private TextView tvShowDate, tvInfoToUser;
    private SQLiteDatabase db;
    private HelperDB hlp;
    private final String[] categories = {"Restaurant", "Recreation", "Shopping", "Transferring money", "Buying online", "Other..."};
    private Spinner spCategory;
    private EditText etAmount, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        siCred = new Intent(this, CreditsActivity.class);
        siSearch = new Intent(this, SearchActivity.class);

        tvShowDate = findViewById(R.id.tvShowDate);
        tvInfoToUser = findViewById(R.id.tvInfoToUser);
        spCategory = findViewById(R.id.spCategory);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categories);
        spCategory.setAdapter(adp);
        spCategory.setOnItemSelectedListener(this);
    }

    /**
     * Valid edit text boolean.
     *
     * @param str the str
     * @return true - if valid. else - false
     */
    public static boolean validEditText(String str)
    {
        String regex = "^[+-]?(\\d+|\\d*\\.\\d+|\\d+\\.)$";
        return str.matches(regex);
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
     * This implementation checks if the selected item is the "Credits" or "AddExpense" or "Search" menu item
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
        else if (id == R.id.menuSearch)
        {
            finish();
            startActivity(siSearch);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDateDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                strDate = (year < 10)? "000" + year : (year < 100)? "00" + year : (year < 1000)? "0" + year : "" + year;
                month++;
                strDate += "-" + ((month < 10)? "0" + month : "" + month);
                strDate += "-" + ((day < 10)? "0" + day : "" + day);

                tvShowDate.setText(strDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void addExpense(View view) {
        if (etAmount.getText().toString().isEmpty())
        {
            tvInfoToUser.setText("Please enter amount!");
        }
        else if (!validEditText(etAmount.getText().toString()))
        {
            tvInfoToUser.setText("Invalid number!");
        }
        else if (tvShowDate.getText().toString().equals("yyyy-MM-dd"))
        {
            tvInfoToUser.setText("Please choose date!");
        }
        else if (etDescription.getText().toString().isEmpty())
        {
            tvInfoToUser.setText("Please enter description!");
        }
        else
        {
            tvInfoToUser.setText("");
            double amount = Double.parseDouble(etAmount.getText().toString());
            String description = etDescription.getText().toString();
            String date = tvShowDate.getText().toString();

            ContentValues cv = new ContentValues();

            cv.put(Expenses.AMOUNT, amount);
            cv.put(Expenses.CATEGORY, strSelectedCategory);
            cv.put(Expenses.DATE, date);
            cv.put(Expenses.DESCRIPTION, description);

            db = hlp.getWritableDatabase();
            db.insert(Expenses.TABLE_EXPENSES, null, cv);
            db.close();

            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        strSelectedCategory = categories[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i("Spinner","Nothing selected");
    }
}