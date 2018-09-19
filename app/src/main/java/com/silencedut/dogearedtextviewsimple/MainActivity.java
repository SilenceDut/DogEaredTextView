package com.silencedut.dogearedtextviewsimple;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.silencedut.dogearedtextview.DogEaredView;
import com.silencedut.taskscheduler.Task;
import com.silencedut.taskscheduler.TaskScheduler;

/**
 * @author SilenceDut
 * @date 2018/9/18
 */
public class MainActivity extends AppCompatActivity {

    private static final String CONFIG_SIMPLE = "configsimple";
    private static final String CONFIG_COMPLEX = "configcomplex";
    private static final String CONFIG_HTML = "confightml";
    private AssetManager mAssetManager ;
    private DogEaredView mDogEaredView;
    private LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAssetManager = getAssets();
        mDogEaredView = findViewById(R.id.sample);
        mContentView = findViewById(R.id.contentView);
        findViewById(R.id.tv_getConfig).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchConfig();
            }
        });
    }

    private void fetchConfig() {


        TaskScheduler.execute(new Task<ConfigBean[]>() {
            @Override
            public ConfigBean[] doInBackground() {
                ConfigBean[] configBeans = new ConfigBean[3];
                configBeans[0] = JSON.parseObject(FetchConfigHelper.getString(CONFIG_SIMPLE,mAssetManager),ConfigBean.class);
                configBeans[1] = JSON.parseObject(FetchConfigHelper.getString(CONFIG_COMPLEX,mAssetManager),ConfigBean.class);
                configBeans[2] = JSON.parseObject(FetchConfigHelper.getString(CONFIG_HTML,mAssetManager),ConfigBean.class);
                return configBeans;
            }

            @Override
            public void onSuccess(ConfigBean[] result) {
                for(ConfigBean configBean : result) {
                    if(configBean!=null) {
                        if(!configBean.uiConfig) {
                            mDogEaredView.setText(configBean.content);
                        }else {
                            createAndAddDogEaredView(configBean);
                        }
                    }
                }

            }
        });

    }

    private void createAndAddDogEaredView(ConfigBean configBean) {
        DogEaredView dogEaredView = new DogEaredView(this);
        dogEaredView.setBackground(Color.parseColor(configBean.backgroundColor))
                .setTriangleBottomLength(configBean.triangleBottomLength)
                .setAngelRad(configBean.angle)
                .setFoodTriangleColor(Color.parseColor(configBean.foldColor))
                .setFrameStokeWidth(configBean.frameStokeWidth)
                .setMaximalLines(configBean.maxLines)
                .setContent(configBean.content);

        dogEaredView.setPadding(50,50,50,50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 10;
        mContentView.addView(dogEaredView,layoutParams);

    }



}
