package io.uscool.contactapp.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.uscool.contactapp.R;
import io.uscool.contactapp.activity.ShowProfileActivity;
import io.uscool.contactapp.adapter.ContactAdapter;
import io.uscool.contactapp.model.Contact;
import io.uscool.contactapp.model.JsonAttributes;

/**
 * Created by ujjawal on 15/7/17.
 * Fragment class that shows a list containing all contacts
 */

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    public static ContactFragment newInstance() {
        return new ContactFragment();
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_no_data);

        List<Contact> contactList = new ArrayList<>();
        try {
           contactList =  getContactList();
        } catch (IOException|JSONException e) {
            Log.e(TAG, "setupRecyclerView: failed to read json file", e.fillInStackTrace() );
        }
        if(contactList != null) {
            linearLayout.setVisibility(View.GONE);
            setupRecyclerView(recyclerView, contactList);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Contact> contactList) {

        ContactAdapter adapter = new ContactAdapter(getContext(), contactList);
        setOnclickListener(adapter, contactList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setOnclickListener(final ContactAdapter subjectAdapter,
                                    final List<Contact> contactList) {
        subjectAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent startIntent = ShowProfileActivity.getStartIntent(getActivity(),
                        contactList.get(position));
                startActivity(startIntent);
            }
        });
    }

    /**
     * reads contact json file
     * @return string containing {@link Contact} in json format
     * @throws IOException in case any exception occurs
     */
    private String readContactsFromResources() throws IOException {
        StringBuilder statesJson = new StringBuilder();
        Resources resources = getResources();
        InputStream rawStates = resources.openRawResource(R.raw.contact);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rawStates));
        String line;
        while ((line = reader.readLine()) != null) {
            statesJson.append(line);
        }

        return statesJson.toString();
    }


    /**
     * reads from a jsonString and converts it into contact {@link Contact}
     * @return all contacts in json file
     * @throws IOException in case json file not presents etc.
     * @throws JSONException when json file related error occured
     */
    private List<Contact> getContactList() throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray(readContactsFromResources());
        JSONObject contact;
        List<Contact> tmpContactList = new ArrayList<>(jsonArray.length());
        for(int i = 0; i < jsonArray.length(); i++) {
            contact = jsonArray.getJSONObject(i);
            final String fullName = contact.getString(JsonAttributes.FULL_NAME);
            final String countryCode = contact.getString(JsonAttributes.COUNTRY_CODE);
            final String mobileNumber = contact.getString(JsonAttributes.MOBILE_NUMBER);
            tmpContactList.add(new Contact(fullName, countryCode+mobileNumber));
        }
        return tmpContactList;
    }

}
