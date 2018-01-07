package de.repa.filesorter.infrastructure.passive.ftp;

import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandHandler;
import org.mockftpserver.core.session.Session;

public class MockFtpServerExceptionCommandHandler implements CommandHandler {

    private final Exception exceptionTpThrow;

    public MockFtpServerExceptionCommandHandler(Exception exceptionToThrow) {
        this.exceptionTpThrow = exceptionToThrow;
    }

    @Override
    public void handleCommand(Command command, Session session) throws Exception {
        throw exceptionTpThrow;
    }
}
