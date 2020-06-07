package com.oleksii.simplechat.mainflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsListFragment extends Fragment {

    public ChatsListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats_list, container, false);

        Button button = rootView.findViewById(R.id.log_out_button);
        button.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());

        return rootView;
    }
}
