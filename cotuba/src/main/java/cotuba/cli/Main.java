package cotuba.cli;

import cotuba.CotubaConfig;
import cotuba.application.Cotuba;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var verboseMode = false;

        try {
            var CLIOptions = new CLIOptionsReader(args);
            var markdownFolder = CLIOptions.getMarkdownFolder();
            var ebookFormat = CLIOptions.getEbookFormat();
            var outputFile = CLIOptions.getOutputFile();
            verboseMode = CLIOptions.isVerboseMode();

            var applicationContext =  new AnnotationConfigApplicationContext(CotubaConfig.class);
            var cotuba = applicationContext.getBean(Cotuba.class);
            cotuba.execute(ebookFormat, markdownFolder, outputFile);
            System.out.println("Arquivo gerado com sucesso: " + outputFile);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (verboseMode) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }

}
