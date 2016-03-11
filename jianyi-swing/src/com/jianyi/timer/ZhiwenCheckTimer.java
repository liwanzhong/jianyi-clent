package com.jianyi.timer;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * 银联wap版支付后3分钟调用查询并同步定时器
 * Created by liwanzhong on 2015/2/3.
 */
public class ZhiwenCheckTimer {

    private static final Logger log = Logger.getLogger(ZhiwenCheckTimer.class);



    /**
     * 对于前台交易，一般请求下以接收后台通知为主，若未收到后台通知(如3分钟后)，则可间隔（2的n次方秒）发起交易查询；
     * @param payCode 支付号
     * @throws Exception
     */
    public void synTradeQuery(final String payCode,String statusCode )throws Exception{
        if(true){//5,10,30,60,120 分钟执行一次查询同步
            Integer [] execTimeArray=new Integer[]{5,10,30,60,120};
            int i=0;
            do {
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                ScheduledFuture<Boolean> schedule = scheduledExecutorService.schedule(new QueryCall(payCode), execTimeArray[i], TimeUnit.MINUTES);
                boolean execSuccess=schedule.get();
                if(execSuccess||i>=execTimeArray.length-1){
                    scheduledExecutorService.shutdown();
                    break;
                }
                i++;
                scheduledExecutorService.shutdown();
            }while (true);
        }else{//其他需要查询接口的情况处理(2的n次 秒执行)
            int maxExcute=10;//最大执行查询数
            int n=1;//n次方
            int j=2;//底数
            do {
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                boolean execSuccess=false;
                if(n==1){//第一次过三分钟后执行
                    ScheduledFuture<Boolean>  schedule = scheduledExecutorService.schedule(new QueryCall(payCode), 60*3, TimeUnit.SECONDS);
                    execSuccess=schedule.get();
                }else{
                    ScheduledFuture<Boolean>   schedule = scheduledExecutorService.schedule(new QueryCall(payCode), (int)Math.pow(j,n), TimeUnit.SECONDS);
                    execSuccess=schedule.get();
                }
                if(execSuccess||n>=maxExcute){
                    scheduledExecutorService.shutdown();
                    break;
                }
                n++;
                scheduledExecutorService.shutdown();
            }while (true);
        }

    }

    private class QueryCall implements Callable<Boolean>{

        private String payCode;

        QueryCall(String payCode){
            this.payCode=payCode;
        }

        @Override
        public Boolean call() throws Exception {
            log.info("-----------------------------银联wap交易同步交易结果-------start-----------------------------------------");

            log.info("-----------------------------银联wap交易同步交易结果-------end-----------------------------------------");
            return false;
        }
    }
}
