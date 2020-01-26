package model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import data.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDAO getContactDao();
}
