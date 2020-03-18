package cn.wqz.java.thread.schedule.timer;

import java.util.TimerTask;


/**
 * 业务类
 */
public class MyTimeTask extends TimerTask {

    // 执行次数
    private int n;

    // 任务名称
    private String name;

    public MyTimeTask(String name, int n){
        this.name = name;
        this.n = n;
    }

    @Override
    public void run() {
        if(n-- > 0){
            System.out.println("name = " + this.name);
        }else{
            // 此种注销方式不好， 最后一次执行完毕后还需再等一个周期，并判断次数决定注销
            cancel(); // 注销当前任务
            System.out.println("task " + this.name + " canceled!");

        }
        // 推荐do{}while();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
