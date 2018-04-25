package com.syh.zookeeper.demo.curator.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ElectionTest {

    static final String path = "/example/leader";

    public static void main(String[] args) {

        CuratorFramework client1 = null;
        CuratorFramework client2 = null;

        Candidate candidate1 = null;
        Candidate candidate2 = null;

        try {
            client1 = CuratorFrameworkFactory.newClient("localhost:2181",
                    new ExponentialBackoffRetry(1000, 10));
            client2 = CuratorFrameworkFactory.newClient("localhost:2181",
                    new ExponentialBackoffRetry(1000, 10));

            client1.start();
            client2.start();

            candidate1 = new Candidate(client1, path) {
                @Override
                void process() {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("candidate1: take leadership");
                        System.out.println("candidate1: is leader:" + isLeader());
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted: candidate1");
                    }
                }
            };

            candidate2 = new Candidate(client2, path) {
                @Override
                void process() {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("candidate2: take leadership");
                        System.out.println("candidate2: is leader:" + isLeader());
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted: candidate2");
                    }
                }
            };

            candidate1.start();
            candidate2.start();

            Scanner scanner = new Scanner(System.in);
            System.out.println("PRESS <ENTER> TO EXIT");
            scanner.nextLine();
        } finally {
            CloseableUtils.closeQuietly(candidate1);
            CloseableUtils.closeQuietly(candidate2);
            CloseableUtils.closeQuietly(client1);
            CloseableUtils.closeQuietly(client2);
        }
    }
}
