package org.success.isp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.success.isp.pojos.IssueQuestion;
import org.success.isp.views.StepMenuFragment;

import java.util.ArrayList;

public class StepMenuPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<IssueQuestion> questions;

    public void setQuestions(ArrayList<IssueQuestion> questions) {
        this.questions = questions;
    }

    public StepMenuPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        StepMenuFragment fragment = new StepMenuFragment();
        fragment.setQuestion(questions.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return questions.size();
    }
}
