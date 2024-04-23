package com.example.dial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends Activity {

    double viewSize = 415;           //---------画面サイズ
    double center = viewSize / 2; //---------画面半径
    double part = viewSize / 10;  //---------濁点の境界線
    int small = 0;                            //---------小文字ID
    int big = 0;                               //---------大文字ID
    //------------------------------- アクションの位置情報
    double downX = 0;
    double downY = 0;
    double moveX = 0;
    double moveY = 0;
    double upX = 0;
    double upY = 0;
    double distance = 0;               //----------移動距離
    double radian = 0;                   //----------移動角度
    double centDis = 0;                //----------中心からの距離
    //--------------------------------アクションの角度情報
    double radianMOVE = 0;
    double radianDOWN = 0;
    double semicircle = 3.14159;  //----------π
    double prevRadian = 0;           //----------1文字前の角度

    //----------------------------------------子音の境界
    double top1 = -2.879793;
    double top2 = -2.356194;
    double top3 = -1.832595;
    double top4 = -1.308996;
    double top5 = -0.785398;
    double top6 = -0.261799;
    double top7 =  0.261799;
    double top8 =  0.785398;
    double top9 =  1.308996;
    double top10 = 1.832595;
    double top11 = 2.356194;
    double top12 = 2.879793;

    //------------------------------------------母音の境界
    double ai = 0.3926991;
    double iu = ai * 3;
    double ue = ai * 5;
    double eo = ai * 7;
    double oba = semicircle * 2 - iu;

    //----------------------------------------文字列操作用
    int lapCounter = 0;           //----------移動中の周回数
    StringBuilder text = new StringBuilder(""); //----------編集可能なテキスト
    String done = "";              //----------入力済みテキスト
    String inputChar = "";      //----------編集中の文字
    boolean deleteFlag = false;  //----------削除フラグ

    //----------------------------------------誤入力判定用
    int typoCount = 0;      //----------誤入力文字数
    int typeCount = 0;      //----------入力文字数

    //-----------------------------------------タスク配列
    String[][] task = {
            {"55", "タスク表示欄", "い", "う", "え", "お", "か", "き", "く", "け", "こ", "リセット用"},
            {"11", "ぬさけれわ", "ひだひむぜ", "めのさせぼ", "えれむぉび", "はばべぉえ", "いぬうむく", "らてるえあ", "ぜすてすち", "やぶふぷい", "たゆくをご", "実験終了11"},
            {"12", "きちくぇど", "もにぱぴせ", "てあいねみ", "づよばみろ", "とぺかんち", "まさよぷぞ", "つぬたゆし", "よじぎやぴ", "れとせぃを", "やざみくも", "実験終了12"},
            {"13", "ぽるらろじ", "つそかちぐ", "ぶあをぴし", "まめねびや", "よんぎれと", "ばしがまじ", "とぺさちぅ", "どりめかぱ", "たえぷふよ", "むわらえん", "実験終了13"},
            {"21", "ぃせはぐぅ", "ゆぢうきり", "ほずらうん", "でぜせっぜ", "びすりけゅ", "きゅぼぽげ", "なゃばぶぃ", "んでいごぷ", "ごほごへぉ", "ざけはゆき", "実験終了21"},
            {"22", "へさよぱゃ", "をゆだゃよ", "えぢごこね", "ぎなはぢゅ", "そどとっじ", "ざほほぉび", "やすじぇっ", "ねねげっれ", "ねづぬせへ", "みぴうよて", "実験終了22"},
            {"23", "ねにぴぇせ", "じだばゎて", "ぱまをごや", "はえちなじ", "れきぎびつ", "おゅでわや", "むべょしま", "ぱずくすょ", "ろぁせだぞ", "ずせむもと", "実験終了23"},
            {"31", "らそとわべ", "ぁれうそぬ", "ぺやすはも", "そげぁどし", "けをほだぼ", "あうせきゃ", "とにしさほ", "ぽづょせじ", "ぞぽたふね", "おぼまらよ", "実験終了31"},
            {"32", "くとひげう", "ふぉぽちく", "げぺとめば", "とえさげお", "うもぶをば", "めによぎこ", "へぷたりら", "てぬでぱふ", "ぶんかへる", "まむりろた", "実験終了32"},
            {"33", "もせかむぶ", "はぷざぉつ", "やてごわな", "ずっるけつ", "ぺめれはき", "びろみぴゅ", "れざべえく", "らがうさび", "ぎをけたけ", "でぱにねょ", "実験終了33"},
            {"41", "げゆらょぞ", "みわおんも", "わぴだむぐ", "ざなずつら", "ほけぅゃう", "すこもはん", "ちのわけふ", "ぢぐんぉで", "ぐめづょご", "ぃばごばは", "実験終了41"},
            {"42", "ばぷこべを", "わべぎぞね", "ぴべほちゆ", "とちげじだ", "ぅぱゅでぺ", "ゆぱせぁぺ", "そかれぺた", "ほぁなろめ", "みたぅづぜ", "さるあをめ", "実験終了42"},
            {"43", "りえわわぢ", "ぬぃびりわ", "ゅごぶせぞ", "ぐろらざへ", "だほぁはと", "ぜがぃてざ", "んいびにと", "びわるだっ", "ばるぞえが", "ずわぷぜい", "実験終了43"}

    };

    int taskEnd = 10;              //----------タスク配列の終わり
    int taskVer = 0;              //----------タスクバージョン
    int taskNum = 0;            //----------タスク番号

    int prevBig = 0;              //----------1文字前の大文字ID

    int partial = 0;            //------------領域ID

    long startTime;           //----------実験開始時間
    long finishTime;         //----------実験終了時間
    double typoRatio = 0;      //----------誤入力率


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //---------------------------------------------------ViewのIDを取得
        View taskText = findViewById(R.id.taskText);
        View doneText = findViewById(R.id.doneText);
        View inputText = findViewById(R.id.inputText);
        View typoText = findViewById(R.id.typoText);
        View typeText = findViewById(R.id.typeText);

        View keyboardImage = findViewById(R.id.keyboardImage);




        //---------------------------------------------------タッチリスナー
        keyboardImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //----------座標取得
                double x = event.getX();
                double y = event.getY();

                switch (event.getAction()) {
                    //-------------------------------------------アクションDOWN
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;

                        //----------中心からの距離を取得
                        distance = getDistance(center,center,downX,downY);
                        //----------キーボード外のアクションを無効化
                        if(distance > center || distance < part * 3){
                            return false;
                        }

                        //----------角度情報を取得
                        radianDOWN = getRadian(center,center,downX,downY);
                        prevRadian = radianDOWN;

                        //----------小文字IDを取得
                        if(radianDOWN <= top1){
                            small = 9;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ya);
                        }else if(radianDOWN <= top2){
                            small = 10;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ra);
                        }else if(radianDOWN <= top3){
                            small = 11;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.wa);
                        }else if(radianDOWN <= top4){
                            small = 12;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.num1);
                        }else if(radianDOWN <= top5){
                            small = 1;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.aa);
                        }else if(radianDOWN <= top6){
                            small = 2;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ka);
                        }else if(radianDOWN <= top7){
                            small = 3;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.sa);
                        }else if(radianDOWN <= top8){
                            small = 4;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ta);
                        }else if(radianDOWN <= top9){
                            small = 5;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.na);
                        }else if(radianDOWN <= top10){
                            small = 6;
                        }else if(radianDOWN <= top11){
                            small = 7;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ha);
                        }else if(radianDOWN <= top12){
                            small = 8;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ma);
                        }else{
                            small = 9;
                            ((ImageView)keyboardImage).setImageResource(R.drawable.ya);
                        }

                        //----------画面に出力
                        big = 1;
                        input(small,big);
                        ((TextView)inputText).setText(inputChar);
                        ((TextView)inputText).setTextColor(Color.RED);
                        //----------バイブレーション
                        ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(27);
                        lapCounter = 0;
                        prevBig = 0;
                        partial = 0;
                        return true;

                    //-------------------------------------------アクションMOVE
                    case MotionEvent.ACTION_MOVE:
                        moveX = x;
                        moveY = y;

                        //----------中心からの距離を取得
                        centDis = getDistance(center,center,moveX,moveY);

                        //----------角度情報を取得
                        radianMOVE = getRadian(center,center,moveX,moveY);

                        //----------周回数をカウント
                        if(moveX < center){
                            if(prevRadian > 0 && radianMOVE < 0){
                                lapCounter++;
                            }else if(prevRadian < 0 && radianMOVE > 0){
                                lapCounter--;
                            }
                        }

                        //----------動いた角度を計算
                        radian = radianMOVE - radianDOWN;
                        radian = radian + (semicircle * 2 * lapCounter);

                        //----------中心が被らないように領域を５分割
                        if(centDis >= part * 3){
                            if(radian <= ai){
                                partial = 1;
                            }else if(radian <= iu){
                                partial = 2;
                            }else if(radian <= ue){
                                partial = 3;
                            }else if(radian <= eo){
                                partial = 4;
                            }else if(radian <= oba){
                                partial = 5;
                            }
                        }

                        //----------大文字IDを取得
                        if(partial == 1){
                            big = 1;
                            if(small == 1){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.aa);
                                if(centDis < part){
                                    big = 6;
                                }
                            }else if(small == 2){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ka);
                                if(centDis < part){
                                    big = 6;
                                }
                            }else if(small == 3){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.sa);
                                if(centDis < part){
                                    big = 6;
                                }
                            }else if(small == 4){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ta);
                                if(centDis < part){
                                    big = 6;
                                }
                            }else if(small == 7){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ha);
                                if(centDis >= part && centDis < part * 3){
                                    big = 6;
                                }else if(centDis < part) {
                                    big = 11;
                                }
                            }else if(small == 9){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ya);
                                if(centDis < part) {
                                    big = 6;
                                }
                            }else if(small == 11){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.wa);
                                if(centDis < part) {
                                    big = 6;
                                }
                            }else if(small == 12){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.num1);
                                if(centDis < part) {
                                    big = 6;
                                }
                            }
                        }else if(partial == 2){
                            big = 2;
                            if(small == 1){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ai);
                                if(centDis < part){
                                    big = 7;
                                }
                            }else if(small == 2){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ki);
                                if(centDis < part){
                                    big = 7;
                                }
                            }else if(small == 3){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.si);
                                if(centDis < part){
                                    big = 7;
                                }
                            }else if(small == 4){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ti);
                                if(centDis < part){
                                    big = 7;
                                }
                            }else if(small == 7){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.hi);
                                if(centDis >= part && centDis < part * 3){
                                    big = 7;
                                }else if(centDis < part) {
                                    big = 12;
                                }
                            }else if(small == 9){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.yu);
                                if(centDis < part) {
                                    big = 7;
                                }
                            }else if(small == 11){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.wo);
                                if(centDis < part) {
                                    big = 7;
                                }
                            }else if(small == 12){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.num2);
                                if(centDis < part) {
                                    big = 7;
                                }
                            }
                        }else if(partial == 3){
                            big = 3;
                            if(small == 1){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.au);
                                if(centDis < part){
                                    big = 8;
                                }
                            }else if(small == 2){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ku);
                                if(centDis < part){
                                    big = 8;
                                }
                            }else if(small == 3){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.su);
                                if(centDis < part){
                                    big = 8;
                                }
                            }else if(small == 4){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.tu);
                                if(centDis >= part && centDis < part * 3){
                                    big = 11;
                                }else if(centDis < part){
                                    big = 8;
                                }
                            }else if(small == 7){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.hu);
                                if(centDis >= part && centDis < part * 3){
                                    big = 8;
                                }else if(centDis < part) {
                                    big = 13;
                                }
                            }else if(small == 9){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.yo);
                                if(centDis < part) {
                                    big = 8;
                                }
                            }else if(small == 11){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.wn);
                                if(centDis < part) {
                                    big = 8;
                                }
                            }else if(small == 12){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.num3);
                                if(centDis < part) {
                                    big = 8;
                                }
                            }
                        }else if(partial == 4){
                            big = 4;
                            if(small == 1){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ae);
                                if(centDis < part){
                                    big = 9;
                                }
                            }else if(small == 2){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ke);
                                if(centDis < part){
                                    big = 9;
                                }
                            }else if(small == 3){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.se);
                                if(centDis < part){
                                    big = 9;
                                }
                            }else if(small == 4){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.te);
                                if(centDis < part){
                                    big = 9;
                                }
                            }else if(small == 7){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.he);
                                if(centDis >= part && centDis < part * 3){
                                    big = 9;
                                }else if(centDis < part) {
                                    big = 14;
                                }
                            }else if(small == 9){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.y1);
                                if(centDis < part) {
                                    big = 9;
                                }
                            }else if(small == 11){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.w1);
                                if(centDis < part) {
                                    big = 9;
                                }
                            }else if(small == 12){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.num4);
                                if(centDis < part) {
                                    big = 9;
                                }
                            }
                        }else if(partial == 5){
                            big = 5;
                            if(small == 1){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ao);
                                if(centDis < part){
                                    big = 10;
                                }
                            }else if(small == 2){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ko);
                                if(centDis < part){
                                    big = 10;
                                }
                            }else if(small == 3){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.so);
                                if(centDis < part){
                                    big = 10;
                                }
                            }else if(small == 4){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.to);
                                if(centDis < part){
                                    big = 10;
                                }
                            }else if(small == 7){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.ho);
                                if(centDis >= part && centDis < part * 3){
                                    big = 10;
                                }else if(centDis < part) {
                                    big = 15;
                                }
                            }else if(small == 9){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.y2);
                                if(centDis < part) {
                                    big = 10;
                                }
                            }else if(small == 11){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.w2);
                                if(centDis < part) {
                                    big = 10;
                                }
                            }else if(small == 12){
                                ((ImageView)keyboardImage).setImageResource(R.drawable.num5);
                                if(centDis < part) {
                                    big = 10;
                                }
                            }
                        }

                        //-----------画面に出力
                        input(small,big);
                        ((TextView)inputText).setText(inputChar);

                        //----------キーボード外の時、色を変える
                        /*distance = getDistance(center,center,moveX,moveY);
                        if(distance > center || distance < center/2)
                            ((TextView)inputText).setTextColor(Color.BLUE);
                        else
                            ((TextView)inputText).setTextColor(Color.RED);*/

                        //----------バイブレーション
                        prevRadian = radianMOVE;
                        if(prevBig > 0){
                            if(prevBig < big || prevBig > big){
                                ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(27);
                            }
                        }
                        prevBig = big;
                        return true;


                    //-----------------------------------------アクションUP
                    case MotionEvent.ACTION_UP:
                        upX = x;
                        upY = y;
                        input(small,big);

                        //----------キーボード外で入力をキャンセル
                        distance = getDistance(center,center,upX,upY);
                        /*if(distance > center || distance < center/2){
                            input(0,0);
                            ((TextView)inputText).setText("");
                            ((ImageView)keyboardImage).setImageResource(R.drawable.top);
                            return false;
                        }*/

                        //radianUP = getRadian(center,center,upX,upY);

                        typeCount++;
                        //----------1文字削除
                        if(deleteFlag) {
                            typeCount--;
                            int last = text.length() - 1;
                            if (last >= 0){
                                text.deleteCharAt(last);
                                typoCount++;
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                            }
                            deleteFlag = false;
                        }
                        ((TextView)typeText).setText(String.valueOf(typeCount));


                        //----------入力文字を追加
                        text.append(inputChar);
                        done = text.toString();
                        ((TextView)doneText).setText(done);
                        ((TextView)inputText).setText("");

                        //領域IDリセット
                        partial = 0;

                        //----------33が入力された時リセット
                        if(done.equals(task[0][0])){
                            done = "";
                            typeCount = 0;
                            typoCount = 0;
                            taskVer = 0;
                            taskNum = 0;
                            text.delete(0, text.length());

                            ((TextView)taskText).setText(task[0][1]);
                            ((TextView)typeText).setText(String.valueOf(typeCount));
                            ((TextView)typoText).setText(String.valueOf(typoCount));
                        }

                        //----------実験バージョンの確認と実験開始
                        if(taskNum == 0){
                            if(done.equals(task[1][taskNum])){
                                taskVer = 1;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[2][taskNum])){
                                taskVer = 2;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[3][taskNum])){
                                taskVer = 3;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[4][taskNum])){
                                taskVer = 4;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[5][taskNum])){
                                taskVer = 5;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[6][taskNum])){
                                taskVer = 6;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[7][taskNum])){
                                taskVer = 7;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[8][taskNum])){
                                taskVer = 8;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[9][taskNum])){
                                taskVer = 9;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[10][taskNum])){
                                taskVer = 10;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView)doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView)typeText).setText(String.valueOf(typeCount));
                                ((TextView)typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView)taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[11][taskNum])) {
                                taskVer = 11;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView) doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView) typeText).setText(String.valueOf(typeCount));
                                ((TextView) typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView) taskText).setText(task[taskVer][taskNum]);
                            }else if(done.equals(task[12][taskNum])) {
                                taskVer = 12;
                                text.delete(0, text.length());
                                done = "";
                                ((TextView) doneText).setText(done);
                                typeCount = 0;
                                typoCount = 0;
                                ((TextView) typeText).setText(String.valueOf(typeCount));
                                ((TextView) typoText).setText(String.valueOf(typoCount));
                                startTime = System.currentTimeMillis();
                                taskNum++;
                                ((TextView) taskText).setText(task[taskVer][taskNum]);
                            }
                        }


                        //----------入力済み文字が正解の時、次もタスクを表示
                        if(done.equals(task[taskVer][taskNum])) {
                            text.delete(0, text.length());
                            done = "";
                            ((TextView)doneText).setText(done);

                            if(taskNum == taskEnd){                  //----------実験終了の処理
                                finishTime = System.currentTimeMillis();
                                double time = ((double)finishTime - (double)startTime) / 1000;
                                typoRatio = Math.floor((double)typoCount / (double)typeCount * 1000) / 10;
                                double cpm = Math.floor((((double)typeCount - (double)typoCount) / time) * 60 * 10) /10;
                                ((TextView)doneText).setText("誤入力率/TIME");
                                ((TextView)inputText).setText(String.valueOf(typoRatio)+"/"+String.valueOf(time));
                                ((TextView)typoText).setText("CPM");
                                ((TextView)typeText).setText(String.valueOf(cpm));
                                ((TextView)inputText).setTextColor(Color.WHITE);
                            }


                            taskNum++;
                            ((TextView)taskText).setText(task[taskVer][taskNum]);
                        }else{
                            //----------テキストを出力

                            ((TextView)doneText).setText(done);
                        }

                        ((ImageView)keyboardImage).setImageResource(R.drawable.top);
                        prevRadian = 0;
                }
                return true;
            }
        });
    }





    protected double getDistance(double x, double y, double x2, double y2){
        double dis = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
        return dis;
    }


    protected double getRadian(double x, double y, double x2, double y2){
        double rad = Math.atan2(y2 - y,x2 - x);
        return rad;
    }


    protected void input(int s, int b){
        if(s == 0) inputChar = "";
        else if(s == 1) {///////////////////////////////////////////////////////////////////あ行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "あ";
            else if(b == 2) inputChar = "い";
            else if(b == 3) inputChar = "う";
            else if(b == 4) inputChar = "え";
            else if(b == 5) inputChar = "お";
            else if(b == 6) inputChar = "ぁ";
            else if(b == 7) inputChar = "ぃ";
            else if(b == 8) inputChar = "ぅ";
            else if(b == 9) inputChar = "ぇ";
            else if(b >= 10) inputChar = "ぉ";
        }
        else if(s == 2) {///////////////////////////////////////////////////////////////////か行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "か";
            else if(b == 2) inputChar = "き";
            else if(b == 3) inputChar = "く";
            else if(b == 4) inputChar = "け";
            else if(b == 5) inputChar = "こ";
            else if(b == 6) inputChar = "が";
            else if(b == 7) inputChar = "ぎ";
            else if(b == 8) inputChar = "ぐ";
            else if(b == 9) inputChar = "げ";
            else if(b >= 10) inputChar = "ご";
        }
        else if(s == 3) {///////////////////////////////////////////////////////////////////さ行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "さ";
            else if(b == 2) inputChar = "し";
            else if(b == 3) inputChar = "す";
            else if(b == 4) inputChar = "せ";
            else if(b == 5) inputChar = "そ";
            else if(b == 6) inputChar = "ざ";
            else if(b == 7) inputChar = "じ";
            else if(b == 8) inputChar = "ず";
            else if(b == 9) inputChar = "ぜ";
            else if(b >= 10) inputChar = "ぞ";
        }
        else if(s == 4) {///////////////////////////////////////////////////////////////////た行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "た";
            else if(b == 2) inputChar = "ち";
            else if(b == 3) inputChar = "つ";
            else if(b == 4) inputChar = "て";
            else if(b == 5) inputChar = "と";
            else if(b == 6) inputChar = "だ";
            else if(b == 7) inputChar = "ぢ";
            else if(b == 8) inputChar = "づ";
            else if(b == 9) inputChar = "で";
            else if(b == 10) inputChar = "ど";
            else if(b >= 11) inputChar = "っ";
        }
        else if(s == 5) {///////////////////////////////////////////////////////////////////な行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "な";
            else if(b == 2) inputChar = "に";
            else if(b == 3) inputChar = "ぬ";
            else if(b == 4) inputChar = "ね";
            else if(b >= 5) inputChar = "の";
        }
        else if(s == 6) {///////////////////////////////////////////////////////////////////、
            if (b == 0) inputChar = "";
            else if(b == 1) {
                inputChar = "";
                deleteFlag = true;
            }
            else if(b >= 2) {
                inputChar = "";
            }
        }
        else if(s == 7) {///////////////////////////////////////////////////////////////////は行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "は";
            else if(b == 2) inputChar = "ひ";
            else if(b == 3) inputChar = "ふ";
            else if(b == 4) inputChar = "へ";
            else if(b == 5) inputChar = "ほ";
            else if(b == 6) inputChar = "ば";
            else if(b == 7) inputChar = "び";
            else if(b == 8) inputChar = "ぶ";
            else if(b == 9) inputChar = "べ";
            else if(b == 10) inputChar = "ぼ";
            else if(b == 11) inputChar = "ぱ";
            else if(b == 12) inputChar = "ぴ";
            else if(b == 13) inputChar = "ぷ";
            else if(b == 14) inputChar = "ぺ";
            else if(b >= 15) inputChar = "ぽ";
        }
        else if(s == 8) {///////////////////////////////////////////////////////////////////ま行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "ま";
            else if(b == 2) inputChar = "み";
            else if(b == 3) inputChar = "む";
            else if(b == 4) inputChar = "め";
            else if(b >= 5) inputChar = "も";
        }
        else if(s == 9) {///////////////////////////////////////////////////////////////////や行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "や";
            else if(b == 2) inputChar = "ゆ";
            else if(b == 3) inputChar = "よ";
            else if(b == 4) inputChar = "「";
            else if(b == 5) inputChar = "」";
            else if(b == 6) inputChar = "ゃ";
            else if(b == 7) inputChar = "ゅ";
            else if(b == 8) inputChar = "ょ";
            else if(b == 9) inputChar = "(";
            else if(b >= 10) inputChar = ")";
        }
        else if(s == 10) {///////////////////////////////////////////////////////////////////ら行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "ら";
            else if(b == 2) inputChar = "り";
            else if(b == 3) inputChar = "る";
            else if(b == 4) inputChar = "れ";
            else if(b >= 5) inputChar = "ろ";
        }
        else if(s == 11) {///////////////////////////////////////////////////////////////////わ行
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "わ";
            else if(b == 2) inputChar = "を";
            else if(b == 3) inputChar = "ん";
            else if(b == 4) inputChar = "ー";
            else if(b == 5) inputChar = "〜";
            else if(b == 6) inputChar = "ゎ";
            else if(b == 7) inputChar = "、";
            else if(b == 8) inputChar = "。";
            else if(b == 9) inputChar = "?";
            else if(b >= 10) inputChar = "!";
        }
        else if(s == 12) {///////////////////////////////////////////////////////////////////数字
            if (b == 0) inputChar = "";
            else if(b == 1) inputChar = "1";
            else if(b == 2) inputChar = "2";
            else if(b == 3) inputChar = "3";
            else if(b == 4) inputChar = "4";
            else if(b == 5) inputChar = "5";
            else if(b == 6) inputChar = "6";
            else if(b == 7) inputChar = "7";
            else if(b == 8) inputChar = "8";
            else if(b == 9) inputChar = "9";
            else if(b == 10) inputChar = "0";
            else if(b == 11) inputChar = "+";
            else if(b == 12) inputChar = "×";
            else if(b == 13) inputChar = "÷";
            else if(b == 14) inputChar = "=";
            else if(b >= 15) inputChar = "%";
        }
    }
}