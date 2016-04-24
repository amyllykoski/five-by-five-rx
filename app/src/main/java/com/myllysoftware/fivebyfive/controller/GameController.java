package com.myllysoftware.fivebyfive.controller;

import android.util.Log;

import com.myllysoftware.fivebyfive.model.GameModelInterface;
import com.myllysoftware.fivebyfive.view.GameViewInterface;

import java.util.List;

import rx.Subscriber;

public class GameController implements GameControllerInterface {
  private static final String TAG = GameController.class.getSimpleName();
  private GameModelInterface mGameModel = null;
  private Subscriber<List<Integer>> mSelectionSubscriber;
  private Subscriber<List<Integer>> mNextMovesSubscriber;

  public GameController(final GameModelInterface gameModel,
                        final GameViewInterface gameView) {
    mGameModel = gameModel;
    setUpObservers(gameModel, gameView);
  }

  private void setUpObservers(final GameModelInterface gameModel,
                              final GameViewInterface gameView) {
    mSelectionSubscriber = new Subscriber<List<Integer>>() {

      @Override
      public void onCompleted() {
        Log.d(TAG, "Complete.");
        gameView.complete();
      }

      @Override
      public void onError(Throwable error) {
        Log.d(TAG, error.getLocalizedMessage());
      }

      @Override
      public void onNext(List<Integer> integers) {
        Log.d(TAG, "Selections=" + integers.toString());
        gameView.update(integers);
      }
    };

    mNextMovesSubscriber = new Subscriber<List<Integer>>() {
      @Override
      public void onCompleted() {
        Log.d(TAG, "Complete.");
      }

      @Override
      public void onError(Throwable error) {
        Log.d(TAG, error.getLocalizedMessage());
      }

      @Override
      public void onNext(List<Integer> integers) {
        Log.d(TAG, "NextMoves=" + integers.toString());
        gameView.nextMoves(integers);
      }
    };

    gameModel.selection$().subscribe(mSelectionSubscriber);
    gameModel.nextMoves$().subscribe(mNextMovesSubscriber);
  }

  @Override
  public void onDestroy() {
    mNextMovesSubscriber.unsubscribe();
    mSelectionSubscriber.unsubscribe();
  }

  @Override
  public void select(int position) {
    mGameModel.select(position);
  }

  @Override
  public void goBack() {
    mGameModel.goBack();
  }

  @Override
  public void reset() {
    mGameModel.reset();
    mGameModel.selection$().subscribe(mSelectionSubscriber);
    mGameModel.nextMoves$().subscribe(mNextMovesSubscriber);
  }
}
