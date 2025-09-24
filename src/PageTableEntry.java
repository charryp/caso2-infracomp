import java.util.HashMap;

public class PageTableEntry {
    private HashMap<Integer, Page> pages;

    public PageTableEntry() {
        this.pages = new HashMap<>();
    }
    public HashMap<Integer, Page> getPages() {
        return pages;
    }
    public void setPages(HashMap<Integer, Page> pages) {
        this.pages = pages;
    }
    public void addPage(int pageNumber, Page page) {
        this.pages.put(pageNumber, page);
    }

    public void removePage(int pageNumber) {
        this.pages.remove(pageNumber);
    }

    public Page getPage(int pageNumber) {
        return this.pages.get(pageNumber);
    }
    


    
}
