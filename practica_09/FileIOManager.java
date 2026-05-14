package practica_09;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileIOManager 
{
    public void writeText(String path, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content);
        }
    }

    public String readText(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    public void writeBinary(String path, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data);
        }
    }

    public byte[] readBinary(String path) throws IOException {
        File file = new File(path);
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(data);
        }
        return data;
    }

    public void createDirectory(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }

    public void createBackup(String sourcePath) throws IOException {
        File source = new File(sourcePath);
        if (!source.exists()) {
            throw new FileNotFoundException("Archivo no encontrado");
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String parentPath = source.getParent() != null ? source.getParent() + File.separator : "";
        String backupPath = parentPath + "backup_" + timestamp + "_" + source.getName();
        Files.copy(source.toPath(), Paths.get(backupPath), StandardCopyOption.REPLACE_EXISTING);
    }
}

