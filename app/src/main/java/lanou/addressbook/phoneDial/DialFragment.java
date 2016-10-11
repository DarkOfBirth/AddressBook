package lanou.addressbook.phoneDial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import lanou.addressbook.R;

/**
 * Created by dllo on 16/9/19.
 */
public class DialFragment extends Fragment implements View.OnClickListener {


    private ImageButton btn_1;
    private ImageButton btn_2;
    private ImageButton btn_3;
    private ImageButton btn_4;
    private ImageButton btn_5;
    private ImageButton btn_6;
    private ImageButton btn_7;
    private ImageButton btn_8;
    private ImageButton btn_9;
    private ImageButton btn_0;
    private ImageButton btn_delete;
    private ImageButton btn_call;
    private TextView record;
    private String callValue;

    private void setclick() {
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);

    }

    private void init() {
        callValue = "";


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone_dial_fragment, null);
        btn_1 = (ImageButton) v.findViewById(R.id.image_btn_1);
        btn_2 = (ImageButton) v.findViewById(R.id.image_btn_2);
        btn_3 = (ImageButton) v.findViewById(R.id.image_btn_3);
        btn_4 = (ImageButton) v.findViewById(R.id.image_btn_4);
        btn_5 = (ImageButton) v.findViewById(R.id.image_btn_5);
        btn_6 = (ImageButton) v.findViewById(R.id.image_btn_6);
        btn_7 = (ImageButton) v.findViewById(R.id.image_btn_7);
        btn_8 = (ImageButton) v.findViewById(R.id.image_btn_8);
        btn_9 = (ImageButton) v.findViewById(R.id.image_btn_9);
        btn_0 = (ImageButton) v.findViewById(R.id.image_btn_0);
        btn_delete = (ImageButton) v.findViewById(R.id.image_btn_delete);
        btn_call = (ImageButton) v.findViewById(R.id.image_btn_bohao);

        record = (TextView) v.findViewById(R.id.tv_record);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setclick();

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record.getText().toString().length()!=0) {
                    String str = record.getText().toString().substring(0, record.getText().toString().length() - 1);
                    record.setText(str);
                }
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "tel:" + record.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(str));
                startActivity(intent);
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn_0:
                callValue = record.getText().toString();
                callValue += "0";
                break;
            case R.id.image_btn_1:
                callValue = record.getText().toString();
                callValue += "1";
                break;
            case R.id.image_btn_2:
                callValue = record.getText().toString();
                callValue += "2";
                break;
            case R.id.image_btn_3:
                callValue = record.getText().toString();
                callValue += "3";
                break;
            case R.id.image_btn_4:
                callValue = record.getText().toString();
                callValue += "4";
                break;
            case R.id.image_btn_5:
                callValue = record.getText().toString();
                callValue += "5";
                break;
            case R.id.image_btn_6:
                callValue = record.getText().toString();
                callValue += "6";
                break;
            case R.id.image_btn_7:
                callValue = record.getText().toString();
                callValue += "7";
                break;
            case R.id.image_btn_8:
                callValue = record.getText().toString();
                callValue += "8";
                break;
            case R.id.image_btn_9:
                callValue = record.getText().toString();
                callValue += "9";
                break;
            case R.id.image_btn_keyhash:
                callValue += "#";
                break;
            case R.id.image_btn_keypound:
                callValue += "*";
                break;

        }
        record.setText(callValue);
    }


}