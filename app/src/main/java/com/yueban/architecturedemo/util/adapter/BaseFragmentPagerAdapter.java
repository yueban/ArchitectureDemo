package com.yueban.architecturedemo.util.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    protected List<Fragment> fragments;
    protected List<String> titles;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, null);
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles.get(position);
        }
        return "";
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}