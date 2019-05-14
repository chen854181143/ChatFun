package com.chenyang.chatfun.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chenyang.chatfun.MainActivity;
import com.chenyang.chatfun.R;
import com.chenyang.chatfun.presenter.LogoutPresenter;
import com.chenyang.chatfun.presenter.impl.LogoutPresenterImpl;
import com.chenyang.chatfun.utils.ToastUtils;
import com.hyphenate.chat.EMClient;


/**
 * Created by fullcircle on 2017/1/3.
 */

public class PlugInFragment extends BaseFragment implements PluginView {
    private ProgressDialog mProgressDialog = null;
    private LogoutPresenter presenter = null;
    private Button btn_logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);
        presenter = new LogoutPresenterImpl(this);
        return inflater.inflate(R.layout.fragment_plugin,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_logout.setText("退("+ EMClient.getInstance().getCurrentUser()+")出");
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("正在退出....");
                presenter.logout();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onLogout(boolean isLogout, String msg) {
        mProgressDialog.hide();
        if(isLogout){
            MainActivity activity = (MainActivity) getActivity();
            activity.startActivity(LoginActivity.class,true);

        }else{
            ToastUtils.showToast(getActivity(),"退出失败");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
