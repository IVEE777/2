package com.example.github2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    TextView mTextView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.TextView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);

                GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
                final retrofit2.Call<User> call =
                        gitHubService.getUser("alexanderklimov");
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();

                            mTextView.setText("Аккаунт Github:" + user.getName() +
                                    "\nСайт: " + user.getBlog() +
                                    "\nКомпания: " + user.getCompany());
                            mProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            int statusCode = response.code();
                            ResponseBody errorBody = response.errorBody();
                            try {
                                mTextView.setText(errorBody.string());
                                mProgressBar.setVisibility(View.INVISIBLE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {
                        mTextView.setText("Что-то пошло не так: " + throwable.getMessage());

                    }
                });
            }
        });
    }
}


