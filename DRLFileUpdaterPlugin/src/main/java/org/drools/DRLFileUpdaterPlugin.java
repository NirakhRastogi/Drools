package org.drools;

import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.drools.models.DRLModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.nio.file.Files.readAllLines;

@Mojo(name = "drlFileUpdaterWorkflow", defaultPhase = LifecyclePhase.GENERATE_TEST_RESOURCES)
public class DRLFileUpdaterPlugin extends AbstractMojo {

    @Parameter(required = true)
    DRLModel[] droolsWorkbooks;

    @Parameter(required = true)
    String clockName;

    @SneakyThrows
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("*******--- Started execution of drlFileUpdaterWorkflow ---*********");


        for (DRLModel drl : droolsWorkbooks) {
            getLog().info("Executing for file " + drl.getInfile() + ".");
            updateDRLWithLoggers(drl.getInfile(), drl.getOutfile(), drl.getFacts());
            getLog().info("Execution completed for file " + drl.getInfile() + ".\nOutput written to file " + drl.getOutfile() + ".");
        }


        getLog().info("*******--- Finished execution of drlFileUpdaterWorkflow ---*********");

    }

    private void updateDRLWithLoggers(String infile, String outfile, String[] facts) throws IOException {
        StringBuilder out = new StringBuilder();
        out.append(getDynamicCodeInsertionLine())
                .append("global org.drools.models.TimedFact ").append(clockName)
                .append("\n")
                .append(getDynamicCodeEndingLine())
                .append("\n");
        Path infilepath = Path.of(infile);
        if (Files.exists(infilepath)) {
            List<String> lines = readAllLines(infilepath);
            String ruleName = "";
            for (String line : lines) {
                if (line.trim().startsWith("rule")) {
                    out.append(line).append("\n");
                    ruleName = line.substring(5);
                } else if (line.trim().startsWith("when")) {
                    out.append(line)
                            .append("\n\t\t").append(getDynamicCodeInsertionLine());

                    out.append("\t\t").append(getEntryDebugPath(ruleName, facts))
                            .append(getDynamicCodeEndingLine()).append("\n");
                } else if (line.trim().startsWith("then")) {
                    out.append("\n\t\t")
                            .append(getDynamicCodeInsertionLine()).append("\t\t").append(getExitDebugPath(ruleName, facts))
                            .append(getDynamicCodeEndingLine()).append("\n");

                    out.append(line).append("\n");
                } else {
                    out.append(line).append("\n");
                }
            }

        }

        Path outfilepath = Path.of(outfile);
        if (!Files.exists(outfilepath)) {
            Files.writeString(outfilepath, out.toString(), StandardOpenOption.CREATE_NEW);
        }
    }

    public String getDynamicCodeInsertionLine() {
        return "//********* Code inserted by " + getClass().getSimpleName() + " ******//\n";
    }

    public String getDynamicCodeEndingLine() {
        return "//********************************************************************//\n";
    }

    public String getEntryDebugPath(String rulename, String[] facts) {
        StringBuilder sb = new StringBuilder();
        for (String fact : facts) {
            sb.append(fact).append("(org.drools.utils.RuleUtil.timedInput(").append(rulename).append(",\"").append(fact).append("\",").append(clockName).append("));")
                    .append("\n\t\t");
        }
        return sb.toString();
    }

    public String getExitDebugPath(String rulename, String[] facts) {
        StringBuilder sb = new StringBuilder();
        for (String fact : facts) {
            sb.append(fact).append("(org.drools.utils.RuleUtil.timedExit(").append(rulename).append(",\"").append(fact).append("\",").append(clockName).append("));").append("\n\t\t");
        }
        return sb.toString();
    }

//    private void updateDRLWithLoggers(String infile, String outfile, String[] facts) throws IOException {
//        StringBuilder out = new StringBuilder();
//        Path infilepath = Path.of(infile);
//        if(Files.exists(infilepath)){
//            List<String> lines = readAllLines(infilepath);
//            String ruleName = "";
//            for(String line: lines){
//                if(line.trim().startsWith("rule")){
//                    out.append(line).append("\n");
//                    ruleName = line.substring(5);
//                } else if(line.trim().startsWith("when")) {
//                    out.append(line).append("\n");
//                    out.append("\t").append(getEntryDebugPath(ruleName, facts)).append("\n");
//                } else if(line.trim().startsWith("then")){
//                    out.append("\t").append(getExitDebugPath(ruleName, facts)).append("\n");
//                    out.append(line).append("\n");
//                } else {
//                    out.append(line).append("\n");
//                }
//            }
//
//        }
//
//        Path outfilepath = Path.of(outfile);
//        if(!Files.exists(outfilepath)){
//            Files.writeString(outfilepath, out.toString(), StandardOpenOption.CREATE_NEW);
//        }
//    }
//
//    public String getEntryDebugPath(String rulename, String[] facts){
//        StringBuilder sb = new StringBuilder();
//        for(String fact: facts) {
//            sb.append(fact).append("(timedInput(").append(rulename).append(",\"").append(fact).append("\"));")
//                    .append("\n\t");
//        }
//        return sb.toString();
//    }
//
//    public String getExitDebugPath(String rulename, String[] facts){
//        StringBuilder sb = new StringBuilder();
//        for(String fact: facts) {
//            sb.append(fact).append("(timedExit(").append(rulename).append(",\"").append(fact).append("\"));").append("\n\t");
//        }
//        return sb.toString();
//    }
}