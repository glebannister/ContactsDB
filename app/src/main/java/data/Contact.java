package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "contact_id")
    private long id;
    @ColumnInfo (name = "contact_name")
    private String name;
    @ColumnInfo (name = "contact_secondName")
    private String secondName;
    @ColumnInfo (name = "contact_email")
    private String email;
    @ColumnInfo (name = "contact_phone")
    private String phone;

    public Contact(long id, String name, String secondName, String email, String phone) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
