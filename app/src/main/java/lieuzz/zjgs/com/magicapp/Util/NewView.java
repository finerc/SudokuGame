package lieuzz.zjgs.com.magicapp.Util;

/**
 * Created by Administrator on 2018/4/8.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

import lieuzz.zjgs.com.magicapp.R;
import lieuzz.zjgs.com.magicapp.activity.sudoku;

public class NewView extends View{

    public NewView(Context context) {
        super(context);
    }
    public NewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //方格长宽
    float width;
    float height;
    //选定的坐标
    int selectX;
    int selectY;
    int rank = 1;
    TextView title;

    public MyGame mGame=new MyGame(this.getRank(),getContext());

    //获得屏幕尺寸
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //每一个小格的长宽
        this.width=w/9f-0.3f;
        this.height=h/9f-0.3f;
    }


    //绘图函数
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画背景
        Paint bgPaint=new Paint();
        bgPaint.setColor(getResources().getColor(R.color.colorBackground));
        canvas.drawRect(0,0,getWidth(),getHeight(), bgPaint);
        //线条画笔
        Paint darkPaint =new Paint();
        darkPaint.setColor(getResources().getColor(R.color.colorDark));
        Paint hilitePaint =new Paint();
        hilitePaint.setColor(getResources().getColor(R.color.colorHilite));
        Paint lightPaint =new Paint();
        lightPaint.setColor(getResources().getColor(R.color.colorLight));
        //绘制线条
        for(int i=0;i<10;i++){
            canvas.drawLine(0, i*height,getWidth(),i*height,lightPaint);
            canvas.drawLine(0, i*height+1,getWidth(),i*height+1,hilitePaint);
            canvas.drawLine(i*width, 0,i*width,getWidth(),lightPaint);
            canvas.drawLine(i*width+1,0,i*width+1,getWidth(),hilitePaint);
            if(i%3==0){
                for(int k=0;k<6;k++)
                {
                    canvas.drawLine(0, (i) * height+k, getWidth(), (i) * height+k, darkPaint);
                    //canvas.drawLine(0, (i + 3) * height + 3, getWidth(), (i + 3) * height + 3, hilitePaint);
                    canvas.drawLine(i * width+k, 0, i * width+k, getWidth(), darkPaint);
                    //canvas.drawLine(i * width + 3, 0, i * width + 3, getWidth(), hilitePaint);
                }
            }
        }
        canvas.drawLine(0, getHeight(),getWidth(),getHeight(),darkPaint);
        //canvas.drawLine(0, (i+3)*height+3,getWidth(),(i+3)*height+3,hilitePaint);


        //绘制数字
        Paint numberPaint =new Paint();
        numberPaint.setColor(Color.BLACK);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(height*0.6f);
        numberPaint.setTextAlign(Align.CENTER);

        Paint selectNumberPaint = new Paint();
        selectNumberPaint.setColor(Color.rgb(255,97,0));
        selectNumberPaint.setStyle(Paint.Style.STROKE);
        selectNumberPaint.setTextSize(height*0.6f);
        selectNumberPaint.setTextAlign(Align.CENTER);

        Paint digNumberPaint = new Paint();
        digNumberPaint.setColor(Color.rgb(51,181,255));
        digNumberPaint.setStyle(Paint.Style.STROKE);
        digNumberPaint.setTextSize(height*0.6f);
        digNumberPaint.setTextAlign(Align.CENTER);

        Paint maskNumberPaint = new Paint();
        maskNumberPaint.setColor(Color.rgb(51,181,255));
        maskNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        maskNumberPaint.setTextSize(height*0.25f);
        maskNumberPaint.setTextAlign(Align.CENTER);

        //////////////////////////////////
        Paint bluePaint =new Paint();
        bluePaint.setColor(Color.rgb(255,97,0));

        Paint yelloPaint = new Paint();
        yelloPaint.setColor(Color.YELLOW);
        //////////////////////////////////

        //调节文字居中
        FontMetrics fMetrics=numberPaint.getFontMetrics();
        float x=width/2;
        float y=height/2-(fMetrics.ascent+fMetrics.descent)/2;
        FontMetrics mMetrics = maskNumberPaint.getFontMetrics();
        float _y = height/6-(mMetrics.ascent+mMetrics.descent)/2;
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<9;j++)
            {
                if(mGame.isMask(i+1,j+1)) {
                    for(int k=0;k<9;k++)
                    {
                        int mask = mGame.getMask(i+1,j+1,k+1);
                        String Smask;
                        if(mask==0)
                            Smask = "";
                        else
                            Smask = ""+ (k+1);
                        canvas.drawText(Smask, i * width + x*((k%3)*2+1)/3, _y*((k/3)+1) + j * height+5, maskNumberPaint);
                    }
                }
                else {
                    //////////////////////////////////////////
                    if (mGame.isDigged(i + 1, j + 1))
                        canvas.drawText(mGame.getNumber(i + 1, j + 1), i * width + x, y + j * height, digNumberPaint);
                    else
                        canvas.drawText(mGame.getNumber(i + 1, j + 1), i * width + x, y + j * height, numberPaint);
                    if (mGame.isSelected(i + 1, j + 1)) {
                        if (!mGame.isDigged(i + 1, j + 1))
                            canvas.drawText(mGame.getNumber(i + 1, j + 1), i * width + x, y + j * height, selectNumberPaint);
                        for (int k = 2; k <= 7; k++) {
                            canvas.drawLine(i * width + 2, j * height + k, (i + 1) * width - 2, j * height + k, bluePaint);
                            canvas.drawLine(i * width + k, j * height + 2, i * width + k, (j + 1) * height - 2, bluePaint);
                        }
                        for (int k = 6; k >= 1; k--) {
                            canvas.drawLine(i * width + 2, (j + 1) * height - k, (i + 1) * width - 2, (j + 1) * height - k, bluePaint);
                            canvas.drawLine((i + 1) * width - k, j * height + 2, (i + 1) * width - k, (j + 1) * height - 2, bluePaint);
                        }
                    }
                }
                /////////////////////////////////////
            }
        }

    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x=(int)(event.getX()/width);
        int y=(int)(event.getY()/height);
        if(x<=8 && y<=8){  //判断点击的是否是游戏界面
            ///////////////////////////////////////
            if(mGame.getMapNumber(x+1, y+1).equals("")){
                int []t=mGame.getUsed(x, y);
                selectX=x;
                selectY=y;
                MyDialog mDialog=new MyDialog(getContext(), t, this); //调用自定义Dialog
                mDialog.show();
            }
            else{
                //清除当前边框
                for(int i = 1; i<10; i++) {
                    mGame.setSelect(i, 0);
                }
                mGame.setSelect(mGame.numbers[x][y], 1);
                invalidate();  //重新绘制
            }
            /////////////////////////////////////
        }
        return super.onTouchEvent(event);
    }
    public void setTitle(int i){
        mGame.setTitle(i,selectX,selectY);
        invalidate();  //每次填写一个数 都要重新进行绘制
        if( mGame.youWin() == true)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setTitle("Tips")
                    .setMessage("You Win !")
                    .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ////////////////////////////////
                            Intent intent = new Intent(getContext(),sudoku.class);
                            intent.putExtra("rank", ""+getRank());
                            intent.putExtra("recLen",sudoku.txtView.getText());
                            getContext().startActivity(intent);
                            invalidate();
                            ((sudoku)getContext()).handler.removeCallbacks(((sudoku)getContext()).runnable);
                            //////////////////////////////////
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();

        }
    }

    public void setRank(int rank){
        this.rank = rank;
        Log.d("rankkkkkk",String.valueOf(this.rank));
        mGame = new MyGame(this.rank, getContext());
    }
    public int getRank(){
        return rank;
    }

}