package bi.udev.assurance;

/**
 * Created by KonstrIctor on 01/11/2019.
 */

public class Client {
    public String id, nom, prenom, CNI, avatar, email, tel, depuis;

    public Client(String id, String nom, String prenom, String CNI, String avatar, String email, String tel, String depuis) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.CNI = CNI;
        this.avatar = avatar;
        this.email = email;
        this.tel = tel;
        this.depuis = depuis;
    }
}
