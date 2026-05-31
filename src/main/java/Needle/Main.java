package Needle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public enum Mode {
        GREP,
        EXACT_MATCH,
        SUBSTRING_MATCH
    }

    public static void main(String[] args) {

        try {
            Core core = new Core();
            String regex =  "";
            String input = "";
            Mode mode =  Mode.EXACT_MATCH;
            List<String> files = new ArrayList<>();

            for(String arg : args) {
                if(arg.startsWith("--regex=")) {
                    regex = arg.substring("--regex=".length());
                }
                else if(arg.startsWith("--input=")) {
                    input = arg.substring("--input=".length());
                }
                else if(arg.startsWith("--mode=")) {
                    String modeArg = arg.substring("--mode=".length());
                    switch (modeArg) {
                        case "exact" : {
                            mode = Mode.EXACT_MATCH;
                            break;
                        }
                        case "contains" : {
                            mode = Mode.SUBSTRING_MATCH;
                            break;
                        }
                        case "grep" : {
                            mode = Mode.GREP;
                            break;
                        }
                    }
                }
                else if(arg.startsWith("--files=")) {
                    String filesArg = arg.substring("--files=".length());

                    for(String file : filesArg.split(",")) {
                        file = file.trim();

                        if(!file.isEmpty()) {
                            files.add(file);
                        }
                    }
                }
            }

            NfaBuilder.NfaFragment graph = core.compile(regex);

            if(mode == Mode.EXACT_MATCH) {
                boolean flag = core.isExactMatch(graph, input);
                System.out.println(flag);
            }
            else if(mode == Mode.SUBSTRING_MATCH) {
                boolean flag = core.contains(graph, input);
                System.out.println(flag);
            }
            else if(mode == Mode.GREP) {
                if (files.isEmpty()) {
                    throw new NeedleException("grep mode requires --files=file1,file2,...");
                }

                boolean multipleFiles = files.size() > 1;

                for (String file : files) {
                    try {
                        List<String> lines = Files.readAllLines(Path.of(file));

                        for (String line : lines) {
                            if (core.contains(graph, line)) {
                                if (multipleFiles) {
                                    System.out.println(file + ":" + line);
                                } else {
                                    System.out.println(line);
                                }
                            }
                        }

                    } catch (IOException ex) {
                        System.err.println("Cannot read file: " + file);
                    }
                }
            }

        }catch (NeedleException ex) {
            System.out.println(ex.getMessage());
        }

    }
}