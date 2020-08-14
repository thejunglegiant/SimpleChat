package com.oleksii.simplechat.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.UsersListAdapter;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.viewmodels.NewGroupViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class FinallyCreateGroupFragment extends Fragment {

    private Toolbar toolbar;
    private EditText roomTitleText;
    private LogoView roomLogo;
    private TextView membersText;
    private RecyclerView usersListRecycler;
    private UsersListAdapter mAdapter = new UsersListAdapter();;
    private NewGroupViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity())
                .get(NewGroupViewModel.class);
        viewModel.getCheckedUsers().observe(getViewLifecycleOwner(), list -> {
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
        mAdapter.enableTickIndicators(false);
        usersListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        usersListRecycler.setAdapter(mAdapter);
    }

    private void setupRoomTitleText() {
        roomTitleText.setText(viewModel.getRoomTitle());
        RxTextView.textChanges(roomTitleText)
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(input -> {
                    if (input.toString().trim().length() > 0) {
                        roomLogo.setVisibility(View.VISIBLE);
                        roomLogo.addText(input.toString().trim());
                    } else {
                        roomLogo.setVisibility(View.INVISIBLE);
                    }

                    viewModel.setRoomTitle(input.toString().trim());
                });
    }

    private void setupFab() {
        fab.setOnClickListener(v -> {
            if (!roomTitleText.getText().toString().trim().isEmpty()) {
                viewModel.createNewGroup();
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), getString(R.string.enter_group_name_first),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}