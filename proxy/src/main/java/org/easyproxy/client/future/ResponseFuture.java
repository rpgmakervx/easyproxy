package org.easyproxy.client.future;


import io.netty.handler.codec.http.HttpResponse;

import java.util.concurrent.*;

/**
 * Description :
 * Created by xingtianyu on 16-12-11
 * 上午2:15
 */

public class ResponseFuture<T extends HttpResponse> implements Future<T> {

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile T response;


    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return response != null;
    }

    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        return response;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
    }
    public void set(T response) {
        this.response = response;
        latch.countDown();
    }
}
