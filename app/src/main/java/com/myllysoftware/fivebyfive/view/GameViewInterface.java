package com.myllysoftware.fivebyfive.view;

import com.myllysoftware.fivebyfive.controller.GameControllerInterface;

import java.util.List;

public interface GameViewInterface {
  void update(List<Integer> selections);

  void nextMoves(List<Integer> nextMoves);

  void setController(final GameControllerInterface gameControllerInterface);

  void complete();

  void onDestroy();
}
