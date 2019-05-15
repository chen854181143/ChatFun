package com.chenyang.chatfun.presenter.impl;

import com.chenyang.chatfun.db.DBUtils;
import com.chenyang.chatfun.presenter.ContactPresenter;
import com.chenyang.chatfun.utils.ThreadUtils;
import com.chenyang.chatfun.view.ContactView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class ContactPresenterImpl implements ContactPresenter {
    private ContactView contactView;

    public ContactPresenterImpl(ContactView contactView) {
        this.contactView = contactView;
    }

    @Override
    public void initContact() {
        //①先到数据库中获取
        List<String> contacts = DBUtils.initContact(EMClient.getInstance().getCurrentUser());
        //ContactOpenHelper openHelper = new ContactOpenHelper()
        contactView.onInitContact(contacts);
        //②联网更新数据库
        getContactsFromServer();
    }

    @Override
    public void updateContact() {
        getContactsFromServer();
    }

    @Override
    public void deleteContact(final String username) {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onDeleteContact(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onDeleteContact(false,e.getMessage());
                        }
                    });
                }
            }
        });

    }

    public void getContactsFromServer() {
        ThreadUtils.runOnNonUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> contactList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //获取到所有联系人之后 需要进行排序
                    Collections.sort(contactList, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //获取数据后 保存到数据库
                    DBUtils.updateContactFromEMServer(EMClient.getInstance().getCurrentUser(),contactList);
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(contactList,true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(null,false,null);
                        }
                    });
                }
            }
        });

    }
}
