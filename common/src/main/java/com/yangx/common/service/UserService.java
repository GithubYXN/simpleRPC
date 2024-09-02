package com.yangx.common.service;

import com.yangx.common.model.User;

public interface UserService {
    /**
     *获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     *测试mock接口返回值
     *
     * @return
     */
    default short getNumber() {return 1;}
}
