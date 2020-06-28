package com.oleksii.simplechat.newgroupscreens;

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

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.PeopleListAdapter;

import java.util.ArrayList;

public class NewGroupFragment extends Fragment {

    private static final String TAG = "NewGroupFragment";

    public NewGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_group, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_newGroupFragment_to_chatsListFragment));

        NewGroupViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(NewGroupViewModel.class);
        viewModel.init();

        RecyclerView recyclerView = rootView.findViewById(R.id.available_people_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PeopleListAdapter adapter = new PeopleListAdapter(new ArrayList<>(), viewModel);
        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.fab).setOnClickListener(v -> {
            if (viewModel.getCheckedUsers().size() > 1)
                Navigation.findNavController(v)
                        .navigate(R.id.action_newGroupFragment_to_finallyCreateGroupFragment);
            else
                Toast.makeText(getContext(), getString(R.string.need_to_add_at_least_2_users),
                        Toast.LENGTH_SHORT).show();
        });

        viewModel.availableUsers.observe(getViewLifecycleOwner(), adapter::submitAll);

        return rootView;
    }
}