package lanou.addressbook.DataBase;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import lanou.addressbook.contactList.ContactEneity;
import lanou.addressbook.contactsPerson.ContactPersonBean;
import lanou.addressbook.message.MessageBean;


public class DBTools {


    private final SQLiteDatabase db;
    private Bitmap bmpout;

    public DBTools(Context context) {
        ContactListHelper helper = new ContactListHelper(context, DBValues.DB_NAME, null, 1);
        db = helper.getWritableDatabase();

    }

    /**
     * 删除数据 contact
     * @param entity
     */
    public void delete_data(ContactEneity entity) {
        String name = entity.getName();
        String number = entity.getNumber();
        String date = entity.getDate();

        Log.d("DBTools", name);
        Log.d("DBTools", number);
        Log.d("DBTools", date);
        int item = db.delete(DBValues.TABLE_NAME, "name = ? and number = ? and date = ? ", new String[]{name, number, date});
        Log.d("DBTools", "item:" + item);
    }

    /**
     * 添加数据: 向系统的contact(通话记录)表添加数据
     * @param entity 数据类 通话记录的数据类
     */
    public void insert_data(ContactEneity entity) {

        Cursor cursor = db.query("contact", null, "date = ?", new String[]{entity.getDate()}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return;
        }
            ContentValues values = new ContentValues();
            Log.d("DBTools", "entity:" + entity);
            Log.d("DBTools", entity.getName());
            values.put("name", entity.getName());
            values.put("number", entity.getNumber());
            values.put("date", entity.getDate());
            values.put("checked", entity.ischecked() ? "true" : "false");
            values.put("visible", entity.isVisibile() ? "true" : "false");
            db.insert(DBValues.TABLE_NAME, null, values);

    }

