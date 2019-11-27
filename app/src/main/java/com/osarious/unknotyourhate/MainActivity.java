package com.osarious.unknotyourhate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import pl.droidsonroids.gif.GifImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.RichPathAnimator;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View.OnTouchListener listener;
    View.OnTouchListener listener2;

    Handler handler = new Handler();
    Handler handler2 = new Handler();

    MediaPlayer peacemusic;
    RichPathView peaceImage;

    ImageView target;
    TextView targetText;
    Bitmap bitmap;
    ProgressBar progressBarText;
    ProgressBar health;
    Boolean isImage = null;
    TranslateAnimation kalashAnimation;
    Boolean pressed = false;
    GifImageView gifImageView;

    FrameLayout bloodClot;
    int rand;

    Button accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//actionBar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
//to hideinidializations
        {
            accept = findViewById(R.id.mercy);
            bloodClot = findViewById(R.id.targetFrame);
            peaceImage = findViewById(R.id.peaceImage);
            peacemusic = MediaPlayer.create(MainActivity.this, R.raw.peacemusic);
            target = findViewById(R.id.target);

        }
        //Detect initial time user came
        choiceDialog(null);


        //target initializations
        {
            progressBarText = findViewById(R.id.progressBarText);
            health = findViewById(R.id.progressBar);
            targetText = findViewById(R.id.targetText);
        }


        final ConstraintLayout mainScreen = findViewById(R.id.mainscreen);


        ImageView Kalashinkov = findViewById(R.id.ak47);
        MediaPlayer reload = MediaPlayer.create(this, R.raw.reload);
        reload.start();
        gifImageView = findViewById(R.id.explosion);
        ImageView bullet = findViewById(R.id.bullet);
        final MediaPlayer kalashSound = MediaPlayer.create(MainActivity.this, R.raw.kalashsound);
        MediaPlayer explosionSound = MediaPlayer.create(MainActivity.this, R.raw.explosionsound);


