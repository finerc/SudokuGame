package lieuzz.zjgs.com.magicapp.Util;

/**
 * Created by Administrator on 2018/4/8.
 */
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


public class MyGame {

    Context context;
    private int rank = 1;
    /*String data1="200680507" +
            "900000012" +
            "748000060" +
            "387490000" +
            "451872639" +
            "029351478" +
            "194200300" +
            "870534196" +
            "506000024";
    String data2="080060000" +
            "953412678" +
            "160809504" +
            "010000062" +
            "849600000" +
            "326170000" +
            "001000009" +
            "070950000" +
            "600000801";
    String data3="800006905" +
            "925108000" +
            "001000003" +
            "002901387" +
            "370402000" +
            "006700000" +
            "000000654" +
            "684370200" +
            "000000008";
    String data4="000509710" +
            "800000006" +
            "500308900" +
            "014207000" +
            "000000081" +
            "000006070" +
            "000152030" +
            "000800000" +
            "009000420";
    String data5="021087005" +
            "067000000" +
            "530420076" +
            "200000004" +
            "000010008" +
            "010006200" +
            "800740000" +
            "000000600" +
            "000003700";*/
    int numbers[][] =new int[9][9];
    SudokuGenerater mGenerater;
    /////////////////////////////
    int map[][] = new int[9][9];
    int select[] = new int[10];
    int mask[][][] = new int[9][9][9];
    int isMask[][] = new int[9][9];

    int[][] dig = new int[9][9];
    //////////////////////////////////

    public MyGame(int rank, Context _context){
        //初始化data
        context = _context;
        mGenerater = new SudokuGenerater(rank, context);
        mGenerater.DiggingHoles();
        numbers = mGenerater.PuzzleBoard;
        dig = mGenerater.DigBoard;
        for(int i=0;i<9;i++) {
            for (int j = 0; j < 9; j++) {
                map[i][j] = numbers[i][j];
            }
            select[i] = 0;
        }
    }

    //得到值
    public String getMapNumber(int x,int y){
        if(map[x-1][y-1]==0)
            return "";
        else
            return ""+map[x-1][y-1];
    }
    //得到值
    public String getNumber(int x,int y){
        if(numbers[x-1][y-1]==0)
            return "";
        else
            return ""+numbers[x-1][y-1];
    }

    public int getMask(int x,int y,int k)
    {
        return mask[x-1][y-1][k-1];
    }


    //是否被选中
    public boolean isSelected(int x,int y){
        if(select[numbers[x-1][y-1]]==0)
            return false;
        else
            return true;
    }

    //该位置是否待填数字
    public boolean isDigged(int x,int y){
        if(dig[x-1][y-1]==0)
            return true;
        else
            return false;
    }

    public boolean isMask(int x,int y)
    {
        if(isMask[x-1][y-1]<=1)
            return false;
        else
            return true;
    }

    //设置选中
    public void setSelect(int x, int i){
        select[x] = i;
    }

    //算出已经被用的数字
    public int[] getUsed(int x,int y){
        int c[]=new int[9];
        //x列
        for(int i=0;i<9;i++)
        {
            if(numbers[x][i]!=0)
            {
                c[numbers[x][i]-1]=numbers[x][i];
            }
        }
        //y排
        for(int i=0;i<9;i++)
        {
            if(numbers[i][y]!=0)
            {
                c[numbers[i][y]-1]=numbers[i][y];
            }
        }
        //小九宫格
        x=(x/3)*3;
        y=(y/3)*3;
        for(int i=0;i<9;i++)
        {
            if(numbers[x+i%3][y+i/3]!=0)
            {
                c[numbers[x+i%3][y+i/3]-1]=numbers[x+i%3][y+i/3];
            }
        }
        return c;
    }
    //设置选定的数字
    public void setTitle(int i,int x,int y){
        if(numbers[x][y] == i)
        {
            numbers[x][y] = 0;
            mask[x][y][i - 1] = 0;
            isMask[x][y]--;
            return;
        }
        if(!mGenerater.isNoConflict(numbers,i,x,y))
            return;
        if(isMask[x][y]==0)        //如果原来标记为0则直接设置一个数字
        {
            numbers[x][y] = i;
            mask[x][y][i-1] = 1;
            isMask[x][y]++;

        }
        else
        {
            if(mask[x][y][i-1]==1)
            {
                mask[x][y][i - 1] = 0;
                isMask[x][y]--;
            }
            else {
                mask[x][y][i - 1] = 1;
                isMask[x][y]++;
            }
            if(isMask[x][y]==1)
            {
                for(int j=0;j<9;j++)
                {
                    if(mask[x][y][j]==1)
                        numbers[x][y] = j+1;
                }
            }
            else
                numbers[x][y] = 0;
        }
    }

    //判断游戏结束
    public boolean youWin(){
        int t=0;
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (numbers[i][j]!=0){
                    t++;

                }
            }
        }
        Log.d("TNumber",String.valueOf(t));
        if (t==81){

            return true;

            /*AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Tips")
                    .setMessage("You Win !")
                    .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();*/
        }
        return false;
    }
}
