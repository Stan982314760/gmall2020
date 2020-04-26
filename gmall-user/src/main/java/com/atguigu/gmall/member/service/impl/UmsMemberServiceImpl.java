package com.atguigu.gmall.member.service.impl;

import com.atguigu.gmall.member.bean.UmsMember;
import com.atguigu.gmall.member.mapper.UmsMemberMapper;
import com.atguigu.gmall.member.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
