package com.app.uas_pb_quran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.app.uas_pb_quran.Model.AudioModel.Audio;
import com.app.uas_pb_quran.Model.AudioModel.AudioFilesItem;
import com.app.uas_pb_quran.Model.SurahModel.Chapters;
import com.app.uas_pb_quran.Model.SurahModel.ChaptersItem;
import com.app.uas_pb_quran.Retrofit.ApiBase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<AudioFilesItem> audio = new ArrayList<>();
    private List<ChaptersItem> surah = new ArrayList<>();

    List<ChaptersItem> listSurah;
    List<AudioFilesItem> listAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataFromApi();
        setUpView();
        setUpRecyclerView();
    }

    private void getDataFromApi() {
        ApiBase.endpoint().getSurah().enqueue(new Callback<Chapters>() {
            @Override
            public void onResponse(@NonNull Call<Chapters> call, @NonNull Response<Chapters> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    MainActivity.this.listSurah = response.body().getChapters();
                    getDataFromApiAudio();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Chapters> call, @NonNull Throwable t) {
                Log.d("ErrorMain", t.toString());
            }
        });
    }
    private void getDataFromApiAudio() {
        ApiBase.endpoint().getAudio().enqueue(new Callback<Audio>() {
            @Override
            public void onResponse(@NonNull Call<Audio> call, @NonNull Response<Audio> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    MainActivity.this.listAudio = response.body().getAudioFiles();
                    mainAdapter.setData(listSurah, listAudio);
                }
            }
            @Override
            public void onFailure(@NonNull Call<Audio> call, @NonNull Throwable t) {

            }
        });
    }
    private void setUpRecyclerView() {

        mainAdapter = new MainAdapter(surah, audio);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);
    }

    private void setUpView() {
        recyclerView = findViewById(R.id.rvSurah);
    }
}