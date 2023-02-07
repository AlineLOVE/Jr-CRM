package com.corp.jr.controller;

import com.corp.jr.model.generator.CoreUser;
import com.corp.jr.service.CoreUserService;
import com.corp.jr.util.ResultUtil;
import com.corp.jr.util.page.ListPageUtil;
import com.corp.jr.web.JsonResult;
import com.corp.jr.web.query.UserQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mycomputer on 2021-11-02.
 * 用户的增删改查
 */
@RestController
@RequestMapping("/coreuser")
public class CoreUserController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    CoreUserService coreUserService;

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getuser")
    public Map getUser(int id) {
        log.info("获取用户信息");
        Map map = new HashMap();
        map.put("code", "0");
        CoreUser user = coreUserService.selectByPrimaryKey(id);
        map.put("user", user);
        return map;
    }

    /**
     * 获取用户信息列表
     *
     * @param condition
     * @return
     */
    @PostMapping("/getusers")
    public JsonResult<List<CoreUser>> queryUsersMessage(@RequestBody UserQuery condition) {
        log.info("获取用户列表");
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize(),true);//注意：只有紧跟着PageHelper.startPage()的sql语句才被pagehelper起作用
        List pageResult = coreUserService.queryUsersMessage(condition);

    /*    ListPageUtil listPageUtil = new ListPageUtil(pageResult);
       int pageNum = condition.getPageNum();//页码
        if(listPageUtil.getPageCount()<pageNum){
            return JsonResult.failMessage("超出最大页码!最大页码为："+listPageUtil.getPageCount());
        }
        listPageUtil.setIndex(pageNum);
        List<CoreUser> list = listPageUtil.nextPage();//设置好pageNum后，获取第pageNum页
        return JsonResult.success(list, pageResult.size());*/

        PageInfo pageInfo = new PageInfo(pageResult);
        return JsonResult.success(pageInfo.getList(), (int) pageInfo.getTotal());
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/deleteusers")
    public Map deleteUsers(@RequestBody List<Integer> ids) {
        Map map = new HashMap();
        map.put("code", "0");
        map = coreUserService.deleteResult(ids);
        return map;
    }

    /**
     * 新增用户信息
     *
     * @param
     * @return
     */
    @PostMapping("/adduser")
    public Map addUserMessage(@RequestBody CoreUser coreUser) {
        boolean cu = coreUserService.existUser(coreUser.getCode());//查询是否已存在，存在：true 不存在：false
        if (cu) {
            return ResultUtil.resultFailMessage("用户已存在！", "");
        }
        int id = coreUserService.addUserMessage(coreUser);
        if (id != 1) {
            return ResultUtil.resultFailMessage("新增失败，请联系管理员！", "");
        }
        CoreUser user = coreUserService.selectByPrimaryKey(coreUser.getId());
        return ResultUtil.resultSuccessMess("新增成功", user);
    }

    /**
     * 修改用户信息
     *
     * @param
     * @return
     */
    @PostMapping("/updateuser")
    public Map updateUserMessage(@RequestBody CoreUser coreUser) {
        Map map = new HashMap();
        map.put("code", "0");
        int id = coreUserService.updateUserMessage(coreUser);
        if (id != 1) {
            map.put("code", "-1");
            return map;
        }
        CoreUser user = coreUserService.selectByPrimaryKey(coreUser.getId());
        map.put("user", user);
        return map;
    }

}
