package com.example.myapplication1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication1.fragments.BelumLunasFragment;
import com.example.myapplication1.fragments.LunasFragment;


public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new BelumLunasFragment();
            case 1:
                return new LunasFragment();
            default:
                return new BelumLunasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
