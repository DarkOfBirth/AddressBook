package lanou.addressbook.phoneDial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by dllo on 16/9/19.
 */
public class DialAdapter extends FragmentPagerAdapter {
    private String[] title = {"拨号","通讯录","联系人","信息"};
    public ArrayList<Fragment> list;
    public DialAdapter(FragmentManager fm) {
        super(fm);
        // 数据已经改变
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Fragment> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
       return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
