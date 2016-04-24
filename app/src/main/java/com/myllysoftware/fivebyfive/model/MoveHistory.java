package com.myllysoftware.fivebyfive.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class MoveHistory {
  private Stack<Integer> mMoveHistory = new Stack<>();

  public boolean contains(final int position) {
    return mMoveHistory.contains(position);
  }

  public void add(final int position) {
    mMoveHistory.push(position);
  }

  public int latestMove() {
    if (mMoveHistory.isEmpty()) {
      return 0;
    }
    return mMoveHistory.peek();
  }

  public List<Integer> get() {
    return new ArrayList<>(mMoveHistory);
  }

  public List<Integer> goBack() {
    if (!mMoveHistory.isEmpty()) {
      mMoveHistory.pop();
    }
    return new ArrayList<>(mMoveHistory);
  }
}
