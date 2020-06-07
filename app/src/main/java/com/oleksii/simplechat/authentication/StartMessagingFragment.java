package com.oleksii.simplechat.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.utils.CountriesCodes;

public class StartMessagingFragment extends Fragment {

    public StartMessagingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start_messaging, container, false);
        Button button = rootView.findViewById(R.id.start_messaging_button);
        button.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.action_startMessagingFragment_to_loginFragment);
        });

        return rootView;
    }
}
