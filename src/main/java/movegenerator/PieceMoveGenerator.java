package movegenerator;

import java.util.List;
import java.util.concurrent.Semaphore;

import model.Position;

public abstract class PieceMoveGenerator implements Runnable{
    private Position position;
    private int rank;
    private int file;
    private List<Position> resultList;
    private Semaphore sem;


    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return this.file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public List<Position> getResultList() {
        return this.resultList;
    }

    public void setResultList(List<Position> resultList) {
        this.resultList = resultList;
    }

    public Semaphore getSem() {
        return this.sem;
    }

    public void setSem(Semaphore sem) {
        this.sem = sem;
    }

    public PieceMoveGenerator(Position position, int rank, int file, List<Position> resultList, Semaphore sem) {
        this.position = position;
        this.rank = rank;
        this.file = file;
        this.resultList = resultList;
        this.sem = sem;
    }


    @Override
    public void run() {
        List<Position> results = computePieceMoves(position, rank, file);
        try {
            sem.acquire();
            resultList.addAll(results);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sem.release();
    }

    public List<Position> computePieceMoves(Position position, int rank, int file) {
        return null;
    }
}
