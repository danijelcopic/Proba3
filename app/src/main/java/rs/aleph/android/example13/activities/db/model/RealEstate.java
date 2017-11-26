package rs.aleph.android.example13.activities.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

import static rs.aleph.android.example13.activities.db.model.RealEstate.FIELD_NAME_PHONE;
import static rs.aleph.android.example13.activities.db.model.RealEstate.FIELD_NAME_PRICE;
import static rs.aleph.android.example13.activities.db.model.RealEstate.FIELD_NAME_ROOMS;
import static rs.aleph.android.example13.activities.db.model.RealEstate.FIELD_NAME_SQUARE;

@DatabaseTable(tableName = RealEstate.TABLE_NAME_NOTES)
public class RealEstate {


    public static final String TABLE_NAME_NOTES = "realestate";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_PICTURES = "pictures";
    public static final String FIELD_NAME_ADDRESS = "address";
    public static final String FIELD_NAME_PHONE = "phone";
    public static final String FIELD_NAME_SQUARE = "square";
    public static final String FIELD_NAME_ROOMS = "rooms";
    public static final String FIELD_NAME_PRICE = "price";



    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String mName;

    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String mDescription;

    @DatabaseField(columnName = FIELD_NAME_PICTURES)
    private String mPictures;

    @DatabaseField(columnName = FIELD_NAME_ADDRESS)
    private String mAddress;

    @DatabaseField(columnName = FIELD_NAME_PHONE)
    private int mPhone;

    @DatabaseField(columnName = FIELD_NAME_SQUARE)
    private double mSquare;

    @DatabaseField(columnName = FIELD_NAME_ROOMS)
    private double mRooms;

    @DatabaseField(columnName = FIELD_NAME_PRICE)
    private double mPrice;



    public RealEstate() {
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPictures() {
        return mPictures;
    }

    public void setmPictures(String mPictures) {
        this.mPictures = mPictures;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getmPhone() {
        return mPhone;
    }

    public void setmPhone(int mPhone) {
        this.mPhone = mPhone;
    }

    public double getmSquare() {
        return mSquare;
    }

    public void setmSquare(double mSquare) {
        this.mSquare = mSquare;
    }

    public double getmRooms() {
        return mRooms;
    }

    public void setmRooms(double mRooms) {
        this.mRooms = mRooms;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public String toString() {
        return mName;
    }
}
