package com.myllysoftware.fivebyfive.model;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class GameModel implements GameModelInterface {

  private MoveHistory mMoveHistory;
  private Subscriber<? super List<Integer>> mSelectionSubscriber;
  private Subscriber<? super List<Integer>> mNextMovesSubscriber;

  public GameModel() {
    reset();
  }

  @Override
  public Observable<List<Integer>> selection$() {
    return Observable.create(
        new Observable.OnSubscribe<List<Integer>>() {
          @Override
          public void call(Subscriber<? super List<Integer>> subscriber) {
            mSelectionSubscriber = subscriber;
          }
        });
  }

  @Override
  public Observable<List<Integer>> nextMoves$() {
    return Observable.create(
        new Observable.OnSubscribe<List<Integer>>() {
          @Override
          public void call(Subscriber<? super List<Integer>> subscriber) {
            mNextMovesSubscriber = subscriber;
          }
        });
  }

  @Override
  public void select(int position) {
    mMoveHistory.add(position);
    mNextMovesSubscriber.onNext(getChoices());
    mSelectionSubscriber.onNext(mMoveHistory.get());
    if (mMoveHistory.get().size() == 25 || getChoices().isEmpty()) {
      mSelectionSubscriber.onCompleted();
    }
  }

  @Override
  public void goBack() {
    mMoveHistory.goBack();
    mNextMovesSubscriber.onNext(getChoices());
    mSelectionSubscriber.onNext(mMoveHistory.get());
  }

  @Override
  public void reset() {
    mMoveHistory = new MoveHistory();
  }

  private boolean isSet(final int position) {
    return mMoveHistory.contains(position);
  }

  private List<Integer> getChoices() {
    if (mMoveHistory.get().size() == 0) {
      return new ArrayList<Integer>();
    }
    List<Integer> moveChoices = new ArrayList<>();
    int x = mMoveHistory.latestMove() % 5;
    int y = mMoveHistory.latestMove() / 5;
    int pos;

    if (x + 3 < 5) { // X to right
      pos = 5 * y + x + 3;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if (x - 3 >= 0) { // X to left
      pos = 5 * y + x - 3;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if (y + 3 < 5) { // Y to down
      pos = 5 * (y + 3) + x;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if (y - 3 >= 0) { // Y to up
      pos = 5 * (y - 3) + x;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if ((x + 2 < 5) && (y - 2 >= 0)) { // north-east
      pos = 5 * (y - 2) + x + 2;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if ((x + 2 < 5) && (y + 2 < 5)) { // south-east
      pos = 5 * (y + 2) + x + 2;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if ((x - 2 >= 0) && (y + 2 < 5)) { // south-west
      pos = 5 * (y + 2) + x - 2;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    if ((x - 2 >= 0) && (y - 2 >= 0)) { // north-west
      pos = 5 * (y - 2) + x - 2;
      if (!isSet(pos)) {
        moveChoices.add(pos);
      }
    }
    return moveChoices;
  }
}
