package weightmachinedesktopapplication;

public class Item {
    private String key;
    private String value;
   

    public Item(String key, String value) {
        this.key = key;
        this.value = value;
       
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
   

    @Override
    public String toString() {
        return "Item{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

   
}
