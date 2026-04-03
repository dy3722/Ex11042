package com.example.ex11042;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Yusupov <dy3722@bs.amalnet.k12.il>
 * @version 1.0
 * @since 28/3/2026
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, View.OnCreateContextMenuListener {
    private Intent siCred, siAddExpense, siSearch;
    private int clickPos;
    private ListView lvExpenses;
    private List<String> expensesList;
    private SQLiteDatabase db;
    private HelperDB hlp;

    /**
     * Initializes the activity and sets up the UI components.
     * <p>
     * This method sets the content view to activity_main, initializes the Intent for
     * the credits screen, and links the layout and TextView variables to their XML IDs.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     * being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        siCred = new Intent(this, CreditsActivity.class);
        siAddExpense = new Intent(this, AddExpenseActivity.class);
        siSearch = new Intent(this, SearchActivity.class);

        lvExpenses = findViewById(R.id.lvExpenses);
        lvExpenses.setOnItemLongClickListener(this);
        lvExpenses.setOnCreateContextMenuListener(this);

        expensesList = new ArrayList<>();
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
        if (id == R.id.menuCredits)
        {
            startActivity(siCred);
        }
        else if (id == R.id.menuAddExpense)
        {
            startActivity(siAddExpense);
        }
        else if (id == R.id.menuSearch)
        {
            startActivity(siSearch);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Options:");
        menu.add("Delete");
        menu.add("Edit");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String op = item.getTitle().toString();
        if (op.equals("Delete"))
        {

        }
        else if (op.equals("Edit"))
        {

        }

        return super.onContextItemSelected(item);
    }

    /**
     * callback method to be invoked when an item in this AdapterView has been clicked and held.
     * <p>
     * This implementation captures the position of the item being long-clicked
     * into the 'clickPos' variable, which is later used by the Context Menu actions.
     *
     * @param parent   The AbsListView where the click happened.
     * @param view     The view within the AbsListView that was clicked.
     * @param position The position of the view in the list.
     * @param id       The row id of the item that was clicked.
     * @return boolean Return false to allow the context menu to be created.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        clickPos = position;

        return false;
    }
}