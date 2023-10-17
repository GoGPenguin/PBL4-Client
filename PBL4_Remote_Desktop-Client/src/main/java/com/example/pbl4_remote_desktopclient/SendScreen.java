package com.example.pbl4_remote_desktopclient;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SendScreen{
    private Robot robot;
    private Socket socket;
    private double pixelsPerCmWidth;
    private double pixelsPerCmHeight;
    public SendScreen(Robot robot, Socket socket, double pixelsPerCmWidth, double pixelsPerCmHeight) {
        this.robot = robot;
        this.socket = socket;
        this.pixelsPerCmWidth = pixelsPerCmWidth;
        this.pixelsPerCmHeight = pixelsPerCmHeight;
        try {
            // Calculate the capture area based on physical screen size
            int clientWidth = (int) (34.0 * pixelsPerCmWidth);
            int clientHeight = (int) (19.0 * pixelsPerCmHeight);
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            while (true) {
                BufferedImage screenCapture = robot.createScreenCapture(screenRect);
                BufferedImage scaledImage = new BufferedImage(clientWidth, clientHeight, BufferedImage.TYPE_INT_RGB);
                scaledImage.getGraphics().drawImage(screenCapture.getScaledInstance(clientWidth, clientHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, "jpg", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Send image length as metadata
                int imageLength = imageBytes.length;
                OutputStream out = socket.getOutputStream();
                out.write(ByteBuffer.allocate(4).putInt(imageLength).array());
                out.write(imageBytes);
                out.flush();
                Thread.sleep(10); // 1 second
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
