package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categories",categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        //1、获取一把锁
        RLock lock = redissonClient.getLock("my-lock");
        //2、加锁
//        lock.lock();//阻塞式等待，默认加锁30s
        //锁自动续期
        //加锁业务完成，不会续期，不需要手动解锁

        lock.lock(30, TimeUnit.SECONDS);
        try {
            System.out.println("加锁成功，执行业务..."+Thread.currentThread().getId());
            Thread.sleep(20000);
        }catch (Exception e){

        }finally {
            System.out.println("释放锁..."+Thread.currentThread().getId());
            lock.unlock();
        }

        return "hello";
    }


    @GetMapping("/write")
    @ResponseBody
    public String writeValue(){
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = lock.writeLock();
        try{
            //1、改数据加写锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue",s);
        }catch (Exception e){

        }finally {
            rLock.unlock();
        }
        return s;
    }
    @GetMapping("/read")
    @ResponseBody
    public String readValue(){
        String s = "";
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = lock.readLock();
        rLock.lock();
        try{
            s = redisTemplate.opsForValue().get("writeValue");
        }catch (Exception e){

        }finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     * 放假锁门
     * countdown锁
     * 5个班全部走完，锁大门
     */
    @GetMapping("/lockdoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();//等待闭锁完成
        return "放假了";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();
        return id+"班的人走了";
    }


}
