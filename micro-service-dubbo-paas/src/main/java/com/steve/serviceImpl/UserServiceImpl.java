package com.steve.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.steve.dao.UserMapper;
import com.steve.framework.core.web.ApiResult;
import com.steve.framework.core.web.RestStatusCode;
import com.steve.model.User;
import com.steve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by SteveJobson on 2017/7/13.
 */

/**
 * 根据服务提供者的服务类型设置集群容错机制
 */
@Component
@Service(version = "1.0.0", interfaceClass = UserService.class, timeout = 5000, retries = 2)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ApiResult<User> getUSerById(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return new ApiResult<User>(RestStatusCode.SUCCESS.code(), "成功", user);
    }

    @Override
    public ApiResult<List<User>> listUser() {
        List<User> list = userMapper.selectAll();
        return new ApiResult<List<User>>(RestStatusCode.SUCCESS.code(), "成功", list);
    }

    @Override
    public ApiResult<User> insertUser(User user) {
        int result = userMapper.insert(user);
        if (result == 1) {
            return new ApiResult<User>(RestStatusCode.SUCCESS.code(), "成功", user);
        } else {
            return new ApiResult<User>(RestStatusCode.INTERNAL_SERVER_ERROR.code(), "新增失败");
        }

    }

    @Override
    public ApiResult<User> updateUser(User user) {
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result == 1) {
            return new ApiResult<User>(RestStatusCode.SUCCESS.code(), "成功", user);
        } else {
            return new ApiResult<User>(RestStatusCode.INTERNAL_SERVER_ERROR.code(), "更新失败");
        }

    }

    @Override
    public ApiResult deleteUser(int userId) {
        int result = userMapper.deleteByPrimaryKey(userId);
        if (result == 1) {
            return new ApiResult(RestStatusCode.SUCCESS.code(), "成功");
        } else {
            return new ApiResult(RestStatusCode.INTERNAL_SERVER_ERROR.code(), "删除失败");
        }

    }
}
