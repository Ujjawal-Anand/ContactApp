package io.uscool.contactapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.uscool.contactapp.R;
import io.uscool.contactapp.model.Contact;
import io.uscool.contactapp.model.Message;

/**
 * Created by ujjawal on 16/7/17.
 * Adapter class used to populate RecycleView in {@link io.uscool.contactapp.fragment.MessageFragment}
 * with a list of {@link Message}
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> mMessageList;
    private ContactAdapter.OnItemClickListener mClickListener;

    /**
     * Class constructor
     * @param context context in which this is running in
     * @param messageList list containing {@link Message} to populate in the view
     */
    public MessageAdapter(Context context, List<Message> messageList) {
        this.mContext = context;
        this.mMessageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_card,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int size = mMessageList.size();

        // as message should be in descending order of date and time
        Message message = mMessageList.get(size-position-1);
        Contact contact = message.getContact();

        holder.contactName.setText(contact.getFullName());
        holder.contactNumber.setText(contact.getNumber());
        holder.contactImage.setImageDrawable(contact.getDrawable());
        holder.messageTime.setText(message.getTimestamp());
        holder.otpCode.setText("Sent OTP : " + message.getOtpCode());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView contactImage; // ImageView to populate text drawable
        private TextView contactName;   // TextView to set contact name
        private TextView contactNumber;  // TextView to set contact Number
        private TextView messageTime;  // TextView to set time at which message had been sent
        private TextView otpCode;      // TextView to set sent OTP code

        public ViewHolder(View itemView) {
            super(itemView);
            contactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactNumber = (TextView) itemView.findViewById(R.id.contact_number);
            messageTime = (TextView) itemView.findViewById(R.id.message_timestamp);
            otpCode = (TextView) itemView.findViewById(R.id.message_otp);
        }
    }
}
