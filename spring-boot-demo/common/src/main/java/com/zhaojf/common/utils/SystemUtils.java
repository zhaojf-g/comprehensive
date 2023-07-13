package com.zhaojf.common.utils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class SystemUtils {

    public static void main(String[] args) throws Exception {

        final String mac = getMaxMac();
        System.out.println(mac);

    }

    public static String getMaxMac() throws Exception {
        int minValue = Integer.MAX_VALUE;
        String macValue = "";
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> macList = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            NetworkInterface iface = enumeration.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                // 获取本地IP对象
                InetAddress ia = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ia);
                if (network == null) {
                    continue;
                }
                // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                int value = 0;
                for (byte m : mac) {
                    value += m & 0xFF;
                }
                if(value < minValue){
                    minValue = value;
                    macValue = parseMac(mac);
                }

            }
        }

        return macValue.toUpperCase(Locale.ROOT);
    }

    public static String parseMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (byte b : mac) {
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(b & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        return sb.toString();
    }

}
