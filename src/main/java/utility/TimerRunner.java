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
        boolean stopper = true;
        while (stopper){
            try {
                Thread.sleep(1000000);
                long nsLeft = nanoSecondsLeft();
                // if (nsLeft % 1000 == 0){
                    System.out.println(nsLeft/1000000000 + " seconds left");
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopper = isTimeLeft();
            if (Conductor.stopCalculating){
                stopper = false;
            }
        }
        // if (!Conductor.stopCalculating){
            Conductor.stop();
        // }
    }
}
