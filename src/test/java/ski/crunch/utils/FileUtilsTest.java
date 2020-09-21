package ski.crunch.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class FileUtilsTest {


    @Test
    public void copyDirectoryTest() throws IOException {
        File tmp = Files.createTempDirectory("tmpFile").toFile();
        File dirToCopy = Files.createTempDirectory("tocopy").toFile();
        System.out.println("src = " + dirToCopy.getAbsolutePath());
        System.out.println("dest = " + tmp.getAbsolutePath());


        File one = new File(dirToCopy, "one");
        File two = new File(dirToCopy, "two");

        one.createNewFile();
        two.createNewFile();

        FileUtils.writeStringToFile("test file one", new File(dirToCopy, "one"));
        FileUtils.writeStringToFile("test file two", new File(dirToCopy, "two"));


        //act
        FileUtils.copyDirectory(dirToCopy.getAbsolutePath(), tmp.getAbsolutePath());

        File copiedOne = new File(tmp, "one");
        File copiedTwo = new File(tmp, "two");
        assertTrue(copiedOne.exists());
        assertTrue(copiedTwo.exists());
        assertEquals("test file one", FileUtils.readFileToString(copiedOne));
    }


    @Test
    public void copyDirectoryRecursivelyTest() throws IOException {
        File tmp = Files.createTempDirectory("tmpFile").toFile();
        File dirToCopy = Files.createTempDirectory("tocopy").toFile();
        System.out.println("src = " + dirToCopy.getAbsolutePath());
        System.out.println("dest = " + tmp.getAbsolutePath());


        File one = new File(dirToCopy, "one");
        File two = new File(dirToCopy, "two");
        two.mkdir();
        File three = new File(two, "three");

        one.createNewFile();
        two.createNewFile();
        three.createNewFile();

        FileUtils.writeStringToFile("test file one", new File(dirToCopy, "one"));

        FileUtils.writeStringToFile("test file three", new File(two, "three"));

        //act
        FileUtils.copyDirectory(dirToCopy.getAbsolutePath(), tmp.getAbsolutePath());

        File copiedOne = new File(tmp, "one");
        File copiedTwo = new File(tmp, "two");
        File copiedThree = new File(copiedTwo, "three");
        assertTrue(copiedOne.exists());
        assertTrue(copiedThree.exists());
        assertEquals("test file three", FileUtils.readFileToString(copiedThree));
    }

    @Test
    public void deleteDirectoryTest()
            throws IOException {

        // set up -> create a directory structure in tmp dir
        File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"));
        File DIR_NAME = new File(TEMP_DIR, "deletedirtest");
        DIR_NAME.mkdir();
        File subDirToDelete = new File(DIR_NAME, "subdir");
        subDirToDelete.mkdir();
        File fileInSubDir = new File(subDirToDelete, "file1.json");
        File fileInSubDir2 = new File(subDirToDelete, "file2.json");
        File fileInRoot = new File(DIR_NAME, "fileroot.json");
        FileUtils.writeStringToFile("test string", fileInRoot);
        FileUtils.writeStringToFile("test string", fileInSubDir);
        FileUtils.writeStringToFile("test string", fileInSubDir2);

        boolean result = FileUtils.deleteDirectory(DIR_NAME);
        Path dirPath = DIR_NAME.toPath();
        assertTrue(result);
        assertFalse(
                Files.exists(dirPath),
                "Directory still exists");

    }
}
