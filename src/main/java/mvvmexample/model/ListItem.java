package mvvmexample.model;

public class ListItem {
    
    private String name;
    private double price;
    
    public ListItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

}
