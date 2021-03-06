package com.test.xyz.demo.presentation.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.xyz.demo.R;
import com.test.xyz.demo.presentation.common.BaseFragment;
import com.test.xyz.demo.presentation.common.di.DaggerApplication;
import com.test.xyz.demo.presentation.common.util.UIHelper;
import com.test.xyz.demo.presentation.mainlobby.MainActivity;
import com.test.xyz.demo.presentation.weather.di.WeatherFragmentModule;
import com.test.xyz.demo.presentation.weather.presenter.WeatherPresenter;
import com.test.xyz.demo.presentation.weather.presenter.WeatherView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WeatherFragment extends BaseFragment implements WeatherView {
    private Unbinder unbinder;

    @BindView(R.id.userNameText) EditText userNameText;
    @BindView(R.id.cityText) EditText cityText;
    @BindView(R.id.btnShowInfo) Button showInfoButton;
    @BindView(R.id.resultView) TextView resultView;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.weatherContainer) LinearLayout weatherContainer;

    @Inject WeatherPresenter presenter;

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        weatherContainer.setOnTouchListener((view, motionEvent) -> {
            UIHelper.hideKeyboard(getActivity());
            return false;
        });
        showInfoButton.setOnClickListener((view) -> {
            UIHelper.hideKeyboard(this.getActivity());
            presenter.requestWeatherInformation();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            DaggerApplication.get(this.getContext())
                    .getAppComponent()
                    .plus(new WeatherFragmentModule(this))
                    .inject(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public String getUserNameText() {
        return userNameText.getText().toString();
    }

    @Override
    public String getCityText() {
        return cityText.getText().toString();
    }

    @Override
    public void showUserNameError(final int messageId) { userNameText.setError(getString(messageId)); }

    @Override
    public void showCityNameError(final int messageId) {
        cityText.setError(getString(messageId));
    }

    @Override
    public void showBusyIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBusyIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showResult(final String result) {
        resultView.setText(result);
    }

    @Override
    public void showGenericError(final int messageID) { UIHelper.showToastMessage(WeatherFragment.this.getActivity(), getString(messageID)); }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}