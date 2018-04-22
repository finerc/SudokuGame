package lieuzz.zjgs.com.magicapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import lieuzz.zjgs.com.magicapp.R;
import lieuzz.zjgs.com.magicapp.Util.Game;
import lieuzz.zjgs.com.magicapp.Util.HardnessDialog;
import lieuzz.zjgs.com.magicapp.Util.MyGame;
import lieuzz.zjgs.com.magicapp.Util.NewView;

import static java.security.AccessController.getContext;

public class sudoku extends AppCompatActivity {
    Button choose, data;
    Button easy,normal,hard,specialist,hell,cancel;
    TextView title,back;
    TextView gameTime,gameLong,easyBest, normalBest,hardBest, speBest,hellBest;
    private int recLen = 0;
    public static TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sudoku);
        txtView = (TextView)findViewById(R.id.text_time);
        title = (TextView) findViewById(R.id.title);

        final NewView newView = (NewView) findViewById(R.id.new_view);
        final AlertDialog.Builder builder = new AlertDialog.Builder(sudoku.this);
        final View view = View.inflate(sudoku.this,R.layout.hardness_table,null);

        builder.setView(view);
        initBtn(view);
        handler.postDelayed(runnable, 1000);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(sudoku.this);
        final View view2 = View.inflate(sudoku.this,R.layout.data_table,null);
        builder2.setView(view2);
        back = (TextView) view2.findViewById(R.id.back);
        gameTime = (TextView) view2.findViewById(R.id.game_times);
        gameLong = (TextView) view2.findViewById(R.id.game_time_long);
        final AlertDialog dialog2 = builder2.create();
        dialog2.setCanceledOnTouchOutside(false);

        easyBest = (TextView) view2.findViewById(R.id.easy_best);
        normalBest = (TextView) view2.findViewById(R.id.normal_best);
        hardBest = (TextView) view2.findViewById(R.id.hard_best);
        speBest = (TextView) view2.findViewById(R.id.specialist_best);
        hellBest = (TextView) view2.findViewById(R.id.hell_best);

        ////////////////////////////////////////////////
        LitePal.getDatabase();
        List<Game> games = DataSupport.findAll(Game.class);
        if(games.size() == 0) {
            Game game = new Game();
            game.setTime(1);
            game.setTimelong(0);
            game.setEasy(0);
            game.setNormal(0);
            game.setHard(0);
            game.setSpe(0);
            game.setHell(0);
            game.save();
        }
        dataT(newView.mGame);

        Intent getI = getIntent();
        if(getI.getStringExtra("rank")!=null) {
            int i = Integer.parseInt(getI.getStringExtra("rank"));
            String s = getI.getStringExtra("recLen");
            String time[] = s.split(":");
            int number = Integer.parseInt(time[0])*60+Integer.parseInt(time[1]);
            Log.d("number", ""+number);
            Game game1 = new Game();
            for (Game game : games) {
                int easy = game.getEasy();
                int normal = game.getNormal();
                int hard = game.getHard();
                int spe = game.getSpe();
                int hell = game.getHell();
                Log.d("wowwwwwww", i+"");
                switch (i) {
                    case 1:
                        if (number < easy || easy == 0)
                            game1.setEasy(number);
                        break;
                    case 2:
                        if (number < normal || normal == 0)
                            game1.setNormal(number);
                        break;
                    case 3:
                        if (number < hard || hard == 0)
                            game1.setHard(number);
                        break;
                    case 4:
                        if (number < spe || spe == 0)
                            game1.setSpe(number);
                        break;
                    case 5:
                        if (number < hell || hell == 0)
                            game1.setHell(number);
                        break;
                }
            }
            game1.updateAll();
        }
        ////////////////////////////////////////////

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LitePal.getDatabase();
                dialog.show();

                /*AlertDialog.Builder builder=new AlertDialog.Builder(sudoku.this);
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
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
                int times=0, timeLong=0, easy=0, normal=0, hard=0, spe=0, hell=0;
                List<Game> games = DataSupport.findAll(Game.class);
                for(Game game: games){
                    Log.d("game.getTime",String.valueOf(game.getTime()));
                    times = game.getTime();
                    easy = game.getEasy();
                    normal = game.getNormal();
                    hard = game.getHard();
                    spe = game.getSpe();
                    hell = game.getHell();
                }
                gameTime.setText(String.valueOf(times));
                easyBest.setText("" + easy / 60 + ":" + easy % 60);
                normalBest.setText("" + normal / 60 + ":" + normal % 60);
                hardBest.setText("" + hard / 60 + ":" + hard % 60);
                speBest.setText("" + spe / 60 + ":" + spe % 60);
                hellBest.setText("" + hell / 60 + ":" + hell% 60);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.hide();
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log.d("gggggggggg",String.valueOf(game.getTime()));
                recLen = 0;
                newView.setRank(1);
                newView.invalidate();
                title.setText("Easy");
                dialog.hide();
                dataT(newView.mGame);
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataT(newView.mGame);
                recLen = 0;
                newView.setRank(2);
                newView.invalidate();
                title.setText("Normal");
                dialog.hide();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataT(newView.mGame);
                recLen = 0;
                newView.setRank(3);
                newView.invalidate();
                title.setText("Hard");
                dialog.hide();
            }
        });
        specialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataT(newView.mGame);
                recLen = 0;
                newView.setRank(4);
                newView.invalidate();
                title.setText("Specialist");
                dialog.hide();
            }
        });
        hell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataT(newView.mGame);
                recLen = 0;
                newView.setRank(5);
                newView.invalidate();
                title.setText("Hell");
                dialog.hide();
            }
        });

    }

    public void initBtn(View view){
        choose =(Button) findViewById(R.id.choose_hard);
        data = (Button) findViewById(R.id.data);
        easy = (Button) view.findViewById(R.id.easy);
        normal = (Button) view.findViewById(R.id.normal);
        hard = (Button) view.findViewById(R.id.hard);
        specialist = (Button) view.findViewById(R.id.specialist);
        hell = (Button) view.findViewById(R.id.hell);
        cancel = (Button) view.findViewById(R.id.cancel);
    }

    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            txtView.setText("" + recLen / 60 + ":" + recLen % 60);
            handler.postDelayed(this, 1000);

            Game game1 = new Game();
            List<Game> games = DataSupport.findAll(Game.class);
            for(Game game: games) {
                int dTime;
                Log.d("game.getTimelong", String.valueOf(game.getTimelong()));
                dTime = game.getTimelong() + 1;
                game1.setTimelong(dTime);
                game1.updateAll();
                gameLong.setText("" + dTime / 60 + ":" + dTime % 60);
            }
        }
    };

    ///////////////////////////////
    public void dataT(MyGame mGame){
        Game game1 = new Game();
        List<Game> games = DataSupport.findAll(Game.class);
        for(Game game: games) {
            int times;
            times = game.getTime();

            game1.setTime(times+1);
            game1.updateAll();
        }
    }
    /////////////////////////////////
}
