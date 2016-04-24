package com.myllysoftware.fivebyfive.view;

import com.myllysoftware.fivebyfive.R;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
  private static List<Integer> sMoveHistory;
  private static List<Integer> sNextMoves;
  private static final int[] CELLS = new int[25];

  public static final int[] NUMS = new int[]{
      R.drawable.p1, R.drawable.p2, R.drawable.p3,
      R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7,
      R.drawable.p8, R.drawable.p9, R.drawable.p10, R.drawable.p11,
      R.drawable.p12, R.drawable.p13, R.drawable.p14, R.drawable.p15,
      R.drawable.p16, R.drawable.p17, R.drawable.p18, R.drawable.p19,
      R.drawable.p20, R.drawable.p21, R.drawable.p22, R.drawable.p23,
      R.drawable.p24, R.drawable.p25};

  public static final int[] DONES = new int[]{
      R.drawable.p1, R.drawable.p2, R.drawable.p3,
      R.drawable.p4, R.drawable.p5, R.drawable.p6done, R.drawable.p7done,
      R.drawable.p8done, R.drawable.p9done, R.drawable.p10done,
      R.drawable.p11done, R.drawable.p12done, R.drawable.p13done,
      R.drawable.p14done, R.drawable.p15done, R.drawable.p16done,
      R.drawable.p17done, R.drawable.p18done, R.drawable.p19done,
      R.drawable.p20done, R.drawable.p21done, R.drawable.p22done,
      R.drawable.p23done, R.drawable.p24done, R.drawable.p25done};

  public static void init() {
    clearBoard();
    sMoveHistory = new ArrayList<>();
    sNextMoves = new ArrayList<>();
  }

  private static void clearBoard() {
    for (int i = 0; i < 25; i++) {
      CELLS[i] = R.drawable.empty;
    }
  }

  public static int getImageResource(final int position) {
    return CELLS[position % 25];
  }

  public static void updateSelections(final List<Integer> selections) {
    sMoveHistory = selections;
    if (selections.isEmpty()) {
      clearBoard();
      return;
    }

    int i = 0;
    for (Integer selection : selections) {
      CELLS[selection] = NUMS[i++];
    }
  }

  public static int currentMove() {
    return sMoveHistory.size();
  }

  public static List<Integer> currentChoices() {
    return sNextMoves;
  }

  public static void updateChoices(final List<Integer> choices) {
    clearPreviousChoices();
    showCurrentChoices(choices);
  }

  public static boolean isValidSelection(final int position) {
    return sNextMoves.contains(position);
  }

  private static void showCurrentChoices(List<Integer> choices) {
    sNextMoves = choices;
    for (Integer choice : choices) {
      CELLS[choice] = R.drawable.choice;
    }
  }

  private static void clearPreviousChoices() {
    for (Integer choice : sNextMoves) {
      CELLS[choice] = R.drawable.empty;
    }
  }

  public static void setComplete() {
    CELLS[sMoveHistory.get(sMoveHistory.size() - 1)] = DONES[currentMove() - 1];
  }
}