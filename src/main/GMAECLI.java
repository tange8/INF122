package main;

import java.util.Scanner;

public class GMAECLI {

    private GMAE gmae;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new GMAECLI().run();
    }

    public GMAECLI() {
        this.gmae = new GMAE();
    }

    public void run() {
        // frontend loop
    }
}
