package com.chenyang.chatfun.view;

import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */
public interface ContactView {
    void onInitContact(List<String> contact);
    void onUpdateContact(List<String> contact,boolean isUpdateSuccess,String errorMsg);
    void onDeleteContact(boolean isDeleteSuccess,String errorMsg);
}
