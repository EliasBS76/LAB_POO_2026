package practica_09;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ItemManager 
{
    public void saveObjects(String path, List<Item> items) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(items);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Item> loadObjects(String path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Item>) ois.readObject();
        }
    }

    public List<Item> processCSV(String path) throws IOException {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    items.add(new Item(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim())));
                }
            }
        }
        return items;
    }

    public void exportToJson(String path, List<Item> items) throws IOException {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            json.append("  {\n");
            json.append("    \"id\": \"").append(item.getId()).append("\",\n");
            json.append("    \"name\": \"").append(item.getName()).append("\",\n");
            json.append("    \"price\": ").append(item.getPrice()).append("\n");
            json.append("  }");
            if (i < items.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(json.toString());
        }
    }
    
}
