package com.silencedut.dogearedtextviewsimple;

/**
 * @author SilenceDut
 * @date 2018/9/18
 *
 * "content":"只从配置拿内容，通过xml定义的配置内容",
 * "uiConfig":"true",
 * "backgroundColor":"#FF7F50",
 * "foldColor":"#FF4500",
 * "angle":"30",
 * "triangleBottomLength":"30"
 */



public class ConfigBean {
    public String content = "";
    /**
     * uiConfig 为true时，动态配置折角View
     */
    public boolean uiConfig;
    public String backgroundColor;
    public String foldColor;
    public float angle ;
    public int triangleBottomLength;
    public int frameStokeWidth;
    public int maxLines;
}
