package cotuba.cli;

import cotuba.application.Cotuba;

public class Main {

    public static void main(String[] args) {
        var verboseMode = false;

        try {
            var CLIOptions = new CLIOptionsReader(args);
            verboseMode = CLIOptions.isVerboseMode();

            var cotuba = new Cotuba();
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
