package com.oleksii.simplechat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oleksii.simplechat.R;

public class InviteFriendsFragment extends Fragment {

    public InviteFriendsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_friends, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_inviteFriendsFragment_to_chatsListFragment));

        Button shareButton = rootView.findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareSub = getString(R.string.check_out_this_perfect_chat);
            String shareBody = getString(R.string.github_author_url);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));
        });

        return rootView;
    }
}
