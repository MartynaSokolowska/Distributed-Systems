package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class ZooKeeperWatcherApp implements Watcher {
    private static ZooKeeper zooKeeper;
    private static Process externalAppProcess;
    private static String externalApp;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        if (args.length < 2) {
            System.out.println("Required 2 arguments: <IP:PORT> <PROCESS NAME>");
            System.exit(1);
        }
        String connectString = args[0];
        externalApp = args[1];
        zooKeeper = new ZooKeeper(connectString, 3000, new ZooKeeperWatcherApp());
        zooKeeper.addWatch("/", AddWatchMode.PERSISTENT_RECURSIVE);

        Stat stat = zooKeeper.exists("/a", true);
        if (stat != null) {
            openEA();
            watchChildren("/a");
        }

        while (true) {
            Thread.sleep(1000);
        }
    }

    private static void updateEA() throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists("/a", true);
        if (stat == null) return;
        List<String> children = zooKeeper.getChildren("/a", false);
        int childrenCount = children.size();
        System.out.println("Aktualizacja liczba potomków węzła /a: " + childrenCount);
        new Thread(() -> {

            String data = Integer.toString(childrenCount);
            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 200, 100);
            g2d.setColor(Color.BLACK);
            g2d.drawString(data, 50, 50);
            g2d.dispose();

            try {
                File file = new File("image.png");
                ImageIO.write(image, "png", file);
                externalAppProcess.destroy();
                externalAppProcess = Runtime.getRuntime().exec("mspaint " + file.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void openEA() {
        System.out.println("Otwieranie aplikacji zewnętrznej...");
        try {
            externalAppProcess = Runtime.getRuntime().exec(externalApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeEA() {
        System.out.println("Zamykanie aplikacji zewnętrznej...");
        externalAppProcess.destroy();
    }

    @Override
    public void process(WatchedEvent event) {
        try {
            String path = event.getPath();
            if (event.getType() == Event.EventType.NodeCreated && path.equals("/a")) {
                openEA();
                watchChildren("/a");
            } else if (event.getType() == Event.EventType.NodeDeleted && path.equals("/a")) {
                closeEA();
                zooKeeper.exists("/a", true);
            } else if (event.getType() == Event.EventType.NodeChildrenChanged && path.equals("/a")) {
                updateEA();
                watchChildren("/a");
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void watchChildren(String path) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, false);
        if (stat != null) {
            zooKeeper.getChildren(path, true);
        }
    }

}
