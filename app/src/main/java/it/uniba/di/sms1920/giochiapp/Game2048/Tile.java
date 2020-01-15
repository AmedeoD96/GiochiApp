package it.uniba.di.sms1920.giochiapp.Game2048;

class Tile extends Cell {
    private final int value;
    private Tile[] mergedFrom = null;

    //costruttore in base alla posizione
    Tile(int x, int y, int value) {
        super(x, y);
        this.value = value;
    }

    //costruttore in base alla cella
    Tile(Cell cell, int value) {
        super(cell.getX(), cell.getY());
        this.value = value;
    }

    //aggiorna la posizione
    void updatePosition(Cell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    int getValue() {
        return this.value;
    }

    //ritorna la Tile di provenienza
    Tile[] getMergedFrom() {
        return mergedFrom;
    }

    //setta la Tile di provenienza
    void setMergedFrom(Tile[] tile) {
        mergedFrom = tile;
    }
}
