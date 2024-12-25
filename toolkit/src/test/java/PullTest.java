import org.omni.toolkit.design.event.Event;
import org.omni.toolkit.design.event.EventListener;
import org.omni.toolkit.design.mq.consumer.PullConsumer;
import org.omni.toolkit.design.mq.consumer.consume.OrderConsume;
import org.omni.toolkit.design.mq.producer.PullProducer;
import org.omni.toolkit.design.mq.topic.Topic;
import org.omni.toolkit.design.mq.topic.pull.OrderPull;
import org.omni.toolkit.vir.Virs;

import java.util.ArrayList;

/**
 * @author Xieningjun
 * @date 2024/11/12 15:22
 * @description
 */
public class PullTest {

    // 没有sleep再多测一些，有序无序
    // todo 一定要简单topic, producer, consumer的初始化
    public static void main(String[] args) throws InterruptedException {
        var topic = new Topic<String>(12);
        var pull = new OrderPull<String>(true);
        topic.setPull(pull);
        var consumerList = new ArrayList<PullConsumer<String>>();
        var producerList = new ArrayList<PullProducer<String>>();
        for (int i = 0; i < 2; i++) {
            var producer = new PullProducer<String>();
            topic.subscribe(producer);
            producer.produce(() -> {
                System.out.println("生产者: " + producer);
                System.out.println("生产线程: " + Thread.currentThread());
                System.out.println("\n");
                return "Hello World";
            });
            producerList.add(producer);
        }
        for (int i = 0; i < 2; i++) {
            var consumer = new PullConsumer<String>();
            var listener = new EventListener<String>() {
                @Override
                public void listen(Event<String> event) {
                    System.out.println("消费者: " + consumer);
                    System.out.println("消费线程: " + Thread.currentThread());
                    System.out.println("监听器收到消息: " + event.getData());
                    System.out.println("\n");
                }
            };
            var consume = new OrderConsume<String>(true);
            consume.setListener(listener);
            consumer.setConsume(consume);
            topic.subscribe(consumer);
            consumerList.add(consumer);
        }
        Virs.sleep(100);
        for (var producer : producerList) {
            producer.produce(() -> {
                System.out.println("生产者: " + producer);
                System.out.println("生产线程: " + Thread.currentThread());
                System.out.println("\n");
                return "Hello World 2";
            });
            producer.produce(() -> {
                System.out.println("生产者: " + producer);
                System.out.println("生产线程: " + Thread.currentThread());
                System.out.println("\n");
                return "Hello World 3";
            });
        }
        Virs.sleep(5000);
        consumerList.forEach(PullConsumer::pull);
        consumerList.forEach(PullConsumer::pull);
        consumerList.forEach(PullConsumer::pull);
        consumerList.forEach(PullConsumer::pull);
        Virs.keepalive();
    }

}
