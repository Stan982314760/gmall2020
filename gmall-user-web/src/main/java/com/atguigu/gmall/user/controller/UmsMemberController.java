package com.atguigu.gmall.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UmsMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class UmsMemberController {
    @Reference
    private UmsMemberService umsMemberService;

    @GetMapping("/listMember")
    public List<UmsMember> listMember() {
        log.debug("查询所有用户信息");

        List<UmsMember> umsMemberList = umsMemberService.listMember();
        return umsMemberList;
    }


    @GetMapping("/member/{id}")
    public UmsMember getUmsMemberById(@PathVariable("id") String id) {
        log.debug("开始查找id={}的member信息", id);

        UmsMember umsMember = umsMemberService.getUmsMemberById(id);
        return umsMember;
    }

    @PostMapping("/member")
    public String saveUmsMember(@RequestBody UmsMember member) {
        log.debug("开始添加member={}", member);

        boolean flag = umsMemberService.saveUmsMember(member);
        if (flag) {
            log.debug("添加成功 用户id={}", member.getId());
            return member.getId();
        }

        return "添加出错";
    }

    @PutMapping("/member")
    public String updateUmsMemberById(@RequestBody UmsMember umsMember) {
        log.debug("开始更新id={}的member", umsMember.getId());

        boolean flag = umsMemberService.updateUmsMemberById(umsMember);
        if (flag) {
            log.debug("更新成功");
            return "更新成功";
        }

        return "更新出错";
    }

    @DeleteMapping("/member/{id}")
    public String deleteUmsMemberById(@PathVariable("id") String id) {
        log.debug("准备删除id={}的member", id);

        boolean flag = umsMemberService.deleteUmsMemberById(id);
        if (flag) {
            log.debug("删除成功");
            return "删除成功";
        }

        return "删除出错";
    }
}
