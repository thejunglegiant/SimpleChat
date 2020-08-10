package com.oleksii.simplechat.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.activities.GoogleSignInActivity;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ImageButton gButton = rootView.findViewById(R.id.google_button);
        gButton.setOnClickListener(this);
        ImageButton phButton = rootView.findViewById(R.id.phone_button);
        phButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.google_button:
                intent = new Intent(getActivity(), GoogleSignInActivity.class);
                startActivityForResult(intent, RC_SIGN_IN);
                Activity currentActivity = getActivity();
                if (currentActivity != null)
                    currentActivity.finish();
                break;
            case R.id.phone_button:
                Navigation.findNavController(v)
                        .navigate(R.id.action_loginFragment_to_enterPhoneNumberFragment);
                break;
            default:
                Log.i(TAG, "Neither google_button nor facebook_button were clicked");
        }
    }


}
