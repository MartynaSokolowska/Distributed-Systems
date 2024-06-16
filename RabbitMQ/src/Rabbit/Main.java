package Rabbit;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws Exception {
        Doctor doctor1 = new Doctor( "1", 20);
        Doctor doctor2 = new Doctor( "2", 20);

        String[] types1 = {"knee", "hip"};
        Technician technician1 = new Technician("1", types1, 2);

        String[] types2 = {"hip", "elbow"};
        Technician technician2 = new Technician("2", types2, 3);

        new Thread(() -> {
            try {
                technician1.Exam();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                technician2.Exam();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                doctor1.receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doctor1.order("Pacjent1", "elbow");
                doctor1.order("Pacjent2", "hip");
                doctor1.order("Pacjent3", "elbow");
                doctor1.order("Pacjent4", "hip");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                doctor2.receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                doctor2.order("Pacjent5", "hip");
                doctor2.order("Pacjent6", "hip");
                doctor2.order("Pacjent7", "knee");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                sleep(100);
                Administrator.sendInfo("check");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        Administrator.log();

    }
}
