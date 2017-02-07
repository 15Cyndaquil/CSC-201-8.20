package com.example.cynda.csc_201_820;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static int ROW = 6;
    private final static int COLUMN=7;

    private int layoutHeight;
    private int layoutWidth;
    private int layoutTopBoarder;
    private int square;
    private int sideArea=0;

    private boolean go = true;
    private boolean button = true;
    private static boolean win = false;

    private static String[][] board = new String[ROW][COLUMN];

    private static boolean turnRed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setArea();
        board=setBoard(board);
        setBoard();
    }

    public static String[][] setBoard(String[][] board){
        for(int x=0; x<COLUMN; x++){
            for(int y=0; y<ROW; y++){
                board[y][x]="B ";
            }
        }
        return board;
    }

    public void setBoard(){
        setContentView(R.layout.activity_main);

        final RelativeLayout activityMain = (RelativeLayout) findViewById(R.id.activity_main);
        activityMain.setPadding(sideArea,0,sideArea,0);
        button = true;

        final ScrollView scrollLayoutVertical = new ScrollView(this);
        scrollLayoutVertical.setVerticalScrollBarEnabled(true);
        scrollLayoutVertical.setPaddingRelative(0,0,0,0);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0,0,0,0);

        for(int x = 0; x<ROW; x++){
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int y=0; y<COLUMN; y++){
                while(button){
                    LinearLayout row1 = new LinearLayout(this);
                    row1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    for(int buttonSet=0; buttonSet<COLUMN; buttonSet++) {
                        final Button butTag = new Button(this);
                        butTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        butTag.setPadding(0,0,0,0);
                        butTag.setId((buttonSet + 1) * ROW);
                        butTag.setText("Place in\nRow: "+(buttonSet+1));
                        butTag.setTextColor(getResources().getColor(R.color.buttonText));
                        butTag.setBackground(getResources().getDrawable(R.color.buttonBackground));
                        butTag.setMaxWidth((square));
                        butTag.setMinimumWidth((square));
                        butTag.setMinWidth((square));

                        final int buttonRowNum = buttonSet;
                        butTag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layout.removeAllViews();
                                go = true;
                                if (!win) {
                                    doButtons(buttonRowNum);
                                }
                                checkWin();
                                setBoard();
                            }
                        });
                        row1.addView(butTag);
                    }
                    layout.addView(row1);
                    button=false;
                }
                final ImageView imgTag = new ImageView(this);
                imgTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imgTag.setPadding(0,0,0,0);
                imgTag.setBackground(getResources().getDrawable(R.mipmap.board_blank));
                imgTag.setId((x+1)*ROW+(y+1)*COLUMN);
                imgTag.setMaxHeight(square);
                imgTag.setMaxWidth(square);
                imgTag.setMinimumHeight(square);
                imgTag.setMinimumWidth(square);

                if(board[x][y].equals("R ")){
                    imgTag.setBackground(getResources().getDrawable(R.mipmap.board_filled_red));
                }if(board[x][y].equals("Y ")){
                    imgTag.setBackground(getResources().getDrawable(R.mipmap.board_filled_yellow));
                }
                row.addView(imgTag);
            }
            layout.addView(row);
        }
        final TextView status = new TextView(this);
        status.setTextSize(50);
        status.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        status.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(win){
            String winner="";
            if(turnRed){
                winner="Yellow Player";
            }else{winner="Red Player";}
            status.setText("Status: "+winner+" Won!");
        }else{
            if(turnRed){
                status.setText("Current Player: Red");
            }else{status.setText("Current Player: Yellow");}
        }

        final Button reset = new Button(this);
        reset.setText("Reset Board");
        reset.setTextSize(50);
        reset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBoard();
                setBoard(board);
                button=true;
                win=false;
                setBoard();
            }
        });
        layout.addView(status);
        layout.addView(reset);

        scrollLayoutVertical.removeAllViews();
        scrollLayoutVertical.addView(layout);

        activityMain.removeAllViews();
        activityMain.addView(scrollLayoutVertical);

        System.out.println(sideArea);
    }

    public void setArea(){
        setContentView(R.layout.activity_main);
        Rect rect = new Rect();
        Window win = getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        layoutHeight = rect.height();
        layoutWidth = rect.width();
        layoutTopBoarder = rect.top;

        square=(layoutWidth)/7;
        sideArea=((layoutWidth%7)/2);

        System.out.println(square);
        System.out.println(layoutWidth);
    }

    public void doButtons(int column) {
        int y=ROW-1;
        while (go) {
            try {
                if (turnRed) {
                    if (board[y][column].equals("B ")) {
                        board[y][column] = "R ";
                        turnRed = false;
                        button = true;
                        go = false;
                    }
                } else if (!turnRed) {
                    if (board[y][column].equals("B ")) {
                        board[y][column] = "Y ";
                        turnRed = true;
                        button = true;
                        go = false;
                    }
                }
                y--;
            }catch (Exception exc){button = true; setBoard(); go=false;}
        }
    }

    public static boolean checkWin(){
        boolean check = false;
        for(int x=0; x<ROW; x++){
            for(int y=0; y<COLUMN; y++){
                if(board[x][y].equals("R ")){
                    if(!check) {
                        check = checkRow(x, y, board, "R ");
                    }if(!check) {
                        check = checkColumn(x, y, "R ");
                    }if(!check) {
                        check = checkDiagLeft(x, y, board, "R ");
                    }if(!check){
                        check=checkDiagRight(x,y,board,"R ");
                    }
                }else if(board[x][y].equals("Y ")){
                    if(!check) {
                        check = checkRow(x, y, board, "Y ");
                    }if(!check) {
                        check = checkColumn(x, y, "Y ");
                    }if(!check) {
                        check = checkDiagLeft(x, y, board, "Y ");
                    }if(!check){
                        check=checkDiagRight(x,y,board,"Y ");
                    }
                }
            }
        }
        return check;
    }

    public static boolean checkRow(int row, int column, String[][] board, String color){
        int count = 0;
        int x=column;
        boolean check=true;
        while(check) {
            try {
                if (board[row][x].equals(color)) {
                    count++;
                    x--;
                }else{check=false;}
            }catch (Exception exc){check=false;}
        }
        if(count>=4){
            win=true;
            System.out.println("checkRow");
            return true;
        }
        return false;
    }

    public static boolean checkColumn(int row, int column, String color){
        int count = 0;
        int x=row;
        boolean check = true;
        while(check){
            try {
                if (board[x][column].equals(color)) {
                    count++;
                    x--;
                }else{check=false;}
            }catch (Exception exc){check=false;}
        }
        if(count>=4){
            win=true;
            System.out.println("checkColumn");
            return true;
        }
        return false;
    }

    public static boolean checkDiagLeft(int row, int column, String[][] board, String color){
        int count = 0;
        boolean check=true;
        int x=row;
        int y=column;
        while(check){
            try {
                if (board[x][y].equals(color)) {
                    count++;
                    x--;
                    y++;
                } else {
                    check = false;
                }
            }catch (Exception exc){check=false;}
        }
        if(count>=4){
            win=true;
            System.out.println("checkDiagLeft");
            return true;
        }
        return false;
    }

    public static boolean checkDiagRight(int row, int column, String[][] board, String color){
        int count = 0;
        boolean check=true;
        int x=row;
        int y=column;
        while(check){
            try {
                if (board[x][y].equals(color)) {
                    count++;
                    x--;
                    y--;
                } else {
                    check = false;
                }
            }catch (Exception exc){check=false;}
        }
        if(count>=4){
            win=true;
            System.out.println("checkDiagRight");
            return true;
        }
        return false;
    }

    public void printBoard(){
        for(int x=0; x<ROW; x++){
            for(int y=0; y<COLUMN; y++){
                System.out.print(board[x][y]);
            }
            System.out.println("");
        }
    }
}
