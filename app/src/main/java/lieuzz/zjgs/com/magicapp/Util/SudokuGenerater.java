package lieuzz.zjgs.com.magicapp.Util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

public class SudokuGenerater {

    int rank;
    Context mContext;
    int[][] FinalBoard = new int[9][9];     //存放终盘， 可作为答案显示(使用回溯法生成）
    int[][] PuzzleBoard = new int[9][9];    //存放待解数独局面
    int[][] DigBoard = new int[9][9];   //“挖洞”矩阵

    void initFinalBoard()        //第一行随机化
    {
        int []firstRow = { 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for(int i=0;i<10;i++)
        {
            int x = rand.nextInt(8)+1;
            int temp = firstRow[0];
            firstRow[0] = firstRow[x];
            firstRow[x] = temp;
        }
        FinalBoard[0] = firstRow;
        for(int i=1;i<9;i++)
            Arrays.fill(FinalBoard[i],0);
    }

    boolean isRowNoConflict(int[][] grid, int value, int row)      //判断行内有无冲突
    {
        for(int i=0;i<9;i++)
            if(grid[row][i]==value)
                return false;
        return true;
    }

    boolean isColumnNoConflict(int[][] grid, int value,int column)      //判断列内有无冲突
    {
        for(int i=0;i<9;i++)
            if(grid[i][column]==value)
                return false;
        return true;
    }

    boolean isGridNoConflict(int[][] grid, int value,int row, int column)       //判断所在小九宫格内有无冲突
    {
        int rowOfGrid = (row/3)*3;
        int columnOfGrid = (column/3)*3;

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(grid[rowOfGrid+i][columnOfGrid+j]==value)
                    return false;
        return true;
    }

    boolean isNoConflict(int[][] grid,int value,int row,int column)
    {
        return isRowNoConflict(grid,value,row)
                && isColumnNoConflict(grid,value,column)
                && isGridNoConflict(grid,value,row,column);
    }

    boolean SudokuSolver(int[][] grid, int row,int column)
    {
        if(row==8&&column==9)
            return true;
        if(column==9)
        {
            column = 0;
            row++;
        }
        if(grid[row][column]!=0)
            return SudokuSolver(grid, row,column+1);
        for(int i = 1;i<10;i++)
        {
            if (isNoConflict(grid, i, row, column)) {
                grid[row][column] = i;
                if(SudokuSolver(grid, row, column + 1))
                {
                    return true;
                }
            }
        }

        grid[row][column] = 0;
        return false;
    }

    SudokuGenerater(int _rank, Context context)
    {
        this.rank = _rank;
        mContext = context;
        initFinalBoard();
        while(!SudokuSolver(this.FinalBoard,0, 0))
        {
            initFinalBoard();
        }
        for(int i=0;i<9;i++)
            Arrays.fill(DigBoard[i],1);
        for(int i=0;i<9;i++)
            System.arraycopy(FinalBoard[i],0,PuzzleBoard[i],0,FinalBoard[i].length);
    }

    void DiggingHoles()
    {
        Random rand = new Random();
        int holes = rand.nextInt(6)+26;
        int holeslimit = 26;
        int holeCount = 0;
        int WholeCount = 0;
        int digRow, digColumn;
        switch (this.rank)
        {
            case 1:             //easy
                holes = rand.nextInt(6)+26;     //holes:26-31
                holeslimit = 26;
                break;
            case 2:
                holes = rand.nextInt(9)+32;     //holes:32-40
                holeslimit = 32;
                break;
            case 3:
                holes=rand.nextInt(9)+41;       //holes:41-49
                holeslimit = 41;
                break;
            case 4:
                /*holes = rand.nextInt(4)+50;     //holes:50-53
                holeslimit = 50;*/
                InputStream is = null;
                try {
                    AssetManager aM = mContext.getAssets();
                    int i = rand.nextInt(20)+1;
                    String specialistfile = "specialist" + i+".txt";
                    is = aM.open(specialistfile);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                setBoardByText(is);
                return;
            case 5:             //evil
                InputStream in = null;
                try {
                    AssetManager aM = mContext.getAssets();
                    int i = rand.nextInt(20)+1;
                    String evilfile = "evil" + i+".txt";
                    in = aM.open(evilfile);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                setBoardByText(in);
                return;
                /*holes = rand.nextInt(6)+54;     //givens amount 22-27
                holeslimit = 54;
                digRow = 0;
                digColumn = 0;
                while(holeCount<holes)
                {
                    if(digColumn==9)
                    {
                        digColumn=0;
                        digRow++;
                    }
                    if(digColumn==0&&digRow==9)         //挖洞个数不足
                    {
                        if(holeCount>=54)
                            break;
                        initFinalBoard();
                        while(!SudokuSolver(this.FinalBoard,0, 0))
                        {
                            initFinalBoard();
                        }
                        for(int i=0;i<9;i++)
                            System.arraycopy(FinalBoard[i],0,PuzzleBoard[i],0,FinalBoard[i].length);
                        WholeCount = 0;
                        holeCount = 0;
                        digRow = digColumn = 0;
                    }
                    if(PuzzleBoard[digRow][digColumn]==0)
                        continue;
                    if(isDigUnique(digRow,digColumn))
                    {
                        PuzzleBoard[digRow][digColumn] = 0;
                        holeCount++;
                    }
                    digColumn++;
                }
                return;*/
            default:
        }
        while(holeCount<holes)
        {
            WholeCount++;
            digRow = rand.nextInt(9);
            digColumn = rand.nextInt(9);
            if(PuzzleBoard[digRow][digColumn]==0)
                continue;
            if(isDigUnique(digRow,digColumn))
            {
                Log.d("digRow",Integer.toString(digRow));
                Log.d("digColumn",Integer.toString(digColumn));
                PuzzleBoard[digRow][digColumn] = 0;
                DigBoard[digRow][digColumn] = 0;
                holeCount++;
            }
            Log.d("holeCount", Integer.toString(holeCount));
            if(WholeCount>500)
            {
                if(holeCount>=holeslimit)
                    break;
                initFinalBoard();
                while(!SudokuSolver(this.FinalBoard,0, 0))
                {
                    initFinalBoard();
                }
                for(int i=0;i<9;i++)
                    Arrays.fill(DigBoard[i],1);
                for(int i=0;i<9;i++)
                    System.arraycopy(FinalBoard[i],0,PuzzleBoard[i],0,FinalBoard[i].length);
                WholeCount = 0;
                holeCount = 0;
            }
        }
    }

    boolean isDigUnique(int row,int column)      //判断该格挖洞后解法是否唯一
    {
        int[][] tempBoard = new int[9][9];
        for(int i=0;i<9;i++)
            System.arraycopy(PuzzleBoard[i],0,tempBoard[i],0,PuzzleBoard[i].length);
        for (int i = 1; i < 10; i++)
        {
            if (i == tempBoard[row][column])
                continue;
            else if(!isNoConflict(tempBoard,i,row,column))
                continue; 
            else
            {
                tempBoard[row][column] = i;
                if(SudokuSolver(tempBoard,0, 0))
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBoardByText(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            int row = 0;
            while ((line = reader.readLine()) != null) {
                String[] temp = line.split("\t");
                for(int j=0;j<temp.length;j++){
                    this.PuzzleBoard[row][j] = Integer.parseInt(temp[j]);
                    if(Integer.parseInt(temp[j])==0)
                        this.DigBoard[row][j] = 0;
                    else
                        this.DigBoard[row][j] = 1;

                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
