package com.example.ex11042;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddExpenseActivity extends AppCompatActivity {
    private Intent siCred, siSearch;
    private String strDate;
    private TextView tvShowDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        siCred = new Intent(this, CreditsActivity.class);
        siSearch = new Intent(this, SearchActivity.class);

        tvShowDate = findViewById(R.id.tvShowDate);
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
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                strDate = (year < 10)? "000" + year : (year < 100)? "00" + year : (year < 1000)? "0" + year : "" + year;
                month++;
                strDate += "-" + ((month < 10)? "0" + month : "" + month);
                strDate += "-" + ((day < 10)? "0" + day : "" + day);

                tvShowDate.setText(strDate);
            }
        }, 2026, 0, 1);
        dialog.show();
    }

    public void addExpense(View view) {
        //...
        finish();
    }
}