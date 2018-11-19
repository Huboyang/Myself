package com.hby.myselfproject.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ctstudio.common.exception.BusinessException;
import com.hby.myselfproject.SAP.SAPDAO;
import com.hby.myselfproject.SAP.SAPJSONCaller;
import com.hby.myselfproject.config.RedisConfig;
import com.hby.myselfproject.entity.SapUser;
import com.hby.myselfproject.entity.SapDict;
import com.hby.myselfproject.service.ISapUserService;
import com.hby.myselfproject.service.SapWerksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("zzjk")
@EnableScheduling
public class ZujkController {

    private static Logger LOG = LoggerFactory.getLogger(ZujkController.class);

    public static final String REDIS_CZ = "SUCCESS";

    @Autowired
    private SAPDAO sapdao;

    @Autowired
    private ISapUserService sapUserService;

    @Autowired
    private SapWerksService sapWerksService;

    @Autowired
    private RedisConfig redisConfig;


    //执行
    @GetMapping("execute")
    public String execute(){
        ddrw();
        return "执行完毕";
    }

    //执行
    @GetMapping("execute2")
    public String execute2(){
        ddrw2();
        return  "执行完毕";
    }
    //获取日志
    @GetMapping("getErrorLog/{date}")
    public JSONArray getErrorLog(@PathVariable String date){
        String errorLog = redisConfig.getJedis().get("ERROR:"+date);
        JSONArray objects = JSONArray.parseArray(errorLog);
        return objects;
    }

    //测试联通性能
    @GetMapping("test")
    public String test(){
        return "hello world";
    }

    //删除redis key
    @GetMapping("deleteId/{id}")
    public String deleteId(@PathVariable String id){
        Jedis jedis = redisConfig.getJedis();
        jedis.del("ZZJK:"+id);
        return "删除成功";
    }


    @Scheduled(cron="0 0 0 * * ?")
    @GetMapping("gxRedis")
    public void gxRedis() {
        EntityWrapper<SapUser> wrapper = new EntityWrapper<>();
        List<SapUser> sapUsers = sapUserService.selectList(wrapper);
        Jedis jedis = redisConfig.getJedis();
        jedis.flushDB();
        sapUsers.forEach( a ->{
            jedis.set("ZZJK:"+a.getId(),REDIS_CZ);
        });
    }

    @Scheduled(cron="0 0 1 * * *")
    public void ddrw(){
        SAPJSONCaller caller = null;
        Jedis jedis = redisConfig.getJedis();
        try {
            caller = sapdao.jsonCaller().setFunction("ZHRFM_DONATION_SEND").subscribe();
            JSONArray users = caller.getTable("IT_USER");
            JSONArray ja = new JSONArray();
            for (int i = 0; i< users.size();i++){
                JSONObject jsonObject = users.getJSONObject(i);
                SapUser su = JSONObject.toJavaObject(jsonObject, SapUser.class);
                su.setPosition((String) jsonObject.get("ZPOSITION"));
                if (!REDIS_CZ.equals(jedis.get("ZZJK:"+su.getId()))){
                    boolean b = sapUserService.insert(su);
                    if (b){
                        jedis.set("ZZJK:"+su.getId(),REDIS_CZ);
                    } else {
                        ja.add("员工编号" + su.getId() + "未存储成功");
                    }
                } else {
                    boolean b = sapUserService.updateById(su);
                    if (!b){
                        ja.add("员工编号" + su.getId() + "未修改成功");
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (ja.size() > 0){
                jedis.set("ERROR:"+sdf.format(new Date()),ja.toString());
            }
        } catch (BusinessException e) {
            LOG.error("发生错误:",e);
            e.printStackTrace();
        }
    }

    @Scheduled(cron="0 0 1 * * *")
    public void ddrw2(){
        SAPJSONCaller caller = null;
        Jedis jedis = redisConfig.getJedis();
        try {
            caller = sapdao.jsonCaller().setFunction("ZHRFM_DONATION_SEND").subscribe();
            JSONArray users = caller.getTable("IT_WERKS");
            JSONArray ja = new JSONArray();
            SapDict sw = null;
            for (int i = 0; i< users.size();i++){
                JSONObject jsonObject = users.getJSONObject(i);
                 sw = new SapDict();
                 sw.setId(jsonObject.get("WERKS").toString());
                 sw.setName(jsonObject.get("PBTXT").toString());
                if (!REDIS_CZ.equals(jedis.get("ZZJK:"+sw.getId()))){
                    boolean b = sapWerksService.insert(sw);
                    if (b){
                        jedis.set("ZZJK:"+sw.getId(),REDIS_CZ);
                    } else {
                        ja.add( sw.getId() + "未存储成功");
                    }
                } else {
                    boolean b = sapWerksService.updateById(sw);
                    if (!b){
                        ja.add(sw.getId() + "未修改成功");
                    }
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (ja.size() > 0){
                jedis.set("ERROR:"+sdf.format(new Date()),ja.toString());
            }
        } catch (BusinessException e) {
            LOG.error("发生错误:",e);
            e.printStackTrace();
        }
    }

}
