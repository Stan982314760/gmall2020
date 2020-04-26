package com.atguigu.gmall.member.service;

import com.atguigu.gmall.member.bean.UmsMember;

import java.util.List;

public interface UmsMemberService {
    List<UmsMember> listMember();

    UmsMember getUmsMemberById(String id);

    boolean saveUmsMember(UmsMember member);

    boolean updateUmsMemberById(UmsMember umsMember);

    boolean deleteUmsMemberById(String id);
}
