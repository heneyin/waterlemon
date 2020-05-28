package com.henvealf.watermelon.common;

import com.google.common.collect.Lists;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 一个方便的 Shell 命令执行器。
 * 依赖 apache-common-exec
 *
 * <code>
 *
 * ExecResult result = ShellCommand.setCommand("echo hello-world")
 * .setExitCode(0)
 * .exec();
 *
 * result.get...
 *
 * <code/>
 * @author Henvealf
 */
public class ShellCommand {

    private List<String> commands;
    private Map<String, String> env;
    private String workDir;
    private long timeout = 10 * 60 * 1000;
    private int exitCode = 0;

    public static ShellCommand setCommand(List<String> commands) {
        return new ShellCommand(commands);
    }

    public static ShellCommand setCommand(String... command) {
        return new ShellCommand(command);
    }

    public ShellCommand(List<String> commands) {
        this.commands = commands;
    }

    public ShellCommand(String... command) {
        this.commands = Lists.newArrayList(command);
    }

    public ShellCommand setEnv(Map<String, String> env) {
        this.env = env;
        return this;
    }

    public ShellCommand setWorkDir(String workDir) {
        this.workDir = workDir;
        return this;
    }

    public ShellCommand setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public ShellCommand setExitCode(int exitCode) {
        this.exitCode = exitCode;
        return this;
    }

    public ExecResult exec() {
        return execShellCommands(commands, env, workDir, timeout, exitCode);
    }

    /**
     * 执行 shell 命令行，环境变量与占位符使用同一个map。
     * @param commands 命令行们，在一个 session 里执行。
     * @param environment 命令执行的环境变量
     * @param workDir 工作目录
     * @param timeout 执行超时时间，单位毫秒。
     * @return 执行结果
     * @see ExecResult
     */
    public static ExecResult execShellCommands(List<String> commands,
                                    Map<String, String> environment,
                                    String workDir,
                                    long timeout,
                                    int exitValue) {
        if (commands == null || commands.isEmpty()) {
            throw new RuntimeException("Shell command is empty");
        }

        ExecResult result = new ExecResult();
        Executor executor = buildCommandExecutor(workDir, timeout, exitValue, result);
        for (String command : commands) {
            try {
                CommandLine commandLine = CommandLine.parse(command);
                commandLine.setSubstitutionMap(environment);
                TimeUse timeUse = TimeUse.get();
                int exitCode = executor.execute(commandLine, environment);
                result.setExitCode(exitCode);
                long took = timeUse.took();
                result.setTook(took);
            } catch (ExecuteException e) {
                if (executor.getWatchdog() != null && executor.getWatchdog().killedProcess())  {
                    throw new RuntimeException("Shell command killed after timeout " + timeout + "ms: " + command);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error when run command line: " + command, e);
            }
        }
        return result;
    }

    private static Executor buildCommandExecutor(String workDir,
                                                long timeoutTime,
                                                int exitValue,
                                                LogOutputStream logOutputStream) {
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutTime);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(exitValue);
        if (StringUtils.isNotEmpty(workDir)) {
            executor.setWorkingDirectory(new File(workDir));
        }
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(new PumpStreamHandler(logOutputStream));
        executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
        return executor;
    }

    public static class ExecResult extends LogOutputStream {

        private int exitCode;
        private long took = -1;

        private final List<String> lines = new LinkedList<>();

        /**
         * @return the exitCode
         */
        public int getExitCode() {
            return exitCode;
        }

        /**
         * @param exitCode the exitCode to set
         */
        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }


        @Override
        protected void processLine(String line, int level) {
            lines.add(line);
        }

        public List<String> getLines() {
            return lines;
        }

        public long getTook() {
            return took;
        }

        public void setTook(long took) {
            this.took = took;
        }

    }

}
