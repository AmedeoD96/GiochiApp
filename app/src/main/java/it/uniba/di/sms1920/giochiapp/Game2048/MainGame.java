package it.uniba.di.sms1920.giochiapp.Game2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.preference.PreferenceManager.*;

public class MainGame {
    static final int SPAWN_ANIMATION = -1;
    static final int MOVE_ANIMATION = 0;
    static final int MERGE_ANIMATION = 1;

    static final int FADE_GLOBAL_ANIMATION = 0;
    private static final long MOVE_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
    private static final long SPAWN_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME;
    private static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;
    private static final long NOTIFICATION_ANIMATION_TIME = MainView.BASE_ANIMATION_TIME * 5;
    private static final int startingMaxValue = 2048;
    private static final int GAME_WIN = 1;
    private static final int GAME_LOST = -1;
    private static final int GAME_NORMAL = 0;
    int gameState = GAME_NORMAL;
    int lastGameState = GAME_NORMAL;
    private int bufferGameState = GAME_NORMAL;
    private static final int GAME_ENDLESS = 2;
    private static final int GAME_ENDLESS_WON = 3;
    private static final String HIGH_SCORE = "high score";
    private static final String FIRST_RUN = "first run";
    private static int endingMaxValue;
    final int numSquaresX = 4;
    final int numSquaresY = 4;
    private final Context mContext;
    private final MainView mView;
    Grid grid = null;
    AnimationGrid aGrid;
    boolean canUndo;
    public long score = 0;
    long highScore = 0;
    long lastScore = 0;
    private long bufferScore = 0;

    //vengono settati il contesto, la view e il valore massimo che decreti la fine del gioco
    MainGame(Context context, MainView view) {
        mContext = context;
        mView = view;
        endingMaxValue = (int) Math.pow(2, view.numCellTypes - 1);
    }

    void newGame() {
        if (grid == null) {
            //crea la Grid
            grid = new Grid(numSquaresX, numSquaresY);
        } else {
            //prepara e salva lo stato di Undo
            prepareUndoState();
            saveUndoState();
            grid.clearGrid();
        }
        //crea la griglia di animazioni e ottiene l'hoghscore
        aGrid = new AnimationGrid(numSquaresX, numSquaresY);
        highScore = getHighScore();
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
        score = 0;
        gameState = GAME_NORMAL;
        //vengono aggiunte le Tile di partenza in punti randomici dello schermo
        addStartTiles();
        //Se ci trovassimo nel primo utilizzo si mostrerebbe l'aiuto
        mView.showHelp = firstRun();
        mView.refreshLastTime = true;
        //viene sincronizzato il tempo
        mView.resyncTime();
        mView.invalidate();
    }

    //vengono aggiunti 2 blocchi iniziali in posti randomici
    private void addStartTiles() {
        int startTiles = 2;
        for (int xx = 0; xx < startTiles; xx++) {
            this.addRandomTile();
        }
    }

    //aggiunge un blocco in una casella disponibile randomica
    private void addRandomTile() {
        if (grid.isCellsAvailable()) {
            //si ottiene il valore da porre randomicamente tra 2 e 4e
            int value = Math.random() < 0.9 ? 2 : 4;
            Tile tile = new Tile(grid.randomAvailableCell(), value);
            //si pone il blocco sullo schermo
            spawnTile(tile);
        }
    }

