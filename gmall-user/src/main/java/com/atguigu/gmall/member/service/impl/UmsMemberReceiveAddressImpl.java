package com.atguigu.gmall.member.service.impl;

import com.atguigu.gmall.member.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.member.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.member.service.UmsMemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberReceiveAddressImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> listMemberReceiveAddress(String memberId) {
        UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId(memberId);

        List<UmsMemberReceiveAddress> umsMemberReceiveAddressList = memberReceiveAddressMapper.select(umsMemberReceiveAddress);
        return umsMemberReceiveAddressList;
    }

    @Override
    public boolean saveMemberReceiverAddress(UmsMemberReceiveAddress receiveAddress) {
        int i = memberReceiveAddressMapper.insertSelective(receiveAddress);
        return i > 0;
    }

    @Override
    public boolean updateMemberReceiverAddress(UmsMemberReceiveAddress receiveAddress) {
        int i = memberReceiveAddressMapper.updateByPrimaryKeySelective(receiveAddress);
        return i > 0;
    }

    @Override
    public boolean deleteMemberReceiveAddressById(String addressId) {
        int i = memberReceiveAddressMapper.deleteByPrimaryKey(addressId);
        return i > 0;
    }

    @Override
    public UmsMemberReceiveAddress getMemberReceiveAddressById(String addressId) {
        UmsMemberReceiveAddress memberReceiveAddress = memberReceiveAddressMapper.selectByPrimaryKey(addressId);
        return memberReceiveAddress;
    }
}
