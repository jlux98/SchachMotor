package utility;

import application.Conductor;

public class TimerRunner implements Runnable {
    
    long stopTime;
    int secondsToCompute;
    

    private long nanoSecondsLeft(){
        return stopTime - System.nanoTime(); 
    }

    private boolean isTimeLeft() {
        if (System.nanoTime() >= stopTime - 1 * TimeUtility.SECOND_TO_NANO) {
            return false;
        }
        return true;
    }

    public TimerRunner(int secondsToCompute){
        this.secondsToCompute = secondsToCompute;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        stopTime = start + secondsToCompute * TimeUtility.SECOND_TO_NANO;
        while (isTimeLeft() && !Conductor.stopCalculating){
            try {
                Thread.sleep(1000);
                System.out.println(nanoSecondsLeft()/1000000000 + " seconds left");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Conductor.stop();
    }
}
