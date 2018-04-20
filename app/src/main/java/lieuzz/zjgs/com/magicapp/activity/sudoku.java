package lieuzz.zjgs.com.magicapp.activity;

import android.content.DialogInterface;
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
import lieuzz.zjgs.com.magicapp.Util.NewView;

import static java.security.AccessController.getContext;

public class sudoku extends AppCompatActivity {
    Button choose, data;
    Button easy,normal,hard,specialist,hell,cancel;
    TextView title,back,gameTime,gameLong;
    private int recLen = 0;
    private TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(new NewView(this));
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
                int dTime = 0;
                List<Game> games = DataSupport.findAll(Game.class);
                for(Game game: games){
                    Log.d("Mmmmmmmmm",String.valueOf(game.getTime()));
                    dTime = game.getTime();

                }
                gameTime.setText(String.valueOf(dTime));


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

                dataT();

                //Log.d("gggggggggg",String.valueOf(game.getTime()));
                recLen = 0;
                newView.setRank(1);
                newView.invalidate();
                title.setText("Easy");
                dialog.hide();
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataT();
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
                dataT();
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
                dataT();
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
                dataT();
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

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            txtView.setText("" + recLen / 60 + ":" + recLen % 60);
            handler.postDelayed(this, 1000);

            Game game1 = new Game();
            List<Game> games = DataSupport.findAll(Game.class);
            for(Game game: games){
                int dTime;
                Log.d("Mmmmmmmmm",String.valueOf(game.getTimelong()));
                dTime = game.getTimelong()+1;
                game1.setTimelong(dTime);
                game1.updateAll();

                gameLong.setText("" + dTime / 60 + ":" + dTime % 60);
            }


        }
    };

    public void dataT(){
        Game game1 = new Game();
        List<Game> games = DataSupport.findAll(Game.class);
        for(Game game: games){
            int dTime;
            Log.d("Mmmmmmmmm",String.valueOf(game.getTime()));
            dTime = game.getTime();
            game1.setTime(dTime+1);
            game1.updateAll();
        }

    }


}
