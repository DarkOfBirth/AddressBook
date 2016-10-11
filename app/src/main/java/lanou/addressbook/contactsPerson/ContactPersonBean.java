package lanou.addressbook.contactsPerson;

import android.graphics.Bitmap;

/**
 * Created by dllo on 16/9/27.
 */
public class ContactPersonBean {
    private String name;
    private String number;
    private Bitmap bitmap;

    public ContactPersonBean() {
        super();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ContactPersonBean(String name, String number, Bitmap bitmap) {
        super();
        this.name = name;
        this.number = number;
        this.bitmap = bitmap;
    }
}