    // 查询数据
    public ArrayList<ContactEneity> querydata() {
        // 使用 helper 类实现数据库查询

        // 参数一: table, 表名
        // 参数二: column, 列名, 如果是空的话, 代表查询所有的列
        // 参数三: selection, 查询条件, "name = ?"
        // 参数四: selectionArgs, 条件的值 ,  new String[]{"张三"}
        // 参数五: groupby, 分组,
        // 参数六: having
        // 参数七: orderby 排序规则

        ArrayList<ContactEneity> list = new ArrayList<>();

        Cursor cursor = db.query(DBValues.TABLE_NAME, null, null, null, null, null, "date desc");
        Log.d("DBTools", "cursor:" + cursor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ContactEneity eneity = new ContactEneity();
                eneity.setName(cursor.getString(cursor.getColumnIndex("name")));
                eneity.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                eneity.setDate(cursor.getString(cursor.getColumnIndex("date")));
                eneity.setIschecked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("checked"))));
                eneity.setVisibile(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("visible"))));
                list.add(eneity);
            }

            cursor.close();

        }
        return list;

    }

    /**
     * 查询联系人
     * @return
     */
    public ArrayList<ContactPersonBean> querydatafromcontactperson() {
        // 使用 helper 类实现数据库查询

        // 参数一: table, 表名
        // 参数二: column, 列名, 如果是空的话, 代表查询所有的列
        // 参数三: selection, 查询条件, "name = ?"
        // 参数四: selectionArgs, 条件的值 ,  new String[]{"张三"}
        // 参数五: groupby, 分组,
        // 参数六: having
        // 参数七: orderby 排序规则

        ArrayList<ContactPersonBean> list = new ArrayList<>();

        Cursor cursor = db.query(DBValues.CONTACT_TABLE_NAME, null, null, null, null, null,"name");
        Log.d("DBTools", "cursor:" + cursor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ContactPersonBean eneity = new ContactPersonBean();
                eneity.setName(cursor.getString(cursor.getColumnIndex("name")));
                eneity.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                byte[] in = cursor.getBlob(cursor.getColumnIndex("imageResource"));
                bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                Log.d("DBTools", "bmpout:" + bmpout);
                eneity.setBitmap(bmpout);
                list.add(eneity);
            }
            cursor.close();
        }


        return list;
    }

    /**
     * 插入联系人
     * @param bean
     */
    public void insert_contactperson_data(ContactPersonBean bean) {
        Cursor curosr = db.query(DBValues.CONTACT_TABLE_NAME,null,"number = ?",new String[]{bean.getNumber()},null,null,null);
        if(curosr.getCount() >0) {
            return;
        }
        //转为字节数组
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bean.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, os);
        ContentValues values1 = new ContentValues();
        Log.d("MainActivity", "os.toByteArray():" + os.toByteArray());
        byte[] bytes = os.toByteArray();

       // String name = transer(bean.getName());
        values1.put("name", bean.getName());
        values1.put("number", bean.getNumber());
        values1.put("imageResource", bytes);
        Long storage_result = db.insert(DBValues.CONTACT_TABLE_NAME, null, values1);
    }

    public String transer(String str) {
        Log.d("DBTools", "进入transer");
        String tmpStr="";
        if(str.length()>0){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                    tmpStr += str.charAt(i);
                }
            }
        }
        Log.d("DBTools:tmpstr", tmpStr);
        return tmpStr;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void insert_message_data(MessageBean bean) {
        Log.d("DBTools", "进入插入数据阶段");
        Cursor cursor = db.query("message",null,"date = ? and name = ?",new String[]{bean.getDate(),bean.getName()},null,null,null);
        if(cursor.getCount() > 0) {
            return;
        }
        cursor.close();
        ContentValues values = new ContentValues();

        values.put("name",bean.getName());
        values.put("date",bean.getDate());
        values.put("content",bean.getContent());
        values.put("phonenumber",bean.getPhonenumber());
        values.put("type",bean.getType());

        Log.d("DBTools", "插入之前的名字" + bean.getName());
        db.insert("message",null,values);
    }
    public ArrayList<MessageBean> query_message_data_simple() {
        ArrayList<MessageBean> list = new ArrayList<>();
        Cursor cursor = db.query("message",null,null,null,"phonenumber",null,"date desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {

                MessageBean bean = new MessageBean();
                bean.setName(cursor.getString(cursor.getColumnIndex("name")));
                bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                bean.setDate(cursor.getString(cursor.getColumnIndex("date")));
                bean.setPhonenumber(cursor.getString(cursor.getColumnIndex("phonenumber")));
                bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                list.add(bean);

            }
        }
        return list;
    }
    /**
     * 按照要求获取短信, 即 相同 联系人 只显示最后一个
     */
    public ArrayList<MessageBean> query_message_data() {
        ArrayList<ArrayList<MessageBean>> outList = new ArrayList<>();
        Cursor outcursor = db.query("message",null,null,null,"name",null,null);
        Log.d("DBTools", "outcursor.getCount():" + outcursor.getCount());
        if(outcursor != null) {
           while (outcursor.moveToNext()) {
               String outname = outcursor.getString(outcursor.getColumnIndex("name"));
               Log.d("DBTools", "outname" + outname);
               ArrayList<MessageBean>  beans = new ArrayList<>();
               // 代码块
               {
                   Cursor cursor = db.query("message",null,"name = ?",new String[]{outname},null,null,null);

                   if(cursor != null) {

                       while (cursor.moveToNext()) {

                           MessageBean bean = new MessageBean();
                           bean.setName(cursor.getString(cursor.getColumnIndex("name")));
                           bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                           bean.setDate(cursor.getString(cursor.getColumnIndex("date")));
                           bean.setPhonenumber(cursor.getString(cursor.getColumnIndex("phonenumber")));
                           Log.d("DBTools","phonenumber" + bean.getPhonenumber());
                           bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
                           beans.add(bean);
                           Log.d("DBTools", "beans:" + beans);
                       }

                   }
                   cursor.close();
               }
               outList.add(beans);
           }
        }
        outcursor.close();
        Log.d("DBTools", "outList:" + outList);
        ArrayList<MessageBean> msgbeanlist = new ArrayList<>();
        MessageBean msgbean;
        for (int i = 0; i <outList.size() ; i++) {
                 msgbean = new MessageBean();
            if(outList.get(i).size() ==1) {
                msgbean.setName(outList.get(i).get(0).getName());
                msgbean.setDate(outList.get(i).get(0).getDate());
                msgbean.setContent(outList.get(i).get(0).getContent());
                msgbean.setPhonenumber(outList.get(i).get(0).getPhonenumber());
                msgbean.setType(outList.get(i).get(0).getType());
            } else {
                for (int j = 0; j <outList.get(i).size() - 1; j++) {

                    if(Long.parseLong(outList.get(i).get(j).getDate()) > Long.parseLong(outList.get(i).get(j+1).getDate())){
                        msgbean.setName(outList.get(i).get(j).getName());
                        msgbean.setDate(outList.get(i).get(j).getDate());
                        msgbean.setContent(outList.get(i).get(j).getContent());
                        msgbean.setPhonenumber(outList.get(i).get(j).getPhonenumber());
                        msgbean.setType(outList.get(i).get(j).getType());
                    }else {
                        msgbean.setName(outList.get(i).get(j +1).getName());
                        msgbean.setDate(outList.get(i).get(j +1).getDate());
                        msgbean.setContent(outList.get(i).get(j + 1).getContent());
                        msgbean.setPhonenumber(outList.get(i).get(j + 1).getPhonenumber());
                        msgbean.setType(outList.get(i).get(j + 1).getType());
                    }
                }

            }



            Log.d("DBTools", "msgbean:" + msgbean);
            Log.d("DBTools", "date"+msgbean.getDate());

            msgbeanlist.add(msgbean);
        }

        return msgbeanlist;
    }
    public String isMatch(String address) {
        Log.d("DBTools", "进入匹配");
        Log.d("MessageFragment", address);

        Cursor cursor = db.query(DBValues.CONTACT_TABLE_NAME,null,"number = ?",new String[]{address},"name",null,null,null);
        Log.d("DBTools", cursor.getColumnName(0));
        Log.d("DBTools", cursor.getColumnName(1));
        Log.d("DBTools", cursor.getColumnName(2));
        Log.d("DBTools", cursor.getColumnName(3));
        Log.d("DBTools", "cursor.getColumnCount():" + cursor.getColumnCount());
        Log.d("MessageFragment", "cursor.getCount()1:" + cursor.getCount());
        if(cursor.getCount() > 0 ) {
            String name = null;
        while(cursor.moveToNext()){

             name = cursor.getString(1);


        }
            cursor.close();
            Log.d("DBTools","name" + name);
            return name;
        }else {
            cursor.close();
            return address;
        }




    }

    public ArrayList<MessageBean> query_message_bynumber(String phonenumber) {
        Log.d("DBTools","number" + phonenumber);
       Cursor cursor = db.query("message",null,"phonenumber = ?",new String[]{phonenumber},null,null,"date asc");
        Log.d("DBTools", "cursor.getCount():" + cursor.getCount());
        ArrayList<MessageBean> beans = new ArrayList<>();
        if(cursor != null) {
            while (cursor.moveToNext()){
                MessageBean bean = new MessageBean();
                bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
                bean.setPhonenumber(cursor.getString(cursor.getColumnIndex("phonenumber")));
                bean.setType(cursor.getInt(cursor.getColumnIndex("type")));
            beans.add(bean);
            }

        }
        cursor.close();
     return beans;
    }
}




