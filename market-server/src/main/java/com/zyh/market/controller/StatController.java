package com.zyh.market.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.zyh.market.R;
import com.zyh.market.biz.StatBiz;
import com.zyh.market.constants.ClickCount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/stat")
@SaCheckRole(value = {"admin","system"}, mode = SaMode.OR)
@SaCheckLogin
public class StatController {
  @Autowired private StatBiz statBiz;
  @GetMapping("/countData")
  public R<Map> getCountData(){
    Map map = statBiz.getCountData();
    return R.ok(map);
  }
  
  @GetMapping("/userData")
  public R<List<Map>> getUserData() {
    List<Map> list  = statBiz.getUserData();
    return R.ok(list);
  }
  @GetMapping("/tableData")
  public R<List<Map>> getTableData() {
    List<Map> list = statBiz.getTableData();
    return R.ok(list);
  }
  
  @GetMapping("/videoData")
  public R<List<Map<String, Object>>> getVideoData() {
    List<Map<String, Object>> list = statBiz.getVideoData();
    return R.ok(list);
  }
  
  @GetMapping("/orderData")
  public R<Map<String, List<Map<String, Long>>>> getOrderData() {
    Map<String, List<Map<String, Long>>> list = statBiz.getOrderData();
    return R.ok(list);
  }
  @GetMapping("/clickCount")
  public int getClickCount() {
    return ClickCount.getClickCount();
  }
}
