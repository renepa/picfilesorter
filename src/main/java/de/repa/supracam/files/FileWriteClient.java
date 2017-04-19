package de.repa.supracam.files;

import de.repa.supracam.files.model.FilesByDayDirectory;
import de.repa.supracam.infrastructure.ftp.FileClientException;

import java.util.Set;

public interface FileWriteClient {
    public void cutFilesFromRootToDayDirectory(Set<FilesByDayDirectory> fileDirectories) throws FileClientException;

}
