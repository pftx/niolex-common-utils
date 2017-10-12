package org.apache.niolex.queue;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class DistributedBlockingQueueTest extends DistributedBlockingQueue<String> {

    @Test
    public void testPut() throws Exception {
        put("not ydfeet implemendted");
        assertEquals("not ydfeet implemendted", offerItem);
    }

    @Test
    public void testOffer() throws Exception {
        offer("nodt ydfeet implemendted", 100, TimeUnit.DAYS);
        assertEquals("nodt ydfeet implemendted", offerItem);
    }

    @Test
    public void testTake() throws Exception {
        offer("abc");
        blink = true;
        assertEquals("abc", take());
    }

    @Test
    public void testPoll() throws Exception {
        offer("abdc");
        blink = true;
        assertEquals("abdc", poll(30, TimeUnit.MICROSECONDS));
        assertEquals("abdc", poll(30, TimeUnit.MICROSECONDS));
    }

    @Test
    public void testRemainingCapacity() throws Exception {
        assertEquals(Integer.MAX_VALUE, remainingCapacity());
    }
    
    @Test
    public void testDrainTo1() {
        offer("nice");
        List<String> list = new ArrayList<String>();
        drainTo(list, 3);
        assertEquals(3, list.size());
    }
    
    @Test
    public void testDrainTo2() {
        offer("nice");
        List<String> list = new ArrayList<String>();
        drainTo(list);
        assertEquals(5, list.size());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testContains() throws Exception {
        contains("abc");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testIterator() throws Exception {
        iterator();
    }
    
    private String offerItem;
    private int size = 5;
    private boolean blink = false;

    /**
     * This is the override of super method.
     * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(String e) {
        offerItem = e;
        return true;
    }

    /**
     * This is the override of super method.
     * @see java.util.Queue#poll()
     */
    @Override
    public String poll() {
        if (blink) {
            blink = false;
            return null;
        }
        if (size-- > 0)
            return offerItem;
        else
            return null;
    }

    /**
     * This is the override of super method.
     * @see java.util.Queue#peek()
     */
    @Override
    public String peek() {
        return offerItem;
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.queue.DistributedBlockingQueue#watchQueue()
     */
    @Override
    protected void watchQueue() throws InterruptedException {
    }

    /**
     * This is the override of super method.
     * @see org.apache.niolex.queue.DistributedBlockingQueue#watchQueue(long, java.util.concurrent.TimeUnit)
     */
    @Override
    protected boolean watchQueue(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * This is the override of super method.
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return 0;
    }

}

