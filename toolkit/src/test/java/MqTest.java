import org.omni.toolkit.design.mq.consumer.PushConsumer;
import org.omni.toolkit.design.mq.consumer.consume.OrderConsume;
import org.omni.toolkit.design.mq.event.Event;
import org.omni.toolkit.design.mq.event.EventListener;
import org.omni.toolkit.design.mq.producer.Producer;
import org.omni.toolkit.design.mq.producer.PushProducer;
import org.omni.toolkit.design.mq.topic.PushTopic;
import org.omni.toolkit.design.mq.topic.push.OrderPush;
import org.omni.toolkit.vir.Virs;

import java.util.ArrayList;

/**
 * @author Xieningjun
 * @date 2024/11/12 15:22
 * @description
 */
public class MqTest {

    // 没有sleep再多测一些，有序无序
    // todo 一定要简单topic, producer, consumer的初始化
    public static void main(String[] args) throws InterruptedException {
        var topic = new PushTopic<String>(12);
        var push = new OrderPush<String>(true);
        topic.setPush(push);
        var producerList = new ArrayList<Producer<String>>();
        for (int i = 0; i < 2; i++) {
            var producer = new PushProducer<String>();
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
            var consumer = new PushConsumer<String>();
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
        }
        Virs.sleep(100);
//        Thread.sleep(1000);
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
        Virs.keepalive();
    }

}
