package com.myllysoftware.fivebyfive.controller;

public interface GameControllerInterface {
  void onDestroy();

  void select(final int position);

  void goBack();

  void reset();
}
