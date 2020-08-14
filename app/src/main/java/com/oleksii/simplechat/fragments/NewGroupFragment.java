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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.delegates.TickDelegate;
import com.oleksii.simplechat.adapters.UsersListAdapter;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.viewmodels.NewGroupViewModel;

public class NewGroupFragment extends Fragment {

    private static final String TAG = NewGroupFragment.class.getName();
    private Toolbar toolbar;
    private NewGroupViewModel viewModel;
    private RecyclerView usersListRecycler;
    private UsersListAdapter mAdapter = new UsersListAdapter();
    private FloatingActionButton fab;

    public NewGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_group, container, false);

        toolbar = rootView.findViewById(R.id.toolbar);
        usersListRecycler = rootView.findViewById(R.id.available_people_list);
        fab = rootView.findViewById(R.id.fab);

        setupToolbar();
        setupViewModel();
        setupUsersListRecycler();
        setupFab();

        return rootView;
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(NewGroupViewModel.class);
        viewModel.getAvailableUsers().observe(getViewLifecycleOwner(), mAdapter::submitAll);
    }

    private void setupUsersListRecycler() {
        usersListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.enableTickIndicators(true);
        mAdapter.setCheckedUsers(viewModel.getCheckedUsers().getValue());
        mAdapter.attachDelegate(new TickDelegate() {
            @Override
            public void checkUser(User user) {
                viewModel.addCheckedUser(user);
            }

            @Override
            public void unCheckUser(User user) {
                viewModel.removeCheckedUser(user);
            }
        });
        usersListRecycler.setAdapter(mAdapter);
    }

    private void setupFab() {
        fab.setOnClickListener(v -> {
            if (viewModel.getCheckedUsers().getValue() != null
                    && viewModel.getCheckedUsers().getValue().size() > 0)
                Navigation.findNavController(v)
                        .navigate(R.id.action_newGroupFragment_to_finallyCreateGroupFragment);
            else
                Toast.makeText(getContext(), getString(R.string.need_to_add_at_least_1_users),
                        Toast.LENGTH_SHORT).show();
        });
    }
}