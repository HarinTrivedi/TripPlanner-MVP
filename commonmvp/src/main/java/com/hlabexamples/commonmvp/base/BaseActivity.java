package com.hlabexamples.commonmvp.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hlabexamples.commonmvp.utils.PreferenceUtils;

/**
 * Created by H.T. on 01/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public PreferenceUtils preferences;

    protected abstract int attachView();

    protected abstract void initView();

    protected abstract void initToolbar();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new PreferenceUtils(this);

        setContentView(attachView());
        initToolbar();
        initView();
    }

    /**
     * Adds the Fragment into layout container
     *
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param currentFragment             Current loaded Fragment to be hide
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public boolean addFragment(final int fragmentContainerResourceId, final Fragment currentFragment, final Fragment nextFragment) throws IllegalStateException {
        if (currentFragment == null || nextFragment == null) {
            return false;
        }
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(nextFragment.getClass().getSimpleName());

        final Fragment parentFragment = currentFragment.getParentFragment();
        fragmentTransaction.hide(parentFragment == null ? currentFragment : parentFragment);
        fragmentTransaction.commit();
        return true;
    }


    /**
     * Replaces the Fragment into layout container
     *
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param fragmentManager             FRAGMENT MANGER
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public boolean replaceFragment(final int fragmentContainerResourceId, final FragmentManager fragmentManager, final Fragment nextFragment) throws IllegalStateException {
        if (nextFragment == null || fragmentManager == null) {
            return false;
        }
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
        return true;
    }
}
