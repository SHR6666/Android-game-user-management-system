package com.example.tetris;

import android.content.Context;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //=======================第一段准备，制作主页准备阶段==================================================
    //游戏区域宽度
    private int xWidth, xHeight;

    //游戏区控件
    View view;
    // ====================第二段准备,写地图和方块的设置与布局======================================================
    // 地图画笔
    Paint mapPaint;
    //辅助线画笔
    Paint linePaint;
    //方块画笔
    Paint boxPaint;

    //地图,用二维数组表示
    boolean[][] maps;
    //方块
    Point[] boxs;
    //方块类型
    int boxType;
    //方块的大小
    int boxSize;
    //自动下落线程
    Thread downThread;
    //自动下落线程的控制变量
    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //更新界面,刷新重绘View
            view.invalidate();
        }
    };

    //暂停状态
    boolean isPause;
    //游戏结束
    boolean isOver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initData();    //初始化数据
        initView();    //初始化视图
        initListener();    //初始化监听器
        //新的方块
        newBoxs();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //=====================================第一段准备，制作主页准备阶段==================================================
        //获取游戏区域宽度
        int width = getScreenwidth(this);
        //设置游戏区域宽度 = 屏幕宽度*2/3
        xWidth = width * 2 / 3;
        //游戏区域高度 = 宽度*2
        xHeight = xWidth * 2;
        //=====================================第二段准备,写地图和方块的设置与布局======================================================
        //初始化地图
        maps = new boolean[10][20];
        //初始化方块
        boxs = new Point[]{new Point(4, 1), new Point(3, 0), new Point(3, 1), new Point(5, 1)};
        //设置方块的大小,单位是像素，游戏宽度区域的1/10
        boxSize = xWidth / maps.length;

    }


    /**
     * 初始化视图
     */
    private void initView() {
        //初始化辅助线画笔
        mapPaint = new Paint();
        mapPaint.setColor(0x50000000);
        //一般把搞锯齿打开
        mapPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(0xFF666666);
        //一般把搞锯齿打开
        linePaint.setAntiAlias(true);

        //初始化方块画笔
        boxPaint = new Paint();
        boxPaint.setColor(0xFF00FF00);
        //一般把搞锯齿打开
        boxPaint.setAntiAlias(true);

        //1.得到父容器
        FrameLayout layoutGame = (FrameLayout) findViewById(R.id.layoutGame);
        //2.实例化游戏区域
        view = new View(this) {
            //重写游戏区域的绘制方法
            @Override
            protected void onDraw(android.graphics.Canvas canvas) {
                super.onDraw(canvas);
                //方块的绘制
                for (int i = 0; i < boxs.length; i++) {
//                    canvas.drawRect(left. top,right, bottom, paint);  //绘制矩形
                    canvas.drawRect(
                            boxs[i].x * boxSize,
                            boxs[i].y * boxSize,
                            boxs[i].x * boxSize + boxSize,
                            boxs[i].y * boxSize + boxSize, boxPaint);
                }
                //地图辅助线的绘制
                for (int x = 0; x < maps.length; x++) {

                    canvas.drawLine(x * boxSize, 0, x * boxSize, view.getHeight(), linePaint);
                }
                for (int y = 0; y < maps[0].length; y++) {   //起始坐标是0，0，结束坐标是游戏区域的宽度，游戏区域的高度即view.getWidth()
                    canvas.drawLine(0, y * boxSize, view.getWidth(), y * boxSize, linePaint);
                }

                //绘制地图
                for (int x = 0; x < maps.length; x++) {
                    for (int y = 0; y < maps[x].length; y++) {
                        if (maps[x][y] == true) {
                            //绘制方块
                            canvas.drawRect(x * boxSize, y * boxSize, (x + 1) * boxSize, (y + 1) * boxSize, mapPaint);
                        }
                    }
                }
            }
        };

        //3.设置游戏区域大小
        view.setLayoutParams(new LinearLayout.LayoutParams(xWidth, xHeight));
        //设置背景颜色
        view.setBackgroundColor(Color.LTGRAY);
        //4.添加游戏区域到父容器
        layoutGame.addView(view);

    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenwidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 初始化监听器
     */
    public void initListener() {
        //设置按钮监听器
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnTop).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.Bottom).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
    }

    /**
     * 捕捉按钮点击事件
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                //方块左移
                if (isPause) {
                    return;
                }
                move(-1, 0);
                break;
            case R.id.btnTop:
                //方块上移
                if (isPause) {
                    return;
                }
                rotate();
                break;
            case R.id.btnRight:
                //方块右移
                if (isPause) {
                    return;
                }
                move(1, 0);
                break;
            case R.id.Bottom:
                //方块下移
                //方块下移,要快速下移
                if (isPause) //如果暂停了，就不能下移了
                    return;
                //如果下落成功就继续下落，如果下落不成功，则结束循环
                while (true) {
                    if (!moveBottom())
                        break;
                }
                break;
            case R.id.btnStart:
                //开始游戏
                startGame();
                break;
            case R.id.btnPause:
                //暂停游戏
                pauseGame();
                //暂停游戏，则更改按钮的文字，如果开始游戏，则更改变成暂停
                if (isPause) {
                    ((Button) v).setText("暂停");
                } else {
                    ((Button) v).setText("开始");
                }
                break;
        }
        view.invalidate(); //要刷新视图
    }


    /**
     * 移动
     */
    public boolean move(int x, int y) {
        for (int i = 0; i < boxs.length; i++) {
            //把方块预定移动的点 传入边界检测
            if (checkBorder(boxs[i].x + x, boxs[i].y + y)) {
                return false;
            }
        }
        //遍历方块数组,每一块都要加上偏移量
        for (int i = 0; i < boxs.length; i++) {
            boxs[i].x += x;
            boxs[i].y += y;
        }
        return true;
    }

    /**
     * 旋转
     */
    public boolean rotate() {
        //田字形不可旋转
        if (boxType == 0)
            return false;
        for (int i = 0; i < boxs.length; i++) {
            //旋转算法(迪卡尔坐标系)  原点在方块中心，顺时针旋转90度
            int checkX = -boxs[i].y + boxs[0].y + boxs[0].x;
            int checkY = boxs[i].x - boxs[0].x + boxs[0].y;
            boxs[i].x = checkX;
            boxs[i].y = checkY;
            //将旋转的方块放入边界检测
            if (checkBorder(boxs[i].x, boxs[i].y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 边界检测
     * 传入x, y去判断是否在边界外
     * true出界，false未出界
     */
    public boolean checkBorder(int x, int y) {
        return (x < 0 || x >= maps.length || y < 0 || y >= maps[0].length || maps[x][y]);
    }

    /**
     * 新的方块
     */
    public void newBoxs() {
        //随机生成方块类型
        Random random = new Random();
        boxType = random.nextInt(7);
        //创建方块
        switch(boxType) {
            //田字形
            case 0:
                boxs = new Point[]{
                        new Point(4, 0),
                        new Point(5, 0),
                        new Point(4, 1),
                        new Point(5, 1)
                };
                break;
            //L形
            case 1:
                boxs = new Point[]{
                        new Point(4, 1),
                        new Point(5, 0),
                        new Point(3, 1),
                        new Point(5, 1)};
                break;
            //反L形
            case 2 :
                boxs = new Point[]{
                        new Point(4, 1),
                        new Point(3, 0),
                        new Point(3, 1),
                        new Point(5, 1)};
                break;
            //S形
            case 3 :
                boxs = new Point[]{
                        new Point(4, 0),
                        new Point(5, 0),
                        new Point(5, 1),
                        new Point(6, 1)};
                break;
            //反S形
            case 4 :
                boxs = new Point[]{
                        new Point(4, 1),
                        new Point(3, 1),
                        new Point(3, 0),
                        new Point(4, 0)};
                break;
            //T字形
            case 5 :
                boxs = new Point[]{
                        new Point(4, 0),
                        new Point(3, 0),
                        new Point(5, 0),
                        new Point(4, 1)};
                break;
            //长条形
            case 6 :
                boxs = new Point[]{
                        new Point(4, 0),
                        new Point(3, 0),
                        new Point(5, 0),
                        new Point(6, 0)};
                break;
        }

    }
    /**
     * 下落
     */
    public boolean moveBottom(){
        //1.移动成功，不做处理
        if (move(0, 1))
            return true;
        //堆积处理
        for(int i = 0; i < boxs.length; i++)
            maps[boxs[i].x][boxs[i].y] = true;
        //3.1 消行处理
        cleanLine();
        //3.生成新的方块
        newBoxs();
        //4.游戏结束判断
        isOver = checkOver();
        return false;
    }

    /**
     * 开始游戏
     */
    public void startGame() {
        //初始化游戏
        if(downThread == null){
            downThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try { //休眠500毫秒的时间
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断游戏是否处于结束状态
                        //判断是否属于暂停状态
                        if (isPause || isOver)
                            //继续循环,即执行continue上面的循环而不执行continue下面的
                            continue;
                        //执行一次下落
                        moveBottom();
                        //通知主线程刷新视图View
                        handler.sendEmptyMessage(0);
                    }
                }
            };
            downThread.start();
        }
        //清除地图
        for (int y = 0; y < maps[0].length; y++) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = false;
            }
        }
        //生成新方块
        newBoxs();
    }

    /**
     * 设置暂停状态
     */
    private void pauseGame() {
        if(isPause)
            isPause = false;
        else
            isPause = true;

    }

    /**
     * 游戏判断
     */
    public boolean checkOver(){
        //遍历每一行
        for(int i = 0; i < boxs.length; i++){
            if(maps[boxs[i].x][boxs[i].y]){
                return true;
            }
        }
        return false;
    }

    /**
     * 消行处理
     */
    public void cleanLine(){
        for (int y = maps[0].length-1; y > 0; y--) {
            //消行判断
            if(checkLine(y)) {
                //执行消行
                deleteLine(y);
                //消行后，需要将y+1行的数据向上移动一行
                y++;
            }else{
                //不消行，继续循环
                continue;
            }
        }
    }
    /**
     * 消行判断
     */
    public boolean checkLine(int y){
        for (int x = 0; x < maps.length; x++) {
            //如果有一个不为true则不能消除
            if(!maps[x][y])
                return false;
        }
        //如果每个maps都为true，则说明该行已经满了，则进行消行处理
        return true;
    }

    /**
     * 执行消行
     */
    public void deleteLine(int dy){
        for (int y = maps[0].length-1; y > 0; y--) {
            for (int x = 0; x < maps.length; x++) {
                maps[x][y] = maps[x][y - 1];  //即x轴不变，y轴-1
            }
        }
    }
}