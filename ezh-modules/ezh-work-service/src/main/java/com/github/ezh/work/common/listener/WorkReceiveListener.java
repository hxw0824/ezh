package com.github.ezh.work.common.listener;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.uitls.RandomUtil;
import com.github.ezh.common.bean.config.EzhConfig;
import com.github.ezh.common.bean.config.QiniuPropertiesConfig;
import com.github.ezh.common.constant.MqQueueConstant;
import com.github.ezh.common.util.PushUtils;
import com.github.ezh.common.util.RedisUtils;
import com.github.ezh.common.util.TelUtil;
import com.github.ezh.work.model.dto.WorkObjectDto;
import com.github.ezh.work.model.entity.CWork;
import com.github.ezh.work.model.entity.User;
import com.github.ezh.work.model.entity.UserConfig;
import com.github.ezh.work.service.CWorkService;
import com.github.ezh.work.service.UserConfigService;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component

public class WorkReceiveListener {

    @Autowired
    private EzhConfig ezhConfig;

    @Autowired
    private CWorkService cWorkService;

    @Autowired
    private UserConfigService userConfigService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private QiniuPropertiesConfig qiniuPropertiesConfig;

    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
    public static SimpleDateFormat SDF_DORMAT = new SimpleDateFormat("yyyy-MM-dd" );

    /*@RabbitHandler
    @RabbitListener(queues = MqQueueConstant.WORK_QUEUE)
    public void receive(WorkObjectDto wod) {
        System.out.println("进入rabbit");
        System.out.println("手机号："+wod.getUser().getMobile());
        CWork cWork = wod.getcWork();
        User user = wod.getUser();
        String dateStr = SDF_DORMAT.format(cWork.getSignTime());

        Long workTime = cWork.getSignTime().getTime();
        String tempPath = user.getId()+ "_" + workTime.toString();
        String filePath = "/ezhdx/work/" + user.getOfficeId();
        if(base64ToUpload(cWork.getPicUrl(),tempPath,filePath)){
            cWork.setPicUrl(filePath + "/" + user.getId() + "/" + tempPath + ".jpg");
        }else{
            cWork.setPicUrl("");
        }

        boolean isSuccess = cWorkService.insert(cWork);
        if(isSuccess){
            String tempStr = cWork.getSignTemp() == 0 ? "（未测量体温）" : "（实时体温：" + cWork.getSignTemp().toString() + "℃） ";
            String content = user.getName() + "家长,您的孩子于 " + SDF.format(cWork.getSignTime()) + " 打卡" + tempStr;
            UserConfig userConfig = userConfigService.getByUserId(user.getId());
            if(userConfig != null && StringUtils.isNotBlank(userConfig.getClientId())){
                IPushResult res = PushUtils.pushToSingle(userConfig.getMobileType(),userConfig.getClientId(),ezhConfig.getAppName() + "（新考勤）",content,"icon.png","","");
                System.out.println(res.getResponse().toString());
            }

            TelUtil.sendWorkSMS(user.getMobile(), content);
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;

            delRedis(RedisUtils.SINGLE_WORK_DATE + user.getId() + "_" + dateStr,
                    RedisUtils.SINGLE_MONTH_WORK_DATE + user.getId() + "_year" + year + "_month" + month,
                    RedisUtils.SINGLE_TEMPERATURE_DATE + user.getId() + "_" + dateStr,
                    RedisUtils.SINGLE_MONTH_TEMPERATURE_DATE + user.getId() + "_year" + year + "_month" + month,
                    RedisUtils.CLASS_WORK_DATE + user.getClassId() + "_" + dateStr,
                    RedisUtils.CLASS_TEMPERATURE_DATE + user.getClassId() + "_" + dateStr);
        }
    }*/


