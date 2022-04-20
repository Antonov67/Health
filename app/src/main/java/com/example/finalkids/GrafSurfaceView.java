package com.example.finalkids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class GrafSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public DrawThread drawThread, drawThread2;
    public Integer variant, count=1; // вариант графика и их количество
    public ArrayList<Integer> pointy; // массив точек для графика
    public ArrayList<String> pointx; //массив точек оси х - даты
    public ArrayList<Integer> pointz; //массив точек для второго графика
    //рисование одного графика
    public GrafSurfaceView(Context context, Integer variant, ArrayList<String> pointx, ArrayList<Integer> pointy) {
        super(context);
        this.variant = variant;
        this.pointx = pointx;
        this.pointy = pointy;
        getHolder().addCallback(this);
    }

    //рисование двух графиков
    public GrafSurfaceView(Context context, Integer variant, ArrayList<String> pointx, ArrayList<Integer> pointy,ArrayList<Integer> pointz, Integer count) {
        super(context);
        this.variant = variant;
        this.pointx = pointx;
        this.pointy = pointy;
        this.pointz = pointz;
        this.count = count;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      if (count != 2) {
            drawThread = new DrawThread(getContext(),getHolder(), variant, pointx, pointy, count);
            drawThread.start();
        }
        if (count == 2) {
            drawThread = new DrawThread(getContext(),getHolder(), variant, pointx, pointy, count);
            drawThread2 = new DrawThread(getContext(),getHolder(), variant, pointx, pointz, count);
            drawThread.start();
            drawThread2.start();


        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drawThread.setTowardPoint((int)event.getX(),(int)event.getY());

        return false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            drawThread.requestStop();
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    //
                }
            }
      if (count == 2){
          boolean retry2 = true;
          drawThread2.requestStop();
          while (retry2) {
              try {
                  drawThread2.join();
                  retry2 = false;
              } catch (InterruptedException e) {
                  //
              }
          }
      }

     }


}

class DrawThread extends Thread {
    public ArrayList<Integer> pointy;
    public ArrayList<String> pointx;
    public Integer variant, count;
    private SurfaceHolder surfaceHolder;
    private int towardPointX, towardPointY; // координаты касания
    private int touchval = 25; // расстояние от точки касания до ближайшей узловой
    volatile boolean running = true;//флаг для остановки потока

    public DrawThread(Context context, SurfaceHolder surfaceHolder, Integer variant, ArrayList<String> pointx, ArrayList<Integer> pointy, Integer count) {
        this.surfaceHolder = surfaceHolder;
        this.variant = variant;
        this.pointx = pointx;
        this.pointy = pointy;
        this.count = count;

    }

    public void requestStop() {
        running = false;
    }

    public void setTowardPoint(int x, int y) {
        this.towardPointX = x;
        this.towardPointY = y;
    }






    public synchronized void grafdraw(){
        Paint paint = new Paint(); //график
        Paint paint2 = new Paint(); // подписи узлов
        int min, max;
        // рабочий массив для нахождения минимального и максимального значения в pointy
        ArrayList<Integer> pointwork = new ArrayList<>();
        pointwork = (ArrayList) pointy.clone();
        Collections.sort(pointwork);
        min = pointwork.get(0);
        max = pointwork.get(pointwork.size()-1);
        if (variant == 1) {paint.setColor(Color.RED);}
        if (variant == 2) {paint.setColor(Color.GREEN);}
        if (variant == 3) {paint.setColor(Color.BLUE);}
        paint.setStrokeWidth(5);
        paint2.setStrokeWidth(1);
        paint2.setColor(Color.BLACK);
        paint2.setSubpixelText(true);
        paint2.setTextSize(25);
        set();

        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                if (count != 1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    canvas.drawColor(Color.WHITE);
                    for (int i = 0; i < pointy.size() - 1; i++) {
                        // рисуем график

                        canvas.drawLine(i * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2, (i + 1) * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight() - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2, paint);

                        //canvas.drawLine(i * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i), (i + 1) * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i + 1), paint);
                        //рисуем вертикальные линии
                        //canvas.drawLine(i * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i), i * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() - 60, paint2);
                        //canvas.drawLine((i + 1) * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i + 1), (i + 1) * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() - 60, paint2);

                        canvas.drawLine(i * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2, i * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() - 60, paint2);
                        canvas.drawLine((i + 1) * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2, (i + 1) * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() - 60, paint2);

                        // нарисуем узелки

                        //    canvas.drawCircle(i * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i), 10, paint2);
                        //    canvas.drawCircle((i + 1) * canvas.getWidth() / pointy.size() + 5, canvas.getHeight() / 2 - pointy.get(i + 1), 10, paint2);

                        canvas.drawCircle(i * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2, 10, paint2);
                        canvas.drawCircle((i + 1) * canvas.getWidth() / pointy.size() + 5, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2, 10, paint2);

                        canvas.drawText(pointy.get(i) + "", i * canvas.getWidth() / pointx.size() + 10, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2+30, paint2);
                        canvas.drawText(pointy.get(i + 1) + "", (i + 1) * canvas.getWidth() / pointx.size() + 10, (canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2+30, paint2);

                      //   canvas.drawText(pointy.get(i) + "", i * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() / 2 - pointy.get(i) - 30, paint2);
                      //  canvas.drawText(pointy.get(i + 1) + "", (i + 1) * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() / 2 - pointy.get(i + 1) - 30, paint2);

                        //подпишем ось х
                        canvas.rotate(60, i * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150);
                        canvas.drawText(pointx.get(i), i * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150, paint2);
                        canvas.rotate(-60, i * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150);
                        canvas.rotate(60, (i + 1) * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150);
                        canvas.drawText(pointx.get(i + 1), (i + 1) * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150, paint2);
                        canvas.rotate(-60, (i + 1) * canvas.getWidth() / pointx.size() + 5, canvas.getHeight() - 150);

                        // проверка касания радом с узловой точкой
                        if (Math.abs(Math.sqrt((i * canvas.getWidth() / pointy.size() + 5 - towardPointX) * (i * canvas.getWidth() / pointy.size() + 5 - towardPointX) + ((canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2 - towardPointY) * ((canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i)-min))/2 - towardPointY))) <= touchval) {
                            canvas.drawText(pointx.get(i) + " | " + pointy.get(i), towardPointX+15, towardPointY+15, paint2);
                        }
                        if (Math.abs(Math.sqrt(((i + 1) * canvas.getWidth() / pointy.size() + 5 - towardPointX) * ((i + 1) * canvas.getWidth() / pointy.size() + 5 - towardPointX) + ((canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2 - towardPointY) * ((canvas.getHeight()  - canvas.getHeight()/(max-min)* (pointy.get(i+1)-min))/2 - towardPointY))) <= touchval) {
                            canvas.drawText(pointx.get(i + 1) + " | " + pointy.get(i + 1), towardPointX+15, towardPointY+15, paint2);
                        }

                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                if (count != 1){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



        }
    }


    @Override
    public void run() {
      grafdraw();

    }
     void set() {}
}