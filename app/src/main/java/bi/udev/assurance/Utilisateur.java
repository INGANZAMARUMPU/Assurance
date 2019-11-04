package bi.udev.assurance;

/**
 * Created by KonstrIctor on 31/10/2019.
 */

class Utilisateur {
    String nom, prenom, username, email, token, organisation, avatar;

    public Utilisateur(String nom, String prenom, String username, String email, String token, String organisation, String avatar) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.email = email;
        this.token = token;
        this.organisation = organisation;
        this.avatar = avatar;
    }
}
