package it.uniba.di.sms1920.giochiapp.Game2048;

import java.util.ArrayList;

class Grid {

    final Tile[][] field;
    final Tile[][] undoField;
    private final Tile[][] bufferField;


    Grid(int sizeX, int sizeY) {
        //si ottengono le posizioni e le misure per la griglia e i pulsanti presenti sullo schermo
        field = new Tile[sizeX][sizeY];
        undoField = new Tile[sizeX][sizeY];
        bufferField = new Tile[sizeX][sizeY];
        //si eliminano eventuali valori o oggetti presenti nella griglia
        clearGrid();
        clearUndoGrid();
    }

    //si ottiene una cella libera random tra tutte quelle presenti
    Cell randomAvailableCell() {
        ArrayList<Cell> availableCells = getAvailableCells();
        if (availableCells.size() >= 1) {
            return availableCells.get((int) Math.floor(Math.random() * availableCells.size()));
        }
        return null;
    }

    //si ottiene un ArrayList di tutte le Celle libere presenti sulla griglia
    private ArrayList<Cell> getAvailableCells() {
        ArrayList<Cell> availableCells = new ArrayList<>();
        //si scorre tutta la schermata
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    availableCells.add(new Cell(xx, yy));
                }
            }
        }
        return availableCells;
    }

    boolean isCellsAvailable() {
        return (getAvailableCells().size() >= 1);
    }

    boolean isCellAvailable(Cell cell) {
        return !isCellOccupied(cell);
    }

    boolean isCellOccupied(Cell cell) {
        return (getCellContent(cell) != null);
    }

    //se la cella non fosse vuota
    Tile getCellContent(Cell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    Tile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }

    //controlla se le celle rientrino effettivamente nella schermata
    //si attua sia ricevendo una cella
    boolean isCellWithinBounds(Cell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }

    //sia ricevendo le coordinate
    private boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }

    //inserimento della Tile nella posizione prevista
    void insertTile(Tile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    //rimozione della Tile nella posizione prevista
    void removeTile(Tile tile) {
        field[tile.getX()][tile.getY()] = null;
    }

    //per ogni elemento nel field
    void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                //se gli elementi nel field fossero nulli
                if (bufferField[xx][yy] == null) {
                    //verrebbero mantenuti come tali
                    undoField[xx][yy] = null;
                } else {
                    //altrimenti verbbero salvati
                    undoField[xx][yy] = new Tile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }

    //controlla e modifica eventualmente il bufferField
    //per effettuare dopo un salvataggio
    void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new Tile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    //riporta la schermata alla situazione precedente riprendendo i valori contenuti in undoField
    void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new Tile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }

    //ogni elemento della Grid viene posto uguale a null
    void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    //ogni elemento della griglia precedente viene posto uguale a null
    private void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }
}
