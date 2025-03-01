package com.yangx.rpc.serializer;

import com.yangx.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工厂（工厂模式，用于获取序列化器对象）
 */
public class SerializerFactory {

//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {{
//        put(SerializerKeys.JDK, new JdkSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//        put(SerializerKeys.KRYO, new KryoSerializer());
//        put(SerializerKeys.HESSIAN, new HessianSerializer());
//    }};
    static {
    SpiLoader.load(Serializer.class);
}

    private static volatile Serializer serializer;

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {

        if (serializer == null) {
            synchronized (Serializer.class) {
                if (serializer == null) {
                    return SpiLoader.getInstance(Serializer.class, key);
                }
            }
        }
        return serializer;
    }
}
