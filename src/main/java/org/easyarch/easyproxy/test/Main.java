package org.easyarch.easyproxy.test;/**
 * Description : 
 * Created by YangZH on 16-7-27
 *  下午3:41
 */

import org.hyperic.sigar.*;

/**
 * Description :
 * Created by YangZH on 16-7-27
 * 下午3:41
 */

public class Main {

    public static void main(String[] args) throws Exception {
        net();
    }

    public static void memory() {
        Sigar sigar = new Sigar();
        try {
            Mem mem = sigar.getMem();
            // 内存总量
            System.out.println("内存总量:   " + mem.getTotal() / 1024L / 1024L / 1024L + "G av");
            // 当前内存使用量
            System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L / 1024L / 1024L + "G used");
            // 当前内存剩余量
            System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L / 1024L / 1024L + "G free");
            Swap swap = sigar.getSwap();
            // 交换区总量
            System.out.println("交换区总量:  " + swap.getTotal() / 1024L / 1024L / 1024L + "G av");
            // 当前交换区使用量
            System.out.println("当前交换区使用量:   " + swap.getUsed() / 1024L / 1024L / 1024L + "G used");
            // 当前交换区剩余量
            System.out.println("当前交换区剩余量:   " + swap.getFree() / 1024L / 1024L / 1024L + "G free");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cpu() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            CpuInfo info = infos[i];
            System.out.println("第" + (i + 1) + "块CPU信息");
            System.out.println("CPU的总量MHz:  " + info.getMhz());// CPU的总量MHz
            System.out.println("CPU生产商: " + info.getVendor());// 获得CPU的卖主，如：Intel
            System.out.println("CPU类别:  " + info.getModel());// 获得CPU的类别，如：Celeron
            System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
            printCpuPerc(cpuList[i]);
        }
    }

    private static void printCpuPerc(CpuPerc cpu) {
        System.out.println("CPU用户使用率:   " + CpuPerc.format(cpu.getUser()));// 用户使用率
        System.out.println("CPU系统使用率:   " + CpuPerc.format(cpu.getSys()));// 系统使用率
        System.out.println("CPU当前等待率:   " + CpuPerc.format(cpu.getWait()));// 当前等待率
        System.out.println("CPU当前错误率:   " + CpuPerc.format(cpu.getNice()));//
        System.out.println("CPU当前空闲率:   " + CpuPerc.format(cpu.getIdle()));// 当前空闲率
        System.out.println("CPU总的使用率:   " + CpuPerc.format(cpu.getCombined()));// 总的使用率
    }

    private static void net() throws Exception {
        Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println("网络设备名:  " + name);// 网络设备名
            System.out.println("IP地址:   " + ifconfig.getAddress());// IP地址
            System.out.println("子网掩码:   " + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }

            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            System.out.println(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            System.out.println(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            System.out.println(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            System.out.println(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            System.out.println(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            System.out.println(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            System.out.println(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            System.out.println(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
        }
    }
}
