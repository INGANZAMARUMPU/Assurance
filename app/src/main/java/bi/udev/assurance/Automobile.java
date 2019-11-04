package bi.udev.assurance;

/**
 * Created by KonstrIctor on 01/11/2019.
 */

public class Automobile {
    public String id, client, nom, plaque, chassis, roues, CNI;

    public Automobile(String id, String client, String nom, String plaque, String chassis, String roues, String CNI) {
        this.id = id;
        this.client = client;
        this.nom = nom;
        this.plaque = plaque;
        this.chassis = chassis;
        this.roues = roues;
        this.CNI = CNI;
    }
}
