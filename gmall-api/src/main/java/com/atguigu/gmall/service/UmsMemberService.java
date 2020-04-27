package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.UmsMember;

import java.util.List;

public interface UmsMemberService {
    List<UmsMember> listMember();

    UmsMember getUmsMemberById(String id);

    boolean saveUmsMember(UmsMember member);

    boolean updateUmsMemberById(UmsMember umsMember);

    boolean deleteUmsMemberById(String id);
}
