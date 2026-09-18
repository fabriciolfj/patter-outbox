package com.github.fabriciolfj.outbox.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class GetHostnameUtil {

    private GetHostnameUtil() {

    }

    public static String getInstanceName() {
        try {
            return InetAddress.getLocalHost().getHostName() + "-" + UUID.randomUUID();
        } catch (UnknownHostException e) {
            return "outbox-" + UUID.randomUUID();
        }
    }
}
