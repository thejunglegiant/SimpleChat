package com.oleksii.simplechat.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oleksii.simplechat.utils.CountriesCodes;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.PhoneNumberEditText;

public class EnterPhoneNumberFragment extends Fragment {

    public EnterPhoneNumberFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Setting up StatusBar color to the primary one
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        View rootView = inflater.inflate(R.layout.fragment_enter_phone_number, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_phone);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_enterPhoneNumberFragment_to_loginFragment));

        EditText countryCodeText = rootView.findViewById(R.id.country_code_text);
        EditText phoneNumberText = rootView.findViewById(R.id.phone_number_text);
        Selection.setSelection(phoneNumberText.getText(), phoneNumberText.getText().length());

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (PhoneNumberEditText.validatePhoneNumber(phoneNumberText.getText().toString())
                && CountriesCodes.getIndex(countryCodeText.getText().toString().substring(1)) != -1) {
                Bundle bundle = new Bundle();
                bundle.putString("countryCode", countryCodeText.getText().toString().trim());
                bundle.putString("phoneNumber", phoneNumberText.getText().toString().replace(" ", ""));
                Navigation.findNavController(v).navigate(
                        R.id.action_enterPhoneNumberFragment_to_verifyPhoneNumberFragment, bundle
                );
            } else {
                Toast.makeText(getContext(), R.string.phone_number_is_incorrect, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        Spinner spinner = rootView.findViewById(R.id.countries_list);
        int size = CountriesCodes.values().length;
        String[] countries = new String[size];
        int i = 0;
        for (CountriesCodes item : CountriesCodes.values()) {
            countries[i] = (item.getCountryName());
            i++;
        }
        spinner.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, countries));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int position, long l) {
                String code = '+' + CountriesCodes.values()[position].getCountryCodeNM();
                if (!countryCodeText.getText().toString().equals(code)) {
                    countryCodeText.setText(code);
                    Selection.setSelection(countryCodeText.getText(), countryCodeText.getText().length());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        countryCodeText.addTextChangedListener(new TextWatcher() {
            String tmpCode = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().startsWith("+")){
                    countryCodeText.setText("+");
                    Selection.setSelection(countryCodeText.getText(), countryCodeText.getText().length());
                }

                int index = CountriesCodes.getIndex(countryCodeText.getText().toString().substring(1));
                if (index != -1) {
                    tmpCode = countryCodeText.getText().toString();
                    spinner.setSelection(index);
                }

                if (countryCodeText.getText().length() > 4 && !tmpCode.equals("")) {
                    phoneNumberText.requestFocus(phoneNumberText.length());
                    countryCodeText.setText(tmpCode);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return rootView;
    }
}
