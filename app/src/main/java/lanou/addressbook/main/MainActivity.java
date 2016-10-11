package lanou.addressbook.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import lanou.addressbook.R;
import lanou.addressbook.contactList.ContactListFragment;
import lanou.addressbook.contactsPerson.ContactsFragment;
import lanou.addressbook.message.MessageFragment;
import lanou.addressbook.phoneDial.DialAdapter;
import lanou.addressbook.phoneDial.DialFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tb;
    private ViewPager vp;


    private ArrayList<Fragment> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 装载数据
        initData();
        // 装载视图
        initView();
        initTab();
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position!=3){
                    MessageFragment messageFragment = (MessageFragment) datas.get(3);
                    messageFragment.dissMissPop();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initTab() {
        int[] selectors = {R.drawable.tab_selector_tel,R.drawable.tab_selector_callrecords
        ,R.drawable.tab_selector_contact,R.drawable.tab_selector_sms};
        for(int i = 0; i < selectors.length; i++) {
             tb.getTabAt(i).setIcon(selectors[i]);
        }
    }

    private void initView() {
        tb = (TabLayout) findViewById(R.id.tb);
        vp = (ViewPager) findViewById(R.id.vp);
        DialAdapter adapter = new DialAdapter(getSupportFragmentManager());
        adapter.setList(datas);
        vp.setAdapter(adapter);
        tb.setupWithViewPager(vp);

    }

    private void initData() {
        DialFragment dialFragment = new DialFragment();
        ContactListFragment contactlistfragment = new ContactListFragment();
        contactlistfragment.setContext(this);
        ContactsFragment contactsfragment = new ContactsFragment();
        MessageFragment messagefragment = new MessageFragment();
        datas.add(dialFragment);
        datas.add(contactlistfragment);
        datas.add(contactsfragment);
        datas.add(messagefragment);
    }
}

