package com.oleksii.simplechat.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.UsersListAdapter;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.viewmodels.NewGroupViewModel;

public class FinallyCreateGroupFragment extends Fragment {

    private Toolbar toolbar;
    private EditText roomTitleText;
    private LogoView roomLogo;
    private TextView membersText;
    private RecyclerView usersListRecycler;
    private UsersListAdapter mAdapter;
    private NewGroupViewModel newGroupViewModel;
    private FloatingActionButton fab;

    public FinallyCreateGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finally_create_group, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        roomTitleText = rootView.findViewById(R.id.room_title_edit);
        roomLogo = rootView.findViewById(R.id.room_logo);
        membersText = rootView.findViewById(R.id.members);
        usersListRecycler = rootView.findViewById(R.id.users_list);
        fab = rootView.findViewById(R.id.fab);

        setupToolbar();
        setupViewModel();
        setupRoomTitleText();
        setupUsersListRecycler();
        setupFab();

        return rootView;
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_finallyCreateGroupFragment_to_newGroupFragment));
    }

    private void setupViewModel() {
        newGroupViewModel = new ViewModelProvider(requireActivity())
                .get(NewGroupViewModel.class);
        newGroupViewModel.checkedUsers.observe(getViewLifecycleOwner(), list -> {
            String str = list.size() + " ";
            if (list.size() > 1) {
                str += getString(R.string.members);
            } else {
                str += getString(R.string.member);
            }

            mAdapter.submitAll(list);
            membersText.setText(str);
        });
    }

    private void setupUsersListRecycler() {
        usersListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UsersListAdapter(newGroupViewModel, false);
        usersListRecycler.setAdapter(mAdapter);
    }

    private void setupRoomTitleText() {
        roomTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    roomLogo.setVisibility(View.VISIBLE);
                    roomLogo.addText(s.toString());
                } else {
                    roomLogo.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setupFab() {
        fab.setOnClickListener(v -> {
            if (!roomTitleText.getText().toString().trim().isEmpty()) {
                newGroupViewModel.createNewGroup(roomTitleText.getText().toString().trim());
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), getString(R.string.enter_group_name_first),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}