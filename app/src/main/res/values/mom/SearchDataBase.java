package vp.mom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by shivkanya.i on 21-01-2016.
 */
public class SearchDataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mom";

    // Contacts table name
    private static final String TABLE_SEARCH_ITEM = "momsearchItem";
    private static final String TABLE_SEARCH_PEOPLE = "momsearchPeople";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
  //  private static final String KEY_PH_NO = "phone_number";

    public SearchDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_SEARCH_ITEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT  UNIQUE" + ")";
        db.execSQL(CREATE_ITEM_TABLE);

        String CREATE_PEOPLE_TABLE = "CREATE TABLE " + TABLE_SEARCH_PEOPLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT  UNIQUE" + ")";
        db.execSQL(CREATE_PEOPLE_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_PEOPLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
  public   void addseracheItem(String serachData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, serachData); // Contact Name
       // values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_SEARCH_ITEM, null, values);
        db.close(); // Closing database connection
    }

    public   void addserachePeople(String serachData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, serachData); // Contact Name
        // values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_SEARCH_PEOPLE, null, values);
        db.close(); // Closing database connection
    }

//    // Getting single contact
//    Contact getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
//                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }

    // Getting All Contacts
    public ArrayList<String> getAllSearchPeople() {
        ArrayList<String> serachItemList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_PEOPLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

              //  contact.setID(Integer.parseInt(cursor.getString(0)));
              //  contact.setName(cursor.getString(1));
              //  contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                serachItemList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        Collections.reverse(serachItemList);
        return serachItemList;
    }
    public ArrayList<String> getAllSearchItem() {
        ArrayList<String> serachItemList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SEARCH_ITEM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                //  contact.setID(Integer.parseInt(cursor.getString(0)));
                //  contact.setName(cursor.getString(1));
                //  contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                serachItemList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        Collections.reverse(serachItemList);
        return serachItemList;
    }

//    // Updating single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contact.getName());
//        values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }

    // Deleting single contact
    public void deleteContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SEARCH_ITEM);
        db.execSQL("delete from "+ TABLE_SEARCH_PEOPLE);
        db.close();
    }


//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
}
