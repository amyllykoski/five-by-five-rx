
# Five-by-five Android Game

The objective of the game is to fill the 5 by 5 grid with numbers from 1 to 25. You can start from any cell, and there are two rules each move
has to follow:

- if moving to north, east, south or west, the target cell has to be empty and behind two cells (empty or non-empty),
- if moving north-east, south-east, south-west or north-west, the target cell has to be empty and behind one cell.

The app gives hints about the valid next moves showing question marks on valid target cells.

When there's no choices for next move (all potential target cells are filled), the game is over.

## Motivation

This is a somewhat standard Android game with a GUI containing some TextViews, Buttons and a GridView. The structure aims at following a traditional
Model-View-Controller (MVC). The game logic has bee built into a GameModel (in model package). Controller orchestrates communication between the Model and
View. View handles user inputs and updates itself based on Model events.

The model publishes two Observables (from RxJava) to let interested parties subscribe to their events. One of the Observables delivers the latest list of
moves as they happen in the UI letting UI update itself accordingly. The other Observable offers a list of choices for the next move. Controller sits
in the middle of the View and Model in order to de-couple them from each other.

With the RxJava approach it is also possible to implement "time travelling" which is done with a back key. By pushing the back button user can revert
moves an go to previous state.

## Findings

TBD



