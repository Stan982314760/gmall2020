package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UmsMemberService;
import com.atguigu.gmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public List<UmsMember> listMember() {
        List<UmsMember> umsMemberList = umsMemberMapper.selectAll();
        return umsMemberList;
    }

    @Override
    public UmsMember getUmsMemberById(String id) {
        UmsMember umsMember = umsMemberMapper.selectByPrimaryKey(id);
        return umsMember;
    }

    @Override
    public boolean saveUmsMember(UmsMember member) {
        int i = umsMemberMapper.insertSelective(member);
        return i > 0;
    }

    @Override
    public boolean updateUmsMemberById(UmsMember umsMember) {
        int i = umsMemberMapper.updateByPrimaryKeySelective(umsMember);
        return i > 0;
    }

    @Override
    public boolean deleteUmsMemberById(String id) {
        int i = umsMemberMapper.deleteByPrimaryKey(id);
        return i > 0;
    }
}
