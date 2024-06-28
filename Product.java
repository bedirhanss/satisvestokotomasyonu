package stokvepersonel;

public class Product {
    private String isim;
    private String tur;
    private String marka;
    private int miktar;
    private String kayitTarihi;
    private String barkod;
    private double fiyat;
    private byte[] resim;

    // Constructor
    public Product(String isim, String tur, String marka, int miktar, String kayitTarihi, String barkod, double fiyat, byte[] resim) {
        this.isim = isim;
        this.tur = tur;
        this.marka = marka;
        this.miktar = miktar;
        this.kayitTarihi = kayitTarihi;
        this.barkod = barkod;
        this.fiyat = fiyat;
        this.resim = resim;
    }

    // Getters and setters
    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public int getMiktar() {
        return miktar;
    }

    public void setMiktar(int miktar) {
        this.miktar = miktar;
    }

    public String getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(String kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public byte[] getResim() {
        return resim;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }
}

