package net.theEvilReaper.batoideas.database.pojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ServiceUpdate implements Serializable {

    private final UpdateAction updateAction;
    private final Object object;

    public ServiceUpdate(UpdateAction updateAction, Object object) {
        this.updateAction = updateAction;
        this.object = object;
    }

    public static ServiceUpdate ofSupport(Object object) {
        return new ServiceUpdate(UpdateAction.TEAMSPEAK_SUPPORT, object);
    }

    public static ServiceUpdate deserialize(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = null;

        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (ServiceUpdate) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return new ServiceUpdate(UpdateAction.NONE, new Object());
    }

    public byte[] serialize() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return null;
    }

    public Object getObject() {
        return object;
    }

    public UpdateAction getUpdateAction() {
        return updateAction;
    }
}
