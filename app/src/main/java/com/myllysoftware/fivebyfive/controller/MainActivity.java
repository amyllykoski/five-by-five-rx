package com.myllysoftware.fivebyfive.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myllysoftware.fivebyfive.model.GameModel;
import com.myllysoftware.fivebyfive.view.GameView;
import com.myllysoftware.fivebyfive.view.GameViewInterface;

public class MainActivity extends AppCompatActivity {

  private GameControllerInterface mGameController;
  private GameViewInterface mGameView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mGameView = new GameView(this);
    mGameController = new GameController(new GameModel(), mGameView);
    mGameView.setController(mGameController);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mGameController.onDestroy();
    mGameView.onDestroy();
  }
}
