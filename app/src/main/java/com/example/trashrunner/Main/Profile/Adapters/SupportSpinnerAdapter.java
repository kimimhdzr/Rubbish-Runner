package com.example.trashrunner.Main.Profile.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trashrunner.R;
import com.example.trashrunner.DataBinding.Model.CompanyBasicModel;

import java.util.List;

public class SupportSpinnerAdapter extends ArrayAdapter<CompanyBasicModel> {
    private final List<CompanyBasicModel> companies;

    public SupportSpinnerAdapter(@NonNull Context context, @NonNull List<CompanyBasicModel> companies) {
        super(context, 0, companies);
        this.companies = companies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createSpinnerItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createSpinnerItemView(position, convertView, parent);
    }

    private View createSpinnerItemView(int position, View convertView, ViewGroup parent) {
        // Inflate the custom layout if it has not been already
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_report, parent, false);
        }

        // Get the current company item
        CompanyBasicModel currentCompany = companies.get(position);

        // Find the TextViews in the layout
        TextView nameTextView = convertView.findViewById(R.id.company_name_text_view);
        TextView stateTextView = convertView.findViewById(R.id.company_state_text_view);

        // Set the company name and state
        nameTextView.setText(currentCompany.getName());
        stateTextView.setText(currentCompany.getState());

        return convertView;
    }
}