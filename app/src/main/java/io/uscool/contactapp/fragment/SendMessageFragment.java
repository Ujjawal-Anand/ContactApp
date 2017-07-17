package io.uscool.contactapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import io.uscool.contactapp.Database.DatabaseHelper;
import io.uscool.contactapp.R;
import io.uscool.contactapp.activity.MainActivity;
import io.uscool.contactapp.model.Contact;
import io.uscool.contactapp.model.Message;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendMessageFragment extends Fragment {
    private final static String TAG = "SendMessageFragment";
    // Its a potentially dangerous practice to put your account information in android app
    // as there is a chance that someone can decrypt the app and get this information
    // don't consider it for production.
    private final static String ACCOUNT_SID = "put your twilio account sid"; // Twilio account sid
    private final static String AUTH_TOKEN = "put your auth token";  // Twilio auth token

    private ProgressBar mProgressBar; // progress bar to show progress on message sending
    private LinearLayout mSendOtpLayout; // Linear layout to display while sending SMS
    private LinearLayout mOtpAuthLayout; // Linear layout to display while matching the OTP
    private TextView mInputOtp;  // TextView where OTP will be entered for matching
    private Button mSendBtn;

    private static String mOtpCode;

    private Contact mContact;

    public SendMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contact instance of {@link Contact}.
     * @return A new instance of fragment SendMessageFragment.
     */
    public static SendMessageFragment newInstance(Contact contact) {
        SendMessageFragment fragment = new SendMessageFragment();
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
        return inflater.inflate(R.layout.fragment_send_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView inputName = (TextView) view.findViewById(R.id.show_name);
        TextView inputNumber = (TextView) view.findViewById(R.id.show_number);
        ImageView profileImage = (ImageView) view.findViewById(R.id.profile_image);
        ImageView profileImage2 = (ImageView) view.findViewById(R.id.profile_image2);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mOtpAuthLayout = (LinearLayout) view.findViewById(R.id.layout_otp);
        mSendOtpLayout = (LinearLayout) view.findViewById(R.id.layout_sms);
        mInputOtp = (TextView) view.findViewById(R.id.inputOtp);
        mSendBtn = (Button) view.findViewById(R.id.btn_request_sms);
        Button verifyOtpBtn = (Button) view.findViewById(R.id.btn_verify_otp);

        final EditText messageText = (EditText) view.findViewById(R.id.input_message);

        inputName.setText(mContact.getFullName());
        inputNumber.setText(mContact.getNumber());
        profileImage.setImageDrawable(mContact.getDrawable());
        profileImage2.setImageDrawable(mContact.getDrawable());



        mOtpCode = getSixDigitRandomNumber();
        String message = getString(R.string.msg_sms).concat(" "+mOtpCode);
        messageText.setText(message);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(messageText.getText().toString());
                mProgressBar.setVisibility(View.VISIBLE);
                mSendBtn.setEnabled(false);
            }
        });

        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               verifyOtp();
            }
        });
    }

    /**
     * Activates OkHttp handler which sends the message and store it in database
     * @param message {@link Message} to be sent
     */
    private void sendMessage(String message) {
        OkHttpHandler handler = new OkHttpHandler(getActivity(), mContact);
        handler.execute(message);
    }

    /**
     * used to generate six digit random number for otp
     * @return six digit random number
     */
    private String getSixDigitRandomNumber() {
        Random ran = new Random();
        // ran.nextInt(900000) gives a random number between 1 and 900000, that's why we have to add 100000
        int num = ran.nextInt(900000) + 100000;
        return String.valueOf(num);
    }

    /**
     * Verify the OTP
     * show a toast and starts main activity after successful match
     * raises an error otherwise
     */
    private void verifyOtp() {
        String otp = mInputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
           if(!otp.equals(mOtpCode)) {
               mInputOtp.setError(getString(R.string.msg_otp_not_equal));
           } else {
               Toast.makeText(getContext(), "OTP matched", Toast.LENGTH_LONG).show();
               startActivity(new Intent(getContext(), MainActivity.class));
               getActivity().finish();
           }
        } else {
            mInputOtp.setError(getString(R.string.msg_enter_otp));
            Toast.makeText(getContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    public class OkHttpHandler extends AsyncTask<String, Void, Boolean> {
        private OkHttpClient client = new OkHttpClient();
        private Activity activity;
        private Contact contact;

        public OkHttpHandler(Activity context, Contact contact) {
            this.activity = context;
            this.contact = contact;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String toPhoneNumber = contact.getNumber();
            String message = params[0];


            // update the otp code, just in the case user has decided to change it
            // replace all non-digits with nothing to read the otp code in message
            mOtpCode = message.replaceAll("\\D+", "");

            // it will be the timestamp
            String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

            String url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/SMS/Messages";
            String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

            RequestBody body = new FormBody.Builder()
                    .add("From", "+16092241426")
                    .add("To", toPhoneNumber)
                    .add("Body", message)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Authorization", base64EncodedCredentials)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                DatabaseHelper.writeMessage(activity.getApplicationContext(), new Message(this.contact, mOtpCode, currentDateTime));
                Log.d(TAG, "sendSms: " + response.body().string());

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s) {
                mProgressBar.setVisibility(View.GONE);
                mSendOtpLayout.setVisibility(View.GONE);
                mOtpAuthLayout.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mSendBtn.setEnabled(true);
                Toast.makeText(activity, "Error Occured", Toast.LENGTH_LONG).show();
            }
        }
    }
}
