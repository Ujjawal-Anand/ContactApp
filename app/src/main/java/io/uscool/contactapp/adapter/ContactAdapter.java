package io.uscool.contactapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import io.uscool.contactapp.R;
import io.uscool.contactapp.model.Contact;


/**
 * Created by ujjawal on 15/7/17.
 * Adapter class used to populate RecyclerView in {@link io.uscool.contactapp.fragment.ContactFragment}
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context mContext;
    private List<Contact> mContactList;
    private OnItemClickListener mClickListener;


    /**
     * Interface used to create on click listener
     */
    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * Constructor, use it to create instance of this class
     * @param context context in which this is running
     * @param contactList list containing {@link Contact}, which will be populated in the view
     */
    public ContactAdapter(Context context, List<Contact> contactList) {
        this.mContext = context;
        this.mContactList = contactList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_card,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        holder.contactName.setText(contact.getFullName()); // full name of contact
        holder.contactNumber.setText(contact.getNumber()); // contact number
        holder.contactImage.setImageDrawable(contact.getDrawable()); // text drawable, just like in Gmail

        // sets on click listener on each item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClick(view, holder.getAdapterPosition());
            }
        });
    }





    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    /**
     * Use this public method to set on click listener
     * @param clickListener implements interface {@link OnItemClickListener}
     */
    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView contactImage; // ImageView where, text drawable will be populated
        private TextView contactName;   // TextView to show contact name
        private TextView contactNumber; // TextView to show contact number

        public ViewHolder(View itemView) {
            super(itemView);
            contactImage = (ImageView) itemView.findViewById(R.id.contact_image);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactNumber = (TextView) itemView.findViewById(R.id.contact_number);
        }
    }
}
