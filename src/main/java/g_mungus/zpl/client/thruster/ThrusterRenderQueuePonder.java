package g_mungus.zpl.client.thruster;

import java.util.ArrayList;
import java.util.List;

public class ThrusterRenderQueuePonder {
    private static final List<ThrusterRenderData> QUEUE = new ArrayList<>();

    public static void enqueue(ThrusterRenderData data) {
        QUEUE.add(data);
    }

    public static List<ThrusterRenderData> getQueue() {
        return QUEUE;
    }

    public static void clear() {
        QUEUE.clear();
    }
}
