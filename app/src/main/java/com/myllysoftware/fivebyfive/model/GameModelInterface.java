package com.myllysoftware.fivebyfive.model;

import java.util.List;

import rx.Observable;

public interface GameModelInterface {
  Observable<List<Integer>> selection$();

  Observable<List<Integer>> nextMoves$();

  void select(final int position);

  void goBack();

  void reset();
}
