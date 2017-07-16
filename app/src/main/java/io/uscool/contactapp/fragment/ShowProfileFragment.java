package io.uscool.contactapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.uscool.contactapp.R;
import io.uscool.contactapp.activity.SendMessageActivity;
import io.uscool.contactapp.model.Contact;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ShowProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowProfileFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;
    private Contact mContact;

    public ShowProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contact instance of {@link .model.Contact }.
     * @return A new instance of fragment ShowProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowProfileFragment newInstance(Contact contact) {
        ShowProfileFragment fragment = new ShowProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(Contact.TAG, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = getArguments().getParcelable(Contact.TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageView = (ImageView) view.findViewById(R.id.contact_image);
        TextView contactNameView = (TextView) view.findViewById(R.id.contact_name);
        TextView contactNumberView = (TextView) view.findViewById(R.id.contact_number);
        Button btnNewActivity = (Button) view.findViewById(R.id.new_message_btn);

        imageView.setImageDrawable(mContact.getDrawable());
        contactNameView.setText(mContact.getFullName());
        contactNumberView.setText(mContact.getNumber());

        btnNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SendMessageActivity.getStartIntent(getContext(), mContact));
                getActivity().finish();
            }
        });
    }

}
