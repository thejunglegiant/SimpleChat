package com.oleksii.simplechat.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.oleksii.simplechat.activities.AskNameActivity;
import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.PhoneNumberEditText;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberFragment extends Fragment {

    private static final String TAG = "VerifyPhoneNumberFragment";
    private String verificationId, countryCode, phoneNumber;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private EditText codeVerification;
    private TextView contactUs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_phone_number, container, false);

        toolbar = rootView.findViewById(R.id.main_toolbar);
        codeVerification = rootView.findViewById(R.id.code_text);
        contactUs = rootView.findViewById(R.id.contact_us);

        countryCode = getArguments().getString("countryCode");
        phoneNumber = getArguments().getString("phoneNumber");

        auth = FirebaseAuth.getInstance();
        auth.useAppLanguage();
        sendVerificationCode(countryCode + phoneNumber);

        setupToolbar();
        setupListeners();

        return rootView;
    }

    private void setupToolbar() {
        toolbar.setTitle(countryCode + " " + PhoneNumberEditText.formatPhoneNumber(phoneNumber));
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.simple_chat);
            builder.setMessage(R.string.stop_verification_process)
                    .setPositiveButton(R.string._continue, (dialog, id) -> {
                        // Just continue
                    })
                    .setNegativeButton(R.string.stop, (dialog, id) -> {
                        Navigation.findNavController(v).navigate(
                                R.id.action_verifyPhoneNumberFragment_to_enterPhoneNumberFragment
                        );
                    });
            builder.show();
        });
    }

    private void setupListeners() {
        codeVerification.setOnEditorActionListener((v, actionId, event) -> {
            String code = codeVerification.getText().toString().trim();
            verifyCode(code);
            return true;
        });

        contactUs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getResources().getString(R.string.author_contact)));
            startActivity(browserIntent);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent;
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            intent = new Intent(getContext(), AskNameActivity.class);
                        } else {
                            intent = new Intent(getContext(), MainActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(),
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e(TAG, e.getMessage());
        }
    };
}
