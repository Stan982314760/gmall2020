package com.atguigu.gmall.member.controller;

import com.atguigu.gmall.member.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.member.service.UmsMemberReceiveAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @GetMapping("/member/address/{memberId}")
    public List<UmsMemberReceiveAddress> listMemberReceiveAddress(@PathVariable("memberId") String memberId) {
        log.debug("开始查找id={}会员的收货地址", memberId);

        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.listMemberReceiveAddress(memberId);
        return memberReceiveAddressList;
    }

    @GetMapping("/member/address/{memberId}/{addressId}")
    public UmsMemberReceiveAddress getMemberReceiveAddressById(@PathVariable("memberId") String memberId,
                                                               @PathVariable("addressId") String addressId) {
        log.debug("开始查找memberId={}, addressId={}收货地址",memberId, addressId);

        UmsMemberReceiveAddress memberReceiveAddress = memberReceiveAddressService.getMemberReceiveAddressById(addressId);
        return memberReceiveAddress;
    }


    @PostMapping("/member/address")
    public String saveMemberReceiverAddress(@RequestBody UmsMemberReceiveAddress receiveAddress) {
        log.debug("id={}的会员准备添加收货地址", receiveAddress.getMemberId());

        boolean flag = memberReceiveAddressService.saveMemberReceiverAddress(receiveAddress);
        if (flag) {
            log.debug("添加成功");
            return "ok";
        }

        log.debug("添加出错");
        return "error";

    }

    @PutMapping("/member/address")
    public String updateMemberReceiverAddress(@RequestBody UmsMemberReceiveAddress receiveAddress) {
        log.debug("准备修改收货地址 addressId={}", receiveAddress.getId());

        boolean flag = memberReceiveAddressService.updateMemberReceiverAddress(receiveAddress);
        if (flag) {
            log.debug("修改成功");
            return "ok";
        }

        log.debug("修改出错");
        return "error";
    }

    @DeleteMapping("/member/address/{addressId}")
    public String deleteMemberReceiveAddressById(@PathVariable("addressId") String addressId) {
        log.debug("开始删除addressId={}收货地址", addressId);

        boolean flag = memberReceiveAddressService.deleteMemberReceiveAddressById(addressId);
        if (flag) {
            log.debug("删除成功");
            return "ok";
        }

        log.debug("删除出错");
        return "error";
    }


}
