package twisk.outils;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class ThreadsManager
{
    private ArrayList<Thread> listeThreads;
    private static ThreadsManager tm = new ThreadsManager();

    /**
     * Constructeur
     */
    private ThreadsManager()
    {
        listeThreads = new ArrayList<>();
    }

    public static ThreadsManager getInstance() {
        return tm;
    }

    /**
     * lancer
     * @param task
     */
    public void lancer(Task task)
    {
        Thread t = new Thread(task);
        listeThreads.add(t);
        t.start();
    }

    /**
     * tout detruire
     */
    public void detruireTout()
    {
        for (Thread t : listeThreads){
            t.interrupt();
        }
    }
}
