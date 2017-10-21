package de.repa.filesorter.files;

import de.repa.filesorter.files.model.FilesByDayDirectory;
import de.repa.filesorter.infrastructure.passive.ftp.FileClientException;

import java.util.Set;

public interface FileWriteClient {
    public void cutFilesFromRootToDayDirectory(Set<FilesByDayDirectory> fileDirectories) throws FileClientException;

}
