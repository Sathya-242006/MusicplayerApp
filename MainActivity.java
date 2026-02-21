package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnPrev, btnPlay, btnNext;
    TextView txtSongName, txtCurrentTime, txtTotalTime;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;

    int[] songs = {R.raw.music, R.raw.music1, R.raw.music2};
    String[] songNames = {"Kaun Tujhe", "Jab Tak", "Besabriyaan"};

    int currentSongIndex = 0;

    boolean isPlaying = false;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrev = findViewById(R.id.btnPrev);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);

        txtSongName = findViewById(R.id.txtSongName);
        txtCurrentTime = findViewById(R.id.txtCurrentTime);
        txtTotalTime = findViewById(R.id.txtTotalTime);

        seekBar = findViewById(R.id.seekBar);

        btnPlay.setOnClickListener(v -> {

            if (!isPlaying) {

                playSong(currentSongIndex);
                btnPlay.setText("II");
                isPlaying = true;

            }
            else {

                mediaPlayer.pause();
                btnPlay.setText("▶");
                isPlaying = false;
            }
        });


        btnNext.setOnClickListener(v -> {

            currentSongIndex++;

            if (currentSongIndex >= songs.length)
                currentSongIndex = 0;

            playSong(currentSongIndex);
            btnPlay.setText("II");
            isPlaying = true;
        });


        btnPrev.setOnClickListener(v -> {

            currentSongIndex--;

            if (currentSongIndex < 0)
                currentSongIndex = songs.length - 1;

            playSong(currentSongIndex);
            btnPlay.setText("II");
            isPlaying = true;
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }


    void playSong(int index) {

        if (mediaPlayer != null)
            mediaPlayer.release();

        mediaPlayer = MediaPlayer.create(this, songs[index]);
        mediaPlayer.start();

        txtSongName.setText(songNames[index]);

        seekBar.setMax(mediaPlayer.getDuration());

        txtTotalTime.setText(formatTime(mediaPlayer.getDuration()));

        updateSeekBar();
    }


    void updateSeekBar() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mediaPlayer != null) {

                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    txtCurrentTime.setText(
                            formatTime(mediaPlayer.getCurrentPosition()));

                    handler.postDelayed(this, 500);
                }
            }
        }, 0);
    }


    String formatTime(int milliseconds) {

        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return minutes + ":" + String.format("%02d", seconds);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mediaPlayer != null)
            mediaPlayer.release();
    }

}
