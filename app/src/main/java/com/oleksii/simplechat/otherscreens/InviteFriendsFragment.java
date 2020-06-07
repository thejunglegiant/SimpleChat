package com.oleksii.simplechat.otherscreens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleksii.simplechat.R;

public class InviteFriendsFragment extends Fragment {

    public InviteFriendsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friends, container, false);



        return rootView;
    }
}
