package cn.wqz.algorithms.jvm.params;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

/**
 * java代码获取JVM及本机参数
 */
public class PrintParam {
    public static void main(String[] args) {

        // 查看JVM内存
        System.out.println("\\r\\n========== JVM 内存 ==========");
        long totalMemory = Runtime.getRuntime().totalMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("Xms: " + totalMemory/1024/1024 + " M");
        System.out.println("Xmx: " + maxMemory/1024/1024 + " M");


        // 获取操作系统参数
        System.out.println("\\r\\n========== 系统参数 ============");
        System.out.println(System.getProperties());

        // 获取环境变量
        System.out.println("\\r\\n========== 环境变量 ============");
        System.out.println(System.getenv());

        // 获取系统内存信息
        System.out.println("\\r\\n========== 系统内存 ============");
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("system total memory: " + operatingSystemMXBean.getTotalPhysicalMemorySize()/1024/1024 + " M");
        System.out.println("system free memory: " + operatingSystemMXBean.getFreePhysicalMemorySize()/1024/1024 + " M");
    }
}
