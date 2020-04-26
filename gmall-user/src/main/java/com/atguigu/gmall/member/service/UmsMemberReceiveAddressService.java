package com.atguigu.gmall.member.service;

import com.atguigu.gmall.member.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UmsMemberReceiveAddressService {
    List<UmsMemberReceiveAddress> listMemberReceiveAddress(String memberId);

    boolean saveMemberReceiverAddress(UmsMemberReceiveAddress receiveAddress);

    boolean updateMemberReceiverAddress(UmsMemberReceiveAddress receiveAddress);

    boolean deleteMemberReceiveAddressById(String addressId);

    UmsMemberReceiveAddress getMemberReceiveAddressById(String addressId);
}
