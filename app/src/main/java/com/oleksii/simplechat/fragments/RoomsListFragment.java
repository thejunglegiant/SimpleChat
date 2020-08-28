package com.oleksii.simplechat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.RoomsListAdapter;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.viewmodels.RoomsListViewModel;

import javax.inject.Inject;

public class RoomsListFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView roomsListRecycler;
    private RoomsListAdapter mAdapter = new RoomsListAdapter();;
    private RoomsListViewModel viewModel;
    private MainActivity parentActivity;
    @Inject Socket mSocket;

    public RoomsListFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms_list, container, false);

        toolbar = rootView.findViewById(R.id.main_toolbar);
        roomsListRecycler = rootView.findViewById(R.id.rooms_list);

        setupToolbar();
        setupAdapter();
        setupViewModel();

        return rootView;
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(RoomsListViewModel.class);
        viewModel.getAvailableRooms().observe(getViewLifecycleOwner(), mAdapter::submitAll);
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.search_button) {
                // TODO
                // temporary
                FirebaseAuth.getInstance().signOut();
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
        toolbar.setNavigationOnClickListener(v -> {
            parentActivity.toggleDrawer();
        });
    }

    private void setupAdapter() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        roomsListRecycler.setLayoutManager(linearLayoutManager);
        roomsListRecycler.setAdapter(mAdapter);
    }
}
