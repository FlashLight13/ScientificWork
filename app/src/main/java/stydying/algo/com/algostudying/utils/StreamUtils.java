package stydying.algo.com.algostudying.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Anton on 08.07.2015.
 */
public class StreamUtils {

    public static final short BYTES_IN_FLOAT = 4;
    public static final short BYTES_IN_INT = 4;

    public static InputStream loadResource(Context context, int id) {
        if (context == null) {
            return null;
        }
        try {
            return context.getResources().openRawResource(id);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public static byte[] streamToBytes(InputStream stream) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        byte[] result;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = stream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            result = output.toByteArray();
        } finally {
            output.close();
        }
        return result;
    }

    public static InputStream bytesToStream(byte[] bytes) throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    public static Object bytesToObject(byte[] bytes) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static byte[] objectToBytes(Object object) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(object);
        return out.toByteArray();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String readTextFileFromRawResource(final Context context,
                                                     final int resourceId) {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

        String nextLine;
        final StringBuilder body = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            return null;
        }

        return body.toString();
    }

    /**
     * @return true if closed successfully and false the otherwise
     */
    public static boolean close(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }
}
