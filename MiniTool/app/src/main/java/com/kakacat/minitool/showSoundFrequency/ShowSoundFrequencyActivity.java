package com.kakacat.minitool.showSoundFrequency;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kakacat.minitool.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ShowSoundFrequencyActivity extends AppCompatActivity {

/*    采样率一般有5个等级 : 11025,22050,24000,44100,48000
    */

    private LineChartView lineChartView;

    private int REQUEST_RECORD_AUDIO = 1;

    LineChartData data;

    private String[] x;
    private float[] y;
    List<PointValue> mPointValues;
    List<AxisValue> mAxisXValues;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){

                lineChartView.setLineChartData(data);
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sound_frequency);


        requestPermission();

        lineChartView = findViewById(R.id.line_char_view);
        mPointValues = new ArrayList<>();
        mAxisXValues = new ArrayList<>();
        x = new String[24];
        y = new float[24];

        fillData();

        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        setLineAttr(line,lines);

        data = new LineChartData();

        data.setLines(lines);


        Axis axisX = new Axis(); //X轴
        Axis axisY = new Axis();  //Y轴
        initAxis(axisX,axisY);
        data.setAxisXBottom(axisX); //x 轴在底部  （顶部底部一旦设置就意味着x轴）
        data.setAxisYLeft(axisY);  //Y轴设置在左边（左面右面一旦设定就意味着y轴）

        lineChartView.setZoomEnabled(true);
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float) 5);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        //设置触摸事件

        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        // 下面的这个api控制 滑动
        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;
        v.right= x.length;

        lineChartView.setCurrentViewport(v);

    }

    private void initAxis(Axis axisX,Axis axisY) {
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(x.length); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        axisX.setHasLines(false); //x 轴分割线  每个x轴上 面有个虚线 与x轴垂直

        axisY.setName("Hz");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.RED);
    }

    private void setLineAttr(Line line,List<Line> lines) {
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        String permission = Manifest.permission.RECORD_AUDIO;
        if(ActivityCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{permission},REQUEST_RECORD_AUDIO);
        }else{
            startRecord();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_RECORD_AUDIO){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startRecord();
            }else{
                finish();
            }
        }

    }

    private void startRecord(){
        new Thread(()->{
            int audioSource = MediaRecorder.AudioSource.MIC;
            int sampleRate = 44100;
            int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            int bufSize = AudioRecord.getMinBufferSize(sampleRate,channelConfig,audioFormat);
            AudioRecord audioRecord = new AudioRecord(audioSource,sampleRate,channelConfig,audioFormat,bufSize);
            byte[] buffer = new byte[bufSize];
            int len;

            float currentFrequency;
            FFT fft = new FFT();
            audioRecord.startRecording();

            int index = 0;
            while((len = audioRecord.read(buffer,0,bufSize)) > 0){
                currentFrequency = fft.getFrequency(buffer, sampleRate, bufSize);
                y[index++] = currentFrequency;
                if(index % 23 == 0) {
                    index = 0;
                    for(int i = 0; i < 24; i++){
                        PointValue pointValue =mPointValues.get(i);
                        pointValue.set(pointValue.getX(),y[i]);
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }


    private void fillData(){
        for(int i = 1; i <= 24; i++){
            x[i - 1] = String.valueOf(i);
            y[i - 1] = i * 1000;
        }

        for (int i = 0; i < x.length; i++) {
            //为每个x轴的标注填充数据
            mAxisXValues.add(new AxisValue(i).setLabel(x[i]));
        }

        for (int i = 0; i < y.length; i++) {
            // 构造函数传参   x标注  及对应的值
            PointValue pointValue = new PointValue(i,y[i]);
            mPointValues.add(new PointValue(i, y[i]));
        }
    }

}
