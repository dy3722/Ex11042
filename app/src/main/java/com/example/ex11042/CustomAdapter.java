package com.example.ex11042;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    public CustomAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        inflater = (LayoutInflater.from(context));
    }

    private Context context;
    private List<Expense> expenseList;
    private LayoutInflater inflater;
    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int i) {
        return expenseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_adapter, viewGroup, false);

        TextView tvDescription = (TextView) view.findViewById(R.id.tvDescriptionInCustAdp);
        TextView tvCategory = (TextView) view.findViewById(R.id.tvCategoryInCustAdp);
        TextView tvAmount = (TextView) view.findViewById(R.id.tvAmountInCustAdp);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDateInCustAdp);

        tvDescription.setText(expenseList.get(i).getDescription());
        tvCategory.setText(expenseList.get(i).getCategory());
        tvAmount.setText(MainActivity.formatClearNumber(expenseList.get(i).getAmount()) + "₪");
        tvDate.setText(expenseList.get(i).getDate());

        return view;
    }
}
