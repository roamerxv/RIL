package com.alcor.ril.web.controller.bean;

import lombok.Data;

/**
 * 服务器信息类。用于前台显示服务器信息的 bean。由 controller 转化成 json
 *
 * @author roamer - 徐泽宇
 * @create 2017-11-2017/11/30  上午9:50
 */
@Data
public class ServerInfo {
    String serverIP;
    String sessionID;
    int localPort;
    String contextPath;
}
