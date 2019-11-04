package bi.udev.assurance;

/**
 * Created by KonstrIctor on 01/11/2019.
 */

class Assurance {
    String id, no_police, montant, debut, materiel, client, fin, plaque;

    public Assurance(String id, String no_police, String montant, String debut, String materiel, String client, String fin, String plaque) {
        this.id = id;
        this.no_police = no_police;
        this.montant = montant;
        this.debut = debut;
        this.materiel = materiel;
        this.client = client;
        this.fin = fin;
        this.plaque = plaque;
    }
}
