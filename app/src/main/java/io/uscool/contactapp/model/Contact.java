package io.uscool.contactapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by ujjawal on 15/7/17.
 *
 */

public class Contact implements Parcelable {
    public static final String TAG = "Contact";


    private String mFirstName; // first name of the contact
    private String mLastName;  // last name of the contact
    private String mFullName;  // full name of the contact
    private String mNumber;    // mobile number of the contact
    private int mDrawableColor;  // color used to draw contact image

    public Contact(String firstName, String lastName, String number) {
        this.mFirstName = firstName;
        this.mLastName = lastName;
        new Contact(firstName+ " "+ lastName, number);
    }

    public Contact(String fullName, String number) {
        this.mFullName = fullName;
        this.mNumber = number;
        this.mDrawableColor = setRandomColor();
    }

    public Contact(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
        mFullName = in.readString();
        mNumber = in.readString();
        mDrawableColor = in.readInt();
    }


    public String getFullName() {
        return mFullName;
    }

    public String getLastName() {
        if(mLastName != null) {
            return mLastName;
        }
        return "";
    }

    public String getFirstName() {
        if(mFirstName != null) {
            return mFirstName;
        }
        return getFullName();
    }

    public TextDrawable getDrawable() {
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();
        return builder.build(getNameInitial(), getColor());
    }

    /**
     * used to create Gmail like contact image
     * @return first letter of name
     */
    public String getNameInitial() {
        return String.valueOf(getFirstName().charAt(0));
    }

    public int getColor() {
        return mDrawableColor;
    }

    public String getNumber() {
        return mNumber;
    }

    private int setRandomColor() {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        return generator.getRandomColor();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(getFirstName());
        dest.writeString(getLastName());
        dest.writeString(getFullName());
        dest.writeString(getNumber());
        dest.writeInt(getColor());
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }


        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
