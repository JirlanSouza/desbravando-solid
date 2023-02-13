package cotuba.cli;

import cotuba.CotubaConfig;
import cotuba.application.Cotuba;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var verboseMode = false;

        try {
            var CLIOptions = new CLIOptionsReader(args);
            verboseMode = CLIOptions.isVerboseMode();

            var applicationContext = new AnnotationConfigApplicationContext(CotubaConfig.class);
            var cotuba = applicationContext.getBean(Cotuba.class);
            cotuba.execute(CLIOptions);
            System.out.println("Arquivo gerado com sucesso: " + CLIOptions.getOutputFile());

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (verboseMode) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }

}
