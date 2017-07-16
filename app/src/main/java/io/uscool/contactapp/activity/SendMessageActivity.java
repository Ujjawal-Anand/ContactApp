package io.uscool.contactapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.uscool.contactapp.R;
import io.uscool.contactapp.fragment.SendMessageFragment;
import io.uscool.contactapp.model.Contact;


/**
 * @author Ujjawal Anand
 * Creates send message activity
 */
public class SendMessageActivity extends AppCompatActivity {

    /**
     * Use this public method to create instance of this class
     * @param context context in which this is running
     * @param contact {@link Contact} To which you want to  send message
     * @return Intent of the {@link SendMessageActivity}
     */
    public static Intent getStartIntent(Context context, Contact contact) {
        Intent starter = new Intent(context, SendMessageActivity.class);
        starter.putExtra(Contact.TAG, contact);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Contact contact =  getIntent().getParcelableExtra(Contact.TAG);
        initToolbar();
        attachFragment(contact);
    }

    /**
     * initialise and populate the toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Send Message");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }

    /**
     * Attach the view fragment
     * @param contact {@link Contact} to which the message will be send
     */
    private void attachFragment(Contact contact) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, SendMessageFragment.newInstance(contact))
                .commit();
    }

}
