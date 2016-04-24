package com.myllysoftware.fivebyfive.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.myllysoftware.fivebyfive.R;
import com.myllysoftware.fivebyfive.controller.GameControllerInterface;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

import static java.lang.String.format;

public class GameView implements GameViewInterface {

  @Bind(R.id.gameBoard) GridView mNumberGrid;
  @Bind(R.id.timeField) TextView mTimeField;
  @Bind(R.id.scoreField) TextView mScoreField;
  @Bind(R.id.backButton) Button mBackButton;
  @Bind(R.id.newGameButton) Button mNewGameButton;

  private CellAdapter mCellAdapter;
  private final Activity mActivity;
  private GameControllerInterface mControllerInterface;

  private long mStartTime;

  private Handler mTimerHandler = new Handler();

  public GameView(final Activity activity) {
    activity.setContentView(R.layout.activity_main);
    ButterKnife.bind(this, activity);
    mActivity = activity;
    mBackButton.setVisibility(View.GONE);
    mCellAdapter = new CellAdapter();
    mNumberGrid.setAdapter(mCellAdapter);
    initOnClickListeners(activity);
    clearBoard();
  }

  public void setController(final GameControllerInterface controllerInterface) {
    mControllerInterface = controllerInterface;
  }

  @Override
  public void complete() {
    mTimerHandler.removeCallbacks(updateTimerThread);
    showSnackBar(mActivity.getString(R.string.game_over));
    GameBoard.setComplete();
    mBackButton.setVisibility(View.GONE);
    mCellAdapter.notifyDataSetChanged();
  }

  @Override
  public void update(List<Integer> selections) {
    GameBoard.updateSelections(selections);
    if (GameBoard.currentMove() == 0) {
      mBackButton.setVisibility(View.GONE);
    } else {
      mBackButton.setVisibility(View.VISIBLE);
    }
    mScoreField.setText((format(Locale.US, "%d", GameBoard.currentMove())));
    mCellAdapter.notifyDataSetChanged();
  }

  @Override
  public void nextMoves(List<Integer> nextMoves) {
    GameBoard.updateChoices(nextMoves);
    mCellAdapter.notifyDataSetChanged();
    animateChoices();
  }

  @Override
  public void onDestroy() {
    mTimerHandler.removeCallbacks(updateTimerThread);
  }

  private void initOnClickListeners(final Activity activity) {
    mBackButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mControllerInterface.goBack();
      }
    });

    mNewGameButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        clearBoard();
        mControllerInterface.reset();
        mTimerHandler.removeCallbacks(updateTimerThread);
      }
    });

    mNumberGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View v,
                              int position, long id) {
        if (GameBoard.currentMove() == 0) {
          startNewGame();
        } else if (!GameBoard.isValidSelection(position)) {
          showSnackBar(activity.getString(R.string.hint));
          return;
        }

        mControllerInterface.select(position);
        flipCell(v, 300, 360);
      }
    });
  }

  private void startNewGame() {
    showSnackBar(mActivity.getString(R.string.game_started));
    mBackButton.setVisibility(View.GONE);
    mControllerInterface.reset();
    mStartTime = SystemClock.uptimeMillis();
    mTimerHandler.removeCallbacks(updateTimerThread);
    mTimerHandler.postDelayed(updateTimerThread, 0);
  }

  private Runnable updateTimerThread = new Runnable() {

    public void run() {
      long timeInMilliseconds = SystemClock.uptimeMillis() - mStartTime;
      int secs = (int) (timeInMilliseconds / 1000);
      int mins = secs / 60;
      secs %= 60;
      mTimeField.setText(format(Locale.US, "%d:%s", mins,
          format(Locale.US, "%02d", secs)));
      mTimerHandler.postDelayed(this, 0);
    }
  };

  private void clearBoard() {
    GameBoard.init();
    mTimeField.setText(R.string.initial_time);
    mScoreField.setText(R.string.initial_score);
    showSnackBar(mActivity.getString(R.string.start_hint));
    mCellAdapter.notifyDataSetChanged();
  }

  private void showSnackBar(final String msg) {
    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)
        mActivity.findViewById(R.id
            .coordinatorLayout);
    assert (coordinatorLayout != null ? coordinatorLayout : null) != null;
    Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG)
        .setActionTextColor(Color.RED)
        .show();
  }

  private void flipChoice(final int pos) {
    RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
    anim.setInterpolator(new LinearInterpolator());
    anim.setDuration(100);
    View v = mNumberGrid.getTouchables().get(pos);
    v.startAnimation(anim);
  }

  private void flipCell(View v, int duration, int degree) {
    Rotate3dAnimation skew = new Rotate3dAnimation(0, degree, v.getWidth()
        / 2.0f, v.getHeight() / 2.0f);
    skew.setDuration(duration);
    skew.setInterpolator(new LinearInterpolator());
    v.startAnimation(skew);
  }

  private float getDisplayWidth() {
    Display display = mActivity.getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x;
  }

  private class CellAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    public CellAdapter() {
      mInflater = (LayoutInflater) mActivity
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
      return 25;
    }

    public Object getItem(int position) {
      return position;
    }

    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ImageView imageView;
      if (convertView == null) {
        float px = (float) Math.floor(getDisplayWidth() / 5);
        convertView = mInflater.inflate(R.layout.cell, null);
        imageView = (ImageView) convertView.findViewById(R.id.cover);
        imageView.setLayoutParams(new GridView.LayoutParams((int) px, (int) px));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setBackgroundColor(0);
        imageView.setBackgroundColor(Color.BLUE);
      } else {
        imageView = (ImageView) convertView;
      }
      imageView.setImageResource(GameBoard.getImageResource(position));
      return imageView;
    }
  }

  private void animateChoices() {
    for (Integer choice : GameBoard.currentChoices()) {
      flipChoice(choice);
    }
  }
}