    //inserisce la casella nello schermo e mostra l'animazione di ingresso nello schermo
    private void spawnTile(Tile tile) {
        grid.insertTile(tile);
        aGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION,
                //se la direzione fosse -1 ci sarebbe l'espansione
                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);
    }

    //salva l'highscore attraverso l'utilizzo di sharedPreferences
    private void recordHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(HIGH_SCORE, highScore);
        editor.apply();
    }

    private long getHighScore() {
        SharedPreferences settings = getDefaultSharedPreferences(mContext);
        return settings.getLong(HIGH_SCORE, -1);
    }

    //in caso di prima partita vengono importati dei settings particolari
    private boolean firstRun() {
        SharedPreferences settings = getDefaultSharedPreferences(mContext);
        if (settings.getBoolean(FIRST_RUN, true)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST_RUN, false);
            editor.apply();
            return true;
        }
        return false;
    }

    //le tiles vengono preparate per un eventuale swipe (e conseguente merge) da parte dell'utente
    private void prepareTiles() {
        for (Tile[] array : grid.field) {
            for (Tile tile : array) {
                if (grid.isCellOccupied(tile)) {
                    tile.setMergedFrom(null);
                }
            }
        }
    }

    //si annulla la tile di destinazione, viene aggiornata la cell relativa
    //infine aggiorna la posizione della Tiel
    private void moveTile(Tile tile, Cell cell) {
        grid.field[tile.getX()][tile.getY()] = null;
        grid.field[cell.getX()][cell.getY()] = tile;
        tile.updatePosition(cell);
    }

    //salva lo stato prima dell'ultimo swipe
    //salva la grid e i punteggi
    private void saveUndoState() {
        grid.saveTiles();
        canUndo = true;
        lastScore = bufferScore;
        lastGameState = bufferGameState;
    }

    //prepara i valori dell'Undo prima del salvataggio
    private void prepareUndoState() {
        grid.prepareSaveTiles();
        bufferScore = score;
        bufferGameState = gameState;
    }

    //annulla l'ultimo cambiamento di stato della grid Undo
    void revertUndoState() {
        if (canUndo) {
            //vengono ripristinati i valori precedenti
            canUndo = false;
            aGrid.cancelAnimations();
            grid.revertTiles();
            score = lastScore;
            gameState = lastGameState;
            mView.refreshLastTime = true;
            mView.invalidate();
        }
    }

    //se lo stato è un numero dispari
    boolean gameWon() {
        return (gameState > 0 && gameState % 2 != 0);
    }

    //è impostato lo stato di game over
    boolean gameLost() {
        return (gameState == GAME_LOST);
    }

    //si controlla che il gamestate sia attivo
    boolean isActive() {
        return !(gameWon() || gameLost());
    }

    void move(int direction) {
        aGrid.cancelAnimations();
        // 0: su, 1: destra, 2: giù, 3:sinistra
        if (!isActive()) {
            //se l'utente non fosse attivo non potrebbe muoversi
            return;
        }
        //si prepara la possibilità di pressione del pulsante Undo
        prepareUndoState();
        Cell vector = getVector(direction);
        //si ottengono gli elementi sulle assi
        List<Integer> traversalsX = buildTraversalsX(vector);
        List<Integer> traversalsY = buildTraversalsY(vector);
        boolean moved = false;

        prepareTiles();

        for (int xx : traversalsX) {
            for (int yy : traversalsY) {
                Cell cell = new Cell(xx, yy);
                Tile tile = grid.getCellContent(cell);

                if (tile != null) {
                    //si ottiene l'elenco di tutte le celle vuote nel lato di vector a partire da cell
                    //si ottiene la cella piena più vicina e la cella più vicina in generale
                    Cell[] positions = findFarthestPosition(cell, vector);
                    //si salva la cella più vicina
                    Tile next = grid.getCellContent(positions[1]);

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        //se il prossimo non fosse vuoto e avessero lo stesso valore inizierebbe il procesos di merge
                        Tile merged = new Tile(positions[1], tile.getValue() * 2);
                        Tile[] temp = {tile, next};
                        merged.setMergedFrom(temp);

                        //si aggiorna la griglia dopo il merge
                        grid.insertTile(merged);
                        grid.removeTile(tile);

                        // quando le posizioni di due tiles convergono
                        tile.updatePosition(positions[1]);

                        //vengono lanciate le animazioni di merge sia in caso di merge orizzontale che verticale
                        int[] extras = {xx, yy};
                        aGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION,
                                MOVE_ANIMATION_TIME, 0, extras);
                        //con la direzione posta = 0 il movimento sarebbe unito
                        aGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION,
                                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

                        // aggiorna il punteggio
                        score = score + merged.getValue();
                        highScore = Math.max(score, highScore);

                        // La schermata di game win
                        if (merged.getValue() >= winValue() && !gameWon()) {
                            gameState = gameState + GAME_WIN;
                            //win state attivato
                            endGame();
                        }
                    } else {
                        //aòltrimenti verrebbe posto accanto e lanciando l'animazione di movimento
                        moveTile(tile, positions[0]);
                        int[] extras = {xx, yy, 0};
                        aGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras); //Direction: 1 = MOVING NO MERGE
                    }
                    //se il movimento fosse effettuato
                    if (!positionsEqual(cell, tile)) {
                        moved = true;
                    }
                }
            }
        }

        if (moved) {
            //e fosse mosso si salvano i valori e si controlla se si possa andare avanti
            saveUndoState();
            addRandomTile();
            checkLose();
        }
        //si aggiornano i dati relativi ai tempi
        mView.resyncTime();
        mView.invalidate();
    }

    private void checkLose() {
        if (!movesAvailable() && !gameWon()) {
            //se fosse possibile muoversi
            gameState = GAME_LOST;
            endGame();
        }
    }

    private void endGame() {
        //in caso di sconfitta si attiverebbe l'animazione e si aggiornerebbero i dati sullo score
        aGrid.startAnimation(AnimationGrid.GLOBAL_ANIMATION_INDEX, AnimationGrid.GLOBAL_ANIMATION_INDEX, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null);
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
    }

    //si ottiene il vettore direzionale
    private Cell getVector(int direction) {
        Cell[] map = {
                new Cell(0, -1), // up
                new Cell(1, 0),  // right
                new Cell(0, 1),  // down
                new Cell(-1, 0)  // left
        };
        return map[direction];
    }

    //crea la lista di interi per i valori orizzontali
    private List<Integer> buildTraversalsX(Cell vector) {
        List<Integer> traversals = new ArrayList<>();

        for (int xx = 0; xx < numSquaresX; xx++) {
            traversals.add(xx);
        }
        if (vector.getX() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    //crea la lista di interi per i valori verticali
    private List<Integer> buildTraversalsY(Cell vector) {
        List<Integer> traversals = new ArrayList<>();

        for (int xx = 0; xx < numSquaresY; xx++) {
            traversals.add(xx);
        }
        if (vector.getY() == 1) {
            Collections.reverse(traversals);
        }

        return traversals;
    }

    //trova la cella vuota più vicina
    private Cell[] findFarthestPosition(Cell cell, Cell vector) {
        Cell previous;
        Cell nextCell = new Cell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new Cell(previous.getX() + vector.getX(),
                    previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        return new Cell[]{previous, nextCell};
    }

    //controlla se ci siano mosse disponibili
    private boolean movesAvailable() {
        return grid.isCellsAvailable() || tileMatchesAvailable();
    }

    //confronta se i valori di 2 tiles siano uguali
    private boolean tileMatchesAvailable() {
        Tile tile;
            //per ogni casella
        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                //si ottiene il cotnenuto
                tile = grid.getCellContent(new Cell(xx, yy));

                if (tile != null) {
                    //se la casella è piena
                    for (int direction = 0; direction < 4; direction++) {
                        //si ottengono le celle in linea
                        Cell vector = getVector(direction);
                        Cell cell = new Cell(xx + vector.getX(), yy + vector.getY());

                        Tile other = grid.getCellContent(cell);

                        //vero confronto
                        if (other != null && other.getValue() == tile.getValue()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    //confronta le coordinate
    private boolean positionsEqual(Cell first, Cell second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }

    private int winValue() {
        if (!canContinue()) {
            return endingMaxValue;
        } else {
            return startingMaxValue;
        }
    }

    //viene cambiato il gamestate
    void setEndlessMode() {
        gameState = GAME_ENDLESS;
        mView.invalidate();
        mView.refreshLastTime = true;
    }

    boolean canContinue() {
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON);
    }
}
