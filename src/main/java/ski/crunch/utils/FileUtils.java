package ski.crunch.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtils {

    public static File writeBase64ToFile(String base64) {

        byte[] data = Base64.getDecoder().decode(base64);
        String name = UUID.randomUUID().toString();
        try (OutputStream stream = new FileOutputStream("/tmp/" + name)) {
            stream.write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new File("/tmp/" + name);
    }


    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    public static void writeStringToFile(String content, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
            fileWriter.flush();
        }
    }

    public static String readFileToString(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try(FileReader fileReader = new FileReader(file)) {
            try( BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while (( line = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(line);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void deleteIfExists(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public static void copyDirectory(String source, String dest) throws IOException {
        File sourceDir = new File(source);
        if(!sourceDir.isDirectory()) {
            throw new IOException("source must be a directory");
        }

        File destDir = new File(dest);
        if(!destDir.isDirectory()) {
            throw new IOException("dest must be a directory");
        }


        Path destination = Paths.get(dest);

        Files.list(Paths.get(source))
                .forEach(x -> {
                    try {
                        if(x.toFile().isDirectory()) {
                            File newDir = new File(destination.toString(), x.getFileName().toString());
                            newDir.mkdir();

                            copyDirectory(x.toFile().getAbsolutePath(), newDir.getAbsolutePath());
                        } else {
                            Files.copy(x, Paths.get(destination.toString(), x.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static long getFolderSizeBytes(Path folder) throws IOException {
        AtomicLong size = new AtomicLong(0);

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }
        });
        return size.longValue();
    }

    public static InputStream readFromResourcesDirectory(Class clazz, String fileName) throws IOException {
        return clazz.getResourceAsStream(fileName);
    }

}
