package lanou.addressbook.message;

import java.io.Serializable;

/**
 * Created by dllo on 16/9/29.
 */
public class MessageBean implements Serializable {
    String  name;
    String  content;
    String  date;  // 1 接收  2 发送
    String  phonenumber;
    public  MessageBean() {
        super();
    }
    public MessageBean(String name, String content, String date, String phonenumber, int type) {
        this.name = name;
        this.content = content;
        this.date = date;
        this.phonenumber = phonenumber;
        this.type = type;
    }

    int  type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
