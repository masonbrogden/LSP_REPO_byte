package org.howard.edu.lsp.midterm.question4;

import java.util.*;
/*
Rationale (Q5):
I kept Device abstract to hold the common stuff every device shares (id, location, heartbeat, connection flag),
while leaving getStatus() to each specific device since they report differently. The Networked and BatteryPowered
interfaces let me “mix in” connectivity and battery behavior only where it makes sense (e.g., DoorLock/Camera
have batteries; Thermostat doesn’t), without bloating the base class. This isn’t multiple inheritance of code—
it’s single inheritance from Device plus multiple interfaces, so I get flexible behavior contracts without sharing
implementation across classes.
*/

public class Main {
  public static void main(String[] args) {
    Device lock   = new DoorLock("DL-101", "DormA-1F", 85);
    Device thermo = new Thermostat("TH-202", "Library-2F", 21.5);
    Device cam    = new Camera("CA-303", "Quad-North", 72);

    System.out.println("\n== Exception test ==");
    try {
      Device badCam = new Camera("CA-404", "Test-Lab", -5);
      System.out.println("ERROR: Exception was not thrown for invalid battery!");
    } catch (IllegalArgumentException e) {
      System.out.println("Caught expected exception: " + e.getMessage());
    }

    System.out.println("\n== Heartbeat timestamps BEFORE ==");
    for (Device d : Arrays.asList(lock, thermo, cam)) {
      System.out.println(d.getId() + " lastHeartbeat=" + d.getLastHeartbeatEpochSeconds());
    }

    lock.heartbeat();
    thermo.heartbeat();
    cam.heartbeat();

    System.out.println("\n== Heartbeat timestamps AFTER ==");
    for (Device d : Arrays.asList(lock, thermo, cam)) {
      System.out.println(d.getId() + " lastHeartbeat=" + d.getLastHeartbeatEpochSeconds());
    }

    List<Device> devices = Arrays.asList(lock, thermo, cam);
    System.out.println("\n== Initial status via Device ==");
    for (Device d : devices) {
      System.out.println(d.getStatus());
    }

    System.out.println("\n== Connect all Networked ==");
    for (Device d : devices) {
      if (d instanceof Networked) {
        ((Networked) d).connect();
      }
    }

    System.out.println("\n== Battery report (BatteryPowered) ==");
    for (Device d : devices) {
      if (d instanceof BatteryPowered) {
        BatteryPowered bp = (BatteryPowered) d;
        System.out.println(d.getClass().getSimpleName() + " battery = " + bp.getBatteryPercent() + "%");
      }
    }

    System.out.println("\n== Updated status via Device ==");
    for (Device d : devices) {
      System.out.println(d.getStatus());
    }
  }
}

