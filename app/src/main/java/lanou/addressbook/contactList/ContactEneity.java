package lanou.addressbook.contactList;

/**
 * Created by dllo on 16/9/21.
 */
public class ContactEneity {
    private String name;
    private String number;
    private String date;
    private boolean ischecked;
    private boolean isVisibile; // 默认不可见

    public boolean isVisibile() {
        return isVisibile;
    }

    public void setVisibile(boolean visibile) {
        isVisibile = visibile;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public ContactEneity() {
        super();
    }

    public ContactEneity(String name, String number, String date) {
        super();
        this.name = name;
        this.number = number;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
