import java.util.ArrayList;
import java.util.List;

public class Page {
    List<Integer> infoPage;
    private int tamanoPagina;
    private int numeroPagina;


    public Page(int tamanoPagina, int numeroPagina) {
        this.infoPage = new ArrayList<>();
        this.tamanoPagina = tamanoPagina;
        this.numeroPagina = numeroPagina;
    }
    public List<Integer> getInfoPage() {
        return infoPage;
    }
    public void setInfoPage(List<Integer> infoPage) {
        this.infoPage = infoPage;
    }
    public void addInfo(int entero) {
        this.infoPage.add(entero);
    }

}
