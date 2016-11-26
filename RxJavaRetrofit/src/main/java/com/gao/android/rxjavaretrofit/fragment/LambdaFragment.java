package com.gao.android.rxjavaretrofit.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gao.android.rxjavaretrofit.R;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GaoMatrix on 2016/11/26.
 */
public class LambdaFragment extends Fragment {

    @BindView(R.id.testLambda)
    Button mTestLambda;
    @BindView(R.id.testLambda11)
    Button mTestLambda11;

    public static LambdaFragment newInstance() {

        Bundle args = new Bundle();

        LambdaFragment fragment = new LambdaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lambda, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.testLambda, R.id.testLambda11})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.testLambda:
                testLambda();
                break;
            case R.id.testLambda11:
                break;
        }
    }

    private void testLambda() {
        new Thread(() -> Logger.d("In Java8, Lambda expression rocks !!")).start();

        List list = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        // list.forEach(n->Logger.d(n));

    }
}
