package cotuba.cli;

import cotuba.application.CotubaParameters;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;

class CLIOptionsReader implements CotubaParameters {
    private Path markdownFolder;
    private String ebookFormat;
    private Path outputFile;
    private boolean verboseMode = false;

    public CLIOptionsReader(String[] args) throws IllegalArgumentException {
        try {
            var options = createOptions();
            var cmd = parseArguments(options, args);

            handleMarkdownFolder(cmd);
            handleEbookFormat(cmd);
            handleEbookOutputFile(cmd);
            handleVerboseMode(cmd);

        } catch (IOException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private Options createOptions() {
        var options = new Options();

        var markdownFolderOption = new Option("d", "dir", true, "Diretório que contém os arquivos md. Default: diretório atual.");
        options.addOption(markdownFolderOption);

        var ebookFormatOption = new Option("f", "format", true, "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
        options.addOption(ebookFormatOption);

        var outputFileOption = new Option("o", "output", true, "Arquivo de saída do ebook. Default: book.{formato}.");
        options.addOption(outputFileOption);

        var verboseModeOption = new Option("v", "verbose", false, "Habilita modo verboso.");
        options.addOption(verboseModeOption);

        return options;
    }

    private CommandLine parseArguments(Options options, String[] args) {
        CommandLineParser cmdParser = new DefaultParser();
        var helpFormatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException exception) {
            helpFormatter.printHelp("cotuba", options);
            throw new IllegalArgumentException("Opção inválida", exception);
        }

        return cmd;
    }

    private void handleMarkdownFolder(CommandLine cmd) {
        String markdownFolderName = cmd.getOptionValue("dir");

        if (markdownFolderName != null) {
            markdownFolder = Paths.get(markdownFolderName);
            if (!Files.isDirectory(markdownFolder)) {
                throw new IllegalArgumentException(markdownFolderName + " não é um diretório.");
            }
        } else {
            markdownFolder = Paths.get("");
        }
    }

    private void handleEbookFormat(CommandLine cmd) {
        String ebookFormatName = cmd.getOptionValue("format");

        if (ebookFormatName != null) {
            ebookFormat = ebookFormatName.toLowerCase();
        } else {
            ebookFormat = "pdf";
        }
    }

    private void handleEbookOutputFile(CommandLine cmd) throws IOException {
        String ebookOutputFileName = cmd.getOptionValue("output");
        outputFile = Paths.get(Objects.requireNonNullElseGet(ebookOutputFileName, () -> "book." + ebookFormat.toLowerCase()));

        if (Files.isDirectory(outputFile)) {
            try (var paths = Files.walk(outputFile)) {
                paths.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

        } else {
            Files.deleteIfExists(outputFile);
        }
    }

    private void handleVerboseMode(CommandLine cmd) {
        verboseMode = cmd.hasOption("verbose");
    }

    @Override
    public Path getMarkdownFolder() {
        return markdownFolder;
    }

    @Override
    public String getEbookFormat() {
        return ebookFormat;
    }

    @Override
    public Path getOutputFile() {
        return outputFile;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }
}
