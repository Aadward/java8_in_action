package com.syh.zookeeper.demo.primitive.leader;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.syh.zookeeper.demo.primitive.ZkClient;

/**
 * A simple leader election DEMO, and it can not be used for production environment directly.
 *
 * @author Yuhang Shen
 */
public class ElectionTest {
    public static void main(String[] args) {
        ZkClient client = null;
        Candidate candidate1 = null;
        Candidate candidate2 = null;
        try {
            client = ZkClient.createClient("localhost:2181");
            candidate1 = new Candidate(client, "/etest") {
                @Override
                void process() {
                    System.out.println("candidate1 take leader");
                    //hold on leadership
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("candidate1 release leadership");
                }
            };
            candidate2 = new Candidate(client, "/etest") {
                @Override
                void process() {
                    System.out.println("candidate2 take leader");
                    //hold on leadership
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("candidate2 release leadership");
                }
            };
            candidate1.elect();
            candidate2.elect();
            System.out.println("PRESS <ENTER> TO EXIT");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        } finally {
            if (client != null) {
                client.close();
            }
            if (candidate1 != null) {
                candidate1.close();
            }

            if (candidate2 != null) {
                candidate2.close();
            }
        }
    }
}
