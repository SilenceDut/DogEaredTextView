package com.silencedut.dogearedtextviewsimple;

import android.content.res.AssetManager;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author SilenceDut
 * @date 2018/9/18
 */
public class FetchConfigHelper {

    /**
     * 从 assets 读取文件转成字符串,异步线程里处理
     */
    @WorkerThread
    public static String getString(String fileName,AssetManager assetManager ) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
