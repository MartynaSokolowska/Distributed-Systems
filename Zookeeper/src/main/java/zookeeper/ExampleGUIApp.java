package zookeeper;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ExampleGUIApp {
    public static void main(String[] args) {
        try {
            // Tekst do wyświetlenia w Paint
            String text = "Otwarte";

            // Utwórz obraz z tekstem
            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 200, 100);
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, 50, 50);
            g2d.dispose();

            // Zapisz obraz do pliku
            File file = new File("image.png");
            ImageIO.write(image, "png", file);

            // Otwórz Paint z obrazem
            Process process = Runtime.getRuntime().exec("mspaint " + file.getAbsolutePath());
            process.waitFor(); // Poczekaj na otwarcie Painta

            // Usuń plik tymczasowy
            file.delete();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
