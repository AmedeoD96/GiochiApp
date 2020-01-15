package it.uniba.di.sms1920.giochiapp.Game2048;

import java.util.ArrayList;

class AnimationGrid {
    final ArrayList<AnimationCell> globalAnimation = new ArrayList<>();
    private final ArrayList[][] field;
    private int activeAnimations = 0;
    private boolean oneMoreFrame = false;

    AnimationGrid(int x, int y) {
        field = new ArrayList[x][y];

        //viene creata un'ArrayList per ogni elemento in field
        for (int xx = 0; xx < x; xx++) {
            for (int yy = 0; yy < y; yy++) {
                field[xx][yy] = new ArrayList<>();
            }
        }
    }

    void startAnimation(int x, int y, int animationType, long length, long delay, int[] extras) {
        AnimationCell animationToAdd = new AnimationCell(x, y, animationType, length, delay, extras);
        if (x == -1 && y == -1) {
            //ricevendo -1, -1 si aggiunge l'animazione globale
            globalAnimation.add(animationToAdd);
        } else {
            //ricevendo la posizione della griglia viene aggiunta l'animazione
            field[x][y].add(animationToAdd);
        }
        activeAnimations = activeAnimations + 1;
    }


    void tickAll(long timeElapsed) {
        //ciclo su animazioni di celle globali
        ArrayList<AnimationCell> cancelledAnimations = new ArrayList<>();
        for (AnimationCell animation : globalAnimation) {
            //aggiorna i tempi delle animazioni
            animation.tick(timeElapsed);
            if (animation.animationDone()) {
                //se l'animazione fosse stata eseguita allora sarebbe aggiunta alle animazioni da cancellare
                cancelledAnimations.add(animation);
                activeAnimations = activeAnimations - 1;
            }
        }

        for (ArrayList[] array : field) {
            //per ogni lista di animazioni
            for (ArrayList<AnimationCell> list : array) {
                //per ogni animazione
                for (AnimationCell animation : list) {
                    animation.tick(timeElapsed);
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation);
                        activeAnimations = activeAnimations - 1;
                    }
                }
            }
        }
        //le animazioni vengono effettivamente cancellati
        for (AnimationCell animation : cancelledAnimations) {
            cancelAnimation(animation);
        }
    }

    boolean isAnimationActive() {
        if (activeAnimations != 0) {
            //se l'animazione fosse attiva
            oneMoreFrame = true;
            return true;
        } else if (oneMoreFrame) {
            //se l'animazione non fosse attiva ma OneMoreFrame fosse ancora a true
            oneMoreFrame = false;
            return true;
        } else {
            return false;
        }
    }

    //si ottiene l'animazione presente in una cella
    ArrayList getAnimationCell(int x, int y) {
        return field[x][y];
    }

    //per ogni animazione
    void cancelAnimations() {
        for (ArrayList[] array : field) {
            for (ArrayList list : array) {
                //vengono rimossi gli elementi dagli arrayList
                list.clear();
            }
        }
        globalAnimation.clear();
        activeAnimations = 0;
    }

    //cancella la singola animazione
    //sia globale che animazione singola
    private void cancelAnimation(AnimationCell animation) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation);
        } else {
            field[animation.getX()][animation.getY()].remove(animation);
        }
    }
}
