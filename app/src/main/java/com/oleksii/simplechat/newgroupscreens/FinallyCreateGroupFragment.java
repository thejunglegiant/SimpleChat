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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.PeopleListAdapter;

import java.util.ArrayList;

public class FinallyCreateGroupFragment extends Fragment {

    public FinallyCreateGroupFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finally_create_group, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_finallyCreateGroupFragment_to_newGroupFragment));

        EditText groupNameText = rootView.findViewById(R.id.group_name_edit);
        TextView membersText = rootView.findViewById(R.id.members);

        NewGroupViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(NewGroupViewModel.class);

        RecyclerView recyclerView = rootView.findViewById(R.id.users_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        PeopleListAdapter adapter = new PeopleListAdapter(new ArrayList<>(),
                viewModel, false);
        recyclerView.setAdapter(adapter);

        rootView.findViewById(R.id.fab).setOnClickListener(v -> {
            if (!groupNameText.getText().toString().trim().isEmpty()) {
                viewModel.createNewGroup(groupNameText.getText().toString().trim());
                viewModel.clear();
                Navigation.findNavController(v)
                        .navigate(R.id.action_finallyCreateGroupFragment_to_chatsListFragment);
            } else {
                Toast.makeText(getContext(), getString(R.string.enter_group_name_first),
                        Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.checkedUsers.observe(getViewLifecycleOwner(), list -> {
            String str = list.size() + " ";
            if (list.size() > 1) {
                str += getString(R.string.members);
            } else {
                str += getString(R.string.member);
            }

            adapter.submitAll(list);
            membersText.setText(str);
        });

        return rootView;
    }


}