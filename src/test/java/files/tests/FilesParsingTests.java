package files.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import files.model.Coffee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FilesParsingTests {
    private final ClassLoader classLoader = FilesParsingTests.class.getClassLoader();

    @Test
    void pdfFromZipFileParsingTest() throws Exception {
        try (ZipFile zf = new ZipFile(new File("src/test/resources/archive.zip"));
             ZipInputStream zis = new ZipInputStream(
                     Objects.requireNonNull(classLoader.getResourceAsStream("archive.zip"))
             )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();

                if (fileName.endsWith(".pdf") && !fileName.startsWith("_")) {
                    try (InputStream stream = zf.getInputStream(entry)) {
                        PDF pdf = new PDF(stream);
                        Assertions.assertEquals("Sliced Invoices", pdf.author);
                    }
                }
            }
        }
    }

    @Test
    void csvFromZipFileParsingTest() throws Exception {

        try (ZipFile zf = new ZipFile(new File("src/test/resources/archive.zip"));
             ZipInputStream zis = new ZipInputStream(
                     Objects.requireNonNull(classLoader.getResourceAsStream("archive.zip"))
             )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();

                if (fileName.endsWith(".csv") && !fileName.startsWith("_")) {
                    CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
                    try (InputStream stream = zf.getInputStream(entry);
                         Reader reader = new InputStreamReader(stream);
                         CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {
                        List<String[]> data = csvReader.readAll();
                        Assertions.assertEquals(6, data.size());
                        Assertions.assertArrayEquals(
                                new String[] {"Identifier", "First name", "Last name"},
                                data.get(0)
                        );
                        Assertions.assertArrayEquals(
                                new String[] {"901242", "Rachel", "Booker"},
                                data.get(1)
                        );
                        Assertions.assertArrayEquals(
                                new String[] {"207074", "Laura", "Grey"},
                                data.get(2)
                        );
                        Assertions.assertArrayEquals(
                                new String[] {"408129", "Craig", "Johnson"},
                                data.get(3)
                        );
                        Assertions.assertArrayEquals(
                                new String[] {"934600", "Mary", "Jenkins"},
                                data.get(4)
                        );
                        Assertions.assertArrayEquals(
                                new String[] {"507916", "Jamie", "Smith"},
                                data.get(5)
                        );
                    }
                }
            }
        }
    }

    @Test
    void xlsxFromZipFileParsingTest() throws Exception {
        try (ZipFile zf = new ZipFile(new File("src/test/resources/archive.zip"));
             ZipInputStream zis = new ZipInputStream(
                     Objects.requireNonNull(classLoader.getResourceAsStream("archive.zip"))
             )) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();

                if (fileName.endsWith(".xlsx") && !fileName.startsWith("_")) {
                    try (InputStream is = zf.getInputStream(entry)) {
                        XLS xls = new XLS(is);

                        String actualValue = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();

                        Assertions.assertTrue(actualValue.contains("Dett"));
                    }
                }
            }
        }
    }

    @Test
    void jsonFileParsingTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(classLoader.getResourceAsStream("coffee542532.json"))
        )) {
            Coffee coffee = objectMapper.readValue(reader, Coffee.class);
            List<String> expectedFlavorNotes = Arrays.asList("chocolate", "caramel", "earthy");

            Assertions.assertEquals(542532, coffee.getId());
            Assertions.assertEquals("Mountain Peak Coffee", coffee.getBrand());
            Assertions.assertEquals("A smooth, rich roast with hints of chocolate and a bold, earthy finish.",
                    coffee.getDescription());
            Assertions.assertEquals("Colombia", coffee.getOriginCountry());
            Assertions.assertEquals("Medium-Dark", coffee.getRoastLevel());
            Assertions.assertEquals("Arabica", coffee.getBeanType());
            Assertions.assertEquals("250g", coffee.getWeight());
            Assertions.assertEquals(expectedFlavorNotes, coffee.getFlavorNotes());
            Assertions.assertEquals("Whole Bean", coffee.getGrindType());
            Assertions.assertEquals(652.99, coffee.getPrice());
            Assertions.assertTrue(coffee.getInStock());
        }
    }
}