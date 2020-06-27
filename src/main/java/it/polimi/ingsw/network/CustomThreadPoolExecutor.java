package it.polimi.ingsw.network;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Custom implementation of a {@link ThreadPoolExecutor}. This version allows the server to instantiate an unlimited number of threads to handle
 * the communication channels with clients, without having the problem of automatic process killing due to idle threads.
 * The creation of a custom implementation is necessary because neither {@link java.util.concurrent.Executors#newFixedThreadPool(int)} nor
 * {@link Executors#newCachedThreadPool()} matches our needs, but we still want to have the abstraction benefits of using an {@link java.util.concurrent.ExecutorService}
 * instead of a raw Thread to enable concurrency.
 */
public final class CustomThreadPoolExecutor {

    private CustomThreadPoolExecutor() {
    }

    /**
     * new factory method to create an instance of our custom ThreadPoolExecutor.
     *
     * @return an instance of {@link ThreadPoolExecutor} with the features described in the general description of this class.
     */
    public static ThreadPoolExecutor createNew() {
        return new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                0L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>()
        );
    }
}
