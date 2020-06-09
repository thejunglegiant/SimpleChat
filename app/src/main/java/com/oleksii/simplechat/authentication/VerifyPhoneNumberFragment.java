package com.oleksii.simplechat.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.utils.PhoneNumberEditText;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNumberFragment extends Fragment {

    private String verificationId;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootview = inflater.inflate(R.layout.fragment_verify_phone_number, container, false);

        auth = FirebaseAuth.getInstance();
        auth.useAppLanguage();
        String countryCode = getArguments().getString("countryCode");
        String phoneNumber = getArguments().getString("phoneNumber");
        sendVerificationCode(countryCode + phoneNumber);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setTitle(countryCode + " " + PhoneNumberEditText.formatPhoneNumber(phoneNumber));
        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.simple_chat);
            builder.setMessage(R.string.stop_verification_process)
                    .setPositiveButton(R.string._continue, (DialogInterface.OnClickListener) (dialog, id) -> {
                        // Just continue
                    })
                    .setNegativeButton(R.string.stop, (DialogInterface.OnClickListener) (dialog, id) -> {
                        Navigation.findNavController(v).navigate(
                                R.id.action_verifyPhoneNumberFragment_to_enterPhoneNumberFragment
                        );
                    });
            builder.show();
        });

        EditText codeVerification = rootview.findViewById(R.id.code_text);
        codeVerification.setOnEditorActionListener((TextView.OnEditorActionListener) (v, actionId, event) -> {
            String code = codeVerification.getText().toString().trim();
            verifyCode(code);
            return true;
        });

        TextView contactUs = rootview.findViewById(R.id.contact_us);
        contactUs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/thejunglegiant"));
            startActivity(browserIntent);
        });

        return rootview;
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
