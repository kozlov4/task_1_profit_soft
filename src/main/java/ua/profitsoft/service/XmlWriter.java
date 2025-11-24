package ua.profitsoft.service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class XmlWriter {

    public void writeStatistics(Map<String, Integer> stats, String attributeName) {
        String fileName = "statistics_by_" + attributeName + ".xml";
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try (var writer = new FileWriter(fileName)) {
            XMLStreamWriter xmlWriter = factory.createXMLStreamWriter(writer);

            xmlWriter.writeStartDocument();
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeStartElement("statistics");
            xmlWriter.writeCharacters("\n");

            stats.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> writeItem(xmlWriter, entry.getKey(), entry.getValue()));

            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();

            xmlWriter.flush();
            xmlWriter.close();

            System.out.println("Report generated: " + Path.of(fileName).toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error writing XML: " + e.getMessage());
        }
    }

    private void writeItem(XMLStreamWriter writer, String value, Integer count) {
        try {
            writer.writeCharacters("  ");
            writer.writeStartElement("item");
            writer.writeCharacters("\n    ");

            writer.writeStartElement("value");
            writer.writeCharacters(value);
            writer.writeEndElement();

            writer.writeCharacters("\n    ");

            writer.writeStartElement("count");
            writer.writeCharacters(String.valueOf(count));
            writer.writeEndElement();

            writer.writeCharacters("\n  ");
            writer.writeEndElement();
            writer.writeCharacters("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}