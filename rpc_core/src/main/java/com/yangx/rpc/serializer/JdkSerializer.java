package com.yangx.rpc.serializer;

import java.io.*;

/**
 * JDK序列化器
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);
        oos.close();
        return bos.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param data
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        try {
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ois.close();
        }
    }
}
