package webmonopoly.backend.controller;

public class PlayerStateCycleLocker {

    boolean changed = false;

    public synchronized void block() throws InterruptedException {
        while (!changed) {
            wait();
        }
        changed = false;
    }

    public synchronized void release() {
        changed = true;
        notifyAll();
    }
}