//initial reload animation
        kalashAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 6.0f);
        kalashAnimation.setDuration(500);  // kalashAnimation duration
        kalashAnimation.setRepeatCount(0);  // kalashAnimation repeat count

        Kalashinkov.startAnimation(kalashAnimation);


        kalashSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (pressed)
                    kalashSound.start();
            }
        });


        killMechanizm(kalashSound, explosionSound, bullet, Kalashinkov, reload);

        mainScreen.setOnTouchListener(listener);


    }

    private void killMechanizm(final MediaPlayer kalashSound, final MediaPlayer explosionSound, final ImageView bullet, final ImageView Kalashinkov, final MediaPlayer reload) {
        listener = new View.OnTouchListener() {


            TranslateAnimation bulletAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -500);


            int healthIncrement = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (peaceImage.getVisibility() == View.GONE) {
                    //int action = event.getAction();
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:



                            if (!kalashSound.isPlaying()) {

                                kalashSound.seekTo(0);
                                kalashSound.start();

                            }
                            kalashAnimation.setDuration(130);
                            kalashAnimation.setRepeatCount(Animation.INFINITE);
                            Kalashinkov.startAnimation(kalashAnimation);

                            bullet.setVisibility(View.VISIBLE);

                            bulletAnimation.setDuration(190);
                            bulletAnimation.setRepeatCount(Animation.INFINITE);


                            bullet.startAnimation(bulletAnimation);


                            pressed = true;
                            handler = new Handler();
                            Thread movment = new Thread(new Runnable(){
                                public void run(){


                                    if (isImage ? health.getProgress() == 0 : progressBarText.getProgress() == 0) {

                                        explosionSound.seekTo(1000);
                                        explosionSound.start();
                                        accept.setVisibility(View.GONE);
                                        gifImageView.setVisibility(View.VISIBLE);
                                        handler.removeCallbacksAndMessages(null);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                acceptanceCheck(accept);
                                            }
                                        }, 2000);

                                    } else if (pressed) {
                                        if (isImage)
                                            health.incrementProgressBy(-1);
                                        else
                                            progressBarText.incrementProgressBy(-1);

                                        if (isImage ? health.getProgress() == 0 : progressBarText.getProgress() == 0)
                                            handler.postDelayed(this, 0);
                                        else
                                            handler.postDelayed(this, 400);
                                    }

                                }});
                            movment.start();






                            break;

                        case MotionEvent.ACTION_MOVE:
                            //User is moving around on the screen
                            break;

                        case MotionEvent.ACTION_UP:
                            pressed = false;
                            kalashSound.pause();
                            reload.start();

                            kalashAnimation.setDuration(500);
                            kalashAnimation.setRepeatMode(1);
                            kalashAnimation.setRepeatCount(0);
                            Kalashinkov.startAnimation(kalashAnimation);

                            bulletAnimation.cancel();
                            bullet.setVisibility(View.GONE);

                            break;
                    }
                }
                return pressed;

            }

            ;


        };
    }

    public void choiceDialog(View view) {

        accept.setVisibility(View.VISIBLE);


        peaceImage.setVectorDrawable(R.drawable.small_icon);
        peaceImage.clearAnimation();
        peaceImage.refreshDrawableState();
        peaceImage.setVisibility(View.GONE);
        peaceImage.setRotation(0);

        handler2.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        peacemusic.stop();

        bloodClot.setVisibility(View.GONE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("What do you hate most ?");

        if(isImage==null) {//first time
            builder.setCancelable(false);

        }

        LinearLayout linearLayout = new LinearLayout(MainActivity.this);
        final EditText input = new EditText(MainActivity.this);
        input.setHint("Name of what you hate");

        linearLayout.addView(input);

        builder.setView(linearLayout);


        builder.setPositiveButton("Ok", null);

        builder.setNegativeButton("Add Image (Optional)", null);

        final AlertDialog dialog = builder.show();


        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);


            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please add the name", Toast.LENGTH_SHORT).show();


                } else {

                    gifImageView.setVisibility(View.GONE);// INCASE OF REPEATING THE PROCESS


                    targetText.setText(input.getText().toString());

                    if (bitmap==null) {//reveal as text
                        target.setVisibility(View.GONE);
                        health.setVisibility(View.GONE);

                        targetText.setVisibility(View.VISIBLE);
                        progressBarText.setVisibility(View.VISIBLE);
                        progressBarText.setProgress(100);
                        isImage = false;
                    } else {//reveal as image
                        target.setVisibility(View.VISIBLE);
                        health.setVisibility(View.VISIBLE);
                        health.setProgress(100);
                        targetText.setVisibility(View.GONE);
                        progressBarText.setVisibility(View.GONE);
bitmap=null;// to makesure it only detects the image reciving
                        isImage = true;
                    }
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Click Any Where To Shoot", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void supportDeveloper(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Support the developer for more weapons");

        builder.setPositiveButton("Rate app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                launchMarket();
            }
        });
        builder.show();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 1:
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
                case 2:
                    bitmap = (Bitmap) data.getExtras().get("data"); //get data and casts it into Bitmap photo
                    break;
            }

        target.setImageBitmap(bitmap);


    }

    private void acceptanceCheck(final Button view) {//comes after explosion

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(targetText.getText().toString() + " is deeply hurt what do you want to do ?");

        builder.setMessage("Remember... " + targetText.getText().toString() + " is just " + targetText.getText().toString());

        builder.setPositiveButton("Hate dissipated make peace", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                bloodClot.setVisibility(View.GONE);//if there is blood remove it
                gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
                accept(view);
            }
        });
        builder.setNegativeButton("Keep attacking", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                view.setVisibility(View.VISIBLE);//MERCY CAN HAPPEN
                bloodClot.setVisibility(View.VISIBLE);
                gifImageView.setVisibility(View.GONE);

                if (isImage)
                    health.setProgress(100);
                else
                    progressBarText.setProgress(100);

            }
        });
        builder.show();

    }

    public void mercyDialog(final View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("You sure you want mercy for " + targetText.getText().toString() + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                accept(view);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();

    }

    public void accept(View view) {
        peacemusic = MediaPlayer.create(MainActivity.this, R.raw.peacemusic);
        peacemusic.start();
        view.setVisibility(View.GONE);//HIDE THE BUTTON
bloodClot.setVisibility(View.GONE);
        peaceImage = findViewById(R.id.peaceImage);


        peaceImage.setAlpha(0f);
        peaceImage.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        peaceImage.animate()
                .alpha(1f)
                .setDuration(2000);

        peaceImage.animate().rotation(360f).setDuration(9600);


        final RichPath[] paths = peaceImage.findAllRichPaths();


        final ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getResources().getColor(R.color.c1));
        colors.add(getResources().getColor(R.color.c2));
        colors.add(getResources().getColor(R.color.c3));
        colors.add(getResources().getColor(R.color.c4));
        colors.add(getResources().getColor(R.color.c5));
        colors.add(getResources().getColor(R.color.c6));


        handler = new Handler();
        Runnable r = new Runnable() {

            public void run() {


                for (int i = 0; i < paths.length; i++) {

                    final int t = i;



                    handler2 = new Handler();

                    Runnable r2 = new Runnable() {
                        public void run() {
                            rand = (int) (Math.random() * colors.size() - 1) + 0;

                            RichPathAnimator
                                    .animate(paths[t]).
                                    duration(1000).
                                    fillColor(colors.get(rand))

                                    .start();

                            handler.postDelayed(this, 1000);
                        }

                    };

                    handler.postDelayed(r2, 0);


                    if (paths.length > 5)
                        colors.add(paths[t].getFillColor());

                }

            }
        };

        handler.postDelayed(r, 10000);


    }
}