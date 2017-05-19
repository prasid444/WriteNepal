package com.bct071.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


/**
 * Created by Prasidha Karki on 5/19/2016.
 */
public class SplashScreen extends Activity {

    private Boolean isFirstRun=null;

    ImageView imageView1,imageView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_splash);





        imageView1=(ImageView)findViewById(R.id.back_image);
        imageView2=(ImageView)findViewById(R.id.front_image);

        //to add animationas
        Animation animationstart= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
       //  Animation animationend=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_out);
        Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.abc_front_animation);
        imageView1.setAnimation(animationstart);
        imageView2.setAnimation(animation1);

        //for doing after animation

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                if(isFirstTime()){


                    //this will be used for first introduction  later

                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }
                else
                {

                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });



    }

    public boolean isFirstTime() {
        if (isFirstRun== null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            isFirstRun = mPreferences.getBoolean("firstTime", true);
            if (isFirstRun) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return isFirstRun;
    }
}


