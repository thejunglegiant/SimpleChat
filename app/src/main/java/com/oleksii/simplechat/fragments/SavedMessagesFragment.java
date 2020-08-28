package com.oleksii.simplechat.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding4.recyclerview.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding4.recyclerview.RxRecyclerView;
import com.oleksii.simplechat.BaseApplication;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.MessagesListAdapter;
import com.oleksii.simplechat.factories.SavedMessagesVMFactory;
import com.oleksii.simplechat.utils.DateUtil;
import com.oleksii.simplechat.utils.KeyboardUtil;
import com.oleksii.simplechat.viewmodels.SavedMessagesViewModel;

import java.sql.Timestamp;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class SavedMessagesFragment extends Fragment {

    private Toolbar toolbar;
    private TextView floatingHeader;
    private ImageButton sendButton;
    private EditText messageEditText;
    private LinearLayoutManager layoutManager;
    private RecyclerView messagesListRecycler;
    private MessagesListAdapter mAdapter = new MessagesListAdapter();
    private SavedMessagesViewModel viewModel;

    public SavedMessagesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_messages, container, false);

        toolbar = rootView.findViewById(R.id.main_toolbar);
        floatingHeader = rootView.findViewById(R.id.floating_header);
        messagesListRecycler = rootView.findViewById(R.id.messages_list);
        sendButton = rootView.findViewById(R.id.send_button);
        messageEditText = rootView.findViewById(R.id.message_box);

        setupToolbar();
        setupAdapter();
        setupViewModel();
        setupFloatingHeader();
        setupMessageBoxSection();

        return rootView;
    }

    private void setupFloatingHeader() {
        final boolean[] animationAllowed = {false};
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        Handler handler = new Handler();
        Runnable runnable = () -> floatingHeader.startAnimation(fadeOut);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                floatingHeader.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingHeader.setVisibility(View.GONE);
                animationAllowed[0] = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        RxRecyclerView.scrollEvents(messagesListRecycler)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecyclerViewScrollEvent>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) { }

                    @Override
                    public void onNext(@NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) {
                        floatingHeader.setText(DateUtil.getDayMonthString(new Timestamp(
                                viewModel.getMessagesList().getValue().get(layoutManager
                                        .findFirstVisibleItemPosition()).getSendingTime().getTime())));

                        handler.removeCallbacks(runnable);
                        if (animationAllowed[0]
                                && layoutManager.findLastVisibleItemPosition()
                                < viewModel.getMessagesList().getValue().size() - 1) {
                            animationAllowed[0] = false;
                            floatingHeader.startAnimation(fadeIn);
                        }
                        handler.postDelayed(runnable, 500);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            goToMainList();
        });
    }

    private void goToMainList() {
        KeyboardUtil.hideKeyboardFrom(messageEditText);
        Navigation.findNavController(toolbar)
                .navigate(R.id.action_savedMessagesFragment_to_roomsListFragment);
    }

    private void setupAdapter() {
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        messagesListRecycler.setLayoutManager(layoutManager);
        messagesListRecycler.setAdapter(mAdapter);
        messagesListRecycler.addOnLayoutChangeListener((v, left, top, right, bottom,
                                                        oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                messagesListRecycler.postDelayed(() ->
                        messagesListRecycler.smoothScrollToPosition(mAdapter.getItemCount()), 100);
            }
        });
    }

    private void setupViewModel() {
        SavedMessagesVMFactory factory = new SavedMessagesVMFactory(
                ((BaseApplication) getActivity().getApplication()).getApplicationComponent().getDatabase().messagesDao()
        );
        viewModel = new ViewModelProvider(this, factory).get(SavedMessagesViewModel.class);
        viewModel.getMessagesList().observe(getViewLifecycleOwner(), list -> {
            mAdapter.submitAll(list);
            messagesListRecycler.scrollToPosition(mAdapter.getItemCount() - 1);
        });
    }

    private void setupMessageBoxSection() {
        Animation appearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_appear);
        Animation disappearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_disappear);
        disappearAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && sendButton.getVisibility() == View.GONE) {
                    sendButton.startAnimation(appearAnim);
                    sendButton.setVisibility(View.VISIBLE);
                } else if (s.length() < 1) {
                    sendButton.startAnimation(disappearAnim);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        sendButton.setOnClickListener(v -> {
            viewModel.saveMessage(messageEditText.getText().toString().trim());
            messageEditText.setText("");
        });
    }
}