package io.uscool.contactapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import io.uscool.contactapp.Database.DatabaseHelper;
import io.uscool.contactapp.R;
import io.uscool.contactapp.adapter.MessageAdapter;
import io.uscool.contactapp.model.Message;

/**
 * Created by ujjawal on 15/7/17.
 *
 * Populates Message tab in {@link io.uscool.contactapp.activity.MainActivity }
 * with list of already sent messages
 */

public class MessageFragment extends Fragment {
    
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;

    /**
     * use this public method to create instance of this class
     * @return {@link MessageFragment}
     */
    public static MessageFragment newInstance() {
        return new MessageFragment();
    }


    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycleview, container, false);

    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.layout_no_data);
    }

    /**
     * Populates the view with messages stored in database
     * Putting this here helps in retrieving all the recent changes in database table
     */
    @Override
    public void onResume() {
        super.onResume();

        List<Message> messageList = DatabaseHelper.getMessages(getContext(), true);
        if(messageList != null) {
            mLinearLayout.setVisibility(View.GONE);
            setupRecyclerView(messageList);
        }
    }

    private void setupRecyclerView(List<Message> messageList) {
        MessageAdapter adapter = new MessageAdapter(getContext(), messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

}