    @RabbitHandler
    @RabbitListener(queues = MqQueueConstant.WORK_QUEUE)
    public void receive(WorkObjectDto wod) {
        System.out.println("进入rabbit");
        System.out.println("手机号："+wod.getUser().getMobile());
        CWork cWork = wod.getcWork();
        User user = wod.getUser();
        String dateStr = SDF_DORMAT.format(cWork.getSignTime());

        String key = user.getId() + "/" + RandomUtil.randomUUID() + ".jpg";
        if(base64ToUpload(cWork.getPicUrl(),key)){
            cWork.setPicUrl(qiniuPropertiesConfig.getQiniuHost() + "/" + key);
        }else{
            cWork.setPicUrl("");
        }

        boolean isSuccess = cWorkService.insert(cWork);
        if(isSuccess){
            String tempStr = cWork.getSignTemp() == 0 ? "（未测量体温）" : "（实时体温：" + cWork.getSignTemp().toString() + "℃） ";
            String content = user.getName() + "家长,您的孩子于 " + SDF.format(cWork.getSignTime()) + " 打卡" + tempStr;
            UserConfig userConfig = userConfigService.getByUserId(user.getId());
            if(userConfig != null && StringUtils.isNotBlank(userConfig.getClientId())){
                IPushResult res = PushUtils.pushToSingle(userConfig.getMobileType(),userConfig.getClientId(),ezhConfig.getAppName() + "（新考勤）",content,"icon.png","","");
                System.out.println(res.getResponse().toString());
            }

            TelUtil.sendWorkSMS(user.getMobile(), content);
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;

            delRedis(RedisUtils.SINGLE_WORK_DATE + user.getId() + "_" + dateStr,
                    RedisUtils.SINGLE_MONTH_WORK_DATE + user.getId() + "_year" + year + "_month" + month,
                    RedisUtils.SINGLE_TEMPERATURE_DATE + user.getId() + "_" + dateStr,
                    RedisUtils.SINGLE_MONTH_TEMPERATURE_DATE + user.getId() + "_year" + year + "_month" + month,
                    RedisUtils.CLASS_WORK_DATE + user.getClassId() + "_" + dateStr,
                    RedisUtils.CLASS_TEMPERATURE_DATE + user.getClassId() + "_" + dateStr);
        }
    }

    private boolean base64ToUpload(String base64,String key){
        base64 = base64.substring(base64.indexOf(",") + 1);
        BASE64Decoder decoder = new BASE64Decoder();
        String pathname = ezhConfig.getUploadPath();
        String imgFilePath = pathname + RandomUtil.randomUUID() + ".jpg";
        boolean isSuccess = false;
        try{
            byte[] b = decoder.decodeBuffer(base64);
            for(int i=0;i<b.length;++i){
                if(b[i]<0){//调整异常数据
                    b[i]+=256;
                }
            }

            File file = new File(pathname);
            if(!file.exists())file.mkdirs();
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone1());
            UploadManager uploadManager = new UploadManager(cfg);
            try {
                File tempFile = new File(imgFilePath);
                InputStream in = new FileInputStream(tempFile);
                Response response = uploadManager.put(in, key,
                        Auth.create(qiniuPropertiesConfig.getAccessKey(), qiniuPropertiesConfig.getSecretKey()).uploadToken(qiniuPropertiesConfig.getBucket()), null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                isSuccess = true;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            File tempFile = new File(imgFilePath);
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
        return isSuccess;
    }

    /*private boolean base64ToUpload(String base64,String tempPath,String filePath){
        base64 = base64.substring(base64.indexOf(",") + 1);
        BASE64Decoder decoder = new BASE64Decoder();
        String pathname = ezhConfig.getUploadPath();
        String imgFilePath = pathname + tempPath + ".jpg";
        boolean isSuccess = false;
        try{
            byte[] b = decoder.decodeBuffer(base64);
            for(int i=0;i<b.length;++i){
                if(b[i]<0){//调整异常数据
                    b[i]+=256;
                }
            }

            File file = new File(pathname);
            if(!file.exists())file.mkdirs();
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            JFTSClient jftsClient = new JFTSClient();
            isSuccess = jftsClient.uploadFile(imgFilePath,tempPath+".jpg", filePath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            File tempFile = new File(imgFilePath);
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
        return isSuccess;
    }*/

    private void delRedis(String... key) {
        for (String k : key) {
            redisTemplate.delete(k);
        }
    }
}
