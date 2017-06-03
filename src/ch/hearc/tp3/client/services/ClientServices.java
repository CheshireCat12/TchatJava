package ch.hearc.tp3.client.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ClientServices extends Service<String>
{
    public SimpleStringProperty connectionMessage = new SimpleStringProperty(
            "Déconnecter");
    public SimpleStringProperty message = new SimpleStringProperty();

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private StringBuffer str_message = new StringBuffer();
    private Socket socket;
    
    private String pathFile = System.getProperty("user.home");
    private String serverAddress = "127.0.0.1";
    private String portAddress = "50885";

    private static boolean isConnected = false;

    @Override
    protected Task<String> createTask()
    {
        return new Task<String>()
        {
            @Override
            protected String call() throws Exception
            {
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                Object dataReceived;
                while (true)
                {
                    dataReceived = inputStream.readObject();
                    if (dataReceived instanceof String)
                    {
                        str_message.append((String) dataReceived + "\n");
                        message.setValue(str_message.toString());
                    } else if (dataReceived instanceof byte[])
                    {
                        String filename = reverseByteName((byte[]) dataReceived);
                        str_message.append("Fichier " + filename + " reçu.\n");
                        message.setValue(str_message.toString());
                    }

                }
            }

        };
    }

    /**
     * Connect the socket to the server
     * Return true if the connection is ok
     * @param serverAddress
     * @param portAddress
     * @return
     */
    public boolean connection()
    {
        isConnected = false;

        try
        {
            socket = new Socket(this.serverAddress, Integer.parseInt(this.portAddress));

            connectionMessage.setValue("Connecté");

            isConnected = true;

        } catch (NumberFormatException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isConnected;
    }

    /**
     * Send a message to the server
     * @param Pseudo
     * @param message
     */
    public void sendMessage(String Pseudo, String message)
    {
        try
        {
            outputStream.writeObject("From " + Pseudo + " : " + message);
            outputStream.flush();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Send a file to the server
     * @param file
     */
    public void sendFile(File file)
    {
        System.out.println(file.getName());

        byte[] mybytearray = readBytesFromFile(file);

        try
        {
            outputStream.writeObject(mybytearray);
            outputStream.flush();
            System.out.println("Done.");
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Reverse the byte array to a file with the filename in the first byte
     * And will save the file
     * @param arrayByte
     * @return
     */
    private String reverseByteName(byte[] arrayByte)
    {
        System.out.println("reverse");
        int sizeName = (int) arrayByte[0];
        int dataSize = arrayByte.length - 1 - sizeName;
        byte[] name = new byte[sizeName];
        byte[] dataReceived = new byte[dataSize];
        System.arraycopy(arrayByte, 1, name, 0, sizeName);
        System.arraycopy(arrayByte, 1 + sizeName, dataReceived, 0, dataSize);
        String filename = new String(name, StandardCharsets.UTF_8);

        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream(pathFile + "\\" +filename);
            fos.write((byte[]) dataReceived);
            fos.close();
        } catch (IOException e)
        {
            System.out.println("Erreur lors de l'écriture du fichier");
        }
        return filename;
    }

    /**
     * Turn the file into a byte array, and add the filename in this byte array
     * @param file
     * @return 
     */
    private byte[] readBytesFromFile(File file)
    {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try
        {
            bytesArray = new byte[(int) file.length()];

            // read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (fileInputStream != null)
            {
                try
                {
                    fileInputStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        /*
         * Add the filename at the beginning of the byte array
         */
        byte[] destination = new byte[1 + (int) file.getName().getBytes().length
                + bytesArray.length];
        destination[0] = (byte) file.getName().getBytes().length;
        System.arraycopy(file.getName().getBytes(), 0, destination, 1,
                file.getName().getBytes().length);
        System.arraycopy(bytesArray, 0, destination,
                1 + file.getName().getBytes().length, bytesArray.length);
        return destination;

    }

    /**
     * Close properly the connection with the server
     */
    public void closeConnection()
    {
        if (isConnected)
        {
            try
            {
                inputStream.close();
                outputStream.close();
                socket.close();
                System.out.println("close socket");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getPathFile()
    {
        return pathFile;
    }

    public void setPathFile(String pathFile)
    {
        this.pathFile = pathFile;
    }

    public String getServeurAdress()
    {
        return serverAddress;
    }

    public void setServeurAdress(String serveurAdress)
    {
        this.serverAddress = serveurAdress;
    }

    public String getPortAddress()
    {
        return portAddress;
    }

    public void setPortAddress(String portAddress)
    {
        this.portAddress = portAddress;
    }


}
