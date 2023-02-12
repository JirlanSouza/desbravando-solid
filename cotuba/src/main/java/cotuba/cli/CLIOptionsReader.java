package cotuba.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class CLIOptionsReader {
    private Path markdownFolder;
    private String ebookFormat;
    private Path outputFile;
    private boolean verboseMode = false;

    public CLIOptionsReader(String[] args) throws IllegalArgumentException {
        try {
            var options = new Options();

            var markdownFolderOption = new Option("d", "dir", true, "Diretório que contém os arquivos md. Default: diretório atual.");
            options.addOption(markdownFolderOption);

            var ebookFormatOption = new Option("f", "format", true, "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
            options.addOption(ebookFormatOption);

            var outputFileOption = new Option("o", "output", true, "Arquivo de saída do ebook. Default: book.{formato}.");
            options.addOption(outputFileOption);

            var verboseModeOption = new Option("v", "verbose", false, "Habilita modo verboso.");
            options.addOption(verboseModeOption);

            CommandLineParser cmdParser = new DefaultParser();
            var helpFormatter = new HelpFormatter();
            CommandLine cmd;

            try {
                cmd = cmdParser.parse(options, args);
            } catch (ParseException exception) {
                helpFormatter.printHelp("cotuba", options);
                throw new IllegalArgumentException("Opção inválida", exception);
            }

            String markdownFolderName = cmd.getOptionValue("dir");

            if (markdownFolderName != null) {
                markdownFolder = Paths.get(markdownFolderName);
                if (!Files.isDirectory(markdownFolder)) {
                    throw new IllegalArgumentException(markdownFolderName + " não é um diretório.");
                }
            } else {
                markdownFolder = Paths.get("");
            }

            String ebookFormatName = cmd.getOptionValue("format");

            if (ebookFormatName != null) {
                ebookFormat = ebookFormatName.toLowerCase();
            } else {
                ebookFormat = "pdf";
            }

            String ebookOutputFileName = cmd.getOptionValue("output");
            if (ebookOutputFileName != null) {
                outputFile = Paths.get(ebookOutputFileName);
            } else {
                outputFile = Paths.get("book." + ebookFormat.toLowerCase());
            }
            if (Files.isDirectory(outputFile)) {
                // deleta arquivos do diretório recursivamente
                Files.walk(outputFile).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            } else {
                Files.deleteIfExists(outputFile);
            }

            verboseMode = cmd.hasOption("verbose");
        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    public Path getMarkdownFolder() {
        return markdownFolder;
    }

    public String getEbookFormat() {
        return ebookFormat;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }
}
